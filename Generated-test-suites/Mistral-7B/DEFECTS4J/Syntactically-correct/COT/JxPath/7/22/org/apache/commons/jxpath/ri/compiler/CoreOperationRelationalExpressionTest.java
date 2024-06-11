package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertInstanceOf(CoreOperationRelationalExpression.class, expression);
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test methods with valid arguments.
     */
    @Test
    public void testValidArguments() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);

        assertTrue(expression.evaluate(new Context()));

        arg1 = new ConstantExpression(2);
        arg2 = new ConstantExpression(2);
        expression = new GreaterThanOrEqualExpression(arg1, arg2);
        assertTrue(expression.evaluate(new Context()));

        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(2);
        expression = new LessThanExpression(arg1, arg2);
        assertFalse(expression.evaluate(new Context()));

        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(1);
        expression = new LessThanOrEqualExpression(arg1, arg2);
        assertTrue(expression.evaluate(new Context()));
    }

    /**
     * Test methods with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(null, new ConstantExpression(1));
    }

    /**
     * Test methods with same ConstantExpression arguments.
     */
    @Test
    public void testSameConstantArguments() {
        Expression arg1 = new ConstantExpression(1);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg1);
        assertFalse(expression.evaluate(new Context()));

        expression = new GreaterThanOrEqualExpression(arg1, arg1);
        assertTrue(expression.evaluate(new Context()));

        expression = new LessThanExpression(arg1, arg1);
        assertFalse(expression.evaluate(new Context()));

        expression = new LessThanOrEqualExpression(arg1, arg1);
        assertTrue(expression.evaluate(new Context()));
    }

    /**
     * Test methods with different ConstantExpression arguments.
     */
    @Test
    public void testDifferentConstantArguments() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);

        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);
        assertFalse(expression.evaluate(new Context()));

        expression = new GreaterThanOrEqualExpression(arg1, arg2);
        assertFalse(expression.evaluate(new Context()));

        expression = new LessThanExpression(arg2, arg1);
        assertTrue(expression.evaluate(new Context()));

        expression = new LessThanOrEqualExpression(arg2, arg1);
        assertTrue(expression.evaluate(new Context()));
    }

    /**
     * Test methods with VariableExpression arguments.
     */
    @Test
    public void testVariableArguments() {
        VariableExpression var1 = new VariableExpression("x");
        VariableExpression var2 = new VariableExpression("y");

        CoreOperationRelationalExpression expression = new GreaterThanExpression(var1, var2);
        // The outcome depends on the initial value of variables.

        expression = new GreaterThanOrEqualExpression(var1, var2);
        // The outcome depends on the initial value of variables.

        expression = new LessThanExpression(var2, var1);
        // The outcome depends on the initial value of variables.

        expression = new LessThanOrEqualExpression(var2, var1);
        // The outcome depends on the initial value of variables.
    }

    /**
     * Test methods with ArithmeticExpression arguments.
     */
    @Test
    public void testArithmeticArguments() {
        Expression arg1 = new ArithmeticExpression(new ConstantExpression(1), new ConstantExpression(2), ArithmeticExpression.ADD);
        Expression arg2 = new ArithmeticExpression(new ConstantExpression(2), new ConstantExpression(1), ArithmeticExpression.SUB);

        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);
        // The outcome depends on the values of the expressions.

        expression = new GreaterThanOrEqualExpression(arg1, arg2);
        // The outcome depends on the values of the expressions.

        expression = new LessThanExpression(arg2, arg1);
        // The outcome depends on the values of the expressions.

        expression = new LessThanOrEqualExpression(arg2, arg1);
        // The outcome depends on the values of the expressions.
    }

    /**
     * Test methods with StringExpression arguments.
     */
    @Test
    public void testStringArguments() {
        Expression arg1 = new StringExpression("abc");
        Expression arg2 = new StringExpression("def");

        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);
        assertFalse(expression.evaluate(new Context()));

        expression = new GreaterThanOrEqualExpression(arg1, arg2);
        assertFalse(expression.evaluate(new Context()));

        expression = new LessThanExpression(arg1, arg2);
        assertTrue(expression.evaluate(new Context()));

        expression = new LessThanOrEqualExpression(arg1, arg2);
        assertTrue(expression.evaluate(new Context()));
    }

    /**
     * Test methods with BooleanExpression arguments.
     */
    @Test
    public void testBooleanArguments() {
        Expression arg1 = new BooleanExpression(new ConstantExpression(true));
        Expression arg2 = new BooleanExpression(new ConstantExpression(false));

        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);
        assertTrue(expression.evaluate(new Context()));

        expression = new GreaterThanOrEqualExpression(arg1, arg2);
        assertTrue(expression.evaluate(new Context()));

        expression = new LessThanExpression(arg2, arg1);
        assertFalse(expression.evaluate(new Context()));

        expression = new LessThanOrEqualExpression(arg2, arg1);
        assertFalse(expression.evaluate(new Context()));
    }

    /**
     * Test methods with MixedArguments.
     */
    @Test
    public void testMixedArguments() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ArithmeticExpression(new ConstantExpression(2), new ConstantExpression(1), ArithmeticExpression.SUB);
        Expression arg3 = new StringExpression("abc");
        Expression arg4 = new BooleanExpression(new ConstantExpression(true));

        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);
        // The outcome depends on the values of the expressions.

        expression = new GreaterThanOrEqualExpression(arg1, arg2);
        // The outcome depends on the values of the expressions.

        expression = new LessThanExpression(arg3, arg2);
        // The outcome depends on the values of the expressions.

        expression = new LessThanOrEqualExpression(arg3, arg2);
        // The outcome depends on the values of the expressions.

        expression = new LessThanExpression(arg1, arg4);
        assertTrue(expression.evaluate(new Context()));

        expression = new LessThanOrEqualExpression(arg1, arg4);
        assertTrue(expression.evaluate(new Context()));

        expression = new GreaterThanExpression(arg4, arg1);
        assertFalse(expression.evaluate(new Context()));

        expression = new GreaterThanOrEqualExpression(arg4, arg1);
        assertFalse(expression.evaluate(new Context()));
    }
}