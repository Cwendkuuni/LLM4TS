import org.junit.Test;

import static org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects() {
        assertTrue("Returns true when both objects are null", Equality.areEqual(null, null));
        assertFalse("Returns false when one object is null", Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_SameObject() {
        Object obj = new Object();
        assertTrue("Returns true when comparing the same object", Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_Arrays() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue("Returns true when comparing equal arrays", Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_DifferentArrays() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 4};
        assertFalse("Returns false when comparing different arrays", Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_MixObjectsAndArrays() {
        Object obj = new Object();
        Object[] array = new Object[]{1, 2, 3};
        assertFalse("Returns false when comparing different types of objects", Equality.areEqual(obj, array));
    }

    @Test
    public void testAreArraysEqual() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue("Returns true when comparing equal arrays", Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3, 4};
        assertFalse("Returns false when arrays have different lengths", Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue("Returns true when array elements are equal", Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsNotEqual() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 4};
        assertFalse("Returns false when array elements are not equal", Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_True() {
        Object[] array = new Object[]{1, 2, 3};
        assertTrue("Returns true when object is an array", Equality.isArray(array));
    }

    @Test
    public void testIsArray_False() {
        Object obj = new Object();
        assertFalse("Returns false when object is not an array", Equality.isArray(obj));
    }
}