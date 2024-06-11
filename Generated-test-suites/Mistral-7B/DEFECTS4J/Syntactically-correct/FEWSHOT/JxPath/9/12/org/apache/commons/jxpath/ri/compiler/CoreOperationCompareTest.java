package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {
    @Test
    public void testCoreOperationCompare() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new Expression(new StringLiteral("1")),
                new Expression(new StringLiteral("1"))) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };

        Assert.assertEquals(2, coreOperationCompare.getPrecedence());
        Assert.assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new Expression(new StringLiteral("1")),
                new Expression(new StringLiteral("1"))) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };

        CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(
                new Expression(new StringLiteral("1")),
                new Expression(new StringLiteral("1"))) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return false;
            }
        };

        Assert.assertEquals(2, coreOperationCompare.getPrecedence());
        Assert.assertEquals(2, coreOperationCompare1.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new Expression(new StringLiteral("1")),
                new Expression(new StringLiteral("1"))) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };

        CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(
                new Expression(new StringLiteral("1")),
                new Expression(new StringLiteral("1"))) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return false;
            }
        };

        Assert.assertTrue(coreOperationCompare.isSymmetric());
        Assert.assertFalse(coreOperationCompare1.isSymmetric());
    }

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new Expression(new StringLiteral("1")),
                new Expression(new StringLiteral("1"))) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };

        EvalContext context = new EvalContext();

        Object l = new StringLiteral("1").compute(context);
        Object r = new StringLiteral("1").compute(context);

        Assert.assertTrue(coreOperationCompare.equal(context, new Expression(l), new Expression(r)));

        l = new StringLiteral("1").compute(context);
        r = new StringLiteral("2").compute(context);

        Assert.assertFalse(coreOperationCompare.equal(context, new Expression(l), new Expression(r)));

        l = new StringLiteral("1").compute(context);
        r = new StringLiteral("1").compute(context);

        Pointer pointer1 = new Pointer();
        pointer1.setValue(l);

        Pointer pointer2 = new Pointer();
        pointer2.setValue(r);

        Assert.assertTrue(coreOperationCompare.equal(context, new Expression(pointer1), new Expression(pointer2)));

        EvalContext context1 = new EvalContext();
        InitialContext initialContext = new InitialContext();
        context1.setInitialContext(initialContext);

        Assert.assertFalse(coreOperationCompare.equal(context1, new Expression(l), new Expression(r)));

        EvalContext context2 = new EvalContext();
        SelfContext selfContext = new SelfContext();
        context2.setSelfContext(selfContext);

        Assert.assertFalse(coreOperationCompare.equal(context2, new Expression(l), new Expression(r)));

        Collection collection = new ArrayList();
        collection.add(l);

        Assert.assertTrue(coreOperationCompare.equal(context, new Expression(new CollectionExpression(collection)), new Expression(new CollectionExpression(new ArrayList<Object>() {{ add(r); }}))));

        Iterator iterator = collection.iterator();

        Assert.assertTrue(coreOperationCompare.equal(context, new Expression(iterator), new Expression(r)));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new Expression(new StringLiteral("1")),
                new Expression(new StringLiteral("1"))) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };

        Collection collection = new ArrayList();
        collection.add(new StringLiteral("1").compute(new EvalContext()));

        Iterator iterator = collection.iterator();

        Assert.assertTrue(coreOperationCompare.contains(iterator, new StringLiteral("1").compute(new EvalContext())));

        Assert.assertFalse(coreOperationCompare.contains(iterator, new StringLiteral("2").compute(new EvalContext())));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new Expression(new StringLiteral("1")),
                new Expression(new StringLiteral("1"))) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };

        Collection collection = new ArrayList();
        collection.add(new StringLiteral("1").compute(new EvalContext()));

        Iterator iterator1 = collection.iterator();

        Collection collection1 = new ArrayList();
        collection1.add(new StringLiteral("1").compute(new EvalContext()));
        collection1.add(new StringLiteral("1").compute(new EvalContext()));

        Iterator iterator2 = collection1.iterator();

        Assert.assertTrue(coreOperationCompare.findMatch(iterator1, iterator2));

        Collection collection2 = new ArrayList();
        collection2.add(new StringLiteral("2").compute(new EvalContext()));

        Iterator iterator3 = collection2.iterator();

        Assert.assertFalse(coreOperationCompare.findMatch(iterator1, iterator3));
    }
}