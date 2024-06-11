import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanWithTwoDoubleNumbers() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(1.5), new CoreNumber(2.5));
        Boolean result = (Boolean) coreOperationLessThan.computeValue(new EvalContext());
        Assert.assertFalse(result);

        coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(1.5), new CoreNumber(1.3));
        result = (Boolean) coreOperationLessThan.computeValue(new EvalContext());
        Assert.assertTrue(result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanWithMismatchedTypes() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(1), new CoreString("2"));
        coreOperationLessThan.computeValue(new EvalContext());
    }

    @Test
    public void testCoreOperationLessThanWithTwoNegativeNumbers() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(-1.5), new CoreNumber(-2.5));
        Boolean result = (Boolean) coreOperationLessThan.computeValue(new EvalContext());
        Assert.assertTrue(result);

        coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(-1.5), new CoreNumber(-1.3));
        result = (Boolean) coreOperationLessThan.computeValue(new EvalContext());
        Assert.assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndNegativeNumber() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(0), new CoreNumber(-1));
        Boolean result = (Boolean) coreOperationLessThan.computeValue(new EvalContext());
        Assert.assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndZero() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(0), new CoreNumber(0));
        Boolean result = (Boolean) coreOperationLessThan.computeValue(new EvalContext());
        Assert.assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanWithNaN() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(Double.NaN), new CoreNumber(Double.NaN));
        Boolean result = (Boolean) coreOperationLessThan.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThanWithPositiveAndNegativeInfinity() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(Double.POSITIVE_INFINITY), new CoreNumber(Double.NEGATIVE_INFINITY));
        Boolean result = (Boolean) coreOperationLessThan.computeValue(new EvalContext());
        Assert.assertTrue(result);
    }

    @Test(expected = ArithmeticException.class)
    public void testCoreOperationLessThanWithDivisionByZero() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNumber(1.0), new CoreNumber(0));
        coreOperationLessThan.computeValue(new EvalContext());
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationLessThanWithNullExpression() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(null, new CoreNumber(1.5));
        coreOperationLessThan.computeValue(new EvalContext());
    }
}