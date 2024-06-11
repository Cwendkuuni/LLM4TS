import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_whenBothArgumentsAreNumbersAndTheFirstOneIsGreaterOrEqualToTheSecondOne_thenReturnTrue() {
        Expression arg1 = new CoreConstantExpression(5.0);
        Expression arg2 = new CoreConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValue_whenBothArgumentsAreNumbersAndTheFirstOneIsLessThanTheSecondOne_thenReturnFalse() {
        Expression arg1 = new CoreConstantExpression(4.0);
        Expression arg2 = new CoreConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext()));
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValue_whenArgumentsAreNotNumbers_thenThrowException() {
        Expression arg1 = new CoreConstantExpression("5");
        Expression arg2 = new CoreConstantExpression("A");
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext());
    }

    @Test
    public void testComputeValue_whenBothArgumentsAreNaN_thenReturnFalse() {
        Expression arg1 = new CoreConstantExpression(Double.NaN);
        Expression arg2 = new CoreConstantExpression(Double.NaN);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsPositiveInfinityAndSecondIsNegativeInfinity_thenReturnFalse() {
        Expression arg1 = new CoreConstantExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new CoreConstantExpression(Double.NEGATIVE_INFINITY);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsNegativeInfinityAndSecondIsPositiveInfinity_thenReturnFalse() {
        Expression arg1 = new CoreConstantExpression(Double.NEGATIVE_INFINITY);
        Expression arg2 = new CoreConstantExpression(Double.POSITIVE_INFINITY);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsPositiveInfinityAndSecondIsPositiveInfinity_thenReturnTrue() {
        Expression arg1 = new CoreConstantExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new CoreConstantExpression(Double.POSITIVE_INFINITY);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext()));
    }
}