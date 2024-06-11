import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testLessThanWithTwoDoubleValues() {
        Expression arg1 = new ConstantExpression(new Double(5.0));
        Expression arg2 = new ConstantExpression(new Double(10.0));
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testLessThanWithTwoDoubleValuesEdgeCase() {
        Expression arg1 = new ConstantExpression(new Double(-5.0));
        Expression arg2 = new ConstantExpression(new Double(5.0));
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testLessThanWithTwoNullValues() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(new Double(5.0));
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        try {
            coreOperationLessThan.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }

        arg1 = new ConstantExpression(new Double(5.0));
        arg2 = new ConstantExpression(null);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(infoSet);
        try {
            coreOperationLessThan.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testLessThanWithTwoStringValues() {
        Expression arg1 = new ConstantExpression("apple");
        Expression arg2 = new ConstantExpression("banana");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testLessThanWithTwoStringValuesEdgeCase() {
        Expression arg1 = new ConstantExpression("zebra");
        Expression arg2 = new ConstantExpression("apple");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testLessThanWithTwoLargeDoubleValues() {
        Expression arg1 = new ConstantExpression(new Double(Double.MAX_VALUE));
        Expression arg2 = new ConstantExpression(new Double(Double.MAX_VALUE + 1.0));
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testLessThanWithTwoLargeDoubleValuesEdgeCase() {
        Expression arg1 = new ConstantExpression(new Double(Double.MIN_VALUE));
        Expression arg2 = new ConstantExpression(new Double(Double.MIN_VALUE - 1.0));
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

}