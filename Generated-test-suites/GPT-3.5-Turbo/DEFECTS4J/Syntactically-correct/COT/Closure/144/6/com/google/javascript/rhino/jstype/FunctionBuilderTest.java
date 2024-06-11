import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.withName("test");
    assertEquals("test", builder.getName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node sourceNode = new Node();
    builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionParamBuilder params = new FunctionParamBuilder(builder).addParam("param1", null);
    builder.withParams(params);
    assertEquals(params.build(), builder.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node parametersNode = new Node();
    builder.withParamsNode(parametersNode);
    assertEquals(parametersNode, builder.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    builder.withReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    builder.withInferredReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
    assertTrue(builder.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(null);
    ObjectType typeOfThis = new ObjectType();
    builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.withTemplateName("template");
    assertEquals("template", builder.getTemplateName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.forConstructor();
    assertTrue(builder.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.forNativeType();
    assertTrue(builder.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType otherType = new FunctionType(null, "test", null, null, null, null, false, false);
    builder.copyFromOtherFunction(otherType);
    assertEquals("test", builder.getName());
    assertFalse(builder.isConstructor());
    assertFalse(builder.isNativeType());
  }

  @Test
  public void testBuild() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.build();
    assertNotNull(functionType);
  }

}