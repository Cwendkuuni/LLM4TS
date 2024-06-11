import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_Arrays() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {3, 2, 1};

        assertTrue(Equality.areEqual(array1, array2));
        assertFalse(Equality.areEqual(array1, array3));
    }

    @Test
    public void testAreEqual_Objects() {
        String s1 = "hello";
        String s2 = "hello";
        String s3 = "world";

        assertTrue(Equality.areEqual(s1, s2));
        assertFalse(Equality.areEqual(s1, s3));
    }

    @Test
    public void testAreArraysEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};

        assertTrue(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3, 4};

        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};

        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray() {
        int[] array = {1, 2, 3};

        assertTrue(Equality.isArray(array));
    }

}