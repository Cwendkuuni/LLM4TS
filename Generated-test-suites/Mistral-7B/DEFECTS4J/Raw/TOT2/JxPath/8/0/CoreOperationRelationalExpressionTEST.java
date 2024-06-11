package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;
import java.util.*;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.*;
import org.apache.commons.jxpath.ri.compiler.*;
import org.junit.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression(true), new LiteralExpression(false));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationRelationalExpression(new LiteralExpression(5.5), new LiteralExpression(6.5));
        context = new EvalContext();
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationRelationalExpression(new LiteralExpression(5.5), new LiteralExpression(5.5));
        context = new EvalContext();
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNullArg() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression(true), null);
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(null, result);
    }

    @Test
    public void testComputeValueWithNonBooleanArg() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression(true), new NumberExpression(3));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCompareIteratorsDifferentSizes() {
        Iterator<Object> lit = PathFactory.newPath("//node()[1]").iterator(new EvalContext());
        Iterator<Object> rit = PathFactory.newPath("//node()[position()<2]").iterator(new EvalContext());
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(lit, rit);
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCompareIteratorsSameElementsDifferentOrders() {
        Iterator<Object> lit = PathFactory.newPath("//node()[1]").iterator(new EvalContext());
        Iterator<Object> rit = PathFactory.newPath("//node()[position()=last()-1]").iterator(new EvalContext());
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(lit, rit);
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCompareIteratorWithItself() {
        Iterator<Object> it = PathFactory.newPath("//node()[1]").iterator(new EvalContext());
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(it, it);
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCompareIteratorWithEmptyIterator() {
        Iterator<Object> it = PathFactory.newPath("//node()[1]").iterator(new EvalContext());
        Iterator<Object> emptyIterator = new ArrayList<Object>().iterator();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(it, emptyIterator);
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCompareIteratorWithInfiniteIterator() {
        Iterator<Object> it = new Iterator<Object>() {
            int counter = 0;

            @Override
            public boolean hasNext() {
                return counter++ < Integer.MAX_VALUE;
            }

            @Override
            public Object next() {
                return new LiteralExpression(counter);
            }
        };
        Iterator<Object> rit = PathFactory.newPath("//node()[1]").iterator(new EvalContext());
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(it, rit);
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCompareDoubleValueWithInfiniteDoubleValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression(Double.POSITIVE_INFINITY), new LiteralExpression(Double.NaN));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}