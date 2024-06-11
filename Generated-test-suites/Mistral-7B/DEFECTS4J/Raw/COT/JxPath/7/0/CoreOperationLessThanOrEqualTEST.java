import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0));
        Assert.assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(1.0));
        Assert.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(1.0));
        Assert.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqualWithMismatchedTypes() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression("str"), new ConstantExpression(1.0));
        coreOperationLessThanOrEqual.computeValue(new EvalContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualWithNullArg() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                null, new ConstantExpression(1.0));
        coreOperationLessThanOrEqual.computeValue(new EvalContext());
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithEdgeCases() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(0.0), new ConstantExpression(0.0));
        Assert.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(Double.MIN_VALUE), new ConstantExpression(Double.MIN_VALUE));
        Assert.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(Double.MAX_VALUE), new ConstantExpression(Double.MAX_VALUE));
        Assert.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithNegativeValues() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(-1.0), new ConstantExpression(-2.0));
        Assert.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(-1.0), new ConstantExpression(-1.0));
        Assert.assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));
    }
}