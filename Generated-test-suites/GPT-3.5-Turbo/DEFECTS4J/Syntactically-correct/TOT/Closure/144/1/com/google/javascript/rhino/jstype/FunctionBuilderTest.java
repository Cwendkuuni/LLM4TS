import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder();
    FunctionType function = builder.withName("testFunction").build();

    assertEquals("testFunction", function.getName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder();
    Node sourceNode = new Node();
    FunctionType function = builder.withSourceNode(sourceNode).build();

    assertEquals(sourceNode, function.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder();
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    Node parametersNode = paramBuilder.addParam("param1").addParam("param2").build();
    FunctionType function = builder.withParams(paramBuilder).build();

    assertEquals(parametersNode, function.getParametersNode());
    assertEquals(2, function.getParameters().size());
    assertEquals("param1", function.getParameters().get(0).getName());
    assertEquals("param2", function.getParameters().get(1).getName());
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder();
    Node parametersNode = new Node();
    FunctionType function = builder.withParamsNode(parametersNode).build();

    assertEquals(parametersNode, function.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder();
    JSType returnType = new JSType();
    FunctionType function = builder.withReturnType(returnType).build();

    assertEquals(returnType, function.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder();
    JSType returnType = new JSType();
    FunctionType function = builder.withInferredReturnType(returnType).build();

    assertEquals(returnType, function.getReturnType());
    assertTrue(function.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder();
    ObjectType typeOfThis = new ObjectType();
    FunctionType function = builder.withTypeOfThis(typeOfThis).build();

    assertEquals(typeOfThis, function.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder();
    String templateName = "Template";
    FunctionType function = builder.withTemplateName(templateName).build();

    assertEquals(templateName, function.getTemplateName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder();
    FunctionType function = builder.forConstructor().build();

    assertTrue(function.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder();
    FunctionType function = builder.forNativeType().build();

    assertTrue(function.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder();
    FunctionType otherType = new FunctionType();
    otherType.setName("otherFunction");
    otherType.setSourceNode(new Node());
    FunctionType function = builder.copyFromOtherFunction(otherType).build();

    assertEquals("otherFunction", function.getName());
    assertEquals(otherType.getSourceNode(), function.getSourceNode());
  }

}