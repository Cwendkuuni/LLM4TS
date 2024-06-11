import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionType functionType = builder.withName(name).build();
    
    assertEquals(name, functionType.getReferenceName());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    Node parametersNode = paramBuilder.addParam("param1").addParam("param2").build();
    FunctionType functionType = builder.withParamsNode(parametersNode).build();
    
    assertEquals(parametersNode, functionType.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = new JSType();
    FunctionType functionType = builder.withReturnType(returnType).build();
    
    assertEquals(returnType, functionType.getReturnType());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "constructorFunction";
    FunctionType functionType = builder.withName(name).forConstructor().build();
    
    assertTrue(functionType.isConstructor());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherFunctionType = new FunctionType(registry, "otherFunction", null, null, null, null, false, false);
    
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType functionType = builder.copyFromOtherFunction(otherFunctionType).build();
    
    assertEquals(otherFunctionType.getReferenceName(), functionType.getReferenceName());
    assertEquals(otherFunctionType.getSource(), functionType.getSource());
    assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
    assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(otherFunctionType.isConstructor(), functionType.isConstructor());
    assertEquals(otherFunctionType.isNativeObjectType(), functionType.isNativeObjectType());
  }
  
  // Add more test cases here for edge cases and error scenarios
  
}