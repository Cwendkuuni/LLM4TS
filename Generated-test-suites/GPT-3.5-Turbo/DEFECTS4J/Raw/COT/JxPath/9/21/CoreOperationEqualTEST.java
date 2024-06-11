import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;

    @Test
    public void testComputeValueEqualToTrue() {
        EvaluationContext context = new EvaluationContext();

        Expression arg1 = new Expression();
        Expression arg2 = new Expression();

        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        Assert.assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValueNotEqualToFalse() {
        EvaluationContext context = new EvaluationContext();

        Expression arg1 = new Expression();
        Expression arg2 = new Expression();

        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        Assert.assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();

        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        Assert.assertEquals("=", coreOperationEqual.getSymbol());
    }
}