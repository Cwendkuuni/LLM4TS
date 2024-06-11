import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {
    @Test
    public void testAreEqual_ObjectsAreSame() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_ObjectsAreDifferent() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_Null() {
        Object nullObject = null;
        assertFalse(Equality.areEqual(nullObject, nullObject));
    }

    @Test
    public void testAreArraysEqual_ArraysAreEqual() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_ArraysAreDifferentLengths() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_ArraysHaveDifferentElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_NonArrayType() {
        Equality.areEqual("string1", 1);
    }

    @Test
    public void testAreArraysEqual_ArraysHaveSameLengthButDifferentElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 3, 3};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_ArrayContainNull() {
        Object[] arr1 = new Object[]{1, null, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }
}