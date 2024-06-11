import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_SameObject() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreArrayLengthsEqual_EqualLength() {
        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[3];
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_NotEqualLength() {
        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[5];
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_SameElements() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentElements() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 5, 3};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_NullElements() {
        Object[] arr1 = {null, 2, 3};
        Object[] arr2 = {null, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_NullDifferentElement() {
        Object[] arr1 = {null, 2, 3};
        Object[] arr2 = {1, 2, 3};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_EmptyArrays() {
        Object[] arr1 = {};
        Object[] arr2 = {};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }
}