
import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual operation;

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(null);
        
        // Test when arg1 and arg2 are equal
        operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(5));
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test when arg1 is less than arg2
        operation = new CoreOperationLessThanOrEqual(new Constant(3), new Constant(5));
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test when arg1 is greater than arg2
        operation = new CoreOperationLessThanOrEqual(new Constant(7), new Constant(5));
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Test when arg1 and arg2 are of different types
        operation = new CoreOperationLessThanOrEqual(new Constant("5"), new Constant(5));
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test when arg1 and arg2 cannot be compared
        operation = new CoreOperationLessThanOrEqual(new Constant(null), new Constant(5));
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(5));
        assertEquals("<=", operation.getSymbol());
    }

}
