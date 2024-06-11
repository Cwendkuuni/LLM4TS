import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;

import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        assertTrue(Equality.areEqual(new Object(), new Object()));
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));
        assertFalse(Equality.areEqual(10, 20));
    }
    
    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3, 4}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{3, 2, 1}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new Object()));
    }
    
    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3, 4}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new Object()));
    }
    
    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
    }
    
    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
        assertFalse(Equality.isArray(new Object()));
    }
    
    @Test
    public void testAreEqual_NullObjects() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }
    
    @Test
    public void testAreArraysEqual_DifferentLengths() {
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3, 4}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
    }
    
    @Test
    public void testAreArraysEqual_DifferentElements() {
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{3, 2, 1}));
    }
    
    @Test
    public void testIsArray_InvalidInput() {
        assertFalse(Equality.isArray(new Object()));
    }
}