import org.junit.Assert.*; // for assertions
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {
    @Test
    public void testAreEqual_ObjectAndObject_ReturnsTrueForEqualObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1; // same object
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_ObjectAndArray_ThrowsIllegalArgumentException() {
        Object obj = new Object();
        Object[] array = {obj};
        try {
            Equality.areEqual(obj, array);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAreArraysEqual_ArrayAndArray_ReturnsTrueForEqualArrays() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_ArrayAndNonArray_ThrowsIllegalArgumentException() {
        Object[] arr = new Object[]{1, 2, 3};
        Object obj = new Object();
        try {
            Equality.areArraysEqual(arr, obj);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}