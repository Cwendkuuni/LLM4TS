
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    // Test cases for equal method
    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Typical use case
        assertTrue(CoreOperationCompare.equal(context, left, right));
        
        // Edge case: null values
        assertFalse(CoreOperationCompare.equal(context, null, right));
        
        // Edge case: different types
        assertFalse(CoreOperationCompare.equal(context, left, "test"));
    }
    
    // Test cases for contains method
    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = "test";
        
        // Typical use case
        assertTrue(CoreOperationCompare.contains(it, value));
        
        // Edge case: empty iterator
        assertFalse(CoreOperationCompare.contains(new Iterator(), value));
        
        // Edge case: null value
        assertFalse(CoreOperationCompare.contains(it, null));
    }
    
    // Test cases for findMatch method
    @Test
    public void testFindMatch() {
        Iterator it1 = new Iterator();
        Iterator it2 = new Iterator();
        
        // Typical use case
        assertTrue(CoreOperationCompare.findMatch(it1, it2));
        
        // Edge case: empty iterator
        assertFalse(CoreOperationCompare.findMatch(new Iterator(), it2));
    }
    
    // Test cases for equal method
    @Test
    public void testEqualObjects() {
        Object l = "test";
        Object r = "test";
        
        // Typical use case
        assertTrue(CoreOperationCompare.equal(l, r));
        
        // Edge case: null values
        assertFalse(CoreOperationCompare.equal(null, r));
        
        // Edge case: different types
        assertFalse(CoreOperationCompare.equal(l, 123));
    }
}
