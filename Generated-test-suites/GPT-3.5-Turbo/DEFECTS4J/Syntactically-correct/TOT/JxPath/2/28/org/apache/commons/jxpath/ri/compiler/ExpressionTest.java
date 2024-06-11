import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testIsContextDependent_True() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        boolean isContextDependent = expression.isContextDependent();
        Assert.assertTrue(isContextDependent);
    }

    @Test
    public void testIsContextDependent_False() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        boolean isContextDependent = expression.isContextDependent();
        Assert.assertFalse(isContextDependent);
    }

    @Test
    public void testComputeValue_NullResult() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext evalContext = new EvalContext();
        Object result = expression.computeValue(evalContext);
        Assert.assertNull(result);
    }

    @Test
    public void testCompute_NullResult() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext evalContext = new EvalContext();
        Object result = expression.compute(evalContext);
        Assert.assertNull(result);
    }

    @Test
    public void testIterate_WithEvalContext() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                EvalContext evalContext = new EvalContext();
                evalContext.addPointer(new NodePointer());
                return evalContext;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext evalContext = new EvalContext();
        Iterator iterator = expression.iterate(evalContext);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_WithResult() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return Collections.singletonList("Value").iterator();
            }
        };

        EvalContext evalContext = new EvalContext();
        Iterator iterator = expression.iterate(evalContext);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_NullResult() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext evalContext = new EvalContext();
        Iterator iterator = expression.iteratePointers(evalContext);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_WithEvalContext() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                EvalContext evalContext = new EvalContext();
                evalContext.addPointer(new NodePointer());
                return evalContext;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext evalContext = new EvalContext();
        Iterator iterator = expression.iteratePointers(evalContext);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_RemoveUnsupportedOperationException() {
        Expression.ValueIterator valueIterator = new Expression.ValueIterator(Collections.emptyList().iterator());
        try {
            valueIterator.remove();
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testPointerIterator_RemoveUnsupportedOperationException() {
        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(Collections.emptyList().iterator(), new QName(), Locale.getDefault());
        try {
            pointerIterator.remove();
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
}