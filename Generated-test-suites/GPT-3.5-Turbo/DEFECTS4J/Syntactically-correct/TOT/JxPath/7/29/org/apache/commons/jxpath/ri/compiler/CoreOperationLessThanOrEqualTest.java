import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use cases
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new ExpressionImpl(5), new ExpressionImpl(10));
        Assert.assertEquals(Boolean.TRUE, op.computeValue(null));

        op = new CoreOperationLessThanOrEqual(new ExpressionImpl(10), new ExpressionImpl(5));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(null));

        // Edge cases
        op = new CoreOperationLessThanOrEqual(new ExpressionImpl(Integer.MIN_VALUE), new ExpressionImpl(Integer.MAX_VALUE));
        Assert.assertEquals(Boolean.TRUE, op.computeValue(null));

        op = new CoreOperationLessThanOrEqual(new ExpressionImpl(Double.MAX_VALUE), new ExpressionImpl(Double.MAX_VALUE));
        Assert.assertEquals(Boolean.TRUE, op.computeValue(null));

        op = new CoreOperationLessThanOrEqual(new ExpressionImpl(Double.NaN), new ExpressionImpl(Double.NEGATIVE_INFINITY));
        Assert.assertEquals(Boolean.TRUE, op.computeValue(null));

        // Error scenarios
        op = new CoreOperationLessThanOrEqual(new ExpressionImpl("5"), new ExpressionImpl(10));
        Assert.assertEquals(Boolean.FALSE, op.computeValue(null));

        op = new CoreOperationLessThanOrEqual(new ExpressionImpl(10), null);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(null));

    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(null, null);
        Assert.assertEquals("<=", op.getSymbol());
    }

}