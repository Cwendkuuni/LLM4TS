package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IteratedNodeList;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationBinaryExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NumberTest;
import org.apache.commons.jxpath.ri.compiler.StringTest;
import org.apache.commons.jxpath.ri.compiler.VariableReference;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @SuppressWarnings("unchecked")
    private CoreOperationRelationalExpression createComparisionExpression(Node leftNode, Object leftValue,
            Node rightNode, Object rightValue, boolean expectedResult) {
        VariableReference leftVarRef = new VariableReference();
        leftVarRef.setVariableName("left");
        VariableReference rightVarRef = new VariableReference();
        rightVarRef.setVariableName("right");

        Expression leftExp = ExpressionFactory.newInstance().createNodeTest(leftNode);
        ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
        Expression leftValExp;
        if (leftValue instanceof Number) {
            leftValExp = expressionFactory.createNumberTest((Number) leftValue);
        } else if (leftValue instanceof String) {
            leftValExp = expressionFactory.createStringTest((String) leftValue);
        } else {
            leftValExp = leftExp;
        }
        Expression rightExp = ExpressionFactory.newInstance().createNodeTest(rightNode);
        Expression rightValExp;
        if (rightValue instanceof Number) {
            rightValExp = expressionFactory.createNumberTest((Number) rightValue);
        } else if (rightValue instanceof String) {
            rightValExp = expressionFactory.createStringTest((String) rightValue);
        } else {
            rightValExp = rightExp;
        }

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { leftExp, leftValExp, rightExp, rightValExp });
        EvalContext context = new EvalContext();
        context.setCurrentNode(new SelfContext());
        context.setCurrentNode(leftNode);
        context.setVariable("left", leftNode);
        context.setVariable("right", rightNode);
        expression.setEvalContext(context);
        return expression;
    }

    @org.junit.Test
    public void testGreaterThan() {
        Node leftNode = new Node() {
            @Override
            public int compareTo(Node n) {
                return 2;
            }
        };
        Node rightNode = new Node() {
            @Override
            public int compareTo(Node n) {
                return 1;
            }
        };
        CoreOperationRelationalExpression expression = createComparisionExpression(leftNode, null, rightNode,
                null, true);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testGreaterThan_Number_Left() {
        Number leftValue = 3.0;
        Number rightValue = 2.0;
        CoreOperationRelationalExpression expression = createComparisionExpression(null, leftValue, null,
                rightValue, true);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testGreaterThan_Number_Right() {
        Number leftValue = 2.0;
        Number rightValue = 3.0;
        CoreOperationRelationalExpression expression = createComparisionExpression(null, leftValue, null,
                rightValue, false);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testGreaterThanOrEqual() {
        Node leftNode = new Node() {
            @Override
            public int compareTo(Node n) {
                return 2;
            }
        };
        Node rightNode = new Node() {
            @Override
            public int compareTo(Node n) {
                return 2;
            }
        };
        CoreOperationRelationalExpression expression = createComparisionExpression(leftNode, null, rightNode,
                null, true);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testGreaterThanOrEqual_Number_Left() {
        Number leftValue = 3.0;
        Number rightValue = 3.0;
        CoreOperationRelationalExpression expression = createComparisionExpression(null, leftValue, null,
                rightValue, true);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testGreaterThanOrEqual_Number_Right() {
        Number leftValue = 2.0;
        Number rightValue = 3.0;
        CoreOperationRelationalExpression expression = createComparisionExpression(null, leftValue, null,
                rightValue, false);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testLessThan() {
        Node leftNode = new Node() {
            @Override
            public int compareTo(Node n) {
                return 1;
            }
        };
        Node rightNode = new Node() {
            @Override
            public int compareTo(Node n) {
                return 2;
            }
        };
        CoreOperationRelationalExpression expression = createComparisionExpression(leftNode, null, rightNode,
                null, false);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThan_Number_Left() {
        Number leftValue = 2.0;
        Number rightValue = 3.0;
        CoreOperationRelationalExpression expression = createComparisionExpression(null, leftValue, null,
                rightValue, false);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThan_Number_Right() {
        Number leftValue = 3.0;
        Number rightValue = 2.0;
        CoreOperationRelationalExpression expression = createComparisionExpression(null, leftValue, null,
                rightValue, true);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanOrEqual() {
        Node leftNode = new Node() {
            @Override
            public int compareTo(Node n) {
                return 1;
            }
        };
        Node rightNode = new Node() {
            @Override
            public int compareTo(Node n) {
                return 2;
            }
        };
        CoreOperationRelationalExpression expression = createComparisionExpression(leftNode, null, rightNode,
                null, false);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanOrEqual_Number_Left() {
        Number leftValue = 2.0;
        Number rightValue = 3.0;
        CoreOperationRelationalExpression expression = createComparisionExpression(null, leftValue, null,
                rightValue, false);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanOrEqual_Number_Right() {
        Number leftValue = 3.0;
        Number rightValue = 2.0;
        CoreOperationRelationalExpression expression = createComparisionExpression(null, leftValue, null,
                rightValue, true);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_InvalidComparisonType() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { null, null });
        expression.computeValue(new EvalContext());
    }

    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { new VariableReference(), new VariableReference() });
        assertFalse(expression.isSymmetric());
    }

    @org.junit.Test
    public void testComputeValue_NullLeft() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { null, new VariableReference() });
        Object result = expression.computeValue(new EvalContext());
        assertNull(result);
    }

    @org.junit.Test
    public void testComputeValue_NullRight() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { new VariableReference(), null });
        Object result = expression.computeValue(new EvalContext());
        assertNull(result);
    }

    @org.junit.Test
    public void testComputeValue_CollectionLeft() {
        List<Node> left = new ArrayList<>();
        left.add(new Node());
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { new NodeTest(Path.createPath("."), NodeTest.NODE),
                        new IteratorTest(left.iterator()), new VariableReference(), null });
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testComputeValue_CollectionRight() {
        List<Node> right = new ArrayList<>();
        right.add(new Node());
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { new VariableReference(), null,
                        new IteratorTest(right.iterator()), new NodeTest(Path.createPath("."), NodeTest.NODE) });
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testComputeValue_CollectionIntersection() {
        List<Node> left = new ArrayList<>();
        left.add(new Node());
        List<Node> right = new ArrayList<>();
        right.add(new Node());
        left.add(right.get(0));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { new NodeTest(Path.createPath("."), NodeTest.NODE),
                        new IteratorTest(left.iterator()), new IteratorTest(right.iterator()),
                        new VariableReference() });
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testComputeValue_EmptyCollectionIntersection() {
        List<Node> left = new ArrayList<>();
        List<Node> right = new ArrayList<>();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { new NodeTest(Path.createPath("."), NodeTest.NODE),
                        new IteratorTest(left.iterator()), new IteratorTest(right.iterator()),
                        new VariableReference() });
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationBinaryExpression[] { new VariableReference(), new VariableReference() });
        int precedence = expression.getPrecedence();
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }
}