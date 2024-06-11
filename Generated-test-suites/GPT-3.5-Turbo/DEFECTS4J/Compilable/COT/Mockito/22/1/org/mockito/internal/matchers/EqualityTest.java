package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
        
        result = Equality.areEqual(null, new Object());
        assertFalse(result);
        
        result = Equality.areEqual(new Object(), null);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArrayInputs() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        boolean result = Equality.areEqual(array1, array2);
        assertTrue(result);
        
        array1 = new Object[]{1, 2, 3};
        array2 = new Object[]{1, 2, 4};
        result = Equality.areEqual(array1, array2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_DifferentArrayLengths() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3, 4};
        boolean result = Equality.areEqual(array1, array2);
        assertFalse(result);
        
        array1 = new Object[]{1, 2, 3, 4};
        array2 = new Object[]{1, 2, 3};
        result = Equality.areEqual(array1, array2);
        assertFalse(result);
    }

}