import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String name = "testFunction";
        builder.withName(name);
        FunctionType functionType = builder.build();
        assertEquals(name, functionType.getName());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node paramsNode = new Node(Token.PARAM_LIST);
        builder.withParamsNode(paramsNode);
        FunctionType functionType = builder.build();
        assertEquals(paramsNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = JSType.UNKNOWN;
        builder.withReturnType(returnType);
        FunctionType functionType = builder.build();
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.forConstructor();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isConstructor());
    }

    // Add more test cases for edge cases and error scenarios

}