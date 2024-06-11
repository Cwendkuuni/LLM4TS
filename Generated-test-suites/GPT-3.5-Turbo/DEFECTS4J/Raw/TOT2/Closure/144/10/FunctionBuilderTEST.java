import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    String testName = "testFunction";
    FunctionBuilder result = builder.withName(testName);
    
    assertEquals(testName, result.getName());
  }
  
  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node testNode = new Node(Token.FUNCTION);
    FunctionBuilder result = builder.withSourceNode(testNode);
    
    assertEquals(testNode, result.getSourceNode());
  }
  
  // Additional test cases for other public methods should be included here
  
}