import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5); // typical use case
        Expression arg2 = new Constant(10); // typical use case
        CoreOperationLessThanOrEqual testOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = testOperation.computeValue(context);
        
        assertFalse((Boolean) result); // 5 <= 10 should return false
    }

    @Test
    public void testComputeValueEdgeCase() {
        Expression arg1 = new Constant(-10); // edge case
        Expression arg2 = new Constant(0); // edge case
        CoreOperationLessThanOrEqual testOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = testOperation.computeValue(context);
        
        assertTrue((Boolean) result); // -10 <= 0 should return true
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(0); // typical use case
        Expression arg2 = new Constant(0); // typical use case
        CoreOperationLessThanOrEqual testOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        String symbol = testOperation.getSymbol();
        
        assertEquals("<=", symbol); // should return "<="
    }

    @Test
    public void testComputeValueErrorScenario() {
        Expression arg1 = new Constant("invalid"); // error scenario
        Expression arg2 = new Constant(5); // typical use case
        CoreOperationLessThanOrEqual testOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        try {
            testOperation.computeValue(context);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

}