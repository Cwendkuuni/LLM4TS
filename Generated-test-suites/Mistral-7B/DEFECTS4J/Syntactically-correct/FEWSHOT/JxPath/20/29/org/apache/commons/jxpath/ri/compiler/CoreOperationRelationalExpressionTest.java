package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        // Test default constructor
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertNull(expression.getArgs());
        assertEquals(0, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(1.0);
        Expression right = new ConstantExpression(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new ConstantExpression(2.0);
        right = new ConstantExpression(1.0);
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        context = new EvalContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new ConstantExpression(1.5);
        right = new ConstantExpression(1.5);
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        context = new EvalContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new ConstantExpression(1.5);
        right = new ConstantExpression(1.4);
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        context = new EvalContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testCompute() {
        Expression left = new ConstantExpression(1.0);
        Expression right = new ConstantExpression(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        Object leftValue = expression.reduce(left.compute(new EvalContext()));
        Object rightValue = expression.reduce(right.compute(new EvalContext()));

        assertTrue(expression.compute(leftValue, rightValue));

        left = new ConstantExpression("foo");
        right = new ConstantExpression("bar");
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        assertFalse(expression.compute(left.compute(new EvalContext()), right.compute(new EvalContext())));

        left = new ConstantExpression(new SelfContext());
        right = new ConstantExpression(new SelfContext());
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Object());
        assertFalse(expression.compute(left.compute(context), right.compute(context)));

        left = new ConstantExpression(new InitialContext());
        right = new ConstantExpression(new InitialContext());
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        assertFalse(expression.compute(left.compute(new EvalContext()), right.compute(new EvalContext())));

        left = new ArrayExpression(new Expression[]{new ConstantExpression(1.0)});
        right = new ArrayExpression(new Expression[]{new ConstantExpression(2.0)});
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        context1.setSingleNodePointer(new Object());
        context2.setSingleNodePointer(new Object());

        Iterator leftIterator = left.compute(context1).iterator();
        Iterator rightIterator = right.compute(context2).iterator();

        assertFalse(expression.compute(leftIterator, rightIterator));

        left = new ArrayExpression(new Expression[]{new ConstantExpression(1.0), new ConstantExpression(2.0)});
        right = new ArrayExpression(new Expression[]{new ConstantExpression(2.0), new ConstantExpression(1.0)});
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        context1 = new EvalContext();
        context2 = new EvalContext();
        context1.setSingleNodePointer(new Object());
        context2.setSingleNodePointer(new Object());

        leftIterator = left.compute(context1).iterator();
        rightIterator = right.compute(context2).iterator();

        assertTrue(expression.compute(leftIterator, rightIterator));

        left = new CollectionExpression(new ArrayExpression(new Expression[]{new ConstantExpression(1.0)}));
        right = new CollectionExpression(new ArrayExpression(new Expression[]{new ConstantExpression(2.0)}));
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        context1 = new EvalContext();
        context2 = new EvalContext();
        context1.setSingleNodePointer(new Object());
        context2.setSingleNodePointer(new Object());

        Iterator leftCollectionIterator = left.compute(context1).iterator();
        Iterator rightCollectionIterator = right.compute(context2).iterator();

        assertFalse(expression.compute(leftCollectionIterator, rightCollectionIterator));

        left = new CollectionExpression(new ArrayList<Object>() {{
            add(new ConstantExpression(1.0));
            add(new ConstantExpression(2.0));
        }});
        right = new CollectionExpression(new ArrayList<Object>() {{
            add(new ConstantExpression(2.0));
            add(new ConstantExpression(1.0));
        }});
        expression = new CoreOperationGreaterThanExpression(new Expression[]{left, right});

        context1 = new EvalContext();
        context2 = new EvalContext();
        context1.setSingleNodePointer(new Object());
        context2.setSingleNodePointer(new Object());

        leftCollectionIterator = left.compute(context1).iterator();
        rightCollectionIterator = right.compute(context2).iterator();

        assertTrue(expression.compute(leftCollectionIterator, rightCollectionIterator));
    }

    @Test
    public void testReduce() {
        Expression constantExpression = new ConstantExpression(1.0);
        Object result = expression.reduce(constantExpression.compute(new EvalContext()));
        assertSame(constantExpression.compute(new EvalContext()), result);

        Iterator arrayIterator = new ArrayExpression(new Expression[]{constantExpression}).compute(new EvalContext()).iterator();
        result = expression.reduce(arrayIterator);
        assertSame(arrayIterator.next(), result);

        Collection collection = new ArrayList<Object>();
        collection.add(constantExpression.compute(new EvalContext()));
        result = expression.reduce(new CollectionExpression(collection).compute(new EvalContext()));
        assertSame(collection.get(0), result);
    }

    @Test
    public void testContainsMatch() {
        Iterator iterator = new ArrayList<Object>() {{
            add(new ConstantExpression(1.0));
            add(new ConstantExpression(2.0));
        }}.iterator();

        Object value = new ConstantExpression(1.0).compute(new EvalContext());

        assertTrue(CoreOperationRelationalExpression.containsMatch(iterator, value));

        value = new ConstantExpression(2.0).compute(new EvalContext());

        assertTrue(CoreOperationRelationalExpression.containsMatch(iterator, value));

        value = new ConstantExpression(3.0).compute(new EvalContext());

        assertFalse(CoreOperationRelationalExpression.containsMatch(iterator, value));
    }

    @Test
    public void testFindMatch() {
        Iterator leftIterator = new ArrayList<Object>() {{
            add(new ConstantExpression(1.0));
            add(new ConstantExpression(2.0));
        }}.iterator();

        Iterator rightIterator = new ArrayList<Object>() {{
            add(new ConstantExpression(1.0));
            add(new ConstantExpression(2.0));
        }}.iterator();

        HashSet leftSet = new HashSet();
        while (leftIterator.hasNext()) {
            leftSet.add(leftIterator.next());
        }

        Object value = new ConstantExpression(1.0).compute(new EvalContext());
        assertTrue(CoreOperationRelationalExpression.findMatch(leftIterator, value));
        assertTrue(leftSet.contains(value));

        value = new ConstantExpression(2.0).compute(new EvalContext());
        assertTrue(CoreOperationRelationalExpression.findMatch(leftIterator, value));
        assertTrue(leftSet.contains(value));

        value = new ConstantExpression(3.0).compute(new EvalContext());
        assertFalse(CoreOperationRelationalExpression.findMatch(leftIterator, value));
        assertFalse(leftSet.contains(value));
    }
}