import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;
  
  @Before
  public void setUp() {
    functionBuilder = new FunctionBuilder(new JSTypeRegistry());
  }
  
  @Test
  public void testWithName() {
    String name = "testName";
    FunctionBuilder result = functionBuilder.withName(name);
    assertEquals(name, result.getName());
  }
  
  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, result.getSourceNode());
  }
  
  // ... generate test methods for the rest of the public methods
  
}