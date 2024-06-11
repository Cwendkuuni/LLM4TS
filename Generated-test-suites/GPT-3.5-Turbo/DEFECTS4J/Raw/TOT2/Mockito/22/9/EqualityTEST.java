import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual("hello", null));
        assertFalse(Equality.areEqual(null, "world"));
    }

    @Test
    public void testAreArraysEqual() {
        Object arr1 = new int[] {1, 2, 3};
        Object arr2 = new int[] {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));

        Object arr3 = new int[] {1, 2};
        assertFalse(Equality.areArraysEqual(arr1, arr3));
      
        Object arr4 = new int[] {1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr4));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object arr1 = new int[3];
        Object arr2 = new int[3];
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));

        Object arr3 = new int[4];
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr3));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object arr1 = new int[] {1, 2, 3};
        Object arr2 = new int[] {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));

        Object arr3 = new int[] {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr3));
    }

    @Test
    public void testIsArray() {
        Object arr = new int[3];
        assertTrue(Equality.isArray(arr));

        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }
}