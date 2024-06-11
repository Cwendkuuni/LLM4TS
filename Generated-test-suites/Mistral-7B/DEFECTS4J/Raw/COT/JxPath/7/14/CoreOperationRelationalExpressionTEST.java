package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @SuppressWarnings("unchecked")
    private CoreOperationRelationalExpression createExpression(Expression... args) {
        return new CoreOperationRelationalExpression(args);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}.
     */
    @Test
    public void testCoreOperationRelationalExpression() {
        CoreOperationRelationalExpression expression = createExpression(new ConstantExpression(5));
        assertNotNull(expression);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = createExpression(new ConstantExpression(5), new ConstantExpression(10));
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = createExpression(new ConstantExpression(5), new ConstantExpression(10));
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluate(ObjectContext)}.
     * Test with valid arguments.
     */
    @Test
    public void testEvaluateValidArguments() {
        CoreOperationRelationalExpression expression = createExpression(new ConstantExpression(5), new ConstantExpression(10));

        ObjectContext context = new ObjectContext();
        context.setCurrentNode(new JXPathContext(new Object[]{}));

        Object result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = createExpression(new ConstantExpression(5), new ConstantExpression(5));
        result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = createExpression(new ConstantExpression(5), new ConstantExpression(5.0));
        result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluate(ObjectContext)}.
     * Test with invalid arguments (different types).
     */
    @Test(expected = ClassCastException.class)
    public void testEvaluateInvalidArguments() {
        CoreOperationRelationalExpression expression = createExpression(new ConstantExpression(5), new ConstantExpression("not a number"));
        expression.evaluate(new ObjectContext());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluate(ObjectContext)}.
     * Test with null context.
     */
    @Test(expected = NullPointerException.class)
    public void testEvaluateNullContext() {
        CoreOperationRelationalExpression expression = createExpression(new ConstantExpression(5), new ConstantExpression(10));
        expression.evaluate(null);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluate(ObjectContext)}.
     * Test with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateNullArguments() {
        CoreOperationRelationalExpression expression = createExpression(null, new ConstantExpression(10));
        expression.evaluate(new ObjectContext());

        expression = createExpression(new ConstantExpression(5), null);
        expression.evaluate(new ObjectContext());
    }
}