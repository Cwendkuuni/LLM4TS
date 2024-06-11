import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgsAreNumbers_thenReturnCorrectBooleanValue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(2.0), new CoreConstantExpression(3.0));
        EqualTestHelper helper = new EqualTestHelper(coreOperationLessThanOrEqual);
        helper.testEqual(false);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenFirstArgIsLessThanSecondArg_thenReturnCorrectBooleanValue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
        EqualTestHelper helper = new EqualTestHelper(coreOperationLessThanOrEqual);
        helper.testEqual(true);
    }

    @Test (expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqual_whenOneArgIsNotANumber_thenThrowException() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression("two"), new CoreConstantExpression(3.0));
    }

    @Test (expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqual_whenSecondArgIsNotANumber_thenThrowException() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(2.0), new CoreConstantExpression("three"));
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgsAreZero_thenReturnCorrectBooleanValue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(0.0), new CoreConstantExpression(0.0));
        EqualTestHelper helper = new EqualTestHelper(coreOperationLessThanOrEqual);
        helper.testEqual(true);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenFirstArgIsNegativeNumber_thenReturnCorrectBooleanValue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(-1.0), new CoreConstantExpression(0.0));
        EqualTestHelper helper = new EqualTestHelper(coreOperationLessThanOrEqual);
        helper.testEqual(true);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenSecondArgIsNegativeNumber_thenReturnCorrectBooleanValue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(0.0), new CoreConstantExpression(-1.0));
        EqualTestHelper helper = new EqualTestHelper(coreOperationLessThanOrEqual);
        helper.testEqual(true);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenFirstArgIsGreaterThanSecondArg_thenReturnCorrectBooleanValue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(3.0), new CoreConstantExpression(2.0));
        EqualTestHelper helper = new EqualTestHelper(coreOperationLessThanOrEqual);
        helper.testEqual(false);
    }

    private static class EqualTestHelper {
        private CoreOperationLessThanOrEqual coreOperationLessThanOrEqual;

        public EqualTestHelper(CoreOperationLessThanOrEqual coreOperationLessThanOrEqual) {
            this.coreOperationLessThanOrEqual = coreOperationLessThanOrEqual;
        }

        public void testEqual(boolean expected) {
            EvalContext context = new EvalContext();
            Object result = coreOperationLessThanOrEqual.computeValue(context);
            assertEquals(expected, result);
        }
    }
}