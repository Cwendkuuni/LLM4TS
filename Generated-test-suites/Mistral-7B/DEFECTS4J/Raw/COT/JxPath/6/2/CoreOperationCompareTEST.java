package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression arg1 = new ExpressionMock();
        Expression arg2 = new ExpressionMock();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Assert.assertEquals(2, coreOperationCompare.getOperands().length);
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new ConstantExpressionMock(1.0);
        Expression right = new ConstantExpressionMock(1.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithDifferentTypes() {
        EvalContext context = new EvalContext();
        Expression left = new ConstantExpressionMock("Hello");
        Expression right = new ConstantExpressionMock(1.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithNull() {
        EvalContext context = new EvalContext();
        Expression left = new ConstantExpressionMock(null);
        Expression right = new ConstantExpressionMock(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithNotEqualValues() {
        EvalContext context = new EvalContext();
        Expression left = new ConstantExpressionMock(1.0);
        Expression right = new ConstantExpressionMock(2.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithInitialContext() {
        EvalContext context = new EvalContext();
        Expression left = new InitialContext(context);
        Expression right = new InitialContext(context);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithSelfContext() {
        EvalContext context = new EvalContext();
        Expression left = new SelfContext(context);
        Expression right = new SelfContext(context);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithCollection() {
        EvalContext context = new EvalContext();
        Collection collection1 = new ArrayList();
        collection1.add(new ConstantExpressionMock(1.0));
        Expression left = new ConstantExpressionMock(collection1);
        Collection collection2 = new ArrayList();
        collection2.add(new ConstantExpressionMock(1.0));
        Expression right = new ConstantExpressionMock(collection2);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext();
        Iterator iterator = new ArrayList(List.of(1.0, 2.0)).iterator();
        Expression left = new ConstantExpressionMock(iterator);
        Object value = new ConstantExpressionMock(1.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, value);
        boolean result = coreOperationCompare.contains(iterator, value);
        Assert.assertTrue(result);
    }

    @Test
    public void testContainsWithValueNotInCollection() {
        EvalContext context = new EvalContext();
        Iterator iterator = new ArrayList(List.of(1.0, 2.0)).iterator();
        Expression left = new ConstantExpressionMock(iterator);
        Object value = new ConstantExpressionMock(3.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, value);
        boolean result = coreOperationCompare.contains(iterator, value);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext();
        Iterator iterator1 = new ArrayList(List.of(1.0, 2.0)).iterator();
        Iterator iterator2 = new ArrayList(List.of(1.0, 3.0)).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.findMatch(iterator1, iterator2);
        Assert.assertFalse(result);
        coreOperationCompare = new CoreOperationCompare(iterator1, iterator2);
        result = coreOperationCompare.findMatch(iterator1, iterator2);
        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatchWithIteratorNotContainingValue() {
        EvalContext context = new EvalContext();
        Iterator iterator1 = new ArrayList(List.of(1.0, 2.0)).iterator();
        Iterator iterator2 = new ArrayList(List.of(3.0, 4.0)).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(iterator1, iterator2);
        boolean result = coreOperationCompare.findMatch(iterator1, iterator2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithDifferentIterators() {
        EvalContext context = new EvalContext();
        Iterator iterator1 = new ArrayList(List.of(1.0, 2.0)).iterator();
        Iterator iterator2 = new ArrayList(List.of(3.0, 4.0)).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(iterator1, iterator2);
        boolean result = coreOperationCompare.equal(context, iterator1, iterator2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithSameIteratorsButDifferentOrder() {
        EvalContext context = new EvalContext();
        Iterator iterator1 = new ArrayList(List.of(1.0, 2.0)).iterator();
        Iterator iterator2 = new ArrayList(List.of(2.0, 1.0)).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(iterator1, iterator2);
        boolean result = coreOperationCompare.equal(context, iterator1, iterator2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithNullIterator() {
        EvalContext context = new EvalContext();
        Iterator iterator1 = null;
        Iterator iterator2 = new ArrayList(List.of(1.0, 2.0)).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(iterator1, iterator2);
        boolean result = coreOperationCompare.equal(context, iterator1, iterator2);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualWithNullLeft() {
        EvalContext context = new EvalContext();
        Expression left = null;
        Expression right = new ConstantExpressionMock(1.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        coreOperationCompare.equal(context, left, right);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualWithNullRight() {
        EvalContext context = new EvalContext();
        Expression left = new ConstantExpressionMock(1.0);
        Expression right = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        coreOperationCompare.equal(context, left, right);
    }
}

class ExpressionMock implements Expression {
    @Override
    public Object compute(EvalContext context) {
        return null;
    }
}

class ConstantExpressionMock implements Expression {
    private Object value;

    public ConstantExpressionMock(Object value) {
        this.value = value;
    }

    @Override
    public Object compute(EvalContext context) {
        return value;
    }
}