import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withName("testName");
        FunctionType function = builder.build();
        assertNotNull(function);
        assertEquals("testName", function.getReferenceName());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        builder.withReturnType(returnType);
        FunctionType function = builder.build();
        assertNotNull(function);
        assertEquals(returnType, function.getReturnType());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        paramBuilder.addParam("param1", new JSType());
        paramBuilder.addParam("param2", new JSType());
        builder.withParams(paramBuilder);
        FunctionType function = builder.build();
        assertNotNull(function);
        assertNotNull(function.getParametersNode());
        // Additional assertions can be added to check each parameter
    }

    // Add more test cases for other methods in FunctionBuilder class

}