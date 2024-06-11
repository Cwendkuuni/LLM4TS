package org.mockito.internal.matchers.test;

import static org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_SameObjects() {
        Object o1 = new Object();
        Object o2 = o1;

        assertTrue(Equality.areEqual(o1, o2));
    }

    // ... Test cases for other methods ...

    @Test
    public void testAreArraysEqual_ArrayOfDifferentTypes() {
        Object[] a1 = new Object[]{1, 2, 3};
        int[] a2 = new int[]{1, 2, 3};

        assertFalse(Equality.areArraysEqual(a1, a2));
    }

    // ... Test cases for other methods ...

    @Test(expected = NullPointerException.class)
    public void testAreArraysEqual_NullArray() {
        Object[] a1 = null;
        Object[] a2 = new Object[]{1, 2, 3};

        assertFalse(Equality.areArraysEqual(a1, a2));
    }

    // ... Test cases for other methods ...

    @Test(expected = NullPointerException.class)
    public void testAreArraysEqual_NullElement() {
        Object[] a1 = new Object[]{1, null, 3};
        Object[] a2 = new Object[]{1, 2, 3};

        assertFalse(Equality.areArraysEqual(a1, a2));
    }

    // ... Test cases for other methods ...

    @Test(expected = ArrayStoreException.class)
    public void testAreArraysEqual_ArrayOfDifferentTypes() {
        Object[] a1 = new Object[]{1, 2, 3};
        int[] a2 = new int[]{1, 2, 3};

        assertFalse(Equality.areArraysEqual(a1, a2));
    }

    // ... Test cases for other methods ...

    @Test(expected = NullPointerException.class)
    public void testAreEqual_NullObject() {
        String s1 = null;
        Object o2 = new Object();

        assertFalse(Equality.areEqual(s1, o2));
    }

    // ... Test cases for other methods ...

    @Test(expected = NullPointerException.class)
    public void testAreEqual_NonNullObject_NullObject() {
        Object o1 = new Object();
        Object o2 = null;

        assertFalse(Equality.areEqual(o1, o2));
    }

    // ... Test cases for other methods ...
}