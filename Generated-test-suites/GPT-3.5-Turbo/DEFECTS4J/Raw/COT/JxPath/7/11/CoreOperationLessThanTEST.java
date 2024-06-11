
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_LessThan_ReturnsTrue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        arg1.setValue(5);
        arg2.setValue(10);
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_NotLessThan_ReturnsFalse() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        arg1.setValue(10);
        arg2.setValue(5);
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_LessThanOrEqual_ReturnsFalse() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        arg1.setValue(5);
        arg2.setValue(5);
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_ArgumentsNull_ReturnsFalse() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_ReturnsLessThanSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);

        String symbol = operation.getSymbol();

        Assert.assertEquals("<", symbol);
    }
}
