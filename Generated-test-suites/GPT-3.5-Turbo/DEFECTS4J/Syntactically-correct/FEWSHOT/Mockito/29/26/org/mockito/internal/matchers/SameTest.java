package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.argThat;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        
        assertTrue(same.matches(obj1));
        assertFalse(same.matches(obj2));
    }
    
    @Test
    public void testDescribeTo() {
        Object obj = new Object();
        Same same = new Same(obj);
        Description description = new Description();
        same.describeTo(description);
        
        String expected = "same(" + obj.toString() + ")";
        assertFalse(description.toString().isEmpty());
        assertTrue(description.toString().equals(expected));
    }
    
    @Test
    public void testAppendQuoting() {
        Same same = new Same("");
        Description description = new Description();
        same.appendQuoting(description);
        
        String expected = "\"";
        assertFalse(description.toString().isEmpty());
        assertTrue(description.toString().equals(expected));
    }
    
}