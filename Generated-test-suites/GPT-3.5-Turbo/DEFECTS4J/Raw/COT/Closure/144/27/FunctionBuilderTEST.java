import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.withName("testName");
    assertEquals("testName", result.getName());
  }
  
  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node sourceNode = new Node();
    FunctionBuilder result = builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, result.getSourceNode());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionParamBuilder params = new FunctionParamBuilder();
    params.addParam("param1", null);
    params.addParam("param2", null);
    FunctionBuilder result = builder.withParams(params);
    assertEquals(params.build(), result.getParametersNode());
  }
  
  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node parametersNode = new Node();
    FunctionBuilder result = builder.withParamsNode(parametersNode);
    assertEquals(parametersNode, result.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    FunctionBuilder result = builder.withReturnType(returnType);
    assertEquals(returnType, result.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    FunctionBuilder result = builder.withInferredReturnType(returnType);
    assertEquals(returnType, result.getReturnType());
    assertTrue(result.getInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(null);
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder result = builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, result.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.withTemplateName("templateName");
    assertEquals("templateName", result.getTemplateName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.forConstructor();
    assertTrue(result.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.forNativeType();
    assertTrue(result.isNativeType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType otherType = new FunctionType();
    FunctionBuilder result = builder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getName(), result.getName());
    assertEquals(otherType.getSourceNode(), result.getSourceNode());
    assertEquals(otherType.getParametersNode(), result.getParametersNode());
    assertEquals(otherType.getReturnType(), result.getReturnType());
    assertEquals(otherType.getTypeOfThis(), result.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), result.getTemplateName());
    assertEquals(otherType.isConstructor(), result.isConstructor());
    assertEquals(otherType.isNativeObjectType(), result.isNativeType());
  }
  
  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType result = builder.build();
    assertEquals(registry, result.getRegistry());
    assertNull(result.getName());
    assertNull(result.getSourceNode());
    assertNull(result.getParametersNode());
    assertNull(result.getReturnType());
    assertNull(result.getTypeOfThis());
    assertNull(result.getTemplateName());
    assertFalse(result.isConstructor());
    assertFalse(result.isNativeObjectType());
  }
}