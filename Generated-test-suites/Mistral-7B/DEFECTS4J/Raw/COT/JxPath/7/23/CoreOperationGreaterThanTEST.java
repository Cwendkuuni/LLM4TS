package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.junit.Test;

/**
 * Test for CoreOperationGreaterThan class.
 *
 * @author Your Name
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThanConstructor() {
        Expression arg1 = new SimpleNode(new SimpleNode(new SimpleNode(1.0)));
        Expression arg2 = new SimpleNode(new SimpleNode(2.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertSame(CoreOperationRelationalExpression.class, coreOperationGreaterThan.getClass());
        assertArrayEquals(new Expression[] { arg1, arg2 }, coreOperationGreaterThan.getArgs());
    }

    @Test
    public void testComputeValue_whenArgumentsAreBothPositiveNumbersAndFirstIsGreaterThanSecond_thenReturnTrue() {
        CoreExpression arg1 = new CoreNumber(3.0);
        CoreExpression arg2 = new CoreNumber(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArgumentsAreBothPositiveNumbersAndFirstIsLessThanOrEqualToSecond_thenReturnFalse() {
        CoreExpression arg1 = new CoreNumber(2.0);
        CoreExpression arg2 = new CoreNumber(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenArgumentsAreNotBothNumbers_thenThrowIllegalArgumentException() {
        CoreExpression arg1 = new CoreString("1");
        CoreExpression arg2 = new CoreNumber(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenOneArgumentIsNull_thenThrowIllegalArgumentException() {
        CoreExpression arg1 = new CoreNull();
        CoreExpression arg2 = new CoreNumber(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreNumber(1.0), new CoreNumber(2.0));
        assertEquals(">", coreOperationGreaterThan.getSymbol());
    }

}