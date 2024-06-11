import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testSubclassRelationship() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");
    SubclassType type = SubclassType.INHERITS;
    applySubclassRelationship(parentCtor, childCtor, type);
    assertEquals("ParentCtor", childCtor.getPrototype().getClassName());
    assertEquals("ParentCtor", childCtor.getClassName());
    assertEquals("ParentCtor.prototype", childCtor.getPrototype().getClass().getName());
    assertEquals("ChildCtor", childCtor.getPrototype().getDeclaredProperty("constructor").getValue().getClassName());
  }

  @Test
  public void testSubclassRelationship_Mixin() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");
    SubclassType type = SubclassType.MIXIN;
    applySubclassRelationship(parentCtor, childCtor, type);
    assertEquals("ParentCtor.prototype", childCtor.getPrototype().getClass().getName());
    assertEquals("ChildCtor.prototype", childCtor.getPrototype().getClass().getName());
  }

  @Test
  public void testApplySubclassRelationship_Null() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");
    SubclassRelationship relationship = getclasssDefinedByCall(null, null);
    assertNull(relationship);
  }

  @Test
  public void testTypeOfClassDefiningName() {
    Node callNode = createMockNode("goog.inherits(SubClass, SuperClass)", SourceName.guessSourceName("test.js"));
    assertEquals(SubclassType.INHERITS, typeofClassDefiningName(callNode.getFirstChild()));
  }

  @Test
  public void testTypeOfClassDefiningName_Mixin() {
    Node callNode = createMockNode("goog.mixin(SubClass.prototype, SuperClass.prototype)", SourceName.guessSourceName("test.js"));
    assertEquals(SubclassType.MIXIN, typeofClassDefiningName(callNode.getFirstChild()));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = createMockFunctionType("getterName");
    FunctionType functionType = createMockFunctionType("FunctionType");
    ObjectType objectType = createMockObjectType("ObjectType");
    applySingletonGetter(functionType, getterType, objectType);
    assertEquals("getterName", functionType.getPrototype().getDeclaredProperty("getInstance").getName());
    assertNotNull(functionType.getPrototype().getDeclaredProperty("instance_"));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node callNode = createMockNode("goog.provide('X')", SourceName.guessSourceName("test.js"));
    assertEquals("X", extractClassNameIfProvide(callNode, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node callNode = createMockNode("goog.require('X')", SourceName.guessSourceName("test.js"));
    assertEquals("X", extractClassNameIfRequire(callNode, null));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createMockNode("goog.addDependency('Type1', 'Type2')", SourceName.guessSourceName("test.js"));
    List<String> typeNames = identifyTypeDeclarationCall(node);
    assertEquals(2, typeNames.size());
    assertEquals("Type1", typeNames.get(0));
    assertEquals("Type2", typeNames.get(1));
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(isSuperClassReference("superClass_"));
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = createMockNode("goog.isDef(name)", SourceName.guessSourceName("test.js"));
    assertTrue(isPropertyTestFunction(callNode));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", getExportPropertyFunction());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = createMockNode("goog.addSingletonGetter(name, getter)", SourceName.guessSourceName("test.js"));
    assertEquals("getName", getSingletonGetterClassName(callNode));
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = createMockNode("param?", SourceName.guessSourceName("test.js"));
    assertFalse(isOptionalParameter(parameter));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = createMockNode("params...", SourceName.guessSourceName("test.js"));
    assertFalse(isVarArgsParameter(parameter));
  }

  private FunctionType createMockFunctionType(String name) {
    ObjectType objectType = createMockObjectType(name);
    return new FunctionType(objectType, null);
  }

  private ObjectType createMockObjectType(String name) {
    return new ObjectType(name, null, null);
  }

  private Node createMockNode(String nodeString, SourceName sourceName) {
    Node node = new Node();
    node.setSource(sourceName);
    node.setType(Token.STRING);
    node.setString(nodeString);
    return node;
  }
}