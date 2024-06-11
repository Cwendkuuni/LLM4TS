import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withName("testName");
        FunctionType functionType = builder.build();
        
        assertEquals("testName", functionType.getName());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        paramBuilder.addParameter("param1", JSType.UNKNOWN);
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withParams(paramBuilder);
        FunctionType functionType = builder.build();
        
        assertEquals(paramBuilder.build(), functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withReturnType(JSType.NUMBER);
        FunctionType functionType = builder.build();
        
        assertEquals(JSType.NUMBER, functionType.getReturnType());
    }

    // Add more test cases for other methods as needed

}