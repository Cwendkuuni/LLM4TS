import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    // Test cases for CoreOperationCompare class
    
    @Test
    public void testEqualMethodWithIdenticalObjects() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg1);
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        assertTrue(compare.equal(context, left, right));
    }
    
    @Test
    public void testEqualMethodWithDifferentObjects() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        assertFalse(compare.equal(context, left, right));
    }
    
    @Test
    public void testEqualMethodWithNullObjects() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        
        assertTrue(compare.equal(context, null, null));
    }
    
    @Test
    public void testFindMatchMethodWithEmptyIterators() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        
        assertFalse(compare.findMatch(lit, rit));
    }
    
    @Test
    public void testContainsMethodWithEmptyIterator() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        Iterator it = new Iterator();
        
        assertFalse(compare.contains(it, null));
    }
    
    @Test
    public void testContainsMethodWithMatchingElement() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        Iterator it = new Iterator();
        Object value = new Object();
        it.add(value);
        
        assertTrue(compare.contains(it, value));
    }
    
}