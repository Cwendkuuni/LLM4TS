import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder fb = new FunctionBuilder(new JSTypeRegistry());
    fb.withName("testName");
    FunctionType ft = fb.build();
    assertEquals("testName", ft.getReferenceName());
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder fb = new FunctionBuilder(new JSTypeRegistry());
    Node paramsNode = new Node(Token.paramList());
    fb.withParamsNode(paramsNode);
    FunctionType ft = fb.build();
    assertEquals(paramsNode, ft.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder fb = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = new SomeType();
    fb.withReturnType(returnType);
    FunctionType ft = fb.build();
    assertEquals(returnType, ft.getReturnType());
  }

  // Add more test cases for other methods and scenarios as needed

}