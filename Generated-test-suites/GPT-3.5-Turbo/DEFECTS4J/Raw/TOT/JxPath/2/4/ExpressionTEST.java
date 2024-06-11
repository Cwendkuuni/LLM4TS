import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.ArrayList;
import java.util.Locale;

public class ExpressionTest {

    // Test computeContextDependent() for a context-dependent expression
    @Test
    public void testComputeContextDependent_True() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        boolean result = expression.isContextDependent();
    
        Assertions.assertTrue(result);
    }
    
    // Test computeContextDependent() for a context-independent expression
    @Test
    public void testComputeContextDependent_False() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        boolean result = expression.isContextDependent();
    
        Assertions.assertFalse(result);
    }
    
    // Test computeValue() for a context-dependent expression and null result
    @Test
    public void testComputeValue_ContextDependent_NullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Object result = expression.computeValue(context);
    
        Assertions.assertNull(result);
    }
    
    // Test computeValue() for a context-dependent expression and non-null result
    @Test
    public void testComputeValue_ContextDependent_NonNullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
    
            public Object computeValue(EvalContext context) {
                return "Result";
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Object result = expression.computeValue(context);
    
        Assertions.assertEquals("Result", result);
    }
    
    // Test computeValue() for a context-independent expression and null result
    @Test
    public void testComputeValue_ContextIndependent_NullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Object result = expression.computeValue(context);
    
        Assertions.assertNull(result);
    }
    
    // Test computeValue() for a context-independent expression and non-null result
    @Test
    public void testComputeValue_ContextIndependent_NonNullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
    
            public Object computeValue(EvalContext context) {
                return "Result";
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Object result = expression.computeValue(context);
    
        Assertions.assertEquals("Result", result);
    }
    
    // Test iterate() for a context-dependent expression and null result
    @Test
    public void testIterate_ContextDependent_NullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Iterator result = expression.iterate(context);
    
        Assertions.assertFalse(result.hasNext());
    }
    
    // Test iterate() for a context-dependent expression and non-null result
    @Test
    public void testIterate_ContextDependent_NonNullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
    
            public Object computeValue(EvalContext context) {
                ArrayList<String> list = new ArrayList<>();
                list.add("Element 1");
                list.add("Element 2");
                return list;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Iterator result = expression.iterate(context);
    
        Assertions.assertTrue(result.hasNext());
        Assertions.assertEquals("Element 1", result.next());
        Assertions.assertTrue(result.hasNext());
        Assertions.assertEquals("Element 2", result.next());
        Assertions.assertFalse(result.hasNext());
    }
    
    // Test iterate() for a context-independent expression and null result
    @Test
    public void testIterate_ContextIndependent_NullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Iterator result = expression.iterate(context);
    
        Assertions.assertFalse(result.hasNext());
    }
    
    // Test iterate() for a context-independent expression and non-null result
    @Test
    public void testIterate_ContextIndependent_NonNullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
    
            public Object computeValue(EvalContext context) {
                ArrayList<String> list = new ArrayList<>();
                list.add("Element 1");
                list.add("Element 2");
                return list;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Iterator result = expression.iterate(context);
    
        Assertions.assertTrue(result.hasNext());
        Assertions.assertEquals("Element 1", result.next());
        Assertions.assertTrue(result.hasNext());
        Assertions.assertEquals("Element 2", result.next());
        Assertions.assertFalse(result.hasNext());
    }
    
    // Test iteratePointers() for a context-dependent expression and null result
    @Test
    public void testIteratePointers_ContextDependent_NullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Iterator result = expression.iteratePointers(context);
    
        Assertions.assertFalse(result.hasNext());
    }
    
    // Test iteratePointers() for a context-dependent expression and non-null result
    @Test
    public void testIteratePointers_ContextDependent_NonNullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
    
            public Object computeValue(EvalContext context) {
                ArrayList<Pointer> list = new ArrayList<>();
                list.add(new Pointer("Element 1"));
                list.add(new Pointer("Element 2"));
                return list;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Iterator result = expression.iteratePointers(context);
    
        Assertions.assertTrue(result.hasNext());
        Assertions.assertEquals(new Pointer("Element 1"), result.next());
        Assertions.assertTrue(result.hasNext());
        Assertions.assertEquals(new Pointer("Element 2"), result.next());
        Assertions.assertFalse(result.hasNext());
    }
    
    // Test iteratePointers() for a context-independent expression and null result
    @Test
    public void testIteratePointers_ContextIndependent_NullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Iterator result = expression.iteratePointers(context);
    
        Assertions.assertFalse(result.hasNext());
    }
    
    // Test iteratePointers() for a context-independent expression and non-null result
    @Test
    public void testIteratePointers_ContextIndependent_NonNullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
    
            public Object computeValue(EvalContext context) {
                ArrayList<Pointer> list = new ArrayList<>();
                list.add(new Pointer("Element 1"));
                list.add(new Pointer("Element 2"));
                return list;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Iterator result = expression.iteratePointers(context);
    
        Assertions.assertTrue(result.hasNext());
        Assertions.assertEquals(new Pointer("Element 1"), result.next());
        Assertions.assertTrue(result.hasNext());
        Assertions.assertEquals(new Pointer("Element 2"), result.next());
        Assertions.assertFalse(result.hasNext());
    }
    
    // Test PointerIterator.next() when it returns a Pointer object
    @Test
    public void testPointerIterator_Next_ReturnsPointer() {
        Iterator iterator = new Expression.PointerIterator(new ArrayList<Pointer>().iterator(), null, null);
    
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            iterator.next();
        });
    }
    
    // Test ValueIterator.next() when it returns a non-Pointer object
    @Test
    public void testValueIterator_Next_ReturnsNonPointer() {
        Iterator iterator = new Expression.ValueIterator(new ArrayList<Object>().iterator());
    
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            iterator.next();
        });
    }
    
    // Test compute() for a context-dependent expression and null result
    @Test
    public void testCompute_ContextDependent_NullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Object result = expression.compute(context);
    
        Assertions.assertNull(result);
    }
    
    // Test compute() for a context-dependent expression and non-null result
    @Test
    public void testCompute_ContextDependent_NonNullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return "Result";
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Object result = expression.compute(context);
    
        Assertions.assertEquals("Result", result);
    }
    
    // Test compute() for a context-independent expression and null result
    @Test
    public void testCompute_ContextIndependent_NullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return null;
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Object result = expression.compute(context);
    
        Assertions.assertNull(result);
    }
    
    // Test compute() for a context-independent expression and non-null result
    @Test
    public void testCompute_ContextIndependent_NonNullResult() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
    
            public Object computeValue(EvalContext context) {
                return null;
            }
    
            public Object compute(EvalContext context) {
                return "Result";
            }
        };
    
        EvalContext context = new EvalContext(null, null, null);
        Object result = expression.compute(context);
    
        Assertions.assertEquals("Result", result);
    }
}