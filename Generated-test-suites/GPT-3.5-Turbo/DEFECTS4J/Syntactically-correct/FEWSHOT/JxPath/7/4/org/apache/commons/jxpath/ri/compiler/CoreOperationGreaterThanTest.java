package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ExpressionMock(), new ExpressionMock());
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ExpressionMock(), new ExpressionMock());
        String symbol = operation.getSymbol();
        Assert.assertNotNull(symbol);
        Assert.assertEquals(">", symbol);
    }
    
    private class ExpressionMock extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}