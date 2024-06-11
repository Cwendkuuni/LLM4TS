import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(5), new ExpressionStub(5));
        Assert.assertEquals(Boolean.TRUE, expression.computeValue(context));

        expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(4), new ExpressionStub(5));
        Assert.assertEquals(Boolean.FALSE, expression.computeValue(context));

        expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(6), new ExpressionStub(5));
        Assert.assertEquals(Boolean.TRUE, expression.computeValue(context));

        expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(5.5), new ExpressionStub(5));
        Assert.assertEquals(Boolean.TRUE, expression.computeValue(context));

        expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(4.5), new ExpressionStub(5));
        Assert.assertEquals(Boolean.FALSE, expression.computeValue(context));

        expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(5.5), new ExpressionStub(5.5));
        Assert.assertEquals(Boolean.TRUE, expression.computeValue(context));

        expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(5.5), new ExpressionStub(6));
        Assert.assertEquals(Boolean.FALSE, expression.computeValue(context));

    }
}