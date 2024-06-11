import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String name = "testFunction";
    FunctionType functionType = functionBuilder.withName(name).build();
    assertEquals(name, functionType.getReferenceName());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    Node parametersNode = new Node(Token.PARAM_LIST);
    FunctionType functionType = functionBuilder.withParamsNode(parametersNode).build();
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = new JSType();
    FunctionType functionType = functionBuilder.withReturnType(returnType).build();
    assertEquals(returnType, functionType.getReturnType());
  }

  // Add more test cases for other methods as needed

}