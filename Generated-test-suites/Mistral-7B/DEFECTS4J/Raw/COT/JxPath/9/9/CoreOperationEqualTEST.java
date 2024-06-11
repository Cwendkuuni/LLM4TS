package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualArg;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqualWithBooleanArg1AndBooleanArg2() {
        Expression arg1 = new CoreOperationEqualArg(true);
        Expression arg2 = new CoreOperationEqualArg(true);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqualWithBooleanArg1AndFalseArg2() {
        Expression arg1 = new CoreOperationEqualArg(true);
        Expression arg2 = new CoreOperationEqualArg(false);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqualWithIntegerArg1AndIntegerArg2() {
        Expression arg1 = new CoreOperationEqualArg(new IntValue(1));
        Expression arg2 = new CoreOperationEqualArg(new IntValue(1));
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqualWithIntegerArg1AndIntegerArg2Different() {
        Expression arg1 = new CoreOperationEqualArg(new IntValue(1));
        Expression arg2 = new CoreOperationEqualArg(new IntValue(2));
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationEqualWithDifferentTypesArg1AndArg2() {
        Expression arg1 = new CoreOperationEqualArg(new IntValue(1));
        Expression arg2 = new CoreOperationEqualArg(new StringValue("1"));
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationEqualWithNullArg1() {
        Expression arg1 = null;
        Expression arg2 = new CoreOperationEqualArg(new IntValue(1));
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationEqualWithNullArg2() {
        Expression arg1 = new CoreOperationEqualArg(new IntValue(1));
        Expression arg2 = null;
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }
}