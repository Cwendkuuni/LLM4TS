package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Parser;
import org.apache.commons.jxpath.ri. Saxon;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    private Parser parser;
    private ExpressionFactory expressionFactory;

    @Before
    public void setUp() {
        parser = new Saxon();
        expressionFactory = parser.getExpressionFactory();
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#CoreOperationGreaterThanOrEqual(Expression, Expression)} .
     */
    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = expressionFactory.parseExpression("5.0");
        Expression arg2 = expressionFactory.parseExpression("4.0");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNotNull(operation);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#computeValue(EvalContext)} .
     */
    @Test
    public void testComputeValue() {
        Expression arg1 = expressionFactory.parseExpression("5.0");
        Expression arg2 = expressionFactory.parseExpression("4.0");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), null, null);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#computeValue(EvalContext)} with different data types.
     */
    @Test
    public void testComputeValueDifferentDataTypes() {
        Expression arg1 = expressionFactory.parseExpression("5");
        Expression arg2 = expressionFactory.parseExpression("4.0");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), null, null);
        Object result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = expressionFactory.parseExpression("5.0");
        arg2 = expressionFactory.parseExpression("4");
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#computeValue(EvalContext)} with equal values.
     */
    @Test
    public void testComputeValueEqualValues() {
        Expression arg1 = expressionFactory.parseExpression("5.0");
        Expression arg2 = expressionFactory.parseExpression("5.0");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), null, null);
        Object result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getSymbol()} .
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = expressionFactory.parseExpression("5.0");
        Expression arg2 = expressionFactory.parseExpression("4.0");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} .
     */
    @Test
    public void testType() {
        Expression arg1 = expressionFactory.parseExpression("5.0");
        Expression arg2 = expressionFactory.parseExpression("4.0");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Class<?> type = operation.getType();
        assertEquals(Boolean.class, type);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} with different data types.
     */
    @Test
    public void testTypeDifferentDataTypes() {
        Expression arg1 = expressionFactory.parseExpression("5");
        Expression arg2 = expressionFactory.parseExpression("4.0");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Class<?> type = operation.getType();
        assertEquals(Boolean.class, type);

        arg1 = expressionFactory.parseExpression("5.0");
        arg2 = expressionFactory.parseExpression("4");
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        type = operation.getType();
        assertEquals(Boolean.class, type);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} with equal values.
     */
    @Test
    public void testTypeEqualValues() {
        Expression arg1 = expressionFactory.parseExpression("5.0");
        Expression arg2 = expressionFactory.parseExpression("5.0");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Class<?> type = operation.getType();
        assertEquals(Boolean.class, type);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} with null arguments.
     */
    @Test
    public void testTypeNullArguments() {
        Expression arg1 = null;
        Expression arg2 = expressionFactory.parseExpression("4.0");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Class<?> type = operation.getType();
        assertEquals(Boolean.class, type);

        arg1 = expressionFactory.parseExpression("5.0");
        arg2 = null;
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        type = operation.getType();
        assertEquals(Boolean.class, type);

        arg1 = null;
        arg2 = null;
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        type = operation.getType();
        assertEquals(Boolean.class, type);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeInvalidArguments() {
        Expression arg1 = expressionFactory.parseExpression("5.0");
        Expression arg2 = null;
        new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} with more than 2 arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeMoreThanTwoArguments() {
        Expression arg1 = expressionFactory.parseExpression("5.0");
        Expression arg2 = expressionFactory.parseExpression("4.0");
        Expression arg3 = expressionFactory.parseExpression("3.0");
        new CoreOperationGreaterThanOrEqual(arg1, arg2, arg3);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} with non-Expression arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeNonExpressionArguments() {
        Object arg1 = new Object();
        Expression arg2 = expressionFactory.parseExpression("4.0");
        new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} with non-double arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeNonDoubleArguments() {
        Expression arg1 = expressionFactory.parseExpression("5");
        Expression arg2 = expressionFactory.parseExpression("4.0");
        new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} with null Expression arguments.
     */
    @Test
    public void testTypeNullExpressionArguments() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Class<?> type = operation.getType();
        assertEquals(Boolean.class, type);

        arg1 = expressionFactory.parseExpression("5.0");
        arg2 = null;
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        type = operation.getType();
        assertEquals(Boolean.class, type);

        arg1 = null;
        arg2 = expressionFactory.parseExpression("5.0");
        operation = new CoreOperationGreaterThanOrEQUAL(arg1, arg2);
        type = operation.getType();
        assertEquals(Boolean.class, type);
    }

    /**
     * Test method for {@link CoreOperationGreaterThanOrEqual#getType()} with invalid Expression arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeInvalidExpressionArguments() {
        Expression arg1 = expressionFactory.parseExpression("5.0");
        Expression arg2 = new CoreOperationRelationalExpression(new Expression[0], null);
        new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }
}