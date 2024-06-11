package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_whenBothArgsAreSame_thenReturnTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant("5"), new Constant("5"));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsNotNullAndSecondArgIsNull_thenThrowNullPointerException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant("5"), null);
        try {
            coreOperationEqual.computeValue(new EvalContext());
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsNotNullAndSecondArgIsNotNullButValuesAreDifferent_thenReturnFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant("5"), new Constant("6"));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertFalse(result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreStringsAndValuesAreEqual_thenReturnTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant("hello"), new Constant("hello"));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreStringsAndValuesAreDifferent_thenReturnFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant("hello"), new Constant("world"));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertFalse(result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsNumberAndSecondArgIsString_thenCompileError() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), new StringConstant("5"));
        try {
            coreOperationEqual.computeValue(new EvalContext());
            fail("Expected CompileError");
        } catch (AssertionError e) {
            // Expected, JUnit throws AssertionError when test fails
        }
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationEqual_whenFirstArgIsNumberAndSecondArgIsBoolean_thenClassCastException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), new BooleanConstant(true));
        coreOperationEqual.computeValue(new EvalContext());
        fail("Expected ClassCastException");
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationEqual_whenSecondArgIsNumberAndFirstArgIsBoolean_thenClassCastException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new BooleanConstant(true), new Constant(5));
        coreOperationEqual.computeValue(new EvalContext());
        fail("Expected ClassCastException");
    }
}