import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // Test case 1
        EvalContext context1 = new EvalContext();
        InfoSetUtil.setValue(context1, 10);
        InfoSetUtil.setValue(context1, 5);
        assertEquals(Boolean.TRUE, operation.computeValue(context1));

        // Test case 2
        EvalContext context2 = new EvalContext();
        InfoSetUtil.setValue(context2, 5);
        InfoSetUtil.setValue(context2, 10);
        assertEquals(Boolean.FALSE, operation.computeValue(context2));

        // Test case 3
        EvalContext context3 = new EvalContext();
        InfoSetUtil.setValue(context3, 5.5);
        InfoSetUtil.setValue(context3, 5.5);
        assertEquals(Boolean.TRUE, operation.computeValue(context3));

        // Test case 4
        EvalContext context4 = new EvalContext();
        InfoSetUtil.setValue(context4, 6.6);
        InfoSetUtil.setValue(context4, 5.5);
        assertEquals(Boolean.TRUE, operation.computeValue(context4));

        // Test case 5
        EvalContext context5 = new EvalContext();
        InfoSetUtil.setValue(context5, 5.5);
        InfoSetUtil.setValue(context5, 6.6);
        assertEquals(Boolean.FALSE, operation.computeValue(context5));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertEquals(">=", operation.getSymbol());
    }
}