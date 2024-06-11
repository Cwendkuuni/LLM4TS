import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, "test"));
        assertFalse(Equality.areEqual("test", null));
    }

    @Test
    public void testAreEqual_EqualObjects() {
        assertTrue(Equality.areEqual("test", "test"));
        assertTrue(Equality.areEqual(123, 123));
    }

    @Test
    public void testAreEqual_NotEqualObjects() {
        assertFalse(Equality.areEqual("test", "Test"));
        assertFalse(Equality.areEqual(123, 456));
    }

    @Test
    public void testAreEqual_Arrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
        
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr3, arr4));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr3, arr4));
    }

    @Test
    public void testIsArray() {
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
        
        String str = "test";
        assertFalse(Equality.isArray(str));
    }
}