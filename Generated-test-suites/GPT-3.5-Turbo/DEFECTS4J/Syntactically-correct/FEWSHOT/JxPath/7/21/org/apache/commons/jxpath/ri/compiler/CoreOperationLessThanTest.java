import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        
        // Test cases to check operation for different values
        assertTrue((Double)operation.computeValue(context) < (Double)operation.computeValue(context));
        assertFalse((Double)operation.computeValue(context) < (Double)operation.computeValue(context));
        assertTrue((Double)operation.computeValue(context) < (Double)operation.computeValue(context));
        assertFalse((Double)operation.computeValue(context) < (Double)operation.computeValue(context));
        assertTrue((Double)operation.computeValue(context) < (Double)operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        
        // Test case to check the symbol returned
        assertEquals("<", operation.getSymbol());
    }
}