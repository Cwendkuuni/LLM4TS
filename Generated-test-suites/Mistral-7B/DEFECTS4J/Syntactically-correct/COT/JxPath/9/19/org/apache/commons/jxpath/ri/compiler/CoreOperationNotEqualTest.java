package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Test cases for CoreOperationNotEqual class.
 */
public class CoreOperationNotEqualTest {

    // Test case for constructor
    @org.junit.Test
    public void testCoreOperationNotEqualConstruction() {
        Expression arg1 = new CoreOperationAdd(new Const(1), new Const(2));
        Expression arg2 = new Const(3);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=" , coreOperationNotEqual.getSymbol());
        assertNotNull(coreOperationNotEqual.getLeftExpression());
        assertNotNull(coreOperationNotEqual.getRightExpression());
    }

    // Test case for computeValue - equal values
    @org.junit.Test
    public void testCoreOperationNotEqualComputeValue_equalValues() {
        Expression arg1 = new Const(1);
        Expression arg2 = new Const(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(new EvalContext());
        assertFalse(result.booleanValue());
    }

    // Test case for computeValue - not equal values
    @org.junit.Test
    public void testCoreOperationNotEqualComputeValue_notEqualValues() {
        Expression arg1 = new Const(1);
        Expression arg2 = new Const(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result.booleanValue());
    }

    // Test case for computeValue - one of the arguments is null
    @org.junit.Test(expected = NullPointerException.class)
    public void testCoreOperationNotEqualComputeValue_nullArgument() {
        Expression arg1 = new Const(1);
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        coreOperationNotEqual.computeValue(new EvalContext());
    }

    // Test case for computeValue - both arguments are null
    @org.junit.Test(expected = NullPointerException.class)
    public void testCoreOperationNotEqualComputeValue_bothNullArguments() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        coreOperationNotEqual.computeValue(new EvalContext());
    }

    // Test case for computeValue - different types of arguments
    @org.junit.Test
    public void testCoreOperationNotEqualComputeValue_differentTypes() {
        Expression arg1 = new Const(1);
        Expression arg2 = new NumberNode(new Double(2.0));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result.booleanValue());
    }
}