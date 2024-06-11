package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan_whenTwoDoubleValuesAreGreaterThanZero_thenReturnsTrue() {
        CoreExpression arg1 = new CoreConstantExpression(new InfoSet().getDocument(), 5.0);
        CoreExpression arg2 = new CoreConstantExpression(new InfoSet().getDocument(), 3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThan_whenTwoDoubleValuesAreEqual_thenReturnsFalse() {
        CoreExpression arg1 = new CoreConstantExpression(new InfoSet().getDocument(), 5.0);
        CoreExpression arg2 = new CoreConstantExpression(new InfoSet().getDocument(), 5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThan_whenFirstDoubleValueIsGreaterThanSecondDoubleValue_thenReturnsTrue() {
        CoreExpression arg1 = new CoreConstantExpression(new InfoSet().getDocument(), 10.0);
        CoreExpression arg2 = new CoreConstantExpression(new InfoSet().getDocument(), 5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThan_whenFirstDoubleValueIsLessThanOrEqualSecondDoubleValue_thenReturnsFalse() {
        CoreExpression arg1 = new CoreConstantExpression(new InfoSet().getDocument(), 5.0);
        CoreExpression arg2 = new CoreConstantExpression(new InfoSet().getDocument(), 10.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThan_whenArgumentsAreNull_thenThrowsIllegalArgumentException() {
        CoreExpression arg1 = null;
        CoreExpression arg2 = new CoreConstantExpression(new InfoSet().getDocument(), 5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThan_whenSecondArgumentIsNull_thenThrowsIllegalArgumentException() {
        CoreExpression arg1 = new CoreConstantExpression(new InfoSet().getDocument(), 5.0);
        CoreExpression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
    }
}