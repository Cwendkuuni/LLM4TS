import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Test for two equal objects
        Object obj1 = "Test";
        Object obj2 = "Test";
        assertTrue(CoreOperationCompare.equal(obj1, obj2));

        // Test for two different objects
        Object obj3 = "Hello";
        Object obj4 = "World";
        assertFalse(CoreOperationCompare.equal(obj3, obj4));
    }

    @Test
    public void testContains() {
        // Test for value contained in Iterator
        Iterator<String> iterator = Arrays.asList("Apple", "Banana", "Orange").iterator();
        String value = "Banana";
        assertTrue(CoreOperationCompare.contains(iterator, value));

        // Test for value not contained in Iterator
        String value2 = "Grape";
        assertFalse(CoreOperationCompare.contains(iterator, value2));
    }

    @Test
    public void testFindMatch() {
        // Test for matching values in two Iterators
        Iterator<Integer> iterator1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> iterator2 = Arrays.asList(4, 5, 1).iterator();
        assertTrue(CoreOperationCompare.findMatch(iterator1, iterator2));

        // Test for no matching values in two Iterators
        Iterator<Integer> iterator3 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> iterator4 = Arrays.asList(4, 5, 6).iterator();
        assertFalse(CoreOperationCompare.findMatch(iterator3, iterator4));
    }

}