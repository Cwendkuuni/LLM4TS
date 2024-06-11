package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.IterateContext;
import org.apache.commons.jxpath.ri.NodeSet;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.Axes.NodeTestIterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGT() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Constant(5.0), new Constant(4.0));
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testGT_False() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Constant(4.0), new Constant(5.0));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testGE() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new Constant(5.0), new Constant(4.0));
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testGE_True() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new Constant(4.0), new Constant(5.0));
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testLT() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new Constant(4.0), new Constant(5.0));
        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testLT_False() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new Constant(5.0), new Constant(4.0));
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testLE() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new Constant(4.0), new Constant(5.0));
        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testLE_True() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new Constant(5.0), new Constant(4.0));
        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testIterators() {
        NodeTest leftNodeTest = new NodeTest(NodeTest.ELEMENT, "foo");
        NodeTestIterator leftNodeTestIterator = new NodeTestIterator(leftNodeTest);

        NodeTest rightNodeTest = new NodeTest(NodeTest.ELEMENT, "bar");
        NodeTestIterator rightNodeTestIterator = new NodeTestIterator(rightNodeTest);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(leftNodeTestIterator, rightNodeTestIterator);
        IterateContext leftContext = new IterateContext(null, null, null, new NodeSet(), null, null, leftNodeTestIterator);
        IterateContext rightContext = new IterateContext(null, null, null, new NodeSet(), null, null, rightNodeTestIterator);

        EvalContext context = new EvalContext(new SelfContext(), null, null, null, leftContext, rightContext);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testIterators_False() {
        NodeTest leftNodeTest = new NodeTest(NodeTest.ELEMENT, "foo");
        NodeTestIterator leftNodeTestIterator = new NodeTestIterator(leftNodeTest);

        NodeTest rightNodeTest = new NodeTest(NodeTest.ELEMENT, "bar");
        NodeTestIterator rightNodeTestIterator = new NodeTestIterator(rightNodeTest);

        CoreOperationRelationalExpression expression = new CoreOperationLessThan(leftNodeTestIterator, rightNodeTestIterator);
        IterateContext leftContext = new IterateContext(null, null, null, new NodeSet(), null, null, leftNodeTestIterator);
        IterateContext rightContext = new IterateContext(null, null, null, new NodeSet(), null, null, rightNodeTestIterator);

        EvalContext context = new EvalContext(new SelfContext(), null, null, null, leftContext, rightContext);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }
}