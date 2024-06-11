import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
    private CoreOperationLessThan coreOperationLessThan;
    private EvalContext evalContext;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        evalContext = new EvalContext();
    }

    @Test
    public void testComputeValueWithLessThan() {
        evalContext.setValue(10);
        Object result = coreOperationLessThan.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithGreaterThan() {
        evalContext.setValue(20);
        Object result = coreOperationLessThan.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithEqualValues() {
        evalContext.setValue(10);
        Object result = coreOperationLessThan.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}