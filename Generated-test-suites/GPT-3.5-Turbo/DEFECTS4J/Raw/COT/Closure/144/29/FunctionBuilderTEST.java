import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    functionBuilder.withName("testName");
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    Node sourceNode = new Node();
    functionBuilder.withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testParams() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(null);
    functionBuilder.withParams(functionParamBuilder);
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testParamsNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    Node parametersNode = new Node();
    functionBuilder.withParamsNode(parametersNode);
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    ObjectType typeOfThis = new ObjectType();
    functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testTemplateName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    functionBuilder.withTemplateName("testTemplateName");
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null);
    FunctionType otherType = new FunctionType();
    functionBuilder.copyFromOtherFunction(otherType);
    FunctionType functionType = functionBuilder.build();
    // Add assertions here to test the functionality of the method
  }
}