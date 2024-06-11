import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Type;
import com.google.javascript.rhino.Token;

import java.util.List;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    Node parentCtorNode = createNode("function Parent() {}");
    Node childCtorNode = createNode("function Child() { goog.inherits(Child, Parent); }");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship((FunctionType) parentCtorNode.getType(),
        (FunctionType) childCtorNode.getType(), SubclassType.INHERITS);

    assertEquals("function Child() { goog.inherits(Child, Parent); }",
        childCtorNode.toSource());
    assertEquals(3, childCtorNode.getChildCount());
    FunctionType childCtor = (FunctionType) childCtorNode.getType();
    assertEquals(Type.PROPERTY, childCtor.getProperty("superClass_").getType().getKind());
    assertEquals("Parent", childCtor.getProperty("superClass_").getName().toString());
    assertEquals(Type.FUNCTION, childCtor.getProperty("constructor").getType().getKind());
    assertEquals("Child", childCtor.getProperty("constructor").getName().toString());
  }

  @Test
  public void testApplySubclassRelationship_mixin() {
    Node parentProtoNode = createNode("var Parent = function() {}; Parent.prototype = {foo: 'foo'};");
    Node childProtoNode = createNode("var Child = function() {}; Child.prototype = {bar: 'bar'}; Child.prototype = Parent.prototype;");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship((FunctionType) null, null, SubclassType.MIXIN);

    assertEquals("var Child = function() {}; Child.prototype = Parent.prototype;",
        childProtoNode.toSource());
    assertEquals(3, childProtoNode.getChildCount());
    ObjectType childProto = (ObjectType) childProtoNode.getType();
    assertEquals("Child.prototype", childProto.getName().toString());
    assertEquals(2, childProto.getDeclaredProperties().size());
    assertEquals("bar", childProto.getProperty("bar").getName().toString());
    assertEquals("string", childProto.getProperty("bar").getType().getName().toString());
    assertEquals("Parent.prototype", childProto.getProperty("constructor").getName().toString());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node provideNode = createNode("goog.provide('MyClass');");
    assertEquals("MyClass", convention.extractClassNameIfProvide(provideNode, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node requireNode = createNode("goog.require('MyClass');");
    assertEquals("MyClass", convention.extractClassNameIfRequire(requireNode, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        convention.getExportPropertyFunction());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node getterNode = createNode("goog.addSingletonGetter(MyClass, 'myProperty');");
    assertEquals("MyClass", convention.getSingletonGetterClassName(getterNode, null));
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testObjectLiteralCast() {
    Node callNode = createNode("goog.reflect.object(new MyType());");
    ObjectLiteralCast expected = new ObjectLiteralCast("MyType", null);
    ObjectLiteralCast actual = convention.getObjectLiteralCast(new NodeTraversal(), callNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testPropertyTestFunctions() {
    Node isDefNode = createNode("goog.isDef(myValue);");
    assertTrue(convention.isPropertyTestFunction(isDefNode));

    Node isDefAndNotNullNode = createNode("goog.isDefAndNotNull(myValue);");
    assertTrue(convention.isPropertyTestFunction(isDefAndNotNullNode));

    Node isStringNode = createNode("goog.isString(myValue);");
    assertTrue(convention.isPropertyTestFunction(isStringNode));

    Node isNumberNode = createNode("goog.isNumber(myValue);");
    assertTrue(convention.isPropertyTestFunction(isNumberNode));

    Node isBooleanNode = createNode("goog.isBoolean(myValue);");
    assertTrue(convention.isPropertyTestFunction(isBooleanNode));

    Node isFunctionNode = createNode("goog.isFunction(myValue);");
    assertTrue(convention.isPropertyTestFunction(isFunctionNode));

    Node isArrayNode = createNode("goog.isArray(myValue);");
    assertTrue(convention.isPropertyTestFunction(isArrayNode));

    Node isObjectNode = createNode("goog.isObject(myValue);");
    assertTrue(convention.isPropertyTestFunction(isObjectNode));

    Node isInstanceofNode = createNode("goog.asserts.assertInstanceof(myValue, MyType);");
    assertTrue(convention.isPropertyTestFunction(isInstanceofNode));
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod",
        convention.getAbstractMethodName());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node callNode = createNode("goog.addDependency('MyType', [MyOtherType]);");
    List<String> expected = ImmutableList.of("MyOtherType");
    List<String> actual = convention.identifyTypeDeclarationCall(callNode);
    assertEquals(expected, actual);
  }

  private Node createNode(String code) {
    NodeTraversal traversal = new NodeTraversal();
    Node node = traversal.parse(code, null, null);
    traversal.setCompiler(null);
    return node;
  }

  private static boolean endsWithPrototype(Node qualifiedName) {
    return convention.endsWithPrototype(qualifiedName);
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedNameNode = createNode("MyClass.prototype");
    assertTrue(endsWithPrototype(qualifiedNameNode));

    Node nonPrototypeNode = createNode("MyClass");
    assertFalse(endsWithPrototype(nonPrototypeNode));
  }

  @Test
  public void testGetClasssDefinedByCall() {
    Node inheritsNode = createNode("goog.inherits(MySubClass, MySuperClass);");
    SubclassRelationship expected = new SubclassRelationship(SubclassType.INHERITS,
        createNode("MySubClass"), createNode("MySuperClass"));
    SubclassRelationship actual = convention.getclasssDefinedByCall(inheritsNode);
    assertEquals(expected, actual);

    Node mixinNode = createNode("MySubClass.prototype = Object.assign(MySubClass.prototype, MySuperClass.prototype);");
    expected = new SubclassRelationship(SubclassType.MIXIN,
        createNode("MySubClass.prototype"), createNode("MySuperClass.prototype"));
    actual = convention.getclasssDefinedByCall(mixinNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameterNode = createNode("function myFunction(args...) { }");
    assertFalse(convention.isVarArgsParameter(parameterNode));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isPrivate("myProperty"));
  }

  @Test
  public void testGetAssertionFunctions() {
    List<AssertionFunctionSpec> expected = ImmutableList.of(
        new AssertionFunctionSpec("goog.asserts.assert", JSTypeNative.ANY),
        new AssertionFunctionSpec("goog.asserts.assertNumber", JSTypeNative.NUMBER_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertString", JSTypeNative.STRING_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertFunction", JSTypeNative.FUNCTION_INSTANCE_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertObject", JSTypeNative.OBJECT_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertArray", JSTypeNative.ARRAY_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertInstanceof", JSTypeNative.OBJECT_TYPE)
    );
    List<AssertionFunctionSpec> actual = convention.getAssertionFunctions();
    assertEquals(expected, actual);
  }

  @Test
  public void testBind() {
    Node fnNode = createNode("function myFn(a, b, c) { }");
    Bind expected = new Bind(fnNode, null,
        createNodeList("a", "b", "c"));
    Bind actual = convention.describeFunctionBind(createNode("goog.bind(myFn, thisArg, arg1, arg2, arg3)"));
    assertEquals(expected, actual);
  }

  @Test
  public void testPartial() {
    Node fnNode = createNode("function myFn(a, b, c) { }");
    Bind expected = new Bind(fnNode, null,
        createNodeList("a", "b"));
    Bind actual = convention.describeFunctionBind(createNode("goog.partial(myFn, arg1, arg2)"));
    assertEquals(expected, actual);
  }

  @Test
  public void testUnknownFunctionCall() {
    Node unknownFunctionCallNode = createNode("goog.unknownFunction();");
    assertNull(convention.describeFunctionBind(unknownFunctionCallNode));
  }

  private Node createNodeList(String... nodes) {
    ImmutableList.Builder<Node> builder = ImmutableList.builder();
    for (String node : nodes) {
      Node nodeValue = createNode(node);
      builder.add(nodeValue);
    }
    return builder.build().get(0);
  }

  private static class SubclassRelationship {
    final SubclassType type;
    final Node subclass;
    final Node superclass;

    SubclassRelationship(SubclassType type, Node subclass, Node superclass) {
      this.type = type;
      this.subclass = subclass;
      this.superclass = superclass;
    }
  }

  private static enum SubclassType {
    INHERITS,
    MIXIN
  }

  private static class AssertionFunctionSpec {
    final String name;
    final Type type;

    AssertionFunctionSpec(String name, Type type) {
      this.name = name;
      this.type = type;
    }
  }
}