import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    
    private CoreOperationCompare coreOperationCompare;
    
    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(null, null);
    }
    
    @Test
    public void testEqualEqual() {
        EvalContext context = null;
        Expression left = null;
        Expression right = null;
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualNotEqual() {
        EvalContext context = null;
        Expression left = null;
        Expression right = null;
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testContains() {
        Iterator it = null;
        Object value = null;
        
        boolean result = coreOperationCompare.contains(it, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = null;
        Iterator rit = null;
        
        boolean result = coreOperationCompare.findMatch(lit, rit);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual() {
        Object l = null;
        Object r = null;
        
        boolean result = coreOperationCompare.equal(l, r);
        
        assertTrue(result);
    }
}