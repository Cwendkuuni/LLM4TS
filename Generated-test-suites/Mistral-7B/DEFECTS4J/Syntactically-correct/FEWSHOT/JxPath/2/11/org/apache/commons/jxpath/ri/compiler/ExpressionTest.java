package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {
    @Test
    public void testIsContextDependent() {
        AbstractExpression exp1 = new ConstantExpression(1);
        AbstractExpression exp2 = new VariableExpression("foo");

        Assert.assertFalse(exp1.isContextDependent());
        Assert.assertTrue(exp2.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        AbstractExpression exp1 = new ConstantExpression(1);
        AbstractExpression exp2 = new VariableExpression("foo");

        EvalContext context = new EvalContext(null, null, null, null);

        Object result1 = exp1.computeValue(context);
        Assert.assertEquals(1, result1.doubleValue());

        Object result2 = exp2.computeValue(context);
        Assert.assertNull(result2);
    }

    @Test
    public void testCompute() {
        AbstractExpression exp1 = new ConstantExpression(1);
        AbstractExpression exp2 = new VariableExpression("foo");

        EvalContext context = new EvalContext(null, null, null, null);

        Object result1 = exp1.compute(context);
        Assert.assertEquals(1, result1.doubleValue());

        Object result2 = exp2.compute(context);
        Assert.assertNull(result2);
    }

    @Test
    public void testIterate() {
        AbstractExpression exp1 = new ConstantExpression(Collections.singletonList("1, 2, 3"));

        EvalContext context = new EvalContext(null, null, null, null);

        Iterator iter = exp1.iterate(context);

        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals("1", iter.next().toString());
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals("2", iter.next().toString());
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals("3", iter.next().toString());
        Assert.assertFalse(iter.hasNext());
    }

    @Test
    public void testIteratePointers() {
        AbstractExpression exp1 = new ConstantExpression(Collections.singletonList(new NodePointer("root", 1)));

        EvalContext context = new EvalContext(null, null, null, null);

        Iterator iter = exp1.iteratePointers(context);

        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals("root", iter.next().getNamespaceURI());
        Assert.assertEquals(1, iter.next().getIndex());
        Assert.assertFalse(iter.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterateRemove() {
        AbstractExpression exp1 = new ConstantExpression(1);

        Iterator iterator = exp1.iterate(null);
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterateValueRemove() {
        AbstractExpression exp1 = new ConstantExpression(Collections.singletonList("1"));

        EvalContext context = new EvalContext(null, null, null, null);

        Iterator iterator = exp1.iterate(context);
        iterator.next();
        iterator.remove();
    }
}

abstract class AbstractExpression {
    public boolean computeContextDependent() {
        throw new UnsupportedOperationException();
    }
}

class ConstantExpression extends Expression {
    private Object constant;

    public ConstantExpression(Object constant) {
        this.constant = constant;
    }

    public ConstantExpression(Iterator iterator) {
        this.constant = iterator.next();
    }

    public ConstantExpression(Object constant, Iterator iterator) {
        this.constant = constant;
    }

    public ConstantExpression(Object constant, Iterator iterator, int size) {
        this.constant = constant;
    }

    public ConstantExpression(Iterable iterable) {
        this.constant = iterable.iterator().next();
    }

    public ConstantExpression(Iterable iterable, int size) {
        this.constant = iterable;
    }

    public Object computeValue(EvalContext context) {
        return constant;
    }

    public Object compute(EvalContext context) {
        return constant;
    }

    public Iterator iterate(EvalContext context) {
        return new ValueUtils.SingletonIterator(constant);
    }

    public Iterator iteratePointers(EvalContext context) {
        return new PointerIterator(new ValueUtils.SingletonIterator(new NodePointer("", 0)),
                new QName(null, "value"),
                Locale.ENGLISH);
    }
}

class VariableExpression extends Expression {
    private String variableName;

    public VariableExpression(String variableName) {
        this.variableName = variableName;
    }

    public boolean computeContextDependent() {
        return true;
    }
}