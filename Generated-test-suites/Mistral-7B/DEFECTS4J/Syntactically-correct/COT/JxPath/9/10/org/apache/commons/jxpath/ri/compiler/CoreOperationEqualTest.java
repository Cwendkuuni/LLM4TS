import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationCompare;
import org.apache.commons.jxpath.ri.core.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqualConstructor() {
        Expression arg1 = new Expression() {};
        Expression arg2 = new Expression() {};
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(" CoreOperationEqual instance created with arg1 and arg2", coreOperationEqual.getClass(), CoreOperationEqual.class);
    }

    @Test
    public void testComputeValue_equal_returnsTrue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertTrue(" computeValue returns true for equal values", coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValue_notEqual_returnsFalse() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 6;
            }
        };
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertFalse(" computeValue returns false for not equal values", coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_nullArg1() {
        EvalContext context = new EvalContext();
        Expression arg1 = null;
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 6;
            }
        };
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_nullArg2() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression() {}, new Expression() {});
        assertEquals(" GetSymbol returns '='", coreOperationEqual.getSymbol(), "=");
    }
}