import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    // Test case for equal method
    @Test
    public void testEqual() {
        CoreOperationCompare coreOperation = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        
        // Test when both values are equal
        boolean result1 = coreOperation.equal(context, new Expression("value"), new Expression("value"));
        assertTrue(result1);
        
        // Test when values are different
        boolean result2 = coreOperation.equal(context, new Expression("value1"), new Expression("value2"));
        assertFalse(result2);
    }

    // Test case for contains method
    @Test
    public void testContains() {
        CoreOperationCompare coreOperation = new CoreOperationCompare(null, null);
        Iterator iterator = new ArrayList().iterator();
        
        // Test when the value is contained in the iterator
        iterator.add("value");
        boolean result1 = coreOperation.contains(iterator, "value");
        assertTrue(result1);
        
        // Test when the value is not contained in the iterator
        boolean result2 = coreOperation.contains(iterator, "value2");
        assertFalse(result2);
    }

    // Test case for findMatch method
    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperation = new CoreOperationCompare(null, null);
        Iterator leftIterator = new ArrayList().iterator();
        Iterator rightIterator = new ArrayList().iterator();
        
        // Test when there is a match between two iterators
        leftIterator.add("value1");
        rightIterator.add("value1");
        boolean result1 = coreOperation.findMatch(leftIterator, rightIterator);
        assertTrue(result1);
        
        // Test when there is no match between two iterators
        rightIterator.add("value2");
        boolean result2 = coreOperation.findMatch(leftIterator, rightIterator);
        assertFalse(result2);
    }

    // Test case for equal method with different types of values
    @Test
    public void testEqualWithDifferentValues() {
        CoreOperationCompare coreOperation = new CoreOperationCompare(null, null);
        
        // Test for boolean values
        boolean result1 = coreOperation.equal(new EvalContext(), new Expression(true), new Expression(true));
        assertTrue(result1);
        
        // Test for number values
        boolean result2 = coreOperation.equal(new EvalContext(), new Expression(10), new Expression(10));
        assertTrue(result2);
        
        // Test for string values
        boolean result3 = coreOperation.equal(new EvalContext(), new Expression("abc"), new Expression("abc"));
        assertTrue(result3);
        
        // Test for null values
        boolean result4 = coreOperation.equal(new EvalContext(), new Expression(null), new Expression(null));
        assertTrue(result4);
        
        // Test for different values
        boolean result5 = coreOperation.equal(new EvalContext(), new Expression(5.5), new Expression("5.5"));
        assertFalse(result5);
    }

}