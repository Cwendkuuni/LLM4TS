package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest {

    /**
     * Test computeValue method with two double values less than each other.
     */
    @Test
    public void testComputeValueWithTwoSmallValues() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationLessThan lessThanExpression = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(null);
        Boolean expectedResult = Boolean.TRUE;
        Boolean actualResult = (Boolean) lessThanExpression.computeValue(context);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test computeValue method with two double values where the first one is greater than the second one.
     */
    @Test
    public void testComputeValueWithFirstValueGreaterThanSecondOne() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(10.0);
        CoreOperationLessThan lessThanExpression = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(null);
        Boolean expectedResult = Boolean.FALSE;
        Boolean actualResult = (Boolean) lessThanExpression.computeValue(context);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbolMethod() {
        CoreOperationLessThan lessThanExpression = new CoreOperationLessThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        String expectedSymbol = "<";
        String actualSymbol = lessThanExpression.getSymbol();
        assertEquals(expectedSymbol, actualSymbol);
    }

    /**
     * Test computeValue method with two LiteralExpressions with the same value.
     */
    @Test
    public void testComputeValueWithTwoIdenticalValues() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationLessThan lessThanExpression = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(null);
        Boolean expectedResult = Boolean.FALSE;
        Boolean actualResult = (Boolean) lessThanExpression.computeValue(context);
        assertEquals(expectedResult, actualResult);
    }
}