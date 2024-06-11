package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {
    private final double EPSILON = 0.000001;
    
    @Test
    public void testConstructor() {
        Expression arg1 = new CoreOperationConstant(5);
        Expression arg2 = new CoreOperationConstant(10);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        
        assertEquals("<", lessThan.getSymbol());
        assertTrue(lessThan.args[0] instanceof CoreOperationConstant);
        assertEquals(5, ((CoreOperationConstant) lessThan.args[0]).getValue());
        assertTrue(lessThan.args[1] instanceof CoreOperationConstant);
        assertEquals(10, ((CoreOperationConstant) lessThan.args[1]).getValue());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreOperationConstant(5);
        Expression arg2 = new CoreOperationConstant(10);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        
        arg1 = new CoreOperationConstant(10);
        arg2 = new CoreOperationConstant(5);
        lessThan = new CoreOperationLessThan(arg1, arg2);
        
        result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithDoubleValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreOperationConstant(5.6);
        Expression arg2 = new CoreOperationConstant(10.3);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        
        arg1 = new CoreOperationConstant(9.99);
        arg2 = new CoreOperationConstant(9.999);
        lessThan = new CoreOperationLessThan(arg1, arg2);
        
        result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}