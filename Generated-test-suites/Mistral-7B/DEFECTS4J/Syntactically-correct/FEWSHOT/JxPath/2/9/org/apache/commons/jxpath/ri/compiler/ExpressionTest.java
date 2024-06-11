package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testAbstractExpressionIsContextDependent() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
        };
        Assert.assertTrue(expression.isContextDependent());
    }

    @Test
    public void testAbstractExpressionIsNotContextDependent() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };
        Assert.assertFalse(expression.isContextDependent());
    }

    @Test
    public void testIterate() {
        Expression expression = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Object[]{"foo", "bar"};
            }

            @Override
            public Iterator iterate(EvalContext context) {
                return new Iterator() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < 2;
                    }

                    @Override
                    public Object next() {
                        return hasNext() ? Integer.toString(index++) : null;
                    }
                };
            }
        };

        Iterator iterator = expression.iterate(new EvalContext(new Object[0], null));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("0", iterator.next().toString());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("1", iterator.next().toString());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Object[]{new NodePointer(new QName("test"), new Object(), Locale.US),
                        new NodePointer(new QName("test2"), new Object(), Locale.US)};
            }

            @Override
            public Iterator iteratePointers(EvalContext context) {
                return new Iterator() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < 2;
                    }

                    @Override
                    public Object next() {
                        return hasNext() ? Integer.toString(index++) : null;
                    }
                };
            }
        };

        Iterator<Pointer> iterator = expression.iteratePointers(new EvalContext(new Object[0], null));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new QName("test"), iterator.next().getQName());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new QName("test2"), iterator.next().getQName());
        Assert.assertFalse(iterator.hasNext());
    }
}