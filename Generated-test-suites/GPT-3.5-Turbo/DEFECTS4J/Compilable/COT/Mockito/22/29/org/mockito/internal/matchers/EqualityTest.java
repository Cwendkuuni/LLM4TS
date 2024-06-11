package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs_ReturnTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_NullInputAndNonNullInput_ReturnFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_NonNullInputAndNullInput_ReturnFalse() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_NonArrayInputs_ReturnResultFromEqualsMethod() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertEquals(o1.equals(o2), Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_ArrayInputs_ReturnResultFromAreArraysEqualMethod() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        assertEquals(Equality.areArraysEqual(array1, array2), Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_InputArraysDifferentLength_ReturnFalse() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArrayInputsDifferentElements_ReturnFalse() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 4, 3};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_InputArrayAndNonArray_ReturnFalse() {
        Object[] array = {1, 2, 3};
        Object nonArray = new Object();
        assertFalse(Equality.areEqual(array, nonArray));
    }

    @Test
    public void testAreArraysEqual_ArrayInputs_ReturnResultFromAreArrayLengthsEqualMethod() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        assertEquals(Equality.areArrayLengthsEqual(array1, array2),
                Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_ArrayInputsDifferentLength_ReturnFalse() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_ArrayInputsDifferentElements_ReturnFalse() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_NonArrayInput_ReturnFalse() {
        assertFalse(Equality.isArray(new Object()));
    }

    @Test
    public void testIsArray_ArrayInput_ReturnTrue() {
        assertTrue(Equality.isArray(new Object[0]));
    }
}

