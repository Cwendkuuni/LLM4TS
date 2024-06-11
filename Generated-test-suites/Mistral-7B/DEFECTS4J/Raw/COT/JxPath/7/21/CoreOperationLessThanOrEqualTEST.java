import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgsAreNumbersAndLeftIsLessOrEqualRight_thenReturnTrue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(1.0), new CoreNumberExpression(2.0));
        EqAssertions.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgsAreNumbersAndLeftIsGreaterThanRight_thenReturnFalse() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(2.0), new CoreNumberExpression(1.0));
        EqAssertions.assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgsAreNumbersAndLeftIsEqualRight_thenReturnTrue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(1.0), new CoreNumberExpression(1.0));
        EqAssertions.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationLessThanOrEqual_whenArgsAreDifferentTypes_thenThrowRuntimeException() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(1.0), new CoreStringExpression("2"));
        coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenLeftArgIsNaN_thenReturnFalse() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(Double.NaN), new CoreNumberExpression(1.0));
        EqAssertions.assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenRightArgIsNaN_thenReturnTrue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(1.0), new CoreNumberExpression(Double.NaN));
        EqAssertions.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgsAreNegativeNumbers_thenReturnTrue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(-3.0), new CoreNumberExpression(-4.0));
        EqAssertions.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenLeftArgIsPositiveAndRightIsNegative_thenReturnFalse() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(1.0), new CoreNumberExpression(-1.0));
        EqAssertions.assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenLeftArgIsZeroAndRightIsPositiveOrZero_thenReturnTrue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(0.0), new CoreNumberExpression(0.0));
        EqAssertions.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(0.0), new CoreNumberExpression(1.0));
        EqAssertions.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenLeftArgIsZeroAndRightIsNegative_thenReturnFalse() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(0.0), new CoreNumberExpression(-1.0));
        EqAssertions.assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenLeftArgIsPositiveAndRightIsVeryLargeNumber_thenReturnFalse() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(1.0), new CoreNumberExpression(Double.POSITIVE_INFINITY));
        EqAssertions.assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }
}