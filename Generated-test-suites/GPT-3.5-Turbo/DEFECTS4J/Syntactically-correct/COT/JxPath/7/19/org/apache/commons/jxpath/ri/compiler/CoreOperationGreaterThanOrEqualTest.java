
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = createContext();
        
        // Basic test case
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
        
        // Test with arg1 equal to arg2
        context.setValue(arg1Path, 5);
        context.setValue(arg2Path, 5);
        result = expression.computeValue(context);
        assertTrue((Boolean) result);

        // Test with arg1 greater than arg2
        context.setValue(arg1Path, 10);
        context.setValue(arg2Path, 5);
        result = expression.computeValue(context);
        assertTrue((Boolean) result);

        // Test with arg1 less than arg2
        context.setValue(arg1Path, 5);
        context.setValue(arg2Path, 10);
        result = expression.computeValue(context);
        assertFalse((Boolean) result);

        // Test with arg1 and arg2 as floating point numbers
        context.setValue(arg1Path, 10.0);
        context.setValue(arg2Path, 5.0);
        result = expression.computeValue(context);
        assertTrue((Boolean) result);
        
        // Test with arg1 and arg2 as strings
        context.setValue(arg1Path, "10");
        context.setValue(arg2Path, "5");
        result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String symbol = expression.getSymbol();
        assertEquals(">=", symbol);
    }

    // Utility methods
    private EvalContext createContext() {
        // Create and initialize EvalContext object as required
    }

}
