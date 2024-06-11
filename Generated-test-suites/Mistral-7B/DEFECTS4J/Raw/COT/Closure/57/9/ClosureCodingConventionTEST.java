import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;

public class ClosureCodingConventionTest {

  @org.junit.Test
  public void testApplySubclassRelationship() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of());
    SubclassType subclassType = SubclassType.INHERITS;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, subclassType);

    FunctionType prototype = parentCtor.getPrototype();
    assertEquals("constructor", childCtor, prototype.getDeclaredProperty("constructor"));
    assertEquals("superClass_", parentCtor, childCtor.getDeclaredProperty("superClass_"));
  }

  @org.junit.Test
  public void testApplySubclassRelationship_MIXIN() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of());
    SubclassType subclassType = SubclassType.MIXIN;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, subclassType);

    FunctionType prototype = childCtor.getPrototype();
    assertEquals("constructor", childCtor, prototype.getDeclaredProperty("constructor"));
    assertNull(prototype.getDeclaredProperty("superClass_"));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testApplySubclassRelationship_InvalidCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of());
    SubclassType subclassType = SubclassType.INHERITS;

    Node parentCtorNode = Node.createNode("function ParentCtor() {}");
    Node childCtorNode = Node.createNode("function ChildCtor() {}");

    codingConvention.applySubclassRelationship(parentCtor, childCtor, subclassType);

    Node callNode = Node.createNode("ChildCtor.inherits(ParentCtor)");
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);
    assertNull(relationship);
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
    assertFalse(codingConvention.isSuperClassReference("otherProperty"));
  }

  @org.junit.Test
  public void testEndsWithPrototype() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node prototypeNode = Node.createNode("function Prototype() {}");
    Node prototypeQualifiedName = prototypeNode.getQualifiedName();
    assertTrue(codingConvention.endsWithPrototype(prototypeNode));
    assertTrue(codingConvention.endsWithPrototype(prototypeQualifiedName));

    Node classNode = Node.createNode("function MyClass() {}");
    Node classQualifiedName = classNode.getQualifiedName();
    assertFalse(codingConvention.endsWithPrototype(classNode));
    assertFalse(codingConvention.endsWithPrototype(classQualifiedName));

    Node prototypePrototypeNode = Node.createNode("Prototype.prototype");
    Node prototypePrototypeQualifiedName = prototypePrototypeNode.getQualifiedName();
    assertTrue(codingConvention.endsWithPrototype(prototypePrototypeNode));
    assertTrue(codingConvention.endsWithPrototype(prototypePrototypeQualifiedName));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node provideNode = Node.createNode("goog.provide('MyClass')");
    String className = codingConvention.extractClassNameIfProvide(provideNode, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node requireNode = Node.createNode("goog.require('MyClass')");
    String className = codingConvention.extractClassNameIfRequire(requireNode, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertEquals("goog.exportProperty", codingConvention.getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetExportSymbolFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertEquals("goog.exportSymbol", codingConvention.getExportSymbolFunction());
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node addDependencyNode = Node.createNode("goog.addDependency(null, ['MyType'])");
    List<String> typeNames = codingConvention.identifyTypeDeclarationCall(addDependencyNode);
    assertEquals(1, typeNames.size());
    assertEquals("MyType", typeNames.get(0));
  }

  @org.junit.Test
  public void testGetAbstractMethodName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertEquals("goog.abstractMethod", codingConvention.getAbstractMethodName());
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node addSingletonGetterNode = Node.createNode("goog.addSingletonGetter(MySingletonGetter, 'MySingletonProperty')");
    String getterClassName = codingConvention.getSingletonGetterClassName(addSingletonGetterNode);
    assertEquals("MySingletonGetter", getterClassName);
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType getterType = new FunctionType("MySingletonGetter", ImmutableList.of());
    ObjectType objectType = new ObjectType("ObjectType");

    Node getterNode = Node.createNode("function MySingletonGetter() {}");
    codingConvention.applySingletonGetter(new FunctionType("SingletonFunction", ImmutableList.of()), getterType, objectType);

    FunctionType singletonFunction = new FunctionType("SingletonFunction", ImmutableList.of());
    FunctionType instance = new FunctionType("Instance", ImmutableList.of());

    Node getInstanceNode = Node.createNode("singletonFunction.getInstance()");
    Node instanceNode = Node.createNode("getInstance()");
    assertEquals(instance, getInstanceNode.getType());
    assertEquals(objectType, instanceNode.getType());
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node isDefNode = Node.createNode("goog.isDef(value)");
    Node isNullNode = Node.createNode("goog.isNull(value)");
    Node isStringNode = Node.createNode("goog.isString(value)");
    Node isNumberNode = Node.createNode("goog.isNumber(value)");
    Node isBooleanNode = Node.createNode("goog.isBoolean(value)");
    Node isFunctionNode = Node.createNode("goog.isFunction(value)");
    Node isArrayNode = Node.createNode("goog.isArray(value)");
    Node isObjectNode = Node.createNode("goog.isObject(value)");

    for (Node call : new Node[] {isDefNode, isNullNode, isStringNode, isNumberNode,
        isBooleanNode, isFunctionNode, isArrayNode, isObjectNode}) {
      assertTrue(codingConvention.isPropertyTestFunction(call));
    }
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node reflectObjectNode = Node.createNode("goog.reflect.object(value, MyType)");
    ObjectLiteralCast expectedCast = new ObjectLiteralCast("MyType", reflectObjectNode.getNext());

    ObjectLiteralCast actualCast = codingConvention.getObjectLiteralCast(NodeTraversal.EMPTY, reflectObjectNode);
    assertEquals(expectedCast.getType(), actualCast.getType());
    assertEquals(expectedCast.getNode(), actualCast.getNode());
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node parameterNode = Node.createNode("function MyFunction(optParam) {}");
    assertFalse(codingConvention.isOptionalParameter(parameterNode));
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node parameterNode = Node.createNode("function MyFunction(args) { var args = args || []; }");
    assertFalse(codingConvention.isVarArgsParameter(parameterNode));
  }

  @org.junit.Test
  public void testIsPrivate() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    assertFalse(codingConvention.isPrivate("publicProperty"));
  }

  @org.junit.Test
  public void testDescribeFunctionBind() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node fnNode = Node.createNode("function MyFunction(self, arg1, arg2) { return arg1 + arg2; }");
    Node bindNode = Node.createNode("goog.bind(MyFunction, this, arg1)");

    Bind expectedBind = new Bind(fnNode, Node.createNode("this"), Node.createNode("arg1"));
    Bind actualBind = codingConvention.describeFunctionBind(bindNode);

    assertEquals(expectedBind.getNode(), actualBind.getNode());
    assertEquals(expectedBind.getFunction(), actualBind.getFunction());
    assertEquals(expectedBind.getThisValue(), actualBind.getThisValue());
    assertEquals(expectedBind.getParameters(), actualBind.getParameters());
  }
}