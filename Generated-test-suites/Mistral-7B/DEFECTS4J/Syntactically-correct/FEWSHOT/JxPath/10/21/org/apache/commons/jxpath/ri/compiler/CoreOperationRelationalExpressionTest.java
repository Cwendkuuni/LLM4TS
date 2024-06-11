package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodeWrapper;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.util.SimpleNode;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValueGT() {
        Expression left = new LiteralExpression(new SimpleNode("1.5"));
        Expression right = new LiteralExpression(new SimpleNode("2.0"));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationUnaryMinus(left), right);
        Object result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueLT() {
        Expression left = new LiteralExpression(new SimpleNode("1.5"));
        Expression right = new LiteralExpression(new SimpleNode("2.0"));
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(left, right);
        Object result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueGE() {
        Expression left = new LiteralExpression(new SimpleNode("1.5"));
        Expression right = new LiteralExpression(new SimpleNode("2.0"));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(left, right);
        Object result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());

        left = new LiteralExpression(new SimpleNode("2.0"));
        result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueLE() {
        Expression left = new LiteralExpression(new SimpleNode("1.5"));
        Expression right = new LiteralExpression(new SimpleNode("2.0"));
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(left, right);
        Object result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        left = new LiteralExpression(new SimpleNode("2.0"));
        result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueWithIterators() {
        Iterator<NodeTest> left = Iterators.iterate(new NodeTest[] { new NodeTest(NodeTest.IS, new NodeWrapper("1.5")) });
        Iterator<NodeTest> right = Iterators.iterate(new NodeTest[] { new NodeTest(NodeTest.IS, new NodeWrapper("2.0")) });
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationUnaryMinus(new CoreOperationIdentify(new LiteralExpression(new SimpleNode("*"))), new NodeTestExpression(new NodeTest(NodeTest.IS, new NodeWrapper("1.5")))), right);

        Object result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());

        left = Iterators.iterate(new NodeTest[] { new NodeTest(NodeTest.IS, new NodeWrapper("2.1")) });
        result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueWithCollections() {
        Collection<NodeTest> left = new HashSet<NodeTest>() {{
            add(new NodeTest(NodeTest.IS, new NodeWrapper("1.5")));
        }};
        Collection<NodeTest> right = new HashSet<NodeTest>() {{
            add(new NodeTest(NodeTest.IS, new NodeWrapper("2.0")));
        }};
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationUnaryMinus(new CoreOperationIdentify(new LiteralExpression(new SimpleNode("*"))), new NodeTestExpression(new NodeTest(NodeTest.IS, new NodeWrapper("1.5")))), new CoreOperationIdentify(new LiteralExpression(new SimpleNode("*"))), right);

        Object result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());

        left.add(new NodeTest(NodeTest.IS, new NodeWrapper("2.1")));
        result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueWithNull() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression(new SimpleNode("2.0"));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(left, right);
        Object result = expression.computeValue(new EvalContext(new Path(""), new SimpleNode("root"), new NodeWrapper[0], new SimpleNode[0], new Object[0]));
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(new SimpleNode("1.0")), new LiteralExpression(new SimpleNode("2.0")));
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(new SimpleNode("1.0")), new LiteralExpression(new SimpleNode("2.0")));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompareGT() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(new SimpleNode("1.5")), new LiteralExpression(new SimpleNode("2.0")));
        boolean result = expression.evaluateCompare(-1);
        assertFalse(result);

        result = expression.evaluateCompare(0);
        assertFalse(result);

        result = expression.evaluateCompare(1);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompareLT() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new LiteralExpression(new SimpleNode("1.5")), new LiteralExpression(new SimpleNode("2.0")));
        boolean result = expression.evaluateCompare(-1);
        assertTrue(result);

        result = expression.evaluateCompare(0);
        assertFalse(result);

        result = expression.evaluateCompare(1);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompareGE() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new LiteralExpression(new SimpleNode("1.5")), new LiteralExpression(new SimpleNode("2.0")));
        boolean result = expression.evaluateCompare(-1);
        assertFalse(result);

        result = expression.evaluateCompare(0);
        assertFalse(result);

        result = expression.evaluateCompare(1);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompareLE() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new LiteralExpression(new SimpleNode("1.5")), new LiteralExpression(new SimpleNode("2.0")));
        boolean result = expression.evaluateCompare(-1);
        assertTrue(result);

        result = expression.evaluateCompare(0);
        assertTrue(result);

        result = expression.evaluateCompare(1);
        assertFalse(result);
    }
}