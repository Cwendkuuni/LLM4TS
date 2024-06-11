package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Criteria;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.apache.commons.jxpath.ri.compiler.Variable;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCreateGreaterExpression() {
        Expression left = new Variable("a");
        Expression right = new Constant(new NumberValue(2));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationBinary(
                ">", new CoreOperationUnary("not", left), right), null);

        Criteria criteria = expression.compile(new Criteria());
        assertNotNull(criteria);
        assertTrue(expression.getClass().isInstance(criteria.getPredicate()));
        assertTrue(expression.toString().startsWith("a > 2"));
    }

    @Test
    public void testCreateGreaterOrEqualExpression() {
        Expression left = new Variable("a");
        Expression right = new Constant(new NumberValue(2));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationBinary(
                ">=", new CoreOperationUnary("not", left), right), null);

        Criteria criteria = expression.compile(new Criteria());
        assertNotNull(criteria);
        assertTrue(expression.getClass().isInstance(criteria.getPredicate()));
        assertTrue(expression.toString().startsWith("a >= 2"));
    }

    @Test
    public void testCreateLessExpression() {
        Expression left = new Variable("a");
        Expression right = new Constant(new NumberValue(2));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationBinary(
                "<", left, right), null);

        Criteria criteria = expression.compile(new Criteria());
        assertNotNull(criteria);
        assertTrue(expression.getClass().isInstance(criteria.getPredicate()));
        assertTrue(expression.toString().startsWith("a < 2"));
    }

    @Test
    public void testCreateLessOrEqualExpression() {
        Expression left = new Variable("a");
        Expression right = new Constant(new NumberValue(2));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationBinary(
                "<=", left, right), null);

        Criteria criteria = expression.compile(new Criteria());
        assertNotNull(criteria);
        assertTrue(expression.getClass().isInstance(criteria.getPredicate()));
        assertTrue(expression.toString().startsWith("a <= 2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidExpression() {
        Expression left = new Variable("a");
        Expression right = new Variable("b");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationBinary(
                " InvalidRelationalOperator ", left, right), null);
    }

}