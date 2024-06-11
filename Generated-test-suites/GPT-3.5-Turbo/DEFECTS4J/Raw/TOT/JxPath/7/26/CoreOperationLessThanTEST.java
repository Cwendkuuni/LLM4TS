import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new ExpressionMock(), new ExpressionMock());

        // Typical use cases
        EvalContext context1 = new EvalContext();
        context1.setContextNode(new NodeMock(5));
        context1.setSingleNodeTest(true);
        Object result1 = lessThan.computeValue(context1);
        Assert.assertEquals(Boolean.TRUE, result1);

        EvalContext context2 = new EvalContext();
        context2.setContextNode(new NodeMock(5));
        context2.setSingleNodeTest(false);
        Object result2 = lessThan.computeValue(context2);
        Assert.assertEquals(Boolean.FALSE, result2);

        // Edge cases
        EvalContext context3 = new EvalContext();
        context3.setContextNode(null);
        context3.setSingleNodeTest(true);
        Object result3 = lessThan.computeValue(context3);
        Assert.assertEquals(Boolean.FALSE, result3);

        EvalContext context4 = new EvalContext();
        context4.setContextNode(new NodeMock(Double.MAX_VALUE));
        context4.setSingleNodeTest(true);
        Object result4 = lessThan.computeValue(context4);
        Assert.assertEquals(Boolean.TRUE, result4);

        EvalContext context5 = new EvalContext();
        context5.setContextNode(new NodeMock(Double.MIN_VALUE));
        context5.setSingleNodeTest(true);
        Object result5 = lessThan.computeValue(context5);
        Assert.assertEquals(Boolean.FALSE, result5);

        // Error scenarios
        EvalContext context6 = new EvalContext();
        context6.setContextNode(new NodeMock("Invalid Number"));
        context6.setSingleNodeTest(true);
        Object result6 = lessThan.computeValue(context6);
        Assert.assertEquals(Boolean.FALSE, result6);
    }

    // Mock classes for testing
    private class ExpressionMock extends Expression {
        public ExpressionMock() {
        }

        @Override
        public Object computeValue(EvalContext context) {
            return InfoSetUtil.doubleValue(context.getContextNode());
        }
    }

    private class NodeMock {
        private Object node;

        public NodeMock(Object node) {
            this.node = node;
        }

        public Object getNode() {
            return node;
        }
    }
}