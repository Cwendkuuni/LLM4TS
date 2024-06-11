package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestUtils;

import java.math.BigDecimal;
import java.util.List;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValueTrue() {
        EvalContext context = TestUtils.createEvalContext();

        Expression arg1 = TestUtils.createNumberExpression(new BigDecimal("5.5"));
        Expression arg2 = TestUtils.createNumberExpression(new BigDecimal("4.4"));

        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueFalse() {
        EvalContext context = TestUtils.createEvalContext();

        Expression arg1 = TestUtils.createNumberExpression(new BigDecimal("4.5"));
        Expression arg2 = TestUtils.createNumberExpression(new BigDecimal("5.5"));

        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithStrings() {
        EvalContext context = TestUtils.createEvalContext();

        Expression arg1 = TestUtils.createStringExpression("5");
        Expression arg2 = TestUtils.createStringExpression("4");

        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithNull() {
        EvalContext context = TestUtils.createEvalContext();

        Expression arg1 = TestUtils.createNumberExpression(new BigDecimal("5.5"));
        Expression arg2 = TestUtils.createNumberExpression(null);

        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

        Object result = expression.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        EvalContext context = TestUtils.createEvalContext();

        Expression arg1 = TestUtils.createNumberExpression(new BigDecimal("5.5"));
        Expression arg2 = TestUtils.createStringExpression("4");

        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

        Object result = expression.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(TestUtils.createNumberExpression(new BigDecimal("5.5")), TestUtils.createNumberExpression(new BigDecimal("4.4")));

        String symbol = expression.getSymbol();

        assertEquals(">", symbol);
    }
}