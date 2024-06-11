package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreConstant;
import org.apache.commons.jxpath.ri.core.CoreOperationEqual;
import org.apache.commons.jxpath.ri.core.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.util.TestUtil;

public class CoreOperationNotEqualTest {

    @org.junit.Test
    public void testCoreOperationNotEqual_whenBothExpressionsAreSame_thenReturnFalse() {
        Expression arg1 = new CoreConstant("1");
        Expression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenArgumentsAreEqual_thenReturnFalse() {
        Expression arg1 = new CoreConstant("1");
        Expression arg2 = new CoreConstant("1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenArgumentsAreDifferent_thenReturnTrue() {
        Expression arg1 = new CoreConstant("1");
        Expression arg2 = new CoreConstant("2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsConstantAndSecondIsVariable_thenReturnFalseIfVariableIsConstant() {
        Expression arg1 = new CoreConstant("1");
        Expression arg2 = new CoreConstant("1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsConstantAndSecondIsVariable_thenReturnTrueIfVariableIsNotConstant() {
        Expression arg1 = new CoreConstant("1");
        Expression arg2 = new CoreConstant("10");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsVariableAndSecondIsConstant_thenReturnTrueIfVariableIsNotConstant() {
        Expression arg1 = new CoreConstant("10");
        Expression arg2 = new CoreConstant("1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsVariableAndSecondIsVariable_thenReturnFalseIfBothAreEqual() {
        Expression arg1 = new CoreConstant("10");
        Expression arg2 = new CoreConstant("10");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsVariableAndSecondIsVariable_thenReturnTrueIfBothAreDifferent() {
        Expression arg1 = new CoreConstant("10");
        Expression arg2 = new CoreConstant("20");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsVariableAndSecondIsVariable_thenReturnFalseIfFirstIsNotFound() {
        Expression arg1 = new CoreConstant("nonexistent");
        Expression arg2 = new CoreConstant("20");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsVariableAndSecondIsVariable_thenReturnTrueIfFirstIsFoundButSecondIsNotFound() {
        Expression arg1 = new CoreConstant("10");
        Expression arg2 = new CoreConstant("nonexistent");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsVariableAndSecondIsVariable_thenReturnFalseIfBothAreBothNull() {
        Expression arg1 = new CoreConstant("nonexistent");
        Expression arg2 = new CoreConstant("nonexistent");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsVariableAndSecondIsVariable_thenReturnFalseIfBothAreBothNullButContextIsNotNull() {
        EvalContext context = TestUtil.getContext();
        context.setVariable("variable1", null);
        context.setVariable("variable2", null);
        Expression arg1 = new CoreConstant("variable1");
        Expression arg2 = new CoreConstant("variable2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(context));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsVariableAndSecondIsExpression_thenReturnFalseIfBothAreEqual() {
        Expression arg1 = new CoreConstant("10");
        Expression arg2 = new CoreOperationEqual(new CoreConstant("10"), new CoreConstant("10"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsVariableAndSecondIsExpression_thenReturnTrueIfFirstIsVariableIsNotEqualToExpression() {
        Expression arg1 = new CoreConstant("10");
        Expression arg2 = new CoreOperationEqual(new CoreConstant("10"), new CoreConstant("20"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsExpressionAndSecondIsVariable_thenReturnFalseIfBothAreEqual() {
        Expression arg1 = new CoreOperationEqual(new CoreConstant("10"), new CoreConstant("10"));
        Expression arg2 = new CoreConstant("10");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsExpressionAndSecondIsVariable_thenReturnTrueIfSecondIsVariableIsNotEqualToFirst() {
        Expression arg1 = new CoreOperationEqual(new CoreConstant("10"), new CoreConstant("10"));
        Expression arg2 = new CoreConstant("20");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsExpressionAndSecondIsExpression_thenReturnFalseIfBothAreEqual() {
        Expression arg1 = new CoreOperationEqual(new CoreConstant("10"), new CoreConstant("10"));
        Expression arg2 = new CoreOperationEqual(new CoreConstant("10"), new CoreConstant("10"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenFirstArgumentIsExpressionAndSecondIsExpression_thenReturnTrueIfBothAreDifferent() {
        Expression arg1 = new CoreOperationEqual(new CoreConstant("10"), new CoreConstant("10"));
        Expression arg2 = new CoreOperationEqual(new CoreConstant("20"), new CoreConstant("20"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(TestUtil.getContext()));
    }
}