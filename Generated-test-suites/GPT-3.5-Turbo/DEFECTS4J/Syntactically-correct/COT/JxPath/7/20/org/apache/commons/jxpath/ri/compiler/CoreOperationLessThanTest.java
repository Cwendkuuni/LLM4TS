import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

import static org.junit.Assert.*;



public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 1;
            }
        };
        Expression arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };

        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        boolean result = (boolean) op.computeValue(new EvalContext());
        assertTrue(result);

        arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };
        arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 1;
            }
        };

        op = new CoreOperationLessThan(arg1, arg2);
        result = (boolean) op.computeValue(new EvalContext());
        assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 1;
            }
        };
        Expression arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };

        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        String symbol = op.getSymbol();
        assertEquals("<", symbol);
    }

}

