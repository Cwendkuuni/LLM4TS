import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Add a name to the function type
    builder.withName("testFunction");
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the name is set correctly
    assertEquals("testFunction", functionType.getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Add a source node to the function type
    Node sourceNode = new Node();
    builder.withSourceNode(sourceNode);
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the source node is set correctly
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testWithParams() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Add parameters to the function type using a FunctionParamBuilder
    FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
    Node paramsNode = paramsBuilder.build();
    builder.withParams(paramsBuilder);
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the parameters are set correctly
    assertEquals(paramsNode, functionType.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Add parameters to the function type using a Node
    Node paramsNode = new Node();
    builder.withParamsNode(paramsNode);
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the parameters are set correctly
    assertEquals(paramsNode, functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Add a return type to the function type
    JSType returnType = new JSType();
    builder.withReturnType(returnType);
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the return type is set correctly
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Add an inferred return type to the function type
    JSType returnType = new JSType();
    builder.withInferredReturnType(returnType);
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the return type and inferred flag are set correctly
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Add a type of this to the function type
    ObjectType typeOfThis = new ObjectType();
    builder.withTypeOfThis(typeOfThis);
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the type of this is set correctly
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Add a template name to the function type
    String templateTypeName = "TemplateType";
    builder.withTemplateName(templateTypeName);
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the template name is set correctly
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Make the function type a constructor
    builder.forConstructor();
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the function type is a constructor
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testForNativeType() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Make the function type a native type
    builder.forNativeType();
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that the function type is a native type
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    // Create a FunctionBuilder
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    // Create another function type to copy from
    FunctionType otherType = new FunctionType(new JSTypeRegistry(), "otherFunction",
        new Node(), new ArrowType(new JSTypeRegistry(), new Node(), new JSType(), false),
        new ObjectType(), "OtherTemplateType", true, false);
    // Copy the information from the other function type
    builder.copyFromOtherFunction(otherType);
    // Build the function type
    FunctionType functionType = builder.build();
    // Verify that all the information is copied correctly
    assertEquals("otherFunction", functionType.getReferenceName());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}