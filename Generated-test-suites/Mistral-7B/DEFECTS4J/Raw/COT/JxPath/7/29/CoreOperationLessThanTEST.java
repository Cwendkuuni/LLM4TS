package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression arg1 = new Expression() {};
        Expression arg2 = new Expression() {};
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(coreOperationLessThan);
        assertArrayEquals(new Expression[] { arg1, arg2 }, coreOperationLessThan.getArgs());
    }

    /**
     * Test computeValue method with positive numbers.
     */
    @Test
    public void testComputeValueWithPositiveNumbers() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        boolean result = coreOperationLessThan.computeValue(new EvalContext()).booleanValue();
        assertTrue(result);
    }

    /**
     * Test computeValue method with negative numbers.
     */
    @Test
    public void testComputeValueWithNegativeNumbers() {
        Expression arg1 = new CoreNumberExpression(-1.0);
        Expression arg2 = new CoreNumberExpression(-2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        boolean result = coreOperationLessThan.computeValue(new EvalContext()).booleanValue();
        assertTrue(result);
    }

    /**
     * Test computeValue method with equal numbers.
     */
    @Test
    public void testComputeValueWithEqualNumbers() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreNumberExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        boolean result = coreOperationLessThan.computeValue(new EvalContext()).booleanValue();
        assertFalse(result);
    }

    /**
     * Test computeValue method with one argument as a number and another as a boolean.
     */
    @Test(expected = RuntimeException.class)
    public void testComputeValueWithMixedTypes() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreBooleanExpression(true);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        coreOperationLessThan.computeValue(new EvalContext());
    }

    /**
     * Test computeValue method with one argument as a number and another as a string.
     */
    @Test(expected = RuntimeException.class)
    public void testComputeValueWithMixedTypesString() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreStringExpression("2");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        coreOperationLessThan.computeValue(new EvalContext());
    }

    /**
     * Test computeValue method with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArguments() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        coreOperationLessThan.computeValue(new EvalContext());
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNumberExpression(1.0), new CoreNumberExpression(2.0));
        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}