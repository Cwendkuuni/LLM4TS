import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        // Test case for withName method in FunctionBuilder
        // Typical use case
        FunctionBuilder builder = new FunctionBuilder(null);
        builder.withName("testFunction");
        assertEquals("testFunction", builder.build().getReferenceName());

        // Edge case
        builder.withName(null);
        assertNull(builder.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        // Test case for withSourceNode method in FunctionBuilder
        // Typical use case
        FunctionBuilder builder = new FunctionBuilder(null);
        builder.withSourceNode(new Node());
        assertNotNull(builder.build().getSource());

        // Edge case
        builder.withSourceNode(null);
        assertNull(builder.build().getSource());
    }

    @Test
    public void testWithParams() {
        // Test case for withParams method in FunctionBuilder
        // Typical use case
        FunctionBuilder builder = new FunctionBuilder(null);
        Node paramsNode = new Node();
        builder.withParams(new FunctionParamBuilder(paramsNode));
        assertEquals(paramsNode, builder.build().getParametersNode());

        // Edge case
        builder.withParams(null);
        assertNull(builder.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        // Test case for withReturnType method in FunctionBuilder
        // Typical use case
        FunctionBuilder builder = new FunctionBuilder(null);
        JSType returnType = new JSType();
        builder.withReturnType(returnType);
        assertEquals(returnType, builder.build().getReturnType());

        // Edge case
        builder.withReturnType(null);
        assertNull(builder.build().getReturnType());
    }

    // Add more test cases for other methods in FunctionBuilder

}