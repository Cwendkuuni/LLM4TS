package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest extends CompilerTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = compileExpression("1");
        Expression arg2 = compileExpression("1");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualFalse() {
        Expression arg1 = compileExpression("1");
        Expression arg2 = compileExpression("2");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualNullArg() {
        Expression arg1 = null;
        Expression arg2 = compileExpression("1");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualNullArg2() {
        Expression arg1 = compileExpression("1");
        Expression arg2 = null;
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationEqualDifferentTypes() {
        Expression arg1 = compileExpression("1");
        Expression arg2 = compileExpression("true");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithVoidContext() {
        Expression arg1 = compileExpression("1");
        Expression arg2 = compileExpression("1");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext() {
            @Override
            public Object getVariableValue(String name) {
                return null;
            }
        });
        assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationEqualWithNullResult() {
        Expression arg1 = compileExpression("1");
        Expression arg2 = compileExpression("null");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertNotNull(result);
        assertTrue(result instanceof RuntimeException);
    }

    @Test
    public void testCoreOperationEqualWithZero() {
        Expression arg1 = compileExpression("0");
        Expression arg2 = compileExpression("0");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithOne() {
        Expression arg1 = compileExpression("1");
        Expression arg2 = compileExpression("1");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypesBooleanAndNumber() {
        Expression arg1 = compileExpression("true");
        Expression arg2 = compileExpression("1");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypesStringAndNumber() {
        Expression arg1 = compileExpression("\"hello\"");
        Expression arg2 = compileExpression("1");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypesStringAndString() {
        Expression arg1 = compileExpression("\"hello\"");
        Expression arg2 = compileExpression("\"world\"");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypesStringAndBoolean() {
        Expression arg1 = compileExpression("\"hello\"");
        Expression arg2 = compileExpression("true");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypesNumberAndBoolean() {
        Expression arg1 = compileExpression("1");
        Expression arg2 = compileExpression("true");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypesNumberAndNumber() {
        Expression arg1 = compileExpression("1.1");
        Expression arg2 = compileExpression("2.2");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertNull(result);
    }
}