import org.junit.Assert.*; // for assertions
import org.junit.Test; // for Test annotation
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.AreEqual;

import java.lang.reflect.Array;

public class EqualityTest {

    @Test
    public void testAreEqualNullNull() {
        AreEqual areEqual = new AreEqual();
        assertTrue(areEqual.areEqual(null, null));
    }

    @Test
    public void testAreEqualNullObject() {
        AreEqual areEqual = new AreEqual();
        assertFalse(areEqual.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqualObjectNull() {
        AreEqual areEqual = new AreEqual();
        assertFalse(areEqual.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqualDifferentObjects() {
        AreEqual areEqual = new AreEqual();
        assertFalse(areEqual.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreEqualSameObjects() {
        AreEqual areEqual = new AreEqual();
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(areEqual.areEqual(obj1, obj2));
    }

    @Test(expected = NullPointerException.class)
    public void testAreEqualNullArgument() {
        AreEqual areEqual = new AreEqual();
        areEqual.areEqual(null, new Object());
    }

    @Test
    public void testAreArraysEqualNullNull() {
        AreEqual areEqual = new AreEqual();
        assertTrue(areEqual.areArraysEqual(null, null));
    }

    @Test
    public void testAreArraysEqualNullArray() {
        AreEqual areEqual = new AreEqual();
        Object[] array1 = null;
        Object[] array2 = new Object[0];
        assertFalse(areEqual.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqualEmptyArrays() {
        AreEqual areEqual = new AreEqual();
        Object[] array1 = new Object[0];
        Object[] array2 = new Object[0];
        assertTrue(areEqual.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqualDifferentLengths() {
        AreEqual areEqual = new AreEqual();
        Object[] array1 = new Object[3];
        Object[] array2 = new Object[2];
        assertFalse(areEqual.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqualSameArrays() {
        AreEqual areEqual = new AreEqual();
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = array1.clone();
        assertTrue(areEqual.areArraysEqual(array1, array2));
    }

    @Test(expected = ArrayStoreException.class)
    public void testAreArraysEqualDifferentTypes() {
        AreEqual areEqual = new AreEqual();
        Object[] array1 = new Object[]{1, 2, 3};
        String[] array2 = new String[]{"1", "2", "3"};
        areEqual.areArraysEqual(array1, array2);
    }

    @Test
    public void testAreArraysEqualEqualArrays() {
        AreEqual areEqual = new AreEqual();
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = array1.clone();
        assertTrue(areEqual.areArraysEqual(array1, array2));
    }

    @Test
    public void testIsArrayNull() {
        assertTrue(Equality.isArray(null));
    }

    @Test
    public void testIsArrayObject() {
        Object obj = new Object()[0];
        assertTrue(Equality.isArray(obj));
    }

    @Test
    public void testIsArrayNonArray() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }
}