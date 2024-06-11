import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer());
        
        assertFalse(op.equal(context, left, right));
    }
    
    @Test
    public void testContains() {
        Expression left = new Expression();
        Object value = new Object();
        CoreOperationCompare op = new CoreOperationCompare(left, null);
        
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                return null;
            }
        };
        
        assertFalse(op.contains(it, value));
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                return null;
            }
        };
        
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                return null;
            }
        };
        
        CoreOperationCompare op = new CoreOperationCompare(null, null);
        assertFalse(op.findMatch(lit, rit));
    }
    
    @Test
    public void testEqualWithPointers() {
        Object l = new Pointer();
        Object r = new Pointer();
        
        CoreOperationCompare op = new CoreOperationCompare(null, null);
        assertFalse(op.equal(l, r));
    }
    
    @Test
    public void testEqualWithBoolean() {
        Object l = true;
        Object r = false;
        
        CoreOperationCompare op = new CoreOperationCompare(null, null);
        assertFalse(op.equal(l, r));
    }
    
    @Test
    public void testEqualWithNumber() {
        Object l = 3;
        Object r = 5;
        
        CoreOperationCompare op = new CoreOperationCompare(null, null);
        assertFalse(op.equal(l, r));
    }
    
    @Test
    public void testEqualWithString() {
        Object l = "foo";
        Object r = "bar";
        
        CoreOperationCompare op = new CoreOperationCompare(null, null);
        assertFalse(op.equal(l, r));
    }
    
    @Test
    public void testEqualWithNull() {
        Object l = null;
        Object r = null;
        
        CoreOperationCompare op = new CoreOperationCompare(null, null);
        assertTrue(op.equal(l, r));
    }
}