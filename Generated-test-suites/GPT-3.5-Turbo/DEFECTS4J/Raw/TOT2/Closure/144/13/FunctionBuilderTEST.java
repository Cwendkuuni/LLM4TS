import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType function = builder.withName("testFunction").build();
        assertEquals("testFunction", function.getReferenceName());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionParamBuilder params = new FunctionParamBuilder();
        params.addParam("param1", JSType.NUMBER);
        FunctionType function = builder.withParams(params).build();
        assertEquals(Node.IS_ARGUMENT_LIST, function.getParametersNode().getType());
    }

    @Test
    public void testBuildConstructor() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType constructor = builder.withName("TestConstructor").forConstructor().build();
        assertTrue(constructor.isConstructor());
    }

    @Test
    public void testInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType inferredReturnType = registry.createTypeFromName("InferredType");
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType function = builder.withInferredReturnType(inferredReturnType).build();
        assertEquals(inferredReturnType, function.getReturnType());
    }

    // Add more test cases for other methods

}