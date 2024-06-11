import org.junit.Test;
import org.mockito.internal.matchers.Equality;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(new Object(), null));
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_SameObject() {
        Object object = new Object();
        assertTrue(Equality.areEqual(object, object));
    }

    @Test
    public void testAreEqual_EqualObjects() {
        assertEquals(true, Equality.areEqual("hello", "hello"));
        assertEquals(true, Equality.areEqual(2, 2));
        assertEquals(true, Equality.areEqual(0.5, 0.5));
        assertEquals(true, Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreEqual_DifferentObjects() {
        assertFalse(Equality.areEqual("hello", "world"));
        assertFalse(Equality.areEqual(2, 3));
        assertFalse(Equality.areEqual(0.5, 1.0));
        assertFalse(Equality.areEqual(new Object(), new StringBuilder()));
    }

    @Test
    public void testAreEqual_ArraysEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArraysNotEqual_Length() {
        int[] array1 = {1, 2};
        int[] array2 = {1, 2, 3};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArraysNotEqual_Elements() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 3, 2};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_EmptyArrays() {
        int[] array1 = {};
        int[] array2 = {};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_UnequalArrayElementTypes() {
        Object[] array1 = {"hello"};
        int[] array2 = {1};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_NullArrays() {
        assertTrue(Equality.areEqual((Object) null, null));
        assertFalse(Equality.areEqual(new Object[0], null));
        assertFalse(Equality.areEqual(null, new Object[0]));
    }

    @Test
    public void testAreEqual_ObjectAndArray() {
        assertFalse(Equality.areEqual("hello", new int[]{1}));
        assertFalse(Equality.areEqual(2, new Object[]{2}));
    }

    @Test
    public void testAreEqual_ErrorScenarios() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        assertFalse(Equality.areEqual(new Object(), new int[]{1}));
    }

}