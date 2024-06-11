import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setUp() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };

        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void computeValue_returnsTrue_whenLeftOperandIsGreaterThanOrEqualRightOperand() {
        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void computeValue_returnsFalse_whenLeftOperandIsLessThanRightOperand() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 1;
            }
        };

        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void getSymbol_returnsGreaterThanOrEqualSymbol() {
        String symbol = operation.getSymbol();

        assertEquals(">=", symbol);
    }
}