package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.test.AbstractTestBase;
import org.apache.commons.jxpath.ri.test.TestEvaluationContext;

import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;

/**
 * Test for CoreOperationNotEqual class.
 *
 * @author Dmitri Plotnikov
 */
public class CoreOperationNotEqualTest extends AbstractTestBase {

    /**
     * Test method for {@link CoreOperationNotEqual#CoreOperationNotEqual(Expression, Expression)} .
     */
    @org.junit.Test
    public void testCoreOperationNotEqualConstruction() {
        Expression arg1 = new ConstExpression(1);
        Expression arg2 = new ConstExpression(2);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertNotNull(coreOperationNotEqual);
        assertEquals("!=".intern(), coreOperationNotEqual.getSymbol());
        assertEquals(2, coreOperationNotEqual.getNumberOfArgs());
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} .
     */
    @org.junit.Test
    public void testComputeValueFalse() {
        Expression arg1 = new ConstExpression(1);
        Expression arg2 = new ConstExpression(2);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new TestEvaluationContext();

        Object value = coreOperationNotEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} .
     */
    @org.junit.Test
    public void testComputeValueTrue() {
        Expression arg1 = new ConstExpression(1);
        Expression arg2 = new ConstExpression(1);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new TestEvaluationContext();

        Object value = coreOperationNotEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with same expressions.
     */
    @org.junit.Test
    public void testComputeValueSameExpressions() {
        Expression arg1 = new ConstExpression(1);
        Expression arg2 = arg1;

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new TestEvaluationContext();

        Object value = coreOperationNotEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#getSymbol()} .
     */
    @org.junit.Test
    public void testGetSymbol() {
        Expression arg1 = new ConstExpression(1);
        Expression arg2 = new ConstExpression(2);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }
}