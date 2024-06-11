import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    assertNull(functionBuilder.getName());
    assertNull(functionBuilder.getSourceNode());
    assertNull(functionBuilder.getParametersNode());
    assertNull(functionBuilder.getReturnType());
    assertNull(functionBuilder.getTypeOfThis());
    assertNull(functionBuilder.getTemplateTypeName());
    assertFalse(functionBuilder.isConstructor());
    assertFalse(functionBuilder.isNativeType());
  }

  // Test for name
  @Test
  public void testFunctionBuilderWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withName("testFunctionBuilder");
    assertEquals("testFunctionBuilder", functionBuilder.getName());
  }

  // Test for sourceNode
  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node sourceNode = new Node();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withSourceNode(sourceNode);
    assertEquals(sourceNode, functionBuilder.getSourceNode());
  }

  // Test for params
  @Test
  public void testFunctionBuilderWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withParams(params);
    assertEquals(params.build(), functionBuilder.getParametersNode());
  }

  // Test for returnType
  @Test
  public void testFunctionBuilderWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType returnType = new IntType();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withReturnType(returnType);
    assertEquals(returnType, functionBuilder.getReturnType());
  }

  // Test for inferredReturnType
  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType returnType = new IntType();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withReturnType(returnType)
        .withInferredReturnType(returnType);
    assertTrue(functionBuilder.isInferredReturnType());
  }

  // Test for typeOfThis
  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType("ObjectType");
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, functionBuilder.getTypeOfThis());
  }

  // Test for templateTypeName
  @Test
  public void testFunctionBuilderWithTemplateName() {
    String templateTypeName = "TemplateTypeName";
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withTemplateName(templateTypeName);
    assertEquals(templateTypeName, functionBuilder.getTemplateTypeName());
  }

  // Test for constructor
  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .forConstructor();
    assertTrue(functionBuilder.isConstructor());
  }

  // Test for nativeType
  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .forNativeType();
    assertTrue(functionBuilder.isNativeType());
  }

  // Test for copying another FunctionType with valid inputs
  @Test
  public void testFunctionBuilderCopyFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String name = "testFunctionBuilder";
    Node sourceNode = new Node();
    FunctionParamBuilder params = new FunctionParamBuilder();
    JSType returnType = new IntType();
    ObjectType typeOfThis = new ObjectType("ObjectType");
    String templateTypeName = "TemplateTypeName";
    FunctionType otherFunctionType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, params.build(), returnType, false), typeOfThis,
        templateTypeName, false, false);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .copyFromOtherFunction(otherFunctionType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(otherFunctionType.getName(), functionType.getName());
    assertEquals(otherFunctionType.getSourceNode(), functionType.getSourceNode());
    assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
    assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(otherFunctionType.isConstructor(), functionType.isConstructor());
    assertEquals(otherFunctionType.isNativeType(), functionType.isNativeType());
  }

  // Test for copying another FunctionType with null FunctionType
  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderCopyNullFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .copyFromOtherFunction(null);
  }

  // Test for copying another FunctionType with invalid input
  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderCopyInvalidFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .copyFromOtherFunction(new NumberType());
  }

  // Test for null JSTypeRegistry
  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderWithNullRegistry() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
  }

  // Test for empty String as name
  @Test
  public void testFunctionBuilderWithEmptyName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withName("");
    assertEquals("", functionBuilder.getName());
  }

  // Test for null Node as sourceNode
  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderWithNullSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry())
        .withSourceNode(null);
  }

  // Test for null FunctionParamBuilder as params
  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderWithNullParams() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry())
        .withParams(null);
  }

  // Test for null Node as parametersNode
  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderWithNullParametersNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry())
        .withParamsNode(null);
  }

  // Test for null JSType as returnType
  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderWithNullReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry())
        .withReturnType(null);
  }

  // Test for null ObjectType as typeOfThis
  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderWithNullTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry())
        .withTypeOfThis(null);
  }

  // Test for null String as templateTypeName
  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderWithNullTemplateTypeName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry())
        .withTemplateName(null);
  }

  // Test for isConstructor and isNativeType with true and false values
  @Test
  public void testFunctionBuilderWithConstructorAndNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forConstructor();
    assertTrue(functionBuilder.isConstructor());
    functionBuilder.forNativeType();
    assertTrue(functionBuilder.isNativeType());
  }

  // Add test cases for copying another FunctionType with other edge cases and exceptions

}