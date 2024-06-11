import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("testName");
    FunctionType functionType = builder.build();
    assertEquals("testName", functionType.getReferenceName());
  }
  
  @Test
  public void testWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node sourceNode = new Node("source");
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withSourceNode(sourceNode);
    FunctionType functionType = builder.build();
    assertEquals(sourceNode, functionType.getSource());
  }
  
  // Add more test cases for other public methods
  
  // Add test cases for edge cases and exception scenarios
  
}