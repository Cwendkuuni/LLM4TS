import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    functionBuilder.withName(name);
    assertEquals(name, functionBuilder.getName());
  }
  
  @Test
  public void testWithReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType returnType = new JSType();
    functionBuilder.withReturnType(returnType);
    assertEquals(returnType, functionBuilder.getReturnType());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    functionBuilder.withParams(params);
    assertEquals(parametersNode, functionBuilder.getParametersNode());
  }
  
  // Add more test cases for other methods
  
}