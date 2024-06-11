import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withName("testFunction").build();
    assertEquals("testFunction", functionType.getName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    Node sourceNode = new Node("SOURCE");
    FunctionType functionType = builder.withSourceNode(sourceNode).build();
    assertEquals(sourceNode, functionType.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());

    // Test with FunctionParamBuilder
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node paramNode = new Node("PARAM");
    params.withParam(paramNode);
    FunctionType functionType1 = builder.withParams(params).build();
    assertEquals(paramNode, functionType1.getParametersNode().getFirstChild());

    // Test with parameters Node
    Node parametersNode = new Node("PARAMETERS");
    parametersNode.addChildToFront(paramNode);
    FunctionType functionType2 = builder.withParamsNode(parametersNode).build();
    assertEquals(paramNode, functionType2.getParametersNode().getFirstChild());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = new JSType("RETURN");
    FunctionType functionType = builder.withReturnType(returnType).build();
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = new JSType("RETURN");
    FunctionType functionType = builder.withInferredReturnType(returnType).build();
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    ObjectType typeOfThis = new ObjectType("THIS");
    FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    String templateTypeName = "TEMPLATE";
    FunctionType functionType = builder.withTemplateName(templateTypeName).build();
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.forConstructor().build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.forNativeType().build();
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(new JSTypeRegistry());

    // Set properties in otherType
    otherType.setName("otherFunction");
    otherType.setSourceNode(new Node("OTHER_SOURCE"));
    Node parametersNode = new Node("OTHER_PARAMETERS");
    parametersNode.addChildToFront(new Node("OTHER_PARAM"));
    otherType.setParametersNode(parametersNode);
    otherType.setReturnType(new JSType("OTHER_RETURN"));
    otherType.setTypeOfThis(new ObjectType("OTHER_THIS"));
    otherType.setTemplateTypeName("OTHER_TEMPLATE");
    otherType.setIsConstructor(true);
    otherType.setIsNativeObjectType(true);

    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.copyFromOtherFunction(otherType).build();

    assertEquals(otherType.getName(), functionType.getName());
    assertEquals(otherType.getSourceNode(), functionType.getSourceNode());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), functionType.isConstructor());
    assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
  }

}