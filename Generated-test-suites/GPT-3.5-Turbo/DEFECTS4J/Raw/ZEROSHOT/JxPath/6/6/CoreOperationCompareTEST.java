import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        CoreOperationCompare core = new CoreOperationCompare(mock(Expression.class), mock(Expression.class)) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };
        EvalContext context = mock(EvalContext.class);
        Expression left = mock(Expression.class);
        Expression right = mock(Expression.class);
        
        boolean result = core.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testContains() {
        CoreOperationCompare core = new CoreOperationCompare(mock(Expression.class), mock(Expression.class)) {
            @Override
            protected boolean contains(Iterator it, Object value) {
                return true;
            }
        };
        Iterator it = mock(Iterator.class);
        Object value = mock(Object.class);
        
        boolean result = core.contains(it, value);
        
        assertTrue(result);
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationCompare core = new CoreOperationCompare(mock(Expression.class), mock(Expression.class)) {
            @Override
            protected boolean findMatch(Iterator lit, Iterator rit) {
                return true;
            }
        };
        Iterator lit = mock(Iterator.class);
        Iterator rit = mock(Iterator.class);
        
        boolean result = core.findMatch(lit, rit);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualWithValue() {
        CoreOperationCompare core = new CoreOperationCompare(mock(Expression.class), mock(Expression.class)) {
            @Override
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        Object l = mock(Object.class);
        Object r = mock(Object.class);
        
        boolean result = core.equal(l, r);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualWithPointer() {
        CoreOperationCompare core = new CoreOperationCompare(mock(Expression.class), mock(Expression.class)) {
            @Override
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        Pointer l = mock(Pointer.class);
        Pointer r = mock(Pointer.class);
        when(l.equals(r)).thenReturn(true);
        
        boolean result = core.equal(l, r);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualWithPointerValue() {
        CoreOperationCompare core = new CoreOperationCompare(mock(Expression.class), mock(Expression.class)) {
            @Override
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        Pointer l = mock(Pointer.class);
        Object r = mock(Object.class);
        when(l.getValue()).thenReturn(r);
        
        boolean result = core.equal(l, r);
        
        assertTrue(result);
    }
    
}