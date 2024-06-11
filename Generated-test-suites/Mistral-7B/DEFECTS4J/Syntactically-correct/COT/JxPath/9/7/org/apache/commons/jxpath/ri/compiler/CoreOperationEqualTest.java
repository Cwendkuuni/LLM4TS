package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.test.TestSupport;

public class CoreOperationEqualTest {

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenBothArgsAreSame_thenReturnsTrue() {
        Expression arg1 = TestSupport.createLiteralExpression(1);
        Expression arg2 = arg1;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenArg1IsEqualArg2_thenReturnsTrue() {
        Expression arg1 = TestSupport.createLiteralExpression("abc");
        Expression arg2 = arg1;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenArg1IsOfDifferentTypeThanArg2_thenThrowsTypeMismatchException() {
        Expression arg1 = TestSupport.createLiteralExpression(1);
        Expression arg2 = TestSupport.createLiteralExpression("abc");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        try {
            coreOperationEqual.computeValue(new EvalContext());
            fail("TypeMismatchException expected");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenArg1IsBooleanTrueAndArg2IsBooleanFalse_thenReturnsFalse() {
        Expression arg1 = TestSupport.createLiteralExpression(Boolean.TRUE);
        Expression arg2 = TestSupport.createLiteralExpression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenArg1IsBooleanFalseAndArg2IsBooleanTrue_thenReturnsFalse() {
        Expression arg1 = TestSupport.createLiteralExpression(Boolean.FALSE);
        Expression arg2 = TestSupport.createLiteralExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenArg1IsNumberAndArg2IsNumberWithDifferentValues_thenReturnsFalse() {
        Expression arg1 = TestSupport.createLiteralExpression(1.0);
        Expression arg2 = TestSupport.createLiteralExpression(2.0);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenArg1IsStringAndArg2IsStringWithDifferentValues_thenReturnsFalse() {
        Expression arg1 = TestSupport.createLiteralExpression("abc");
        Expression arg2 = TestSupport.createLiteralExpression("def");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenArg1IsNullAndArg2IsNull_thenReturnsTrue() {
        Expression arg1 = TestSupport.createNullExpression();
        Expression arg2 = arg1;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenArg1IsNullAndArg2IsNotNull_thenReturnsFalse() {
        Expression arg1 = TestSupport.createNullExpression();
        Expression arg2 = TestSupport.createLiteralExpression("abc");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testCoreOperationEqual_whenArg1IsNotNullAndArg2IsNull_thenReturnsFalse() {
        Expression arg1 = TestSupport.createLiteralExpression(1);
        Expression arg2 = TestSupport.createNullExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}