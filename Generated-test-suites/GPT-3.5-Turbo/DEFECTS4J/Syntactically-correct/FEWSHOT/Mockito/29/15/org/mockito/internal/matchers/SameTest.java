package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Test;

import java.io.Serializable;

public class SameTest {
    @Test
    public void testMatches() {
        Same same = new Same("foo");
        assertTrue(same.matches("foo"));
        assertTrue(same.matches(null));
        Object obj = new Object();
        assertTrue(same.matches(obj));
    }
    
    @Test
    public void testDescribeTo() {
        Description description = new Description();
        Same same = new Same("foo");
        same.describeTo(description);
        assertEquals("same(\"foo\")", description.toString());
    }
    
    @Test
    public void testAppendQuoting() {
        Description description = new Description();
        Same same = new Same("foo");
        same.appendQuoting(description);
        assertEquals("\"", description.toString());
        
        description = new Description();
        same = new Same('a');
        same.appendQuoting(description);
        assertEquals("'", description.toString());
        
        description = new Description();
        same = new Same(10);
        same.appendQuoting(description);
        assertEquals("", description.toString());
    }
}