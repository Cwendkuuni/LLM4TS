import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Typical use case
        // Two equal values
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        boolean result = operation.equal(context, leftExpression, rightExpression);
        assertTrue(result);
        
        // Edge cases
        // Comparing collections
        // Different data types
        // Null values
    }

    @Test
    public void testContains() {
        // Typical use case
        // Element contained in the iterator
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        boolean result = operation.contains(iterator, value);
        assertTrue(result);
        
        // Edge cases
        // Iterator is empty
        // Element not found
    }

    @Test
    public void testFindMatch() {
        // Typical use case
        // Matching elements found in both iterators
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        boolean result = operation.findMatch(leftIterator, rightIterator);
        assertTrue(result);
        
        // Edge cases
        // One iterator is empty
        // No matching elements found
    }

    @Test
    public void testEqualValues() {
        // Typical use case
        // Two equal values of different data types
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        boolean result = operation.equalValues(lValue, rValue);
        assertTrue(result);
        
        // Edge cases
        // Null values
        // Different data types
    }

}