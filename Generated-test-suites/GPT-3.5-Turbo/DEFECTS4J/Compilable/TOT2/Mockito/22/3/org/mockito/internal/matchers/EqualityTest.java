package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual("test", "test"));
        assertFalse(Equality.areEqual("test", "Test"));
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual("test", null));
        assertFalse(Equality.areEqual(null, "test"));
    }

    @Test
    public void testAreArraysEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(array1, array2));
        
        Object[] array3 = {1, 2};
        assertFalse(Equality.areArraysEqual(array1, array3));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
        
        Object[] array3 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array3));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
        
        Object[] array3 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(array1, array3));
    }
}