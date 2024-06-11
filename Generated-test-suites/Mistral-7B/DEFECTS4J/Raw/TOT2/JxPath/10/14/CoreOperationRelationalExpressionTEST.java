package org.junit.jupiter.params.provider.argument.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIterable;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.iterators.ArrayListIterator;
import org.apache.commons.jxpath.ri.iterators.IteratorExpression;
import org.apache.commons.jxpath.ri.literals.LiteralExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private static final int[][] TEST_DATA = {
            {0, true},
            {1, true},
            {-1, false},
            {Integer.MAX_VALUE, true},
            {Integer.MIN_VALUE, false},
            {Double.POSITIVE_INFINITY, true},
            {Double.NEGATIVE_INFINITY, false}
    };

    @Test
    public void testComputeValueTrue() {
        Expression left = new LiteralExpression(5);
        Expression right = new LiteralExpression(10);
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ComparisonExpression(">", left, right));
        EvalContext context = new EvaluationContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueFalse() {
        Expression left = new LiteralExpression(5);
        Expression right = new LiteralExpression(10);
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ComparisonExpression(">", left, right));
        EvalContext context = new EvaluationContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueIterators() {
        Iterator<Object> leftIterator = new ArrayList<Object>().iterator();
        Iterator<Object> rightIterator = new ArrayList<Object>().iterator();
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ComparisonExpression(">", leftIterator, rightIterator));
        EvaluationContext context = new EvaluationContext();
        context.setIterateIterable(new IterateIterable(leftIterator, rightIterator));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueDouble() {
        Expression left = new LiteralExpression(5.0);
        Expression right = new LiteralExpression(10.0);
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ComparisonExpression(">", left, right));
        EvalContext context = new EvaluationContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueIllegalArgument() {
        Expression left = new LiteralExpression(5);
        Expression right = null;
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ComparisonExpression(">", left, right));
        EvaluationContext context = new EvaluationContext();
        Object result = expression.computeValue(context);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ComparisonExpression(">", new LiteralExpression(1), new LiteralExpression(2)));
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ComparisonExpression(">", new LiteralExpression(1), new LiteralExpression(2)));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new MyCoreOperationRelationalExpression(new LiteralExpression(1.0), new LiteralExpression(2.0)));
        for (int[] testData : TEST_DATA) {
            int compare = testData[0];
            boolean expected = testData[1];
            boolean actual = expression.evaluateCompare(compare);
            assertEquals(expected, actual);
        }
    }
}

class MyCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

    public MyCoreOperationRelationalExpression(Expression... args) {
        super(args);
    }

    @Override
    protected boolean evaluateCompare(int compare) {
        return super.evaluateCompare(compare);
    }
}