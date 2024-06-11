import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.core.ExpressionUtil;
import org.junit.Test;
import org.junit.Assert;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testGreaterThanOrEqual() {
        Expression arg1 = new Literal(new Double(5.0));
        Expression arg2 = new Literal(new Double(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfo(1, "root", null, null, null, null, null));

        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1 = new Literal(new Double(5.0));
        arg2 = new Literal(new Double(4.0));

        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext();
        context.setCurrentNode(new NodeInfo(1, "root", null, null, null, null, null));

        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1 = new Literal(new Double(4.0));
        arg2 = new Literal(new Double(5.0));

        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext();
        context.setCurrentNode(new NodeInfo(1, "root", null, null, null, null, null));

        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        arg1 = new Literal(new Double(NaN));
        arg2 = new Literal(new Double(5.0));

        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext();
        context.setCurrentNode(new NodeInfo(1, "root", null, null, null, null, null));

        result = expression.computeValue(context);
        Assert.assertNull(result);

        arg1 = new Literal(new Double(5.0));
        arg2 = new Literal(new Double(NaN));

        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext();
        context.setCurrentNode(new NodeInfo(1, "root", null, null, null, null, null));

        result = expression.computeValue(context);
        Assert.assertNull(result);
    }

    @Test
    public void testDifferentTypes() {
        Expression arg1 = new Literal(new Double(5.0));
        Expression arg2 = new Literal(new Integer(5));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfo(1, "root", null, null, null, null, null));

        try {
            expression.computeValue(context);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}