import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    String name = "testFunctionName";
    
    FunctionType functionType = builder.withName(name).build();
    
    assertEquals(name, functionType.getReferenceName());
  }
  
  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    Node sourceNode = new Node(Token.FUNCTION);
    
    FunctionType functionType = builder.withSourceNode(sourceNode).build();
    
    assertEquals(sourceNode, functionType.getSource());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    
    FunctionType functionType = builder.withParams(params).build();
    
    assertEquals(parametersNode, functionType.getParametersNode());
  }
  
  // Additional test cases for other methods can be added here
  
}