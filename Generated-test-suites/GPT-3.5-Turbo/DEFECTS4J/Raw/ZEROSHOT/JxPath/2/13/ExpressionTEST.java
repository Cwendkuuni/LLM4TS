import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;

import java.util.Iterator;
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

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertEquals("test", result);
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

        EvalContext context = new EvalContext();
        Object result = expression.compute(context);

        assertEquals("test", result);
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
                return new String[]{"test1", "test2", "test3"};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Iterator iterator = expression.iterate(context);

        assertTrue(iterator.hasNext());
        assertEquals("test1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test3", iterator.next());
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
                return new String[]{"test1", "test2", "test3"};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(
                new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return new Pointer();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                },
                new QName(null, "value"),
                Locale.getDefault()
        );

        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(
                new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return new Pointer();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                }
        );

        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }
}