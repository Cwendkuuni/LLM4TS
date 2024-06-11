import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValueNotEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }
    
    @Test
    public void testGetSymbolNotEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        assertEquals("!=", operation.getSymbol());
    }
    
    @Test
    public void testComputeValueEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        operation.arg1 = "test";
        operation.arg2 = "test";
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }
}