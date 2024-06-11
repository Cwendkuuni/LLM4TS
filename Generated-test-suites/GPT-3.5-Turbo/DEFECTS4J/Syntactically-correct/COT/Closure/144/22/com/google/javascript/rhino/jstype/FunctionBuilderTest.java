
import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.withName("test");
    assertEquals("test", builder.build().getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node sourceNode = new Node();
    builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.build().getSource());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withParam("param1", "number");
    paramBuilder.withParam("param2", "string");
    builder.withParams(paramBuilder);
    Node expectedParams = paramBuilder.build();
    assertEquals(expectedParams, builder.build().getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node paramsNode = new Node();
    builder.withParamsNode(paramsNode);
    assertEquals(paramsNode, builder.build().getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    builder.withReturnType(returnType);
    assertEquals(returnType, builder.build().getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    builder.withInferredReturnType(returnType);
    assertEquals(returnType, builder.build().getReturnType());
    assertTrue(builder.build().isReturnTypeInferred());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(null);
    ObjectType typeOfThis = new ObjectType();
    builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.build().getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    String templateTypeName = "Type";
    builder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, builder.build().getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.forConstructor();
    assertTrue(builder.build().isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.forNativeType();
    assertTrue(builder.build().isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType otherType = new FunctionType(null, "test", null, null, null, null, false, false);
    builder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getReferenceName(), builder.build().getReferenceName());
    assertEquals(otherType.getSource(), builder.build().getSource());
    assertEquals(otherType.getParametersNode(), builder.build().getParametersNode());
    assertEquals(otherType.getReturnType(), builder.build().getReturnType());
    assertEquals(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), builder.build().getTemplateTypeName());
    assertEquals(otherType.isConstructor(), builder.build().isConstructor());
    assertEquals(otherType.isNativeObjectType(), builder.build().isNativeObjectType());
  }
}
