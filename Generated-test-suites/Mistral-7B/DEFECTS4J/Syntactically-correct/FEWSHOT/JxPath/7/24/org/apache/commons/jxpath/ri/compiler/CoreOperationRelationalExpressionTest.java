package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.ComparisonType;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.Path;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCreateGreaterThanExpression() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = new NodeTest(NodeTest.TYPE_NUMBER, "2");
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.GT);

        assertEquals("(1 > 2)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCreateGreaterThanOrEqualExpression() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = new NodeTest(NodeTest.TYPE_NUMBER, "2");
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.GTE);

        assertEquals("(1 >= 2)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCreateLessThanExpression() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = new NodeTest(NodeTest.TYPE_NUMBER, "2");
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.LT);

        assertEquals("(1 < 2)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCreateLessThanOrEqualExpression() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = new NodeTest(NodeTest.TYPE_NUMBER, "2");
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.LTE);

        assertEquals("(1 <= 2)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testToStringWithNullArguments() {
        Expression expression = new CoreOperationRelationalExpression(null, ComparisonType.GT);
        assertNull(expression.toString());
    }

    @Test
    public void testToStringWithEmptyArguments() {
        Expression expression = new CoreOperationRelationalExpression(new Expression[0], ComparisonType.GT);
        assertNull(expression.toString());
    }

    @Test
    public void testToStringWithSingleArgument() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left}, ComparisonType.GT);

        assertEquals("(1 >)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testToStringWithArgumentsOfDifferentTypes() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = new Path();
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.GT);

        assertEquals("(1 > /)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testIsSymmetricWithDifferentTypes() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = new Path();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.EQ);

        assertFalse(expression.isSymmetric());

        expression = new CoreOperationRelationalExpression(new Expression[]{right, left}, ComparisonType.EQ);

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testIsSymmetricWithSameTypes() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = new NodeTest(NodeTest.TYPE_NUMBER, "1");

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.EQ);

        assertFalse(expression.isSymmetric());

        expression = new CoreOperationRelationalExpression(new Expression[]{right, left}, ComparisonType.EQ);

        assertTrue(expression.isSymmetric());
    }

    @Test
    public void testCreateGreaterThanOrEqualExpressionWithSameNodes() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = left;
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.GTE);

        assertEquals("(1 >= 1)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertTrue(expression.isSymmetric());
    }

    @Test
    public void testCreateLessThanOrEqualExpressionWithSameNodes() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = left;
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.LTE);

        assertEquals("(1 <= 1)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertTrue(expression.isSymmetric());
    }

    @Test
    public void testCreateGreaterThanOrEqualExpressionWithIdenticalExpressions() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.GTE);

        assertEquals("(1 >= 1)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertTrue(expression.isSymmetric());

        assertSame(left, expression.getChild(0));
        assertSame(right, expression.getChild(1));
    }

    @Test
    public void testCreateLessThanOrEqualExpressionWithIdenticalExpressions() {
        Expression left = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression right = new NodeTest(NodeTest.TYPE_NUMBER, "1");
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{left, right}, ComparisonType.LTE);

        assertEquals("(1 <= 1)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertTrue(expression.isSymmetric());

        assertSame(left, expression.getChild(0));
        assertSame(right, expression.getChild(1));
    }

}