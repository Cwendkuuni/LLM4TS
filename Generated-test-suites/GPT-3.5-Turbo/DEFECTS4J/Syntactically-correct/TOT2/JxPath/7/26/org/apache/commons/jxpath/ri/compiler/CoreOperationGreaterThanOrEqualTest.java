import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual;

    @Before
    public void setUp() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_PositiveNumbers() {
        EvalContext context = new MockEvalContext(5, 3);
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_NegativeNumbers() {
        EvalContext context = new MockEvalContext(-5, -3);
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_EqualNumbers() {
        EvalContext context = new MockEvalContext(10, 10);
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_DoubleNumbers() {
        EvalContext context = new MockEvalContext(3.5, 3.2);
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_ObjectArguments() {
        EvalContext context = new MockEvalContext("test", "test");
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_NullArguments() {
        EvalContext context = new MockEvalContext(null, null);
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_IncompatibleTypes() {
        EvalContext context = new MockEvalContext("5", 5);
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals(">=", coreOperationGreaterThanOrEqual.getSymbol());
    }
}