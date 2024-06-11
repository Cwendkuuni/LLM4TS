import java.util.Iterator;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
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
    public void testComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
            @Override
            public Object computeValue(EvalContext context) {
                return "Value";
            }
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertEquals("Value", result);
    }

    @Test
    public void testCompute() {
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
                return "Result";
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        Assert.assertEquals("Result", result);
    }

    @Test
    public void testIterate() {
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
                return new Object[]{1, 2, 3};
            }
        };

        EvalContext context = new EvalContext();
        Iterator iterator = expression.iterate(context);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
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
                return new Object[]{1, 2, 3};
            }
        };

        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }
            @Override
            public Object next() {
                return new Pointer("/test");
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }, new QName("", "test"), null);

        Assert.assertTrue(iterator.hasNext());
        Object next = iterator.next();
        Assert.assertTrue(next instanceof NodePointer);
        Assert.assertEquals("/test", ((NodePointer)next).asString());

        iterator.remove();
        Assert.assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }
            @Override
            public Object next() {
                return new Pointer("test");
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        });

        Assert.assertTrue(iterator.hasNext());
        Object next = iterator.next();
        Assert.assertEquals("test", next);

        iterator.remove();
        Assert.assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

}