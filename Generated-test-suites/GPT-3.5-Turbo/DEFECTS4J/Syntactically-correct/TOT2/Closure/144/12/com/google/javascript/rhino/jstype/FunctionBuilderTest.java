import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {
    
    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withName("testName");
        assertEquals("testName", builder.build().getReferenceName());
    }
    
    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node("sourceNode");
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withSourceNode(sourceNode);
        assertEquals(sourceNode, builder.build().getSource());
    }
    
    @Test
    public void testWithParams() {
        FunctionParamBuilder params = new FunctionParamBuilder();
        params.addParamType(JSType.STRING);
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withParams(params);
        // Assertion for params can be added once the Node comparison logic is implemented
    }
    
    // Additional test cases for other methods can be added here

}