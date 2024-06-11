import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;

public class ClosureCodingConventionTest {

  private ClosureCodingConvention codingConvention;
  private FunctionType parentCtor;
  private FunctionType childCtor;
  private SubclassType type;
  private Node callNode;

  @Before
  public void setUp() {
    codingConvention = new ClosureCodingConvention();
    parentCtor = mock(FunctionType.class);
    childCtor = mock(FunctionType.class);
  }

  @Test
  public void testApplySubclassRelationship_INHERITS() {
    when(parentCtor.getPrototype()).thenReturn(new ObjectType(JSTypeNative.OBJECT_TYPE));
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertNotNull(childCtor.getPrototype().getProperties().get("constructor"));
    assertNotNull(childCtor.getPrototype().getProperties().get("superClass_"));
  }

  @Test
  public void testApplySubclassRelationship_MIXIN() {
    when(parentCtor.getPrototype()).thenReturn(new ObjectType(JSTypeNative.OBJECT_TYPE));
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);

    assertNull(childCtor.getPrototype().getProperties().get("constructor"));
    assertNotNull(childCtor.getPrototype().getProperties().get("superClass_"));
  }

  @Test
  public void testGetclasssDefinedByCall_INHERITS_HasSuperclass() {
    callNode = Node.newCall("SubClass.inherits",
        Node.newThis(), "SuperClass");
    SubclassRelationship result = codingConvention.getclasssDefinedByCall(callNode);

    assertNotNull(result);
    assertEquals(SubclassType.INHERITS, result.getType());
    assertEquals("SubClass", result.getSubclass());
    assertEquals("SuperClass", result.getSuperclass());
  }

  @Test
  public void testGetclasssDefinedByCall_INHERITS_NoSuperclass() {
    callNode = Node.newCall("SubClass.inherits", Node.newThis());
    SubclassRelationship result = codingConvention.getclasssDefinedByCall(callNode);

    assertNull(result);
  }

  @Test
  public void testGetclasssDefinedByCall_MIXIN() {
    callNode = Node.newCall("SubClass.mixin", Node.newThis(),
        Node.newCall("SuperClass.prototype"));
    SubclassRelationship result = codingConvention.getclasssDefinedByCall(callNode);

    assertNotNull(result);
    assertEquals(SubclassType.MIXIN, result.getType());
    assertEquals("SubClass", result.getSubclass());
    assertEquals("SuperClass", result.getSuperclass());
  }

  @Test
  public void testIsSuperClassReference_superClass_() {
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testIsSuperClassReference_Other() {
    assertFalse(codingConvention.isSuperClassReference("other"));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newCall("goog.provide", Node.newString("X"));
    String className = codingConvention.extractClassNameIfProvide(node, null);

    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newCall("goog.require", Node.newString("X"));
    String className = codingConvention.extractClassNameIfRequire(node, null);

    assertEquals("X", className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", codingConvention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol", codingConvention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newCall("goog.addDependency",
        Node.newArray(), Node.newArray(Node.newString("X"), Node.newString("Y")));
    List<String> typeNames = codingConvention.identifyTypeDeclarationCall(node);

    assertEquals(2, typeNames.size());
    assertEquals("X", typeNames.get(0));
    assertEquals("Y", typeNames.get(1));
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod", codingConvention.getAbstractMethodName());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newCall("goog.addSingletonGetter",
        Node.newString("X"), Node.newCall("getX"));
    String className = codingConvention.getSingletonGetterClassName(node);

    assertEquals("X", className);
  }

  @Test
  public void testApplySingletonGetter() {
    ObjectType getterType = mock(ObjectType.class);
    ObjectType objectType = mock(ObjectType.class);

    when(getterType.getPrototype()).thenReturn(objectType);

    codingConvention.applySingletonGetter(new FunctionType(JSTypeNative.VOID_TYPE,
        ImmutableList.of(JSTypeNative.OBJECT_TYPE)), getterType, objectType);

    assertNotNull(getterType.getPrototype().getProperties().get("getInstance"));
    assertNotNull(getterType.getPrototype().getProperties().get("instance_"));
  }

  @Test
  public void testGetGlobalObject() {
    assertEquals("goog.global", codingConvention.getGlobalObject());
  }

  @Test
  public void testPropertyTestFunctions() {
    assertFalse(codingConvention.isPropertyTestFunction(null));
    Node testNode = Node.newCall("goog.isDef", Node.newNull());

    assertTrue(codingConvention.isPropertyTestFunction(testNode));
  }

  @Test
  public void testObjectLiteralCast() {
    Node callNode = Node.newCall("goog.reflect.object.cast",
        Node.newThis(), Node.newString("String"));
    ObjectLiteralCast result = codingConvention.getObjectLiteralCast(
        NodeTraversal.NULL, callNode);

    assertNotNull(result);
    assertEquals("String", result.getTypeName());
    assertNotNull(result.getObjectNode());
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = Node.newIdentifier("p");

    assertFalse(codingConvention.isOptionalParameter(parameter));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = Node.newIdentifier("p");
    Node varArgsNode = Node.newIdentifier("args");

    Node callNode = Node.newCall(Node.newIdentifier("fn"), parameter, varArgsNode);

    assertFalse(codingConvention.isVarArgsParameter(parameter));
    assertFalse(codingConvention.isVarArgsParameter(varArgsNode));
    assertTrue(codingConvention.isVarArgsParameter(callNode));
  }

  @Test
  public void testIsPrivate() {
    assertFalse(codingConvention.isPrivate("name"));
  }

  @Test
  public void testGetAssertionFunctions() {
    List<AssertionFunctionSpec> assertionFunctions = codingConvention.getAssertionFunctions();

    assertEquals(8, assertionFunctions.size());
    for (AssertionFunctionSpec functionSpec : assertionFunctions) {
      assertNotNull(functionSpec.getFunctionName());
      assertNotNull(functionSpec.getFunctionType());
    }
  }

  @Test
  public void testBind_fn_bind() {
    Node fnNode = Node.newIdentifier("fn");
    Node selfNode = Node.newThis();
    Node argNode1 = Node.newIdentifier("arg1");
    Node argNode2 = Node.newIdentifier("arg2");

    Node callNode = Node.newCall("goog.bind", fnNode, selfNode, argNode1, argNode2);

    Bind result = codingConvention.describeFunctionBind(callNode);

    assertNotNull(result);
    assertEquals(fnNode, result.getFunctionNode());
    assertEquals(selfNode, result.getThisValueNode());
    assertEquals(ImmutableList.of(argNode1, argNode2), result.getArguments());
  }

  @Test
  public void testBind_fn_partial() {
    Node fnNode = Node.newIdentifier("fn");
    Node argNode1 = Node.newIdentifier("arg1");
    Node argNode2 = Node.newIdentifier("arg2");

    Node callNode = Node.newCall("goog.partial", fnNode, argNode1, argNode2);

    Bind result = codingConvention.describeFunctionBind(callNode);

    assertNotNull(result);
    assertEquals(fnNode, result.getFunctionNode());
    assertNull(result.getThisValueNode());
    assertEquals(ImmutableList.of(argNode1, argNode2), result.getArguments());
  }
}