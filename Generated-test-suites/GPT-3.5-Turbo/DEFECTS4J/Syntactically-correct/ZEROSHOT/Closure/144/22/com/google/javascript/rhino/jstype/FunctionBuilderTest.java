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
    Node sourceNode = new Node();
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, result.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.withParams(params);
    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.withParamsNode(parametersNode);
    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType(null);
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.withReturnType(returnType);
    assertEquals(returnType, result.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType(null);
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.withInferredReturnType(returnType);
    assertEquals(returnType, result.getReturnType());
    assertTrue(result.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder builder = new FunctionBuilder(null);
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
    FunctionType functionType = new FunctionType(null, "testName", null, null, null, null, false, false);
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.copyFromOtherFunction(functionType);
    assertEquals("testName", result.getName());
    assertNull(result.getSourceNode());
    assertNull(result.getParametersNode());
    assertNull(result.getReturnType());
    assertNull(result.getTypeOfThis());
    assertNull(result.getTemplateName());
    assertFalse(result.isConstructor());
    assertFalse(result.isNativeType());
  }

  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType result = builder.build();
    assertNotNull(result);
  }  
}