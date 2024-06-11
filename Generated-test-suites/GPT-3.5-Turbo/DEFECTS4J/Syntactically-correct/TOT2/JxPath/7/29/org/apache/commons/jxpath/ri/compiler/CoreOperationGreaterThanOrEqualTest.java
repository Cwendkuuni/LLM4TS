import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual;

    @Before
    public void setUp() {
        Expression arg1 = new Constant(5); // Sample value
        Expression arg2 = new Constant(3); // Sample value
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        assertEquals(">=", coreOperationGreaterThanOrEqual.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(null, null);
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext());
    }

    @Test
    public void testUnequalNumericValues() {
        Expression arg1 = new Constant(10);
        Expression arg2 = new Constant(5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testEqualNumericValues() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    // Add more test cases for edge cases and error scenarios

}