import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual operation = new CoreOperationEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test when both arguments are equal
        operation.args[0] = "test";
        operation.args[1] = "test";
        Object result1 = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result1);
        
        // Test when both arguments are not equal
        operation.args[0] = "test";
        operation.args[1] = "different";
        Object result2 = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result2);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(new Expression(), new Expression());
        String symbol = operation.getSymbol();
        assertEquals("=", symbol);
    }
}