import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testBooleanEqual() {
        Expression left = Expression.parse("true()", new EvaluationContext());
        Expression right = Expression.parse("true()", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        boolean result = op.compute(context);

        Assert.assertTrue(result);
    }

    @Test
    public void testBooleanNotEqual() {
        Expression left = Expression.parse("true()", new EvaluationContext());
        Expression right = Expression.parse("false()", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        boolean result = op.compute(context);

        Assert.assertFalse(result);
    }

    @Test
    public void testStringEqual() {
        Expression left = Expression.parse("'abc'[1]", new EvaluationContext());
        Expression right = Expression.parse("'abc'[1]", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        JXPathContext jxpathContext = new JXPathContext(new Object[]{"abcdefghijklmnopqrstuvwxyz"});

        EvaluationContext context = new EvaluationContext(jxpathContext);

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(true, ((Boolean) result).booleanValue());
    }

    @Test
    public void testStringNotEqual() {
        Expression left = Expression.parse("'abc'[1]", new EvaluationContext());
        Expression right = Expression.parse("'def'[1]", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        JXPathContext jxpathContext = new JXPathContext(new Object[]{"abcdefghijklmnopqrstuvwxyz"});

        EvaluationContext context = new EvaluationContext(jxpathContext);

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(false, ((Boolean) result).booleanValue());
    }

    @Test
    public void testNumberEqual() {
        Expression left = Expression.parse("1+1", new EvaluationContext());
        Expression right = Expression.parse("1+1", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(true, ((Boolean) result).booleanValue());
    }

    @Test
    public void testNumberNotEqual() {
        Expression left = Expression.parse("1+1", new EvaluationContext());
        Expression right = Expression.parse("2+1", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(false, ((Boolean) result).booleanValue());
    }

    @Test
    public void testCollectionEqual() {
        Expression left = Expression.parse("'(1,2,3)'[*]", new EvaluationContext());
        Expression right = Expression.parse("'(1,2,3)'[*]", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(true, ((Boolean) result).booleanValue());
    }

    @Test
    public void testCollectionNotEqual() {
        Expression left = Expression.parse("'(1,2,3)'[*]", new EvaluationContext());
        Expression right = Expression.parse("'(1,3,3)'[*]", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(false, ((Boolean) result).booleanValue());
    }

    @Test
    public void testNullEqual() {
        Expression left = Expression.parse("null()", new EvaluationContext());
        Expression right = Expression.parse("null()", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(true, ((Boolean) result).booleanValue());
    }

    @Test
    public void testNullNotEqual() {
        Expression left = Expression.parse("null()", new EvaluationContext());
        Expression right = Expression.parse("'abc'[1]", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(false, ((Boolean) result).booleanValue());
    }

    @Test
    public void testEmptyCollectionsEqual() {
        Expression left = Expression.parse("'()'", new EvaluationContext());
        Expression right = Expression.parse("'()'", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(true, ((Boolean) result).booleanValue());
    }

    @Test
    public void testEmptyCollectionsNotEqual() {
        Expression left = Expression.parse("'()'", new EvaluationContext());
        Expression right = Expression.parse("'(1,2,3)'[*]", new EvaluationContext());

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(false, ((Boolean) result).booleanValue());
    }

    @Test
    public void testIteratorEqual() {
        List<Object> list = new LinkedList<>(Arrays.asList(1, 2, 3));
        Expression left = Expression.parse("$it", new EvaluationContext(new JXPathContext(list.iterator())));
        Expression right = Expression.parse("$it", new EvaluationContext(new JXPathContext(list.iterator())));

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(true, ((Boolean) result).booleanValue());
    }

    @Test
    public void testIteratorNotEqual() {
        List<Object> listLeft = new LinkedList<>(Arrays.asList(1, 2, 3));
        List<Object> listRight = new LinkedList<>(Arrays.asList(1, 3, 3));

        Expression left = Expression.parse("$it", new EvaluationContext(new JXPathContext(listLeft.iterator())));
        Expression right = Expression.parse("$it", new EvaluationContext(new JXPathContext(listRight.iterator())));

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        EvaluationContext context = new EvaluationContext();

        Object result = op.compute(context);

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(false, ((Boolean) result).booleanValue());
    }
}