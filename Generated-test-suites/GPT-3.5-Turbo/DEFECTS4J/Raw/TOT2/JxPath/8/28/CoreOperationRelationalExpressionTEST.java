import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test when left is greater than right
        // edge case: left = 5, right = 4
        assertTrue(new TestOperationRelationalExpression().computeValue(5, 4));

        // Test when left is less than right
        // edge case: left = 2, right = 3
        assertFalse(new TestOperationRelationalExpression().computeValue(2, 3));

        // Test when left is equal to right
        // edge case: left = 5, right = 5
        assertFalse(new TestOperationRelationalExpression().computeValue(5, 5));
    }

    @Test
    public void testReduce() {
        // Test when object is SelfContext
        // edge case: SelfContext object
        assertEquals(5, new TestOperationRelationalExpression().reduce(new SelfContext(5)));

        // Test when object is Collection
        // edge case: Collection object
        assertEquals(5, new TestOperationRelationalExpression().reduce(new ArrayList<>(Arrays.asList(5))).next());
    }

    @Test
    public void testContainsMatch() {
        // Test when Iterator contains match
        // edge case: Iterator with matching value
        assertTrue(new TestOperationRelationalExpression().containsMatch(Arrays.asList(1, 2, 3).iterator(), 2));

        // Test when Iterator does not contain match
        // edge case: Iterator without matching value
        assertFalse(new TestOperationRelationalExpression().containsMatch(Arrays.asList(1, 2, 3).iterator(), 4));
    }

    @Test
    public void testFindMatch() {
        // Test when matching values are found
        // edge case: Matching values in the iterators
        assertTrue(new TestOperationRelationalExpression().findMatch(Arrays.asList(1, 2, 3).iterator(), Arrays.asList(3, 4, 5).iterator()));

        // Test when no matching values are found
        // edge case: No matching values in the iterators
        assertFalse(new TestOperationRelationalExpression().findMatch(Arrays.asList(1, 2, 3).iterator(), Arrays.asList(4, 5, 6).iterator()));
    }
    
}