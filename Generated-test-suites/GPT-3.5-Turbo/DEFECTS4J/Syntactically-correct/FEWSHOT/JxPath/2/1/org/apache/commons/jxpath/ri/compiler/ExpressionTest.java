import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.java.Expression;
import org.java.Pointer;
import org.java.EvalContext;
import org.java.NodePointer;
import org.java.QName;
import org.java.ValueUtils;
import org.junit.Test;
import java.util.Collections;
import java.util.Locale;

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
    public void testComputeContextDependent() {
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
        
        assertFalse(expression.computeContextDependent());
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
                return Collections.singleton("test").iterator();
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        assertTrue(expression.iterate(null).hasNext());
        assertEquals("test", expression.iterate(null).next());
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
                return Collections.singleton("test").iterator();
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        assertFalse(expression.iteratePointers(null).hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(Collections.singleton(new Pointer()).iterator(), new QName(null, "test"), Locale.US);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(Collections.singleton(new Pointer()).iterator());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
}