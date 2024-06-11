package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.apache.commons.jxpath.ri.expr.Variable;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Variable("x"), new Variable("x"));
        Equal equal = new Equal();
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Expression arg1 = new Variable("1");
        Expression arg2 = arg1;
        CoreOperationEqual coreOperationEqual1 = new CoreOperationEqual(x, arg1);
        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(x, y);
        CoreOperationEqual coreOperationEqual3 = new CoreOperationEqual(arg1, y);
        Object result1 = coreOperationEqual1.computeValue(context);
        assertFalse(result1 instanceof Boolean);
        assertNull(result1);
        Object result2 = coreOperationEqual2.computeValue(context);
        assertFalse(result2 instanceof Boolean);
        assertNull(result2);
        Object result3 = coreOperationEqual3.computeValue(context);
        assertFalse(result3 instanceof Boolean);
        assertNull(result3);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Expression arg1 = new Variable("1");
        Expression arg2 = new Number(2.0);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(x, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithDifferentTypesAndValues() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Expression arg1 = new Number(1);
        Expression arg2 = new Number(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithEqualValues() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Expression arg1 = new Number(1);
        Expression arg2 = arg1;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNotEqualValues() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Expression arg1 = new Number(1);
        Expression arg2 = new Number(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }
}