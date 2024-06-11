package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import org.junit.Assert;

public class CoreOperationEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertNotNull(operationEqual);
    }

    @Test
    public void testComputeValue_EqualArgs() {
        EvalContext context = new EvalContext();
        Expression arg1 = new NodePointerExpression();
        Expression arg2 = new NodePointerExpression();
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, operationEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_UnequalArgs() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression("test");
        Expression arg2 = new ConstantExpression("sample");
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertEquals(Boolean.FALSE, operationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(new DummyExpression(), new DummyExpression());
        Assert.assertEquals("=", operationEqual.getSymbol());
    }

    private static class DummyExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }

    private static class NodePointerExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return context.getNodePointer();
        }
    }

    private static class ConstantExpression extends Expression {
        private Object value;

        public ConstantExpression(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}