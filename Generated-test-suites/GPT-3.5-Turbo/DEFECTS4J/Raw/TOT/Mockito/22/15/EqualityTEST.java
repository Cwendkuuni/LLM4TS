import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_nullObjects() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_nonNullObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertFalse(Equality.areEqual(obj1, obj2));
        assertFalse(Equality.areEqual(obj2, obj1));

        assertTrue(Equality.areEqual(obj1, obj1));
        assertTrue(Equality.areEqual(obj2, obj2));
    }

    @Test
    public void testAreEqual_arrayObjects() {
        int[] array1 = new int[]{1, 2, 3};
        int[] array2 = new int[]{1, 2, 3};
        int[] array3 = new int[]{1, 3, 2};

        assertTrue(Equality.areEqual(array1, array1));
        assertTrue(Equality.areEqual(array2, array2));
        assertTrue(Equality.areEqual(array1, array2));
        assertFalse(Equality.areEqual(array1, array3));
    }

    @Test
    public void testAreEqual_arrayObjects_differentLength() {
        int[] array1 = new int[]{1, 2, 3};
        int[] array2 = new int[]{1, 2};

        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test(expected = NullPointerException.class)
    public void testAreEqual_arrayObjects_null() {
        int[] array = new int[]{1, 2, 3};
        Equality.areEqual(array, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAreEqual_arrayObjects_null_reverse() {
        int[] array = new int[]{1, 2, 3};
        Equality.areEqual(null, array);
    }

    @Test
    public void testAreArrayLengthsEqual_sameLength() {
        int[] array1 = new int[]{1, 2, 3};
        int[] array2 = new int[]{1, 2, 3};

        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_differentLength() {
        int[] array1 = new int[]{1, 2, 3};
        int[] array2 = new int[]{1, 2};

        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test(expected = NullPointerException.class)
    public void testAreArrayLengthsEqual_null() {
        int[] array = new int[]{1, 2, 3};
        Equality.areArrayLengthsEqual(array, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAreArrayLengthsEqual_null_reverse() {
        int[] array = new int[]{1, 2, 3};
        Equality.areArrayLengthsEqual(null, array);
    }

    @Test
    public void testAreArrayElementsEqual_sameElements() {
        int[] array1 = new int[]{1, 2, 3};
        int[] array2 = new int[]{1, 2, 3};

        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_differentElements() {
        int[] array1 = new int[]{1, 2, 3};
        int[] array2 = new int[]{1, 3, 2};

        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test(expected = NullPointerException.class)
    public void testAreArrayElementsEqual_null() {
        int[] array = new int[]{1, 2, 3};
        Equality.areArrayElementsEqual(array, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAreArrayElementsEqual_null_reverse() {
        int[] array = new int[]{1, 2, 3};
        Equality.areArrayElementsEqual(null, array);
    }

    @Test
    public void testIsArray_array() {
        int[] array = new int[]{1, 2, 3};
        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testIsArray_nonArray() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }
}