import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test case 1
        double arg1Value = 5.0;
        double arg2Value = 3.0;
        double expected = 1.0;
        context.setNodeSet(new NodeSet(arg1Value, arg2Value));
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        
        // Test case 2
        arg1Value = 10.0;
        arg2Value = 20.0;
        expected = 0.0;
        context.setNodeSet(new NodeSet(arg1Value, arg2Value));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String expected = ">=";
        String result = operation.getSymbol();
        assertEquals(expected, result);
    }

}