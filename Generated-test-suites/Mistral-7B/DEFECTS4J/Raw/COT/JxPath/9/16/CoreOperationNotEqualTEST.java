package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_whenArg1EqArg2_thenReturnFalse() {
        CoreOperationCompare arg1 = new CoreOperationCompare(newLiteral(5), newLiteral(5));
        CoreOperationCompare arg2 = new CoreOperationNotEqual(arg1, newLiteral(5));
        EvalContext context = new EvalContext();
        Object result = arg2.computeValue(context);
        assertFalse("Expected false", result);
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1NotEqArg2_thenReturnTrue() {
        CoreOperationCompare arg1 = new CoreOperationCompare(newLiteral(5), newLiteral(6));
        CoreOperationNotEqual arg2 = new CoreOperationNotEqual(arg1, newLiteral(5));
        EvalContext context = new EvalContext();
        Object result = arg2.computeValue(context);
        assertTrue("Expected true", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_whenArgIsNull_thenThrowIllegalArgumentException() {
        CoreOperationCompare arg1 = new CoreOperationCompare(newLiteral(5), null);
        CoreOperationNotEqual arg2 = new CoreOperationNotEqual(arg1, newLiteral(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_whenArg2IsNull_thenThrowIllegalArgumentException() {
        CoreOperationCompare arg1 = new CoreOperationCompare(newLiteral(5), newLiteral(5));
        CoreOperationNotEqual arg2 = new CoreOperationNotEqual(arg1, null);
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfNumber_arg2IsInstanceOfString_thenThrowRuntimeException() {
        CoreOperationCompare arg1 = new CoreOperationCompare(newLiteral(5.0), newLiteral(5));
        CoreOperationNotEqual arg2 = new CoreOperationNotEqual(arg1, newLiteral("5"));
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfString_arg2IsInstanceOfNumber_thenThrowRuntimeException() {
        CoreOperationCompare arg1 = new CoreOperationCompare(newLiteral("5"), newLiteral(5.0));
        CoreOperationNotEqual arg2 = new CoreOperationNotEqual(arg1, newLiteral(5));
    }
}