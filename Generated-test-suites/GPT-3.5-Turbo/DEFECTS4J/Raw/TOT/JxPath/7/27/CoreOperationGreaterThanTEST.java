import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.core.CoreOperationGreaterThan;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operationGreaterThan;

    @Before
    public void setUp() {
        operationGreaterThan = new CoreOperationGreaterThan(new Expression(2), new Expression(1));
    }

    @Test
    public void testGetSymbol() {
        String symbol = operationGreaterThan.getSymbol();
        assertEquals(">", symbol);
    }

    @Test
    public void testComputeValue_FirstArgGreaterThanSecondArg_ReturnsTrue() {
        EvalContext context = new EvalContext();
        double result = operationGreaterThan.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_FirstArgLessThanSecondArg_ReturnsFalse() {
        operationGreaterThan = new CoreOperationGreaterThan(new Expression(1), new Expression(2));
        EvalContext context = new EvalContext();
        double result = operationGreaterThan.computeValue(context);
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_FirstArgEqualToSecondArg_ReturnsFalse() {
        operationGreaterThan = new CoreOperationGreaterThan(new Expression(1), new Expression(1));
        EvalContext context = new EvalContext();
        double result = operationGreaterThan.computeValue(context);
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_FirstArgNotConvertibleToDouble_ReturnsFalse() {
        operationGreaterThan = new CoreOperationGreaterThan(new Expression("string"), new Expression(2));
        EvalContext context = new EvalContext();
        double result = operationGreaterThan.computeValue(context);
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_SecondArgNotConvertibleToDouble_ReturnsFalse() {
        operationGreaterThan = new CoreOperationGreaterThan(new Expression(1), new Expression("string"));
        EvalContext context = new EvalContext();
        double result = operationGreaterThan.computeValue(context);
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_BothArgsNotConvertibleToDouble_ReturnsFalse() {
        operationGreaterThan = new CoreOperationGreaterThan(new Expression("string1"), new Expression("string2"));
        EvalContext context = new EvalContext();
        double result = operationGreaterThan.computeValue(context);
        assertFalse((boolean) result);
    }
}