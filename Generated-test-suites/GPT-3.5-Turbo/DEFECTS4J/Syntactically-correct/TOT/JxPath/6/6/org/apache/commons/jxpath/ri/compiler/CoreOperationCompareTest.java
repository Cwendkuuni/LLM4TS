
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare operation;

    @Before
    public void setUp() {
        operation = new CoreOperationCompare(null, null);
    }

    @Test
    public void testEqual_NullValues_ReturnsTrue() {
        boolean result = operation.equal(null, null);
        assertTrue(result);
    }

    @Test
    public void testEqual_EqualBooleanValues_ReturnsTrue() {
        boolean result = operation.equal(true, true);
        assertTrue(result);
    }

    @Test
    public void testEqual_DifferentBooleanValues_ReturnsFalse() {
        boolean result = operation.equal(true, false);
        assertFalse(result);
    }

    @Test
    public void testEqual_EqualNumberValues_ReturnsTrue() {
        boolean result = operation.equal(10, 10);
        assertTrue(result);
    }

    @Test
    public void testEqual_DifferentNumberValues_ReturnsFalse() {
        boolean result = operation.equal(10, 20);
        assertFalse(result);
    }

    @Test
    public void testEqual_EqualStringValues_ReturnsTrue() {
        boolean result = operation.equal("abc", "abc");
        assertTrue(result);
    }

    @Test
    public void testEqual_DifferentStringValues_ReturnsFalse() {
        boolean result = operation.equal("abc", "def");
        assertFalse(result);
    }

    @Test
    public void testEqual_DifferentTypeValues_ReturnsFalse() {
        boolean result = operation.equal(true, "abc");
        assertFalse(result);
    }

    @Test
    public void testEqual_NullAndNonNullValues_ReturnsFalse() {
        boolean result = operation.equal(null, "abc");
        assertFalse(result);
    }

    @Test
    public void testContains_ValueContainedInIterator_ReturnsTrue() {
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        boolean result = operation.contains(iterator, 2);
        assertTrue(result);
    }

    @Test
    public void testContains_ValueNotContainedInIterator_ReturnsFalse() {
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        boolean result = operation.contains(iterator, 4);
        assertFalse(result);
    }

    @Test
    public void testFindMatch_MatchFoundInIterators_ReturnsTrue() {
        Iterator<Integer> iterator1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> iterator2 = Arrays.asList(4, 5, 6, 2).iterator();
        boolean result = operation.findMatch(iterator1, iterator2);
        assertTrue(result);
    }

    @Test
    public void testFindMatch_NoMatchFoundInIterators_ReturnsFalse() {
        Iterator<Integer> iterator1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> iterator2 = Arrays.asList(4, 5, 6).iterator();
        boolean result = operation.findMatch(iterator1, iterator2);
        assertFalse(result);
    }
}

