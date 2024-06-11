package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.TestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.TestHelper;

public class CoreOperationLessThanTest extends TestCase {

    // Test constructor
    public void testCoreOperationLessThanConstructor() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createNumberExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(coreOperationLessThan);
    }

    // Test computeValue method with positive numbers
    public void testComputeValueWithPositiveNumbers() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createNumberExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Object result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = TestHelper.createNumberExpression(1.0);
        arg2 = TestHelper.createNumberExpression(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Test computeValue method with negative numbers
    public void testComputeValueWithNegativeNumbers() {
        Expression arg1 = TestHelper.createNumberExpression(-1.0);
        Expression arg2 = TestHelper.createNumberExpression(-2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Object result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = TestHelper.createNumberExpression(-1.0);
        arg2 = TestHelper.createNumberExpression(-3.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    // Test computeValue method with zero
    public void testComputeValueWithZero() {
        Expression arg1 = TestHelper.createNumberExpression(0.0);
        Expression arg2 = TestHelper.createNumberExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Object result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = TestHelper.createNumberExpression(-0.0);
        arg2 = TestHelper.createNumberExpression(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = TestHelper.createNumberExpression(0.0);
        arg2 = TestHelper.createNumberExpression(0.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Test computeValue method with different data types
    public void testComputeValueWithDifferentDataTypes() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createStringExpression("2");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Object result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertEquals(result, null);

        arg1 = TestHelper.createStringExpression("1");
        arg2 = TestHelper.createNumberExpression(2.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertEquals(result, null);

        arg1 = TestHelper.createNumberExpression(1.0);
        arg2 = TestHelper.createBooleanExpression(true);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertEquals(result, null);

        arg1 = TestHelper.createBooleanExpression(true);
        arg2 = TestHelper.createNumberExpression(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(new EvalContext(new InfoSet()));
        assertEquals(result, null);
    }

    // Test getSymbol method
    public void testGetSymbol() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(TestHelper.createNumberExpression(1.0), TestHelper.createNumberExpression(2.0));
        String symbol = coreOperationLessThan.getSymbol();
        assertEquals("<", symbol);
    }
}