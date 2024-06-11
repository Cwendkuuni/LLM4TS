package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.nodes.NumberNode;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NumberNode(10.5)),
                new CoreConstantExpression(new NumberNode(5.3)));

        EvalContext context = new EvalContext(new TestInfoSet());

        assertTrue(testee.computeValue(context).booleanValue());

        // Reversed order
        testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NumberNode(5.3)),
                new CoreConstantExpression(new NumberNode(10.5)));

        assertFalse(testee.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithMixedTypes() {
        CoreOperationGreaterThan testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NumberNode(10.5)),
                new CoreStringExpression(new StringNode("12.6")));

        EvalContext context = new EvalContext(new TestInfoSet());

        assertFalse(testee.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithNaN() {
        CoreOperationGreaterThan testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NumberNode.NaN()),
                new CoreConstantExpression(new NumberNode(5.3)));

        EvalContext context = new EvalContext(new TestInfoSet());

        assertFalse(testee.computeValue(context).booleanValue());

        testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NumberNode(5.3)),
                new CoreConstantExpression(new NumberNode.NaN()));

        assertFalse(testee.computeValue(context).booleanValue());

        testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NumberNode.NaN()),
                new CoreConstantExpression(new NumberNode.NaN()));

        assertFalse(testee.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithInfinities() {
        CoreOperationGreaterThan testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NumberNode.POSITIVE_INFINITY()),
                new CoreConstantExpression(new NumberNode.POSITIVE_INFINITY()));

        EvalContext context = new EvalContext(new TestInfoSet());

        assertTrue(testee.computeValue(context).booleanValue());

        testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NumberNode.POSITIVE_INFINITY()),
                new CoreConstantExpression(new NumberNode.NEGATIVE_INFINITY()));

        assertTrue(testee.computeValue(context).booleanValue());

        testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NumberNode.NEGATIVE_INFINITY()),
                new CoreConstantExpression(new NumberNode.POSITIVE_INFINITY()));

        assertFalse(testee.computeValue(context).booleanValue());
    }
}

class TestInfoSet extends InfoSet {
    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Object getAttributeValue(String name) {
        return null;
    }

    @Override
    public Object getItem(int index) {
        return null;
    }

    @Override
    public Object getItemByKey(String key) {
        return null;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getLength(int index) {
        return 0;
    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public Object getPropertyValue(String name) {
        return null;
    }

    @Override
    public double doubleValue(Object object) {
        if (object instanceof NumberNode) {
            return ((NumberNode) object).doubleValue();
        }
        throw new IllegalArgumentException();
    }
}