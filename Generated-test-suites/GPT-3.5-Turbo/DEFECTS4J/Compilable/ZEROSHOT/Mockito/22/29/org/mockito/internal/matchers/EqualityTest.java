package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_objectsAreEqual() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertTrue(Equality.areEqual(obj1, obj1));
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_oneObjectIsNull() {
        Object obj = new Object();

        assertFalse(Equality.areEqual(obj, null));
        assertFalse(Equality.areEqual(null, obj));
    }

    @Test
    public void testAreEqual_bothObjectsAreNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_objectsAreArrays() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {1, 2, 4};

        assertTrue(Equality.areEqual(array1, array1));
        assertTrue(Equality.areEqual(array1, array2));
        assertFalse(Equality.areEqual(array2, array3));
    }

    @Test
    public void testAreEqual_arrayLengthsNotEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};

        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_arrayElementsNotEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};

        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_oneObjectIsArray() {
        int[] array = {1, 2, 3};
        Object obj = new Object();

        assertFalse(Equality.areEqual(array, obj));
        assertFalse(Equality.areEqual(obj, array));
    }

    @Test
    public void testAreEqual_neitherObjectIsArray() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertTrue(Equality.areEqual(obj1, obj1));
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreArrayLengthsEqual_lengthsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};

        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_lengthsNotEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};

        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_elementsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};

        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_elementsNotEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};

        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_objectIsArray() {
        int[] array = {1, 2, 3};

        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testIsArray_objectIsNotArray() {
        Object obj = new Object();

        assertFalse(Equality.isArray(obj));
    }

}