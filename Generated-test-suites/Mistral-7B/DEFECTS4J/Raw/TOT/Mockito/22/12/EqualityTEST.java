import org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {
    // Test for areEqual method with null objects
    @Test
    public void testAreEqualNullObjects() {
        assertTrue(Equality.areEqual(null, null));
    }

    // Test for areEqual method with same non-null object
    @Test
    public void testAreEqualSameObject() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    // Test for areEqual method with different non-null objects
    @Test
    public void testAreEqualDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    // Test for areEqual method with array types
    @Test
    public void testAreEqualArrays() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    // Test for areEqual method with arrays of different lengths
    @Test
    public void testAreEqualArraysDifferentLengths() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2};
        assertFalse(Equality.areEqual(array1, array2));
    }

    // Test for areEqual method with arrays containing different elements
    @Test
    public void testAreEqualArraysDifferentElements() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 3, 3};
        assertFalse(Equality.areEqual(array1, array2));
    }

    // Test for areEqual method with arrays containing null elements
    @Test
    public void testAreEqualArraysNullElements() {
        Object[] array1 = new Object[]{1, null, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertFalse(Equality.areEqual(array1, array2));
    }

    // Test for areEqual method with primitive types
    @Test
    public void testAreEqualPrimitiveTypes() {
        int int1 = 1;
        int int2 = 1;
        assertTrue(Equality.areEqual(int1, int2));

        long long1 = 1L;
        long long2 = 1L;
        assertTrue(Equality.areEqual(long1, long2));

        float float1 = 1.0f;
        float float2 = 1.0f;
        assertTrue(Equality.areEqual(float1, float2));

        double double1 = 1.0;
        double double2 = 1.0;
        assertTrue(Equality.areEqual(double1, double2));

        char char1 = 'a';
        char char2 = 'a';
        assertTrue(Equality.areEqual(char1, char2));

        boolean boolean1 = true;
        boolean boolean2 = true;
        assertTrue(Equality.areEqual(boolean1, boolean2));
    }

    // Test for areEqual method with primitive arrays
    @Test
    public void testAreEqualPrimitiveArrays() {
        int[] intArray1 = new int[]{1, 2, 3};
        int[] intArray2 = new int[]{1, 2, 3};
        assertTrue(Equality.areEqual(intArray1, intArray2));
    }

    // Test for areEqual method with primitive arrays of different lengths
    @Test
    public void testAreEqualPrimitiveArraysDifferentLengths() {
        int[] intArray1 = new int[]{1, 2, 3};
        int[] intArray2 = new int[]{1, 2};
        assertFalse(Equality.areEqual(intArray1, intArray2));
    }
}