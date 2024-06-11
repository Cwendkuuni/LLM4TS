import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Test when both values are equal
        CoreOperationCompare op = new CoreOperationCompare();
        boolean result = op.equal(new EvalContext(), new Expression(), new Expression());
        
        assertTrue(result);
    }

    @Test
    public void testContains() {
        // Test when Iterator contains the value
        CoreOperationCompare op = new CoreOperationCompare();
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        boolean result = op.contains(it, 2);
        
        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        // Test when a match is found between two Iterators
        CoreOperationCompare op = new CoreOperationCompare();
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(4, 2, 6).iterator();
        boolean result = op.findMatch(lit, rit);
        
        assertTrue(result);
    }

    @Test
    public void testEqualPointers() {
        // Test when Pointers are equal
        CoreOperationCompare op = new CoreOperationCompare();
        Object l = new Pointer();
        Object r = new Pointer();
        boolean result = op.equal(l, r);
        
        assertTrue(result);
    }

    @Test
    public void testEqualBoolean() {
        // Test when comparing boolean values
        CoreOperationCompare op = new CoreOperationCompare();
        boolean result = op.equal(true, false);
        
        assertFalse(result);
    }

    @Test
    public void testEqualNumber() {
        // Test when comparing number values
        CoreOperationCompare op = new CoreOperationCompare();
        boolean result = op.equal(10, 10.0);
        
        assertTrue(result);
    }

    @Test
    public void testEqualString() {
        // Test when comparing string values
        CoreOperationCompare op = new CoreOperationCompare();
        boolean result = op.equal("hello", "hello");
        
        assertTrue(result);
    }

    @Test
    public void testEqualNull() {
        // Test when one value is null
        CoreOperationCompare op = new CoreOperationCompare();
        boolean result = op.equal(null, "test");
        
        assertFalse(result);
    }
}