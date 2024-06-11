import org.junit.Test;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_objectsNull_expectedTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_firstObjectNull_expectedFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_secondObjectNull_expectedFalse() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_bothObjectsNotNull_expectedTrue() {
        assertTrue(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreEqual_firstObjectArray_secondObjectNotArray_expectedFalse() {
        assertFalse(Equality.areEqual(new Object[0], new Object()));
    }

    @Test
    public void testAreEqual_bothObjectsArrayWithDifferentLengths_expectedFalse() {
        assertFalse(Equality.areEqual(new Object[1], new Object[2]));
    }

    @Test
    public void testAreEqual_bothObjectsArrayWithSameLengths_expectedTrue() {
        assertTrue(Equality.areEqual(new Object[2], new Object[2]));
    }

    @Test
    public void testAreEqual_bothObjectsArrayWithSameElements_expectedTrue() {
        Object[] array1 = {1, 2};
        Object[] array2 = {1, 2};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_bothObjectsArrayWithDifferentElements_expectedFalse() {
        Object[] array1 = {1, 2};
        Object[] array2 = {1, 3};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_objectsNotArray_expectedFalse() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreArrayLengthsEqual_arraysWithDifferentLengths_expectedFalse() {
        assertFalse(Equality.areArrayLengthsEqual(new Object[1], new Object[2]));
    }

    @Test
    public void testAreArrayLengthsEqual_arraysWithSameLengths_expectedTrue() {
        assertTrue(Equality.areArrayLengthsEqual(new Object[2], new Object[2]));
    }

    @Test
    public void testAreArrayElementsEqual_arraysWithSameElements_expectedTrue() {
        Object[] array1 = {1, 2};
        Object[] array2 = {1, 2};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_arraysWithDifferentElements_expectedFalse() {
        Object[] array1 = {1, 2};
        Object[] array2 = {1, 3};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_objectIsArray_expectedTrue() {
        assertTrue(Equality.isArray(new Object[2]));
    }

    @Test
    public void testIsArray_objectNotArray_expectedFalse() {
        assertFalse(Equality.isArray(new Object()));
    }
}