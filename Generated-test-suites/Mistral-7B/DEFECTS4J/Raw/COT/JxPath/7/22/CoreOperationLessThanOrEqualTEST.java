package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgumentsAreNumbersAndFirstIsLessThanSecond_thenReturnTrue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(2.5),
                new CoreNumberExpression(5.0)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgumentsAreNumbersAndFirstIsGreaterThanSecond_thenReturnFalse() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(5.0),
                new CoreNumberExpression(2.5)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgumentsAreNumbersAndFirstIsEqualToSecond_thenReturnTrue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(5.0),
                new CoreNumberExpression(5.0)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_whenArgumentsCountIsNotTwo_thenThrowException() {
        new CoreOperationLessThanOrEqual(new Expression[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_whenArgumentsAreNotNumberType_thenThrowException() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreStringExpression("2"),
                new CoreStringExpression("5")
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        // The operation should not be executed, as exception is expected
        assertFalse(result instanceof Boolean);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqual_whenFirstArgumentIsNotNumberType_thenThrowException() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreStringExpression("2"),
                new CoreNumberExpression(5.0)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        // The operation should not be executed, as exception is expected
        assertFalse(result instanceof Boolean);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqual_whenSecondArgumentIsNotNumberType_thenThrowException() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumberExpression(2.5),
                new CoreStringExpression("5")
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        // The operation should not be executed, as exception is expected
        assertFalse(result instanceof Boolean);
    }
}