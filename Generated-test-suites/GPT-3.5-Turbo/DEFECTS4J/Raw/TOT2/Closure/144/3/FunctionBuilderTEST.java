import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {
    
    @Test
    public void testWithName() {
        // Test setting name for FunctionBuilder
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withName("testName");
        
        assertEquals("testName", builder.build().getReferenceName());
    }
    
    @Test
    public void testWithParams() {
        // Test setting parameters for FunctionBuilder
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node paramsNode = new Node(Node.FUNCTION);
        builder.withParamsNode(paramsNode);
        
        assertEquals(paramsNode, builder.build().getParametersNode());
    }
    
    @Test
    public void testWithReturnType() {
        // Test setting return type for FunctionBuilder
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        builder.withReturnType(returnType);
        
        assertEquals(returnType, builder.build().getReturnType());
    }
    
    @Test
    public void testWithInferredReturnType() {
        // Test setting inferred return type for FunctionBuilder
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        builder.withInferredReturnType(returnType);
        
        assertTrue(builder.build().isInferredReturnType());
        assertEquals(returnType, builder.build().getReturnType());
    }
    
    @Test
    public void testBuildFunctionType() {
        // Test building FunctionType instance from FunctionBuilder
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType functionType = builder.build();
        
        assertNotNull(functionType);
        assertEquals(functionType.getClass(), FunctionType.class);
    }
}