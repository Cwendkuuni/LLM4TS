package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(new SelfContext(1));
        Expression right = new LiteralExpression(new SelfContext(2));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);

        EvalContext context = new EvalContext();
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(new SelfContext(1));
        right = new LiteralExpression(new SelfContext(1));
        expression = new GreaterThanOrEqualExpression(left, right);
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(new SelfContext(2));
        right = new LiteralExpression(new SelfContext(1));
        expression = new GreaterThanOrEqualExpression(left, right);
        assertFalse(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(new SelfContext(1));
        right = new LiteralExpression(new SelfContext(1));
        expression = new LessThanExpression(left, right);
        assertFalse(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(new SelfContext(1));
        right = new LiteralExpression(new SelfContext(2));
        expression = new LessThanOrEqualExpression(left, right);
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(new SelfContext(1));
        right = new LiteralExpression(new SelfContext(1));
        expression = new EqualsExpression(left, right);
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(new SelfContext(1));
        right = new LiteralExpression(new SelfContext(2));
        expression = new NotEqualsExpression(left, right);
        assertFalse(expression.computeValue(context) == Boolean.TRUE);

        // Test with numbers
        left = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        right = new LiteralExpression(InfoSetUtil.createDoubleValue(2.0));
        expression = new GreaterThanExpression(left, right);
        assertFalse(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        right = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        expression = new GreaterThanOrEqualExpression(left, right);
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(InfoSetUtil.createDoubleValue(2.0));
        right = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        expression = new GreaterThanOrEqualExpression(left, right);
        assertFalse(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        right = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        expression = new LessThanExpression(left, right);
        assertFalse(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        right = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        expression = new LessThanOrEqualExpression(left, right);
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        right = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        expression = new EqualsExpression(left, right);
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        left = new LiteralExpression(InfoSetUtil.createDoubleValue(1.0));
        right = new LiteralExpression(InfoSetUtil.createDoubleValue(2.0));
        expression = new NotEqualsExpression(left, right);
        assertFalse(expression.computeValue(context) == Boolean.TRUE);

        // Test with collections
        List leftList = new ArrayList();
        leftList.add(new SelfContext(1));
        leftList.add(new SelfContext(2));

        List rightList = new ArrayList();
        rightList.add(new SelfContext(1));

        Expression leftCollection = new CollectionExpression(leftList);
        Expression rightCollection = new CollectionExpression(rightList);

        expression = new GreaterThanOrEqualExpression(leftCollection, rightCollection);
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        leftList.add(new SelfContext(3));
        assertTrue(expression.computeValue(context) == Boolean.FALSE);

        rightList.add(new SelfContext(3));
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        // Test with iterators
        Iterator leftIterator = Collections.singletonList(new SelfContext(1)).iterator();
        Iterator rightIterator = Collections.singletonList(new SelfContext(1)).iterator();

        expression = new GreaterThanOrEqualExpression(new IteratorExpression(leftIterator), new IteratorExpression(rightIterator));
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        leftIterator = Collections.singletonList(new SelfContext(2)).iterator();
        assertFalse(expression.computeValue(context) == Boolean.TRUE);

        rightIterator = Collections.singletonList(new SelfContext(1)).iterator();
        assertTrue(expression.computeValue(context) == Boolean.TRUE);

        leftIterator = Collections.singletonList(new SelfContext(1)).iterator();
        rightIterator = Collections.singletonList(new SelfContext(2)).iterator();

        assertFalse(expression.computeValue(context) == Boolean.TRUE);
    }
}