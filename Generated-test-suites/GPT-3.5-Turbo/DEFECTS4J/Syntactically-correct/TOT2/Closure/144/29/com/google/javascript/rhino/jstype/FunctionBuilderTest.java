import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    builder.withName("testFunction");
    assertEquals("testFunction", builder.build().getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    Node sourceNode = new Node("functionSource");
    builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.build().getSource());
  }

  // Add more test cases for other methods and edge cases

}