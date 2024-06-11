import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression() {
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
    }

    @Test
    public void testIsContextDependent() {
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependent() {
        assertTrue(expression.computeContextDependent());
    }

    @Test
    public void testComputeValue() {
        assertNull(expression.computeValue(null));
    }

    @Test
    public void testCompute() {
        assertNull(expression.compute(null));
    }

    @Test
    public void testIterate() {
        Iterator iterator = expression.iterate(null);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_NullResult() {
        Iterator iterator = expression.iteratePointers(null);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_ResultIsEvalContext() {
        Iterator iterator = expression.iteratePointers(null);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(Collections.emptyIterator(), null, Locale.getDefault());
        assertFalse(pointerIterator.hasNext());
        assertNull(pointerIterator.next());
        assertThrows(UnsupportedOperationException.class, pointerIterator::remove);
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator valueIterator = new Expression.ValueIterator(Collections.emptyIterator());
        assertFalse(valueIterator.hasNext());
        assertNull(valueIterator.next());
        assertThrows(UnsupportedOperationException.class, valueIterator::remove);
    }
}