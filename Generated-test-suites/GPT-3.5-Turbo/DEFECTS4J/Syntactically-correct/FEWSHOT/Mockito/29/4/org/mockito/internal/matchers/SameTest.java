package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {
    @Test
    public void testMatches() {
        Object obj1 = new Object();
        Same same = new Same(obj1);
        
        assertTrue(same.matches(obj1));
        
        Object obj2 = new Object();
        assertFalse(same.matches(obj2));
    }
    
    @Test
    public void testDescribeTo() {
        Object obj = "test";
        Same same = new Same(obj);
        
        Description description = new StringDescription();
        same.describeTo(description);
        
        assertEquals("same(\"test\")", description.toString());
    }
}