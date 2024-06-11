package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for CoreOperationNotEqual class.
 */
public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualConstructor() {
        Expression arg1 = new CoreOperationAdd(new CoreConstantExpression(1), new CoreConstantExpression(2));
        Expression arg2 = new CoreConstantExpression(3);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!={1+2, 3}", coreOperationNotEqual.getSymbol());
        assertSame(arg1, coreOperationNotEqual.getLeftOperand());
        assertSame(arg2, coreOperationNotEqual.getRightOperand());
    }

    @Test
    public void testCoreOperationNotEqualWithNullLeftOperand() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, new CoreConstantExpression(1));
        assertNull("Left operand should be null", coreOperationNotEqual.getLeftOperand());
    }

    @Test
    public void testCoreOperationNotEqualWithNullRightOperand() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreConstantExpression(1), null);
        assertNull("Right operand should be null", coreOperationNotEqual.getRightOperand());
    }

    @Test
    public void testCoreOperationNotEqualWithSameValues() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse("Expected false, but got true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithDifferentValues() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue("Expected true, but got false", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualWithIncompatibleTypes() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreOperationAdd(new CoreConstantExpression(1), new CoreConstantExpression(2));
        new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationNotEqualWithStringLeftOperandAndNumberRightOperand() {
        Expression arg1 = new CoreConstantExpression("hello");
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse("Expected false, but got true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithNumberLeftOperandAndStringRightOperand() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression("hello");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue("Expected true, but got false", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithNullValues() {
        Expression arg1 = new CoreConstantExpression(null);
        Expression arg2 = new CoreConstantExpression(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue("Expected true, but got false", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanLeftOperandAndBooleanRightOperand() {
        Expression arg1 = new CoreConstantExpression(true);
        Expression arg2 = new CoreConstantExpression(false);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse("Expected false, but got true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanLeftOperandAndBooleanRightOperandTrue() {
        Expression arg1 = new CoreConstantExpression(true);
        Expression arg2 = new CoreConstantExpression(true);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse("Expected false, but got true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanLeftOperandAndNullRightOperand() {
        Expression arg1 = new CoreConstantExpression(true);
        Expression arg2 = new CoreConstantExpression(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue("Expected true, but got false", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithNullLeftOperandAndBooleanRightOperand() {
        Expression arg1 = new CoreConstantExpression(null);
        Expression arg2 = new CoreConstantExpression(true);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse("Expected false, but got true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanLeftOperandAndBooleanRightOperandFalse() {
        Expression arg1 = new CoreConstantExpression(false);
        Expression arg2 = new CoreConstantExpression(false);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse("Expected false, but got true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanLeftOperandAndBooleanRightOperandFalseAndNullContext() {
        Expression arg1 = new CoreConstantExpression(false);
        Expression arg2 = new CoreConstantExpression(false);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse("Expected false, but got null", coreOperationNotEqual.computeValue(null).booleanValue());
    }
}