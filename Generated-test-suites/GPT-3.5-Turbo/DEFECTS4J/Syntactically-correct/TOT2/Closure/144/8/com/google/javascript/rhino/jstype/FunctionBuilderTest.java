import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    String name = "testFunction";
    builder.withName(name);
    FunctionType function = builder.build();

    assertEquals(name, function.getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    Node node = new Node();
    builder.withSourceNode(node);
    FunctionType function = builder.build();

    assertEquals(node, function.getSource());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.addParam("param1");
    paramBuilder.addParam("param2");
    builder.withParams(paramBuilder);
    FunctionType function = builder.build();

    assertEquals(2, function.getParametersNode().getChildCount());
  }

  // Additional test cases for other methods can be added here

}