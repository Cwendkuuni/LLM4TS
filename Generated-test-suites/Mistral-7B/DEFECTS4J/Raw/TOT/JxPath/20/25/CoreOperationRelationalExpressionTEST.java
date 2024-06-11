package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodesTestUtil;
import org.apache.commons.jxpath.ri.TestSupport;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest extends TestSupport {

    private Node rootNode;

    @Before
    public void initialize() throws Exception {
        Document doc = createTestDocument();
        rootNode = (Node) doc.getDocumentElement();
    }

    /**
     * Test for CoreOperationRelationalExpression with integers.
     */
    @Test
    public void testRelationalExpressionWithIntegers() {
        Expression left = createNumberExpression(3);
        Expression right = createNumberExpression(4);

        CoreOperationRelationalExpression expression = new GtExpression(left, right);
        Object result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = createNumberExpression(2);
        right = createNumberExpression(4);

        expression = new GtExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertFalse(result instanceof Boolean);

        left = createNumberExpression(4);
        right = createNumberExpression(3);

        expression = new LtExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = createNumberExpression(4);
        right = createNumberExpression(4);

        expression = new EqExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = createNumberExpression(3);
        right = createNumberExpression(4);

        expression = new EqExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for CoreOperationRelationalExpression with doubles.
     */
    @Test
    public void testRelationalExpressionWithDoubles() {
        Expression left = createNumberExpression(3.0);
        Expression right = createNumberExpression(4.0);

        CoreOperationRelationalExpression expression = new GtExpression(left, right);
        Object result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = createNumberExpression(2.0);
        right = createNumberExpression(4.0);

        expression = new GtExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertFalse(result instanceof Boolean);

        left = createNumberExpression(4.0);
        right = createNumberExpression(3.0);

        expression = new LtExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = createNumberExpression(4.0);
        right = createNumberExpression(4.0);

        expression = new EqExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = createNumberExpression(3.0);
        right = createNumberExpression(4.0);

        expression = new EqExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for CoreOperationRelationalExpression with iterators.
     */
    @Test
    public void testRelationalExpressionWithIterators() {
        Expression left = new IteratorNodeIterator(NodesTestUtil.createNodeList(createNumberExpression(1), createNumberExpression(3), createNumberExpression(4)));
        Expression right = new IteratorNodeIterator(NodesTestUtil.createNodeList(createNumberExpression(4), createNumberExpression(3), createNumberExpression(1)));

        CoreOperationRelationalExpression expression = new GtExpression(left, right);
        Object result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new IteratorNodeIterator(NodesTestUtil.createNodeList(createNumberExpression(1), createNumberExpression(3), createNumberExpression(4)));
        right = new IteratorNodeIterator(NodesTestUtil.createNodeList(createNumberExpression(1), createNumberExpression(3), createNumberExpression(4)));

        expression = new EqExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new IteratorNodeIterator(NodesTestUtil.createNodeList(createNumberExpression(1), createNumberExpression(3), createNumberExpression(4)));
        right = new IteratorNodeIterator(NodesTestUtil.createNodeList(createNumberExpression(4), createNumberExpression(3), createNumberExpression(1)));

        expression = new EqExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for CoreOperationRelationalExpression with null values.
     */
    @Test
    public void testRelationalExpressionWithNullValues() {
        Expression left = createConstantExpression(null);
        Expression right = createConstantExpression(null);

        CoreOperationRelationalExpression expression = new EqExpression(left, right);
        Object result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = createConstantExpression(null);
        right = createNumberExpression(3);

        expression = new EqExpression(left, right);
        result = expression.compute(new EvaluationContext(new JXPathContext(rootNode)));
        assertFalse(result instanceof Boolean);
    }
}