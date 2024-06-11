import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;

import static org.junit.Assert.*;

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

        assertFalse(expression.isContextDependent());
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
                return "Test";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        assertEquals("Test", expression.computeValue(context));
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
                return "Test";
            }
        };

        EvalContext context = new EvalContext();
        assertEquals("Test", expression.compute(context));
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
                return new String[]{"Test1", "Test2", "Test3"};
            }
        };

        EvalContext context = new EvalContext();
        assertNotNull(expression.iterate(context));
        assertTrue(expression.iterate(context).hasNext());
        assertEquals("Test1", expression.iterate(context).next());
        assertEquals("Test2", expression.iterate(context).next());
        assertEquals("Test3", expression.iterate(context).next());
        assertFalse(expression.iterate(context).hasNext());
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
                return new String[]{"Test1", "Test2", "Test3"};
            }
        };

        EvalContext context = new EvalContext();
        Pointer pointer = new Pointer() {
            @Override
            public Object getBaseValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return null;
            }

            @Override
            public Pointer getPropertyPointer(QName name) {
                return null;
            }

            @Override
            public Iterator setPropertyPointer(QName name, Object value) {
                return null;
            }

            @Override
            public Iterator pushScope(QName name, Object value) {
                return null;
            }

            @Override
            public Pointer popScope() {
                return null;
            }

            @Override
            public boolean isCollection() {
                return false;
            }
        };

        context.setCurrentNodePointer(pointer);

        assertNotNull(expression.iteratePointers(context));
        assertTrue(expression.iteratePointers(context).hasNext());
        assertNotNull(expression.iteratePointers(context).next());
        assertFalse(expression.iteratePointers(context).hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(new StringIterator(), new QName(null, "value"), null);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    private class StringIterator implements Iterator {
        private int index = 0;
        private String[] strings = {"Test1", "Test2", "Test3"};

        @Override
        public boolean hasNext() {
            return index < strings.length;
        }

        @Override
        public Object next() {
            return strings[index++];
        }
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(new PointerIterator());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    private class PointerIterator implements Iterator {
        private int index = 0;
        private String[] strings = {"Test1", "Test2", "Test3"};

        @Override
        public boolean hasNext() {
            return index < strings.length;
        }

        @Override
        public Object next() {
            return new Expression.Pointer() {
                @Override
                public Object getBaseValue() {
                    return null;
                }

                @Override
                public Object getImmediateNode() {
                    return strings[index++];
                }

                @Override
                public Expression.Pointer getPropertyPointer(QName name) {
                    return null;
                }

                @Override
                public Iterator setPropertyPointer(QName name, Object value) {
                    return null;
                }

                @Override
                public Iterator pushScope(QName name, Object value) {
                    return null;
                }

                @Override
                public Expression.Pointer popScope() {
                    return null;
                }

                @Override
                public boolean isCollection() {
                    return false;
                }
            };
        }
    }
}