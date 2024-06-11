import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Node;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

/**
 * Test cases for {@link Expression} class.
 */
public class ExpressionTest {

    @Test
    public void testConstantExpression() {
        Expression expression = new ConstantExpression();
        Assert.assertFalse(expression.isContextDependent());
        Assert.assertEquals(1.0, expression.computeValue(new EvalContext()));
        Assert.assertEquals(1.0, expression.compute(new EvalContext()));
        Assert.assertNull(expression.iterate(new EvalContext()));
        Assert.assertNull(expression.iteratePointers(new EvalContext()));
    }

    @Test
    public void testContextDependentExpression() {
        Expression expression = new ContextDependentExpression();
        Assert.assertTrue(expression.isContextDependent());
        EvalContext context = new EvalContext();
        context.setCurrentNodePointer(new NodePointer(new QName("root"), new Node("root")));;
        Assert.assertEquals(context.getCurrentNodePointer(), expression.computeValue(context));
        Assert.assertEquals(context.getCurrentNodePointer(), expression.compute(context));
        Assert.assertEquals(Collections.singletonList(context.getCurrentNodePointer()), expression.iterate(context).next());
        Assert.assertEquals(Collections.singletonList(context.getCurrentNodePointer()), expression.iteratePointers(context).next());
        context = new EvalContext();
        Assert.assertNull(expression.computeValue(context));
        Assert.assertNull(expression.compute(context));
        Assert.assertNull(expression.iterate(context).next());
        Assert.assertNull(expression.iteratePointers(context).next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        Expression expression = new Expression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return new Iterator() {
                    private boolean hasNext = true;

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public Object next() {
                        hasNext = false;
                        return null;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        expression.iterate(new EvalContext());
    }

    /**
     * This is a simple expression which always returns a constant value.
     */
    static class ConstantExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return 1.0;
        }

        @Override
        public Object compute(EvalContext context) {
            return 1.0;
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return Collections.emptyIterator();
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return Collections.emptyList().iterator();
        }
    }

    /**
     * This is a context dependent expression which returns the current node pointer.
     */
    static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return context.getCurrentNodePointer();
        }

        @Override
        public Object compute(EvalContext context) {
            return context.getCurrentNodePointer();
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return new Iterator() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return context.getCurrentNodePointer();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return new Iterator() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return context.getCurrentNodePointer();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
}