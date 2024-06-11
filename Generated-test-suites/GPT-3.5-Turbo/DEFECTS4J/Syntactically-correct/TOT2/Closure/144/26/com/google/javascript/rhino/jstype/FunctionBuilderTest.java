import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    String name = "testFunction";
    FunctionType function = builder.withName(name).build();
    
    assertEquals(name, function.getName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    Node sourceNode = new Node(NodeType.FUNCTION);
    FunctionType function = builder.withSourceNode(sourceNode).build();
    
    assertEquals(sourceNode, function.getSource());
  }

  // More test cases for other methods can be added here

}