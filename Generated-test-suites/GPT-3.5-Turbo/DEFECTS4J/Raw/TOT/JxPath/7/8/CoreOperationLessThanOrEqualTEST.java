package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    
    private CoreOperationLessThanOrEqual operator;
    private EvalContext context;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();      
        operator = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext();
    }

    @Test
    public void testComputeValue_Positive() {
        double arg1 = 5.0;
        double arg2 = 10.0;
        context.set(arg1);
        context.set(arg2);
        
        Object result = operator.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_Negative() {
        double arg1 = 10.0;
        double arg2 = 5.0;
        context.set(arg1);
        context.set(arg2);
        
        Object result = operator.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullContext() {
        operator.computeValue(null);
    }

    @Test
    public void testGetSymbol() {
        String expected = "<=";
        String result = operator.getSymbol();

        Assert.assertEquals(expected, result);
    }
}