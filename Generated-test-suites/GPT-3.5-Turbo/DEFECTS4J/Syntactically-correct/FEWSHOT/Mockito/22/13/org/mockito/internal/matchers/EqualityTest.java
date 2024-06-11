
package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        // test case: both null
        assertTrue(Equality.areEqual(null, null));
        
        // test case: o1 is null, o2 is not null
        assertFalse(Equality.areEqual(null, new Object()));
        
        // test case: o1 is not null, o2 is null
        assertFalse(Equality.areEqual(new Object(), null)); 
        
        // test case: o1 and o2 are different instances with same content
        assertTrue(Equality.areEqual("hello", new String("hello")));
        
        // test case: o1 and o2 are different instances with different content
        assertFalse(Equality.areEqual("hello", "world"));
    }

    @Test
    public void testAreArraysEqual() {
        // test case: both null
        assertTrue(Equality.areArraysEqual(null, null));
        
        // test case: o1 is null, o2 is not null
        assertFalse(Equality.areArraysEqual(null, new Object[] {1, 2, 3}));
        
        // test case: o1 is not null, o2 is null
        assertFalse(Equality.areArraysEqual(new Object[] {1, 2, 3}, null)); 
        
        // test case: o1 and o2 have different lengths
        assertFalse(Equality.areArraysEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3, 4}));
        
        // test case: o1 and o2 have the same length, but different elements
        assertFalse(Equality.areArraysEqual(new Object[] {1, 2, 3}, new Object[] {4, 5, 6}));
        
        // test case: o1 and o2 have the same length and same elements
        assertTrue(Equality.areArraysEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // test case: both null
        assertTrue(Equality.areArrayLengthsEqual(null, null));
        
        // test case: o1 is null, o2 is not null
        assertFalse(Equality.areArrayLengthsEqual(null, new Object[] {1, 2, 3}));
        
        // test case: o1 is not null, o2 is null
        assertFalse(Equality.areArrayLengthsEqual(new Object[] {1, 2, 3}, null)); 
        
        // test case: o1 and o2 have different lengths
        assertFalse(Equality.areArrayLengthsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3, 4}));
        
        // test case: o1 and o2 have the same length 
        assertTrue(Equality.areArrayLengthsEqual(new Object[] {1, 2, 3}, new Object[] {4, 5, 6}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        // test case: both null
        assertTrue(Equality.areArrayElementsEqual(null, null));
        
        // test case: o1 is null, o2 is not null
        assertFalse(Equality.areArrayElementsEqual(null, new Object[] {1, 2, 3}));
        
        // test case: o1 is not null, o2 is null
        assertFalse(Equality.areArrayElementsEqual(new Object[] {1, 2, 3}, null)); 
        
        // test case: o1 and o2 have different elements
        assertFalse(Equality.areArrayElementsEqual(new Object[] {1, 2, 3}, new Object[] {4, 5, 6}));
        
        // test case: o1 and o2 have the same elements
        assertTrue(Equality.areArrayElementsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3}));
    }

    @Test
    public void testIsArray() {
        // test case: null object
        assertFalse(Equality.isArray(null));

        // test case: non-array object
        assertFalse(Equality.isArray("hello"));
        
        // test case: array object
        assertTrue(Equality.isArray(new Object[] {1, 2, 3}));
    }
}
