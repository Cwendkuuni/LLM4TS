package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIterator;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.Nodes;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(5.0), new ConstantExpression(3.0));
        EvalContext context = new EvalContext(createDocument());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanFalse() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(3.0), new ConstantExpression(5.0));
        EvalContext context = new EvalContext(createDocument());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(5.0), new ConstantExpression(5.0));
        EvalContext context = new EvalContext(createDocument());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualTrue() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(5.0), new ConstantExpression(6.0));
        EvalContext context = new EvalContext(createDocument());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(3.0), new ConstantExpression(5.0));
        EvalContext context = new EvalContext(createDocument());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(5.0), new ConstantExpression(5.0));
        EvalContext context = new EvalContext(createDocument());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualTrue() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(5.0), new ConstantExpression(4.0));
        EvalContext context = new EvalContext(createDocument());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanFalse() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(5.0), new ConstantExpression(4.0));
        EvalContext context = new EvalContext(createDocument());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testNullComparison() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(null), new ConstantExpression(null));
        EvalContext context = new EvalContext(createDocument());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testIteratorsComparison() {
        Document document = createDocument();
        Element root = document.createElement("root");
        root.appendChild(document.createElement("child1"));
        root.appendChild(document.createElement("child2"));
        document.appendChild(root);

        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("$it"), new ConstantExpression("child2"));
        EvalContext context = new EvalContext(document, new IterateIterator(Nodes.selectNodes(document, "//child[1]"), document));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext(document, new IterateIterator(Nodes.selectNodes(document, "//child[2]"), document));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression() {
        new CoreOperationRelationalExpression(new Expression[0]);
    }

    private Document createDocument() {
        Document document = Nodes.newDocument();
        document.setXmlVersion("1.0");
        document.setDocumentType(Nodes.newDocumentType("root", null, ""));
        document.appendChild(Nodes.newElement("root"));
        return document;
    }
}