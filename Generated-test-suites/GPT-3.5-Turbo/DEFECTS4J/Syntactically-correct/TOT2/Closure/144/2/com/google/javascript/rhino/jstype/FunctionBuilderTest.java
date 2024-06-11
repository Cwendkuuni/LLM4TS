import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String functionName = "testFunction";
        FunctionType functionType = builder.withName(functionName).build();
        
        assertEquals(functionName, functionType.getName());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node parametersNode = new Node(Token.PARAM_LIST, new Node(Token.NAME, new Node(Token.STRING, "param1")), new Node(Token.NAME, new Node(Token.STRING, "param2")));
        FunctionType functionType = builder.withParamsNode(parametersNode).build();
        
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType("int");
        FunctionType functionType = builder.withReturnType(returnType).build();
        
        assertEquals(returnType, functionType.getReturnType());
    }

    // Add more test cases for other methods and edge cases

}