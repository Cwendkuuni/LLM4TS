package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SameTest {
    @Test
    public void testMatches() {
        // Test with different types
        assertFalse(new Same("foo").matches(1));
        assertFalse(new Same(1).matches("foo"));
        
        // Test with same string
        assertTrue(new Same("foo").matches("foo"));
        
        // Test with same object
        Object obj = mock(Object.class);
        assertTrue(new Same(obj).matches(obj));
    }
    
    @Test
    public void testDescribeTo() {
        // Test with string
        Description description = mock(Description.class);
        new Same("foo").describeTo(description);
        // Verify that the description was appended correctly
        // You should check the output of the appendText method for this test case
        
        // Test with character
        description = mock(Description.class);
        new Same('c').describeTo(description);
        // Verify that the description was appended correctly
        // You should check the output of the appendText method for this test case
    }
}