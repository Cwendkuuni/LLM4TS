package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_False() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_True() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_NullArg1() {
        Expression arg1 = null;
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_NullArg2() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_NullBoth() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_DifferentTypes() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new StringExpression("1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_SameTypesButDifferentValues() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_SameTypesButSameValues() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_ObjectAndBoolean() {
        Expression arg1 = new ConstantExpression("hello");
        Expression arg2 = new ConstantExpression(true);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_ObjectAndString() {
        Expression arg1 = new ConstantExpression("hello");
        Expression arg2 = new StringExpression("world");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_StringAndObject() {
        Expression arg1 = new StringExpression("hello");
        Expression arg2 = new ConstantExpression("world");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }

    @Test
    public void testCoreOperationNotEqual_StringAndString() {
        Expression arg1 = new StringExpression("hello");
        Expression arg2 = new StringExpression("world");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvaluationContext()));
    }
}