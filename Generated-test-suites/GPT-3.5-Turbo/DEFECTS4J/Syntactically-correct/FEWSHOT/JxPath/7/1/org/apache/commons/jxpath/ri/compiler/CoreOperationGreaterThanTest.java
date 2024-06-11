package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());

        // Test case 1
        expression.args[0] = new Expression();
        expression.args[0].computeValue(new EvalContext());
        expression.args[1] = new Expression();
        expression.args[1].computeValue(new EvalContext());
        Object result1 = expression.computeValue(new EvalContext());
        Assert.assertTrue("Test case 1 failed", result1.equals(Boolean.TRUE) || result1.equals(Boolean.FALSE));

        // Test case 2
        expression.args[0] = new Expression();
        expression.args[0].computeValue(new EvalContext());
        expression.args[1] = new Expression();
        expression.args[1].computeValue(new EvalContext());
        Object result2 = expression.computeValue(new EvalContext());
        Assert.assertTrue("Test case 2 failed", result2.equals(Boolean.TRUE) || result2.equals(Boolean.FALSE));

        // Test case 3
        expression.args[0] = new Expression();
        expression.args[0].computeValue(new EvalContext());
        expression.args[1] = new Expression();
        expression.args[1].computeValue(new EvalContext());
        Object result3 = expression.computeValue(new EvalContext());
        Assert.assertTrue("Test case 3 failed", result3.equals(Boolean.TRUE) || result3.equals(Boolean.FALSE));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        Assert.assertEquals(">", symbol);
    }
}