package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThanBasicFunctionality() {
        // Given
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreNumber(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        // When
        Object result = coreOperationGreaterThan.computeValue(context);

        // Then
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanEdgeCases() {
        // Given
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreNumber(5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        // When and Then
        Assert.assertEquals(Boolean.FALSE, coreOperationGreaterThan.computeValue(context));

        // Given
        arg1 = new CoreNumber(Double.MAX_VALUE);
        arg2 = new CoreNumber(Double.MIN_VALUE);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet());

        // When
        Object result = coreOperationGreaterThan.computeValue(context);

        // Then
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanTypeMismatch() {
        // Given
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreBoolean(Boolean.TRUE);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        // When
        coreOperationGreaterThan.computeValue(context);

        // Expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanNullArguments() {
        // Given
        Expression arg1 = null;
        Expression arg2 = new CoreNumber(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        // When
        coreOperationGreaterThan.computeValue(context);

        // Expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanNullContext() {
        // Given
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreNumber(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = null;

        // When
        coreOperationGreaterThan.computeValue(context);

        // Expected exception
    }
}