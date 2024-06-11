package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(3.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Basic test cases for all public methods

    @Test
    public void testComputeValueWithDoubleValuesGreaterThan() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(3.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithDoubleValuesEqual() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithDoubleValuesLessThan() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(3.0));
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }

    // Edge cases and exception handling test cases

    @Test(expected = ClassCastException.class)
    public void testComputeValueWithNonNumericArg1() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), "5");
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(3.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueWithNonNumericArg2() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), "3");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

    @Test(expected = NumberFormatException.class)
    public void testComputeValueWithInvalidDoubleValueArg1() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), "abc");
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(3.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

    @Test(expected = NumberFormatException.class)
    public void testComputeValueWithInvalidDoubleValueArg2() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), "abc");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

    @Test
    public void testComputeValueWithNaNAndFiniteNumber() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), Double.NaN);
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(3.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithNegativeNumberAndPositiveNumber() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(-3.0));
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithEqualDoubleValues() {
        CoreExpression arg1 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreExpression arg2 = new CoreLiteralExpression(new InfoSet().getDocument(), new Double(5.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}