import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertTrue(Equality.areEqual(o1, o2)); // same object

        o1 = new Object();
        o2 = new Object();
        assertFalse(Equality.areEqual(o1, o2)); // different objects

        Object o3 = new Object[] {1, 2, 3};
        Object o4 = new Object[] {1, 2, 3};
        assertTrue(Equality.areEqual(o3, o4)); // same arrays with same elements

        Object o5 = new Object[] {1, 2, 3};
        Object o6 = new Object[] {1, 2, 4};
        assertFalse(Equality.areEqual(o5, o6)); // different arrays

        Object o7 = new Object[3];
        Object o8 = new Object[3];
        assertFalse(Equality.areEqual(o7, o8)); // different length arrays
    }

    @Test
    public void testAreArraysEqual() {
        Object o1 = new Object[] {1, 2, 3};
        Object o2 = new Object[] {1, 2, 3};
        assertTrue(Equality.areArraysEqual(o1, o2));

        Object o3 = new Object[] {1, 2, 3};
        Object o4 = new int[] {1, 2, 3};
        assertFalse(Equality.areArraysEqual(o3, o4)); // different types

        Object o5 = new Object[] {1, null, 3};
        Object o6 = new Object[] {1, 2, 3};
        assertFalse(Equality.areArraysEqual(o5, o6)); // different elements

        Object o7 = new Object[] {null, null, null};
        Object o8 = new Object[3];
        assertFalse(Equality.areArraysEqual(o7, o8)); // null vs empty
    }

    @Test
    public void testIsArray() {
        Object o1 = new Object();
        assertFalse(Equality.isArray(o1));

        Object o2 = new Object[] {1, 2, 3};
        assertTrue(Equality.isArray(o2));
    }
}