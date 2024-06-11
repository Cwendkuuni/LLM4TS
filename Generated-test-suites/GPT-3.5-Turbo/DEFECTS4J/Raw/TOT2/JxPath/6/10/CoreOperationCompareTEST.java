import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_NullObjects() {
        CoreOperationCompare coreOp = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        
        boolean result = coreOp.equal(context, null, null);
        
        assertTrue(result);
    }

    @Test
    public void testEqual_SameObject() {
        Object obj = new Object();
        CoreOperationCompare coreOp = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        
        boolean result = coreOp.equal(context, obj, obj);
        
        assertTrue(result);
    }
    
    @Test
    public void testContains_MatchingElement() {
        CoreOperationCompare coreOp = new CoreOperationCompare(null, null);
        Iterator iterator = List.of("a", "b", "c").iterator();
        Object value = "b";
        
        boolean result = coreOp.contains(iterator, value);
        
        assertTrue(result);
    }
    
    @Test
    public void testContains_NoMatchingElement() {
        CoreOperationCompare coreOp = new CoreOperationCompare(null, null);
        Iterator iterator = List.of("a", "b", "c").iterator();
        Object value = "d";
        
        boolean result = coreOp.contains(iterator, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch_MatchingElements() {
        CoreOperationCompare coreOp = new CoreOperationCompare(null, null);
        Iterator left = List.of("a", "b", "c").iterator();
        Iterator right = List.of("x", "b", "y").iterator();
        
        boolean result = coreOp.findMatch(left, right);
        
        assertTrue(result);
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}