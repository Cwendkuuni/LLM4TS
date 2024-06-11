package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_Boolean_Boolean() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new JXPathContext(), new NodeInfo[0]);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected Boolean.TRUE", result instanceof Boolean);
        assertTrue("Expected true", ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationEqual_Boolean_Boolean_False() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new JXPathContext(), new NodeInfo[0]);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected Boolean.FALSE", result instanceof Boolean);
        assertFalse("Expected false", ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationEqual_Number_Number() {
        Expression arg1 = new CoreConstantExpression(Double.valueOf(1.0));
        Expression arg2 = new CoreConstantExpression(Double.valueOf(1.0));

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new JXPathContext(), new NodeInfo[0]);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected Boolean.TRUE", result instanceof Boolean);
        assertTrue("Expected true", ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationEqual_Number_Number_False() {
        Expression arg1 = new CoreConstantExpression(Double.valueOf(1.0));
        Expression arg2 = new CoreConstantExpression(Double.valueOf(2.0));

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new JXPathContext(), new NodeInfo[0]);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected Boolean.FALSE", result instanceof Boolean);
        assertFalse("Expected false", ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationEqual_String_String() {
        Expression arg1 = new CoreConstantExpression("foo");
        Expression arg2 = new CoreConstantExpression("foo");

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new JXPathContext(), new NodeInfo[0]);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected Boolean.TRUE", result instanceof Boolean);
        assertTrue("Expected true", ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationEqual_String_String_False() {
        Expression arg1 = new CoreConstantExpression("foo");
        Expression arg2 = new CoreConstantExpression("bar");

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new JXPathContext(), new NodeInfo[0]);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected Boolean.FALSE", result instanceof Boolean);
        assertFalse("Expected false", ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationEqual_Object_Object_Null() {
        Expression arg1 = new CoreConstantExpression(Integer.valueOf(1));
        Expression arg2 = null;

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new JXPathContext(), new NodeInfo[0]);

        Object result = coreOperationEqual.computeValue(context);
        assertNull("Expected null", result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationEqual_Object_Object_InvalidType() {
        Expression arg1 = new CoreConstantExpression("foo");
        Expression arg2 = new CoreNumberExpression(Double.valueOf(1.0));

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new JXPathContext(), new NodeInfo[0]);

        Object result = coreOperationEqual.computeValue(context);
        fail("Should throw ClassCastException");
    }
}