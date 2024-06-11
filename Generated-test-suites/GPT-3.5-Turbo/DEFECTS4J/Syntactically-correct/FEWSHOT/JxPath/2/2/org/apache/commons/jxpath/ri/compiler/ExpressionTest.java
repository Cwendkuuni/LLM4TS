import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.junit.Test;

public class ExpressionTest {
    @Test
   public void testIsContextDependent() {
        // Test when context dependency is known to be false
        Expression expr = new Expression() {
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
        assertFalse(expr.isContextDependent());
    }
   
    @Test
    public void testComputeValue() {
        // Test when result is null
        Expression expr = new Expression() {
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
        assertNull(expr.computeValue(null));
        
        // Test when result is a String
        expr = new Expression() {
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
                return "test";
            }
        };
        assertEquals("test", expr.computeValue(null));
        
        // Test when result is a NodePointer
        Pointer pointer = new NodePointer();
        expr = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
            @Override
            public Object computeValue(EvalContext context) {
                return pointer;
            }
            @Override
            public Object compute(EvalContext context) {
                return pointer;
            }
        };
        assertEquals(pointer, expr.computeValue(null));
    }

    @Test
    public void testCompute() {
        // Test when result is null
        Expression expr = new Expression() {
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
        assertNull(expr.compute(null));
        
        // Test when result is a String
        expr = new Expression() {
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
                return "test";
            }
        };
        assertEquals("test", expr.compute(null));
        
        // Test when result is an EvalContext
        EvalContext evalContext = new EvalContext();
        expr = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
            @Override
            public Object computeValue(EvalContext context) {
                return evalContext;
            }
            @Override
            public Object compute(EvalContext context) {
                return evalContext;
            }
        };
        assertEquals(evalContext, expr.compute(null));
    }

    @Test
    public void testIterate() {
        // Test when result is null
        Expression expr = new Expression() {
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
        assertFalse(expr.iterate(null).hasNext());
        
        // Test when result is a String
        expr = new Expression() {
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
                return "test";
            }
        };
        assertEquals(Arrays.asList("test").iterator(), expr.iterate(null));
        
        // Test when result is an EvalContext
        EvalContext evalContext = new EvalContext();
        expr = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
            @Override
            public Object computeValue(EvalContext context) {
                return evalContext;
            }
            @Override
            public Object compute(EvalContext context) {
                return evalContext;
            }
        };
        assertEquals(new Expression.ValueIterator(evalContext), expr.iterate(null));
    }

    @Test
    public void testIteratePointers() {
        // Test when result is null
        Expression expr = new Expression() {
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
        assertFalse(expr.iteratePointers(null).hasNext());
        
        // Test when result is a String
        expr = new Expression() {
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
                return "test";
            }
        };
        assertFalse(expr.iteratePointers(null).hasNext());
        
        // Test when result is an EvalContext
        EvalContext evalContext = new EvalContext();
        expr = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
            @Override
            public Object computeValue(EvalContext context) {
                return evalContext;
            }
            @Override
            public Object compute(EvalContext context) {
                return evalContext;
            }
        };
        assertEquals(new Expression.PointerIterator(ValueUtils.iterate(evalContext), new QName(null, "value"), null), expr.iteratePointers(null));
    }


    }