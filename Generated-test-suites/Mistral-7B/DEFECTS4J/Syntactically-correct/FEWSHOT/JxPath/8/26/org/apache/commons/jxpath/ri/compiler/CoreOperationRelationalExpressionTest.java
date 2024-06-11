package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Test;

/**
 * Test cases for {@link CoreOperationRelationalExpression} class.
 */
public class CoreOperationRelationalExpressionTest extends CoreOperationTest {

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(new Double(1.0));
        Expression right = new ConstantExpression(new Double(2.0));
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(left, right);
        Object result = expression.computeValue(getEvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new ConstantExpression(new Double(2.0));
        right = new ConstantExpression(new Double(1.0));
        expression = new CoreOperationGtExpression(left, right);
        result = expression.computeValue(getEvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);

        left = new SelfContext();
        right = new SelfContext();
        expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        result = expression.computeValue(getEvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new InitialContext();
        right = new InitialContext();
        expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        result = expression.computeValue(getEvalContext());
        assertNull(result);

        List<Object> collection1 = TestUtils.createList(1.0, 2.0, 3.0);
        Expression iterator1 = new IteratorExpression(collection1.iterator());
        List<Object> collection2 = TestUtils.createList(3.0, 2.0, 1.0);
        Expression iterator2 = new IteratorExpression(collection2.iterator());
        expression = new CoreOperationGtExpression(iterator1, iterator2);
        result = expression.computeValue(getEvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);

        collection1 = TestUtils.createList(1.0, 2.0);
        collection2 = TestUtils.createList(2.0);
        iterator1 = new IteratorExpression(collection1.iterator());
        iterator2 = new IteratorExpression(collection2.iterator());
        expression = new CoreOperationGtExpression(iterator1, iterator2);
        result = expression.computeValue(getEvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        collection1 = TestUtils.createList(1.0, 2.0, 3.0);
        collection2 = TestUtils.createList(3.0, 2.0, 1.0);
        iterator1 = new IteratorExpression(collection1.iterator());
        iterator2 = new IteratorExpression(collection2.iterator());
        expression = new CoreOperationGeqExpression(iterator1, iterator2);
        result = expression.computeValue(getEvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        collection1 = TestUtils.createList(1.0, 2.0);
        collection2 = TestUtils.createList(2.0);
        iterator1 = new IteratorExpression(collection1.iterator());
        iterator2 = new IteratorExpression(collection2.iterator());
        expression = new CoreOperationGeqExpression(iterator1, iterator2);
        result = expression.computeValue(getEvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);

        collection1 = TestUtils.createList(1.0, 2.0, 3.0);
        collection2 = TestUtils.createList(3.0, 2.0, 1.0);
        iterator1 = new IteratorExpression(collection1.iterator());
        iterator2 = new IteratorExpression(collection2.iterator());
        expression = new CoreOperationLtExpression(iterator1, iterator2);
        result = expression.computeValue(getEvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        collection1 = TestUtils.createList(1.0, 2.0);
        collection2 = TestUtils.createList(2.0);
        iterator1 = new IteratorExpression(collection1.iterator());
        iterator2 = new IteratorExpression(collection2.iterator());
        expression = new CoreOperationLtExpression(iterator1, iterator2);
        result = expression.computeValue(getEvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);

        collection1 = TestUtils.createList(1.0, 2.0, 3.0);
        collection2 = TestUtils.createList(3.0, 2.0, 1.0);
        iterator1 = new IteratorExpression(collection1.iterator());
        iterator2 = new IteratorExpression(collection2.iterator());
        expression = new CoreOperationLeqExpression(iterator1, iterator2);
        result = expression.computeValue(getEvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        collection1 = TestUtils.createList(1.0, 2.0);
        collection2 = TestUtils.createList(2.0);
        iterator1 = new IteratorExpression(collection1.iterator());
        iterator2 = new IteratorExpression(collection2.iterator());
        expression = new CoreOperationLeqExpression(iterator1, iterator2);
        result = expression.computeValue(getEvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationGeqExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationLtExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationLeqExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGtExpression(new Expression[0]);
        assertEquals(3, expression.getPrecedence());

        expression = new CoreOperationGeqExpression(new Expression[0]);
        assertEquals(3, expression.getPrecedence());

        expression = new CoreOperationLtExpression(new Expression[0]);
        assertEquals(3, expression.getPrecedence());

        expression = new CoreOperationLeqExpression(new Expression[0]);
        assertEquals(3, expression.getPrecedence());
    }

}