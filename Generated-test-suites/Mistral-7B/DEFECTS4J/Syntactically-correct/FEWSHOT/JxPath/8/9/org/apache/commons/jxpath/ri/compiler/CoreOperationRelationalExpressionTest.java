package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodeType;
import org.apache.commons.jxpath.ri.text.Text;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2.0));
        assertTrue(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(1.5));
        assertFalse(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationGreaterThanOrEqual("1", new CoreConstantExpression(2.0));
        assertTrue(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationGreaterThanOrEqual("1", new CoreConstantExpression(1.5));
        assertTrue(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationLessThan("1", new CoreConstantExpression(2.0));
        assertFalse(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        expression = new CoreOperationLessThanOrEqual("1", new CoreConstantExpression(1.5));
        assertTrue(expression.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2.0));
        assertTrue(expression.getPrecedence() == 3);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2.0));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual("1", new CoreConstantExpression(2.0));
        assertTrue(expression.evaluateCompare(0) == 0);
        assertTrue(expression.evaluateCompare(1) == 1);
        assertTrue(expression.evaluateCompare(-1) == -1);
    }

    @Test
    public void testComputeWithInitialContext() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("a/child::*[1]", new CoreConstantExpression("3"));
        EvalContext context = new EvalContext(new TestInfoSet());
        Node node = new NodeTest(new Text("apple"), NodeType.TEXT, -1);
        context.setCurrentNode(node);

        assertTrue(expression.computeValue(context).equals(Boolean.FALSE));

        node = new NodeTest(new Text("banana"), NodeType.TEXT, -1);
        context.setCurrentNode(node);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeWithCollection() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2.0));
        EvalContext context = new EvalContext(new TestInfoSet());
        context.setCurrentNode(new NodeTest(new Text("1"), NodeType.TEXT, -1));

        Collection collection = new HashSet();
        collection.add(new NodeTest(new Text("2"), NodeType.TEXT, -1));
        collection.add(new NodeTest(new Text("1"), NodeType.TEXT, -1));

        context.setCurrentNode(new NodeTest(new Text("1"), NodeType.TEXT, -1));
        context.setVariable("myCollection", collection);

        assertTrue(expression.computeValue(context).equals(Boolean.FALSE));

        context.setCurrentNode(new NodeTest(new Text("2"), NodeType.TEXT, -1));
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeWithIterator() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2.0));

        EvalContext context = new EvalContext(new TestInfoSet());
        NodeIterator iterator = context.getIterator();
        iterator.add(new NodeTest(new Text("1"), NodeType.TEXT, -1));
        iterator.add(new NodeTest(new Text("2"), NodeType.TEXT, -1));

        context.setCurrentNode(new NodeTest(new Text("1"), NodeType.TEXT, -1));
        assertTrue(expression.computeValue(context).equals(Boolean.FALSE));

        context.setCurrentNode(new NodeTest(new Text("2"), NodeType.TEXT, -1));
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2.0));

        EvalContext context = new EvalContext(new TestInfoSet());
        NodeIterator leftIterator = context.getIterator();
        leftIterator.add(new NodeTest(new Text("1"), NodeType.TEXT, -1));
        leftIterator.add(new NodeTest(new Text("2"), NodeType.TEXT, -1));

        NodeIterator rightIterator = context.getIterator();
        rightIterator.add(new NodeTest(new Text("2"), NodeType.TEXT, -1));
        rightIterator.add(new NodeTest(new Text("1"), NodeType.TEXT, -1));

        assertTrue(expression.findMatch(leftIterator, rightIterator));

        leftIterator = context.getIterator();
        leftIterator.add(new NodeTest(new Text("1"), NodeType.TEXT, -1));
        leftIterator.add(new NodeTest(new Text("2"), NodeType.TEXT, -1));

        rightIterator = context.getIterator();
        rightIterator.add(new NodeTest(new Text("3"), NodeType.TEXT, -1));

        assertFalse(expression.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan("1", new CoreConstantExpression(2.0));

        EvalContext context = new EvalContext(new TestInfoSet());
        NodeIterator iterator = context.getIterator();
        iterator.add(new NodeTest(new Text("1"), NodeType.TEXT, -1));
        iterator.add(new NodeTest(new Text("2"), NodeType.TEXT, -1));

        assertTrue(expression.containsMatch(iterator, new NodeTest(new Text("2"), NodeType.TEXT, -1)));

        iterator = context.getIterator();
        iterator.add(new NodeTest(new Text("1"), NodeType.TEXT, -1));
        iterator.add(new NodeTest(new Text("2"), NodeType.TEXT, -1));

        assertFalse(expression.containsMatch(iterator, new NodeTest(new Text("3"), NodeType.TEXT, -1)));
    }

}