import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual("test", "test"));
        assertFalse(Equality.areEqual("test1", "test2"));
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, "test"));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        Object[] arr3 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr3));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
        Object[] arr3 = {1, 2};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr3));
    }

    @Test
    public void testIsArray() {
        Object arr = new int[3];
        assertTrue(Equality.isArray(arr));
        assertFalse(Equality.isArray("test"));
    }
}