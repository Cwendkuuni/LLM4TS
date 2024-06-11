

package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionStub(10), new ExpressionStub(5));
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));

        operation = new CoreOperationLessThanOrEqual(new ExpressionStub(5), new ExpressionStub(10));
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));

        operation = new CoreOperationLessThanOrEqual(new ExpressionStub(10), new ExpressionStub(10));
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionStub(10), new ExpressionStub(5));
        Assert.assertEquals("<=", operation.getSymbol());
    }

    class ExpressionStub extends Expression {
        private Object value;

        public ExpressionStub(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}
