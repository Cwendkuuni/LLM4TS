import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.util.List;

public class ClosureCodingConventionTest {

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");

    applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertNotNull(childCtor.getPrototype().getDeclaredProperty("constructor"));
    assertEquals(childCtor, childCtor.getPrototype().getDeclaredProperty("constructor"));
    assertNotNull(childCtor.getPrototype().getDeclaredProperty("superClass_"));
    assertEquals(parentCtor, childCtor.getPrototype().getDeclaredProperty("superClass_"));
  }

  @org.junit.Test
  public void testApplySubclassRelationship_mixin() {
    FunctionType parentPrototype = createMockFunctionType("ParentPrototype");
    FunctionType childPrototype = createMockFunctionType("ChildPrototype");

    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");

    parentCtor.setPrototype(parentPrototype);
    childCtor.setPrototype(childPrototype);

    applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);

    assertNotNull(childPrototype.getDeclaredProperty("constructor"));
    assertEquals(childCtor, childPrototype.getDeclaredProperty("constructor"));
    assertNotNull(childPrototype.getDeclaredProperty("superClass_"));
    assertEquals(parentPrototype, childPrototype.getDeclaredProperty("superClass_"));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testApplySubclassRelationship_invalid_inherit() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");

    applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    childCtor.getPrototype().setDeclaredProperty("constructor", null);

    try {
      applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);
      fail("Expected an error to be thrown");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testApplySubclassRelationship_invalid_mixin() {
    FunctionType parentPrototype = createMockFunctionType("ParentPrototype");
    FunctionType childPrototype = createMockFunctionType("ChildPrototype");

    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");

    parentCtor.setPrototype(parentPrototype);
    childCtor.setPrototype(childPrototype);

    applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);

    childPrototype.setDeclaredProperty("superClass_", null);

    try {
      applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);
      fail("Expected an error to be thrown");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    assertEquals("superClass_", isSuperClassReference("superClass_"));
    assertFalse(isSuperClassReference("anotherProperty"));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = createMockNode(
        "goog.provide('MyClass')", SourceName.TEST);
    assertEquals("MyClass", extractClassNameIfProvide(node, null));
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = createMockNode(
        "goog.require('MyClass')", SourceName.TEST);
    assertEquals("MyClass", extractClassNameIfRequire(node, null));
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol", getExportSymbolFunction());
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createMockNode("goog.addDependency('MyType')", SourceName.TEST);
    List<String> types = identifyTypeDeclarationCall(node);
    assertEquals(1, types.size());
    assertEquals("MyType", types.get(0));
  }

  @org.junit.Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod", getAbstractMethodName());
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node node = createMockNode(
        "goog.addSingletonGetter(MyClass, 'myProperty')", SourceName.TEST);
    assertEquals("MyClass.myProperty", getSingletonGetterClassName(node));
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createMockFunctionType("MyClass");
    FunctionType getterType = createMockFunctionType("GetterFunction");
    ObjectType objectType = createMockObjectType("ObjectType");

    applySingletonGetter(functionType, getterType, objectType);

    assertTrue(functionType.getDeclaredProperties().containsKey("getInstance"));
    assertEquals(getterType, functionType.getDeclaredProperties().get("getInstance"));
    assertTrue(functionType.getDeclaredProperties().containsKey("instance_"));
    assertEquals(objectType, functionType.getDeclaredProperties().get("instance_"));
  }

  @org.junit.Test
  public void testExtractClassNameIfGoog() {
    Node node = createMockNode("goog.foo('MyClass')", SourceName.TEST);
    assertEquals("MyClass", extractClassNameIfGoog(node, null, "goog.foo"));
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node node = createMockNode("goog.isDef(x)", SourceName.TEST);
    assertTrue(isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    Node node = createMockNode(
        "goog.reflect.object.getPrototypeOf(obj)", SourceName.TEST);
    ObjectLiteralCast expected = new ObjectLiteralCast("ObjectType", node.getLastChild());
    ObjectLiteralCast actual = getObjectLiteralCast(null, node);
    assertEquals(expected.getTypeName(), actual.getTypeName());
    assertEquals(expected.getCastedNode(), actual.getCastedNode());
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    Node node = createMockNode("function foo(x = 1) {}", SourceName.TEST);
    assertFalse(isOptionalParameter(node.getFirstChild()));
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    Node node = createMockNode("function foo(x, ...y) {}", SourceName.TEST);
    assertFalse(isVarArgsParameter(node.getFirstChild()));
  }

  @org.junit.Test
  public void testIsPrivate() {
    String name = "privateVariable";
    assertFalse(isPrivate(name));
  }

  @org.junit.Test
  public void testGetAssertionFunctions() {
    List<AssertionFunctionSpec> functions = getAssertionFunctions();
    assertEquals(8, functions.size());

    for (AssertionFunctionSpec function : functions) {
      String functionName = function.getName();
      JSType expectedType = function.getType();
      assertNotNull(functionName);
      assertNotNull(expectedType);
    }
  }

  @org.junit.Test
  public void testDescribeFunctionBind() {
    Node node = createMockNode("goog.bind(fn, this, args)", SourceName.TEST);
    Bind expected = new Bind("fn", "this", node.getLastChild());
    Bind actual = describeFunctionBind(node);
    assertEquals(expected.getFunction(), actual.getFunction());
    assertEquals(expected.getThisValue(), actual.getThisValue());
    assertEquals(expected.getArguments(), actual.getArguments());
  }

  private FunctionType createMockFunctionType(String name) {
    FunctionType functionType = new FunctionType(name, ImmutableList.of());
    return functionType;
  }

  private ObjectType createMockObjectType(String name) {
    ObjectType objectType = new ObjectType(name);
    return objectType;
  }

  private Node createMockNode(String code, SourceName sourceName) {
    return new Node(code, sourceName, null);
  }
}