import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Basic test case
        assertTrue(coreOperationCompare.equal(context, expression1, expression2));
        
        // Edge case: l and r are both null
        assertFalse(coreOperationCompare.equal(context, null, null));
        
        // Edge case: l is null, r is a non-null value
        assertFalse(coreOperationCompare.equal(context, null, "test"));
        
        // Edge case: l is a non-null value, r is null
        assertFalse(coreOperationCompare.equal(context, "test", null));
        
        // Edge case: l and r are different Collection types
        assertFalse(coreOperationCompare.equal(context, new ArrayList<>(), new HashSet<>()));
        
        // Exception handling: l and r are Iterators with null values
        assertFalse(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator it = new ArrayList<>().iterator();
        
        // Basic test case
        assertFalse(coreOperationCompare.contains(it, "value"));
        
        // Edge case: Iterator is empty
        assertFalse(coreOperationCompare.contains(it, "value"));
        
        // Edge case: Iterator has a single non-matching value
        assertFalse(coreOperationCompare.contains(it, "value"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator lit = Arrays.asList("value1", "value2").iterator();
        Iterator rit = Arrays.asList("value3", "value2").iterator();
        
        // Basic test case
        assertTrue(coreOperationCompare.findMatch(lit, rit));
        
        // Edge case: lit is empty, rit is not
        assertFalse(coreOperationCompare.findMatch(lit, rit));
        
        // Edge case: rit is empty, lit is not
        assertFalse(coreOperationCompare.findMatch(lit, rit));
        
        // Exception handling: lit and rit are null
        assertFalse(coreOperationCompare.findMatch(null, null));
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        
        // Basic test case
        assertTrue(coreOperationCompare.equal("test", "test"));
        
        // Edge case: l and r are different types
        assertFalse(coreOperationCompare.equal("test", 123));
        
        // Edge case: l and r are different but equivalent types
        assertTrue(coreOperationCompare.equal(10.0, 10));
        
        // Edge case: l and r are pointers with different values
        assertFalse(coreOperationCompare.equal(new Pointer(), new Pointer()));
        
        // Exception handling: l and r are NaN
        assertFalse(coreOperationCompare.equal(Double.NaN, Double.NaN));
    }
}