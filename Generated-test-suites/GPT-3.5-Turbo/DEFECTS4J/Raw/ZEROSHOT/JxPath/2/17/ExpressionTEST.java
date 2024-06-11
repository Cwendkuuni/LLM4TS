import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Collections;
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
        
        EvalContext evalContext = new EvalContext();
        
        assertEquals("test", expression.computeValue(evalContext));
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
        
        EvalContext evalContext = new EvalContext();
        
        assertEquals("test", expression.compute(evalContext));
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
                return new Iterator<String>() {
                    private boolean hasNextCalled = false;
                    
                    @Override
                    public boolean hasNext() {
                        if (hasNextCalled) {
                            return false;
                        } else {
                            hasNextCalled = true;
                            return true;
                        }
                    }
                    @Override
                    public String next() {
                        return "test";
                    }
                };
            }
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        EvalContext evalContext = new EvalContext();
        Iterator iterator = expression.iterate(evalContext);
        
        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
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
                return new Iterator<String>() {
                    private boolean hasNextCalled = false;
                    
                    @Override
                    public boolean hasNext() {
                        if (hasNextCalled) {
                            return false;
                        } else {
                            hasNextCalled = true;
                            return true;
                        }
                    }
                    @Override
                    public String next() {
                        return "test";
                    }
                };
            }
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        EvalContext evalContext = new EvalContext();
        Iterator iterator = expression.iteratePointers(evalContext);
        
        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPointerIterator() {
        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(
            Collections.singletonList("test").iterator(),
            new QName(null, "value"),
            Locale.getDefault()
        );
        
        assertTrue(pointerIterator.hasNext());
        assertEquals("test", pointerIterator.next());
        assertFalse(pointerIterator.hasNext());
    }
    
    @Test
    public void testValueIterator() {
        Expression.ValueIterator valueIterator = new Expression.ValueIterator(
            Collections.singletonList(new Pointer() {
                @Override
                public Object getValue() {
                    return "test";
                }
                @Override
                public void setValue(Object value) {}
                @Override
                public Object getBaseValue() {
                    return null;
                }
                @Override
                public boolean isCollection() {
                    return false;
                }
                @Override
                public boolean isLeaf() {
                    return false;
                }
                @Override
                public int getLength() {
                    return 0;
                }
                @Override
                public int getPosition() {
                    return 0;
                }
                @Override
                public void setIndex(int index) {}
                @Override
                public int getIndex() {
                    return 0;
                }
                @Override
                public NodePointer getValuePointer() {
                    return null;
                }
                @Override
                public Locale getLocale() {
                    return null;
                }
            }).iterator()
        );
        
        assertTrue(valueIterator.hasNext());
        assertEquals("test", valueIterator.next());
        assertFalse(valueIterator.hasNext());
    }
}