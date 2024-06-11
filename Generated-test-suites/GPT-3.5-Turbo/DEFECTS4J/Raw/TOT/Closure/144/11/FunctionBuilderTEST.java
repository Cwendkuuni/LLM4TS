import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    FunctionBuilder result = functionBuilder.withName("testName");
    
    assertNotNull(result);
    assertEquals("testName", result.getName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    FunctionBuilder result = functionBuilder.withSourceNode(new Node());
    
    assertNotNull(result);
    assertEquals(new Node(), result.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    FunctionParamBuilder params = new FunctionParamBuilder();
    FunctionBuilder result = functionBuilder.withParams(params);
    
    assertNotNull(result);
    assertEquals(params.build(), result.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    Node parametersNode = new Node();
    FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
    
    assertNotNull(result);
    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withReturnType(returnType);
    
    assertNotNull(result);
    assertEquals(returnType, result.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
    
    assertNotNull(result);
    assertEquals(returnType, result.getReturnType());
    assertTrue(result.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
    
    assertNotNull(result);
    assertEquals(typeOfThis, result.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    FunctionBuilder result = functionBuilder.withTemplateName("templateName");
    
    assertNotNull(result);
    assertEquals("templateName", result.getTemplateName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    FunctionBuilder result = functionBuilder.forConstructor();
    
    assertNotNull(result);
    assertTrue(result.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    FunctionBuilder result = functionBuilder.forNativeType();
    
    assertNotNull(result);
    assertTrue(result.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    FunctionType otherType = new FunctionType();
    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    
    assertNotNull(result);
    assertEquals(otherType.getReferenceName(), result.getName());
    assertEquals(otherType.getSource(), result.getSourceNode());
    assertEquals(otherType.getParametersNode(), result.getParametersNode());
    assertEquals(otherType.getReturnType(), result.getReturnType());
    assertEquals(otherType.getTypeOfThis(), result.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), result.getTemplateName());
    assertTrue(result.isConstructor());
    assertEquals(otherType.isNativeObjectType(), result.isNativeType());
  }

  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withName("testName")
        .withSourceNode(new Node())
        .withParams(new FunctionParamBuilder())
        .withReturnType(new JSType())
        .withTypeOfThis(new ObjectType())
        .withTemplateName("templateName")
        .forConstructor()
        .forNativeType();

    FunctionType result = functionBuilder.build();

    assertNotNull(result);
    assertEquals("testName", result.getName());
    assertEquals(new Node(), result.getSourceNode());
    assertEquals(newFunctionParamNode(), result.getParametersNode());
    assertEquals(new JSType(), result.getReturnType());
    assertEquals(new ObjectType(), result.getTypeOfThis());
    assertEquals("templateName", result.getTemplateTypeName());
    assertTrue(result.isConstructor());
    assertTrue(result.isNativeObjectType());
  }

  private Node newFunctionParamNode() {
    // Create and return a new Node for function parameters
    return new Node();
  }
}