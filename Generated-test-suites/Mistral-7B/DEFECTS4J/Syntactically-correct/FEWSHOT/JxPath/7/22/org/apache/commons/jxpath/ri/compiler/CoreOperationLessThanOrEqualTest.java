package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValueTrue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(1.5)),
                new ConstantExpression(new Double(2.1)));

        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueFalse1() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(2.1)),
                new ConstantExpression(new Double(1.5)));

        assertEquals(Boolean.FALSE, expression.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueFalse2() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(1.5)),
                new ConstantExpression(new Double(2.1)));

        assertEquals(Boolean.FALSE, expression.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueNull() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(null),
                new ConstantExpression(new Double(2.1)));

        assertNull(expression.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueNull2() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(2.1)),
                new ConstantExpression(null));

        assertNull(expression.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(1.5)),
                new ConstantExpression(new Double(2.1)));

        assertEquals("<=", expression.getSymbol());
    }

    @Test
    public void testGetSymbolWithMixedArgs() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new VariableExpression("x"),
                new ConstantExpression(new Double(2.1)));

        assertEquals("<=", expression.getSymbol());
    }
}