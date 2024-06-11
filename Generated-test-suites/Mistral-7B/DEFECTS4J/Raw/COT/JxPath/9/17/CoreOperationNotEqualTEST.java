package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.TestCase;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest extends TestCase {

    /**
     * Test constructor.
     */
    public void testCoreOperationNotEqualConstructor() {
        Expression arg1 = new CoreOperationAdd(new Const(1), new Const(1));
        Expression arg2 = new Const(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("Expected CoreOperationNotEqual", CoreOperationNotEqual.class, coreOperationNotEqual.getClass());
    }

    /**
     * Test computeValue method with equal values.
     */
    public void testComputeValueWithEqualValues() {
        Expression arg1 = new Const(1);
        Expression arg2 = new Const(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse("Expected false", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    /**
     * Test computeValue method with different values.
     */
    public void testComputeValueWithDifferentValues() {
        Expression arg1 = new Const(1);
        Expression arg2 = new Const(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue("Expected true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    /**
     * Test computeValue method with null values.
     */
    public void testComputeValueWithNullValues() {
        Expression arg1 = new Const(1);
        Expression arg2 = new Null();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue("Expected true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    /**
     * Test computeValue method with null arg1 and non-null arg2.
     */
    public void testComputeValueWithNullArg1AndNonnullArg2() {
        Expression arg1 = new Null();
        Expression arg2 = new Const(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse("Expected false", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    /**
     * Test computeValue method with null arg2 and non-null arg1.
     */
    public void testComputeValueWithNullArg2AndNonnullArg1() {
        Expression arg1 = new Const(1);
        Expression arg2 = new Null();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue("Expected true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    /**
     * Test computeValue method with both null values.
     */
    public void testComputeValueWithBothNullValues() {
        Expression arg1 = new Null();
        Expression arg2 = new Null();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue("Expected true", coreOperationNotEqual.computeValue(context).booleanValue());
    }

    /**
     * Test getSymbol method.
     */
    public void testGetSymbol() {
        Expression arg1 = new Const(1);
        Expression arg2 = new Const(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("Expected '!= '", "!=", symbol);
    }
}