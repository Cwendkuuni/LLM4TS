
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
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

        assertTrue(expression.isContextDependent());
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
                return "test";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        assertEquals("test", expression.computeValue(null));
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
                return "test";
            }
        };

        assertEquals("test", expression.compute(null));
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
                return new String[]{"test1", "test2"};
            }
        };

        Iterator iterator = expression.iterate(null);
        assertTrue(iterator.hasNext());
        assertEquals("test1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test2", iterator.next());
        assertFalse(iterator.hasNext());
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
                return new String[]{"test1", "test2"};
            }
        };

        Iterator iterator = expression.iteratePointers(null);
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertTrue(next instanceof NodePointer);
        assertEquals("test1", ((NodePointer) next).getValue());
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertTrue(next instanceof NodePointer);
        assertEquals("test2", ((NodePointer) next).getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator pointerIterator =
                new Expression.PointerIterator(Collections.singletonList("test").iterator(),
                        new QName(null, "value"),
                        Locale.US);

        assertTrue(pointerIterator.hasNext());
        Object next = pointerIterator.next();
        assertTrue(next instanceof NodePointer);
        assertEquals("test", ((NodePointer) next).getValue());
        assertFalse(pointerIterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator valueIterator =
                new Expression.ValueIterator(Collections.singletonList(new Pointer("test")).iterator());

        assertTrue(valueIterator.hasNext());
        assertEquals("test", valueIterator.next());
        assertFalse(valueIterator.hasNext());
    }
}
