import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_NullNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_NullObject() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_ObjectNull() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_SameObjects() {
        Object obj1 = new Object();
        assertTrue(Equality.areEqual(obj1, obj1));
    }

    @Test
    public void testAreEqual_EqualObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        obj1.equals(obj2); //assuming equals method returns true for these objects
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_DifferentTypes() {
        Object obj1 = new Object();
        int obj2 = 1;
        try {
            Equality.areEqual(obj1, obj2);
        } catch (IllegalArgumentException e) {
            assertEquals("argument types are not instance of [Ljava.lang.Object;[ or [Ljava.lang.Object; and [" +
                            obj2.getClass().getName() + "]", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testAreArraysEqual_SameArrays() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqual_DifferentLengths() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2};
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqual_EqualLengthsDifferentElements() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{4, 2, 3};
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqual_EqualLengthsSameElementsInDifferentOrder() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{2, 3, 1};
        assertTrue(Equality.areArraysEqual(array1, array2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreArraysEqual_DifferentTypesElements() {
        Object[] array1 = new Object[]{1, 2, new Object()};
        Object[] array2 = new Object[]{1, 2, "string"};
        try {
            Equality.areArraysEqual(array1, array2);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}