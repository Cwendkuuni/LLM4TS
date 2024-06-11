package org.apache.commons.jxpath.ri.compiler;

import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new ExpressionMock(), new ExpressionMock());
        EvalContext context = new EvalContextMock();

        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new ExpressionMock(), new ExpressionMock());

        String symbol = expression.getSymbol();

        Assert.assertEquals("<=", symbol);
    }
}

class ExpressionMock extends Expression {
    public Object computeValue(EvalContext context) {
        return null;
    }
}

class EvalContextMock extends EvalContext {
    public double doubleValue(Object obj) {
        return 0;
    }
}