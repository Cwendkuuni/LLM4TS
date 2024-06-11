import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    builder.withName("testName");
    FunctionType functionType = builder.build();
    
    assertEquals("testName", functionType.getReferenceName());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionParamBuilder params = new FunctionParamBuilder();
    params.addParam("param1", JSType.UNKNOWN);
    params.addParam("param2", JSType.BOOLEAN);
    builder.withParams(params);
    FunctionType functionType = builder.build();
    
    assertNotNull(functionType.getParametersNode());
    assertEquals(2, functionType.getParametersNode().getChildCount());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType otherType = new FunctionType(new JSTypeRegistry(), "testFunction", null, null, null, null, false, false);
    
    builder.copyFromOtherFunction(otherType);
    FunctionType functionType = builder.build();
    
    assertEquals("testFunction", functionType.getReferenceName());
  }
  
  @Test
  public void testInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = JSType.STRING;
    builder.withInferredReturnType(returnType);
    FunctionType functionType = builder.build();
    
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.hasInferredReturnType());
  }
  
  // Add more test cases for other methods and edge cases
  
}