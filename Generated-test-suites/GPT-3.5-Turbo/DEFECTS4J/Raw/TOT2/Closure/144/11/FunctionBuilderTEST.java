import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    builder.withName("testFunction");
    FunctionType functionType = builder.build();
    
    assertEquals("testFunction", functionType.getReferenceName());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionParamBuilder params = new FunctionParamBuilder();
    params.addParam("param1", JSType.STRING);
    builder.withParams(params);
    FunctionType functionType = builder.build();
    
    assertNotNull(functionType.getParametersNode());
    // Add more assertions for specific parameters if needed
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    builder.withReturnType(JSType.NUMBER);
    FunctionType functionType = builder.build();

    assertEquals(JSType.NUMBER, functionType.getReturnType());
  }

  // Add more test cases for other public methods as needed

  @Test
  public void testWithConstructor() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    builder.forConstructor();
    FunctionType functionType = builder.build();
    
    assertTrue(functionType.isConstructor());
  }

  // Add test cases for edge cases and exception handling scenarios

}