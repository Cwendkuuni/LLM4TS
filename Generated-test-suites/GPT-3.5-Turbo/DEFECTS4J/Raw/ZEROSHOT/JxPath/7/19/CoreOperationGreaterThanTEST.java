
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    private CoreOperationGreaterThan coreOperationGreaterThan;

    @Before
    public void setUp() {
        coreOperationGreaterThan = new CoreOperationGreaterThan(new Expression(), new Expression());
    }

    @Test
    public void testComputeValue_LessThan_ReturnsFalse() {
        EvalContext context = new EvalContext(null);
        context.setNodeSet(new NodeSet());
        double l = 1.0;
        double r = 2.0;
        InfoSetUtil.mockDoubleValue(context, l, r);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_GreaterThan_ReturnsTrue() {
        EvalContext context = new EvalContext(null);
        context.setNodeSet(new NodeSet());
        double l = 2.0;
        double r = 1.0;
        InfoSetUtil.mockDoubleValue(context, l, r);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_Equal_ReturnsFalse() {
        EvalContext context = new EvalContext(null);
        context.setNodeSet(new NodeSet());
        double l = 1.0;
        double r = 1.0;
        InfoSetUtil.mockDoubleValue(context, l, r);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanSymbol() {
        Assert.assertEquals(">", coreOperationGreaterThan.getSymbol());
    }
}
