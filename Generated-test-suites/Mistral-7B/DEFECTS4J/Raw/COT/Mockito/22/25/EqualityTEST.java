import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.areArraysEqual;
import org.mockito.internal.matchers.Equality.areArrayElementsEqual;
import org.mockito.internal.matchers.Equality.areArrayLengthsEqual;
import org.mockito.internal.matchers.Equality.isArray;

import java.lang.reflect.Array;

public class EqualityTest {

    @Test
    public void testAreEqual_nullAndNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_nullAndObject() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_ObjectAndNull() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_ObjectAndObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Equality.areEqual(obj1, obj2));
        obj2 = new Object() {}; // different instance
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_ThrowsIllegalArgumentException_DifferentTypes() {
        Equality.areEqual(new Object(), new Integer(1));
    }

    @Test
    public void testAreEqual_PrimitiveTypes() {
        assertTrue(Equality.areEqual(1, 1));
        assertTrue(Equality.areEqual(1.0f, 1.0f));
        assertTrue(Equality.areEqual(1.0, 1.0));
        assertTrue(Equality.areEqual(true, true));
        assertFalse(Equality.areEqual(1, 2));
        assertFalse(Equality.areEqual('a', 'b'));
    }

    @Test
    public void testAreArraysEqual_NullArrays() {
        Object[] arr1 = null;
        Object[] arr2 = null;
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_ArraysWithDifferentLengths() {
        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[2];
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreArraysEqual_ThrowsIllegalArgumentException_DifferentTypes() {
        Equality.areArraysEqual(new Object[1], new int[1]);
    }

    @Test
    public void testAreArraysEqual_ArraysWithSameLengthButNotEqualElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_ArraysWithSameLengthAndEqualElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new Object[0]));
        assertFalse(Equality.isArray(new Object()));
    }
}