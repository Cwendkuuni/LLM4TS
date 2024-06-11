import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    builder.withName("testName");
    FunctionType function = builder.build();
    assertEquals("testName", function.getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    Node sourceNode = new Node();
    builder.withSourceNode(sourceNode);
    FunctionType function = builder.build();
    assertEquals(sourceNode, function.getSource());
  }

  // Add more test cases for other public methods in FunctionBuilder class

}