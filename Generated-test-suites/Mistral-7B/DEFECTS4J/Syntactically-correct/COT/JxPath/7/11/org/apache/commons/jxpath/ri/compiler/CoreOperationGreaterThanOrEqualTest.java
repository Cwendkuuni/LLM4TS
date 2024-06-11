import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.core.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_whenBothArgumentsAreNumbersAndTheFirstIsGreaterOrEqualToTheSecond_thenReturnTrue() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(5.0), new CoreConstantExpression(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenBothArgumentsAreNumbersAndTheFirstIsLessThanTheSecond_thenReturnFalse() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(4.0), new CoreConstantExpression(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsANumberAndSecondIsANumber_thenCorrectlyHandleEqualValues() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(5.0), new CoreConstantExpression(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenArgumentsAreNotNumbers_thenThrowIllegalArgumentException() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreStringConstantExpression("string1"), new CoreStringConstantExpression("string2"));
        EvalContext context = new EvalContext(new InfoSet());
        coreOperation.computeValue(context);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsNaN_thenReturnFalse() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(Double.NaN), new CoreConstantExpression(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsPositiveInfinity_thenReturnTrue() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(Double.POSITIVE_INFINITY), new CoreConstantExpression(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsNegativeInfinity_thenReturnFalse() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(Double.NEGATIVE_INFINITY), new CoreConstantExpression(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}