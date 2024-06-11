package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // ... (previous code from Expert 1)
    }

    @Test
    public void testAreArraysEqual() {
        // ... (previous code from Expert 2)
    }

    @Test
    public void testAreArrayElementsEqual() {
        // ... (previous code from Expert 3)
    }

    @Test(expected = NullPointerException.class)
    public void testAreEqualWithNullInputs() {
        Object obj1 = null;
        Object obj2 = new Object();
        Equality.areEqual(obj1, obj2);
    }

    @Test(expected = NullPointerException.class)
    public void testAreArraysEqualWithNullInputs() {
        Object[] array1_nullInput = null;
        Object[] array2 = new Object[]{1, 2, 3};
        Equality.areArraysEqual(array1_nullInput, array2);
    }
}