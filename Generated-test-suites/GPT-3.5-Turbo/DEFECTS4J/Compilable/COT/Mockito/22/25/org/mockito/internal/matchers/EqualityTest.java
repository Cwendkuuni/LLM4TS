package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        assertFalse(Equality.areEqual(new Object(), new Object()));
        assertTrue(Equality.areEqual("hello", "hello"));
    }
    
    @Test
    public void testAreEqualEdgeCases() {
        assertTrue(Equality.areEqual(new int[0], new int[0]));
        assertFalse(Equality.areEqual(new int[0], new int[1]));
        assertFalse(Equality.areEqual(new Object(), 123));
        assertFalse(Equality.areEqual(123, new Object()));
    }
    
    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(new int[0], new int[0]));
        assertFalse(Equality.areArraysEqual(new int[0], new int[1]));
        assertTrue(Equality.areArraysEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 4}));
    }
    
    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[0], new int[0]));
        assertFalse(Equality.areArrayLengthsEqual(new int[0], new int[1]));
        assertTrue(Equality.areArrayLengthsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3}));
        assertFalse(Equality.areArrayLengthsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3, 4}));
    }
    
    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 4}));
    }
    
    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[0]));
        assertFalse(Equality.isArray(new Object()));
    }
    
}