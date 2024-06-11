package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreNumberExpression(1.0);
        arg2 = new CoreNumberExpression(1.0);
        operation = new CoreOperationLessThan(arg1, arg2);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreNumberExpression(1.0);
        arg2 = new CoreNumberExpression(0.5);
        operation = new CoreOperationLessThan(arg1, arg2);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithNullArg() {
        Expression arg1 = null;
        Expression arg2 = new CoreNumberExpression(1.0);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithNullArg2() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = null;
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithMismatchedTypes() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreStringExpression("1");
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithInvalidTypes() {
        Expression arg1 = new CoreNumberExpression(new Object());
        Expression arg2 = new CoreNumberExpression(1.0);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
    }
}