import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_sameObject() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_bothNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_oneNull() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_differentTypes() {
        assertFalse(Equality.areEqual("Hello", 5));
    }

    @Test
    public void testAreEqual_sameArray() {
        int[] array = new int[] {1, 2, 3};
        assertTrue(Equality.areEqual(array, array));
    }

    @Test
    public void testAreEqual_equalArrays() {
        int[] array1 = new int[] {1, 2, 3};
        int[] array2 = new int[] {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_differentArrays() {
        int[] array1 = new int[] {1, 2, 3};
        int[] array2 = new int[] {4, 5, 6};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_arrayAndNonArray() {
        int[] array = new int[] {1, 2, 3};
        assertFalse(Equality.areEqual(array, new Object()));
    }

    @Test
    public void testAreArrayLengthsEqual_sameArray() {
        int[] array = new int[] {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(array, array));
    }

    @Test
    public void testAreArrayLengthsEqual_equalArrays() {
        int[] array1 = new int[] {1, 2, 3};
        int[] array2 = new int[] {4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_differentArrays() {
        int[] array1 = new int[] {1, 2, 3};
        int[] array2 = new int[] {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_sameArray() {
        int[] array = new int[] {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array, array));
    }

    @Test
    public void testAreArrayElementsEqual_equalArrays() {
        int[] array1 = new int[] {1, 2, 3};
        int[] array2 = new int[] {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_differentArrays() {
        int[] array1 = new int[] {1, 2, 3};
        int[] array2 = new int[] {4, 5, 6};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_array() {
        int[] array = new int[] {1, 2, 3};
        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testIsArray_nonArray() {
        assertFalse(Equality.isArray(new Object()));
    }
}