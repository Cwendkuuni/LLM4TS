package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;

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
        Object obj = new Object();
        Same same = new Same(obj);
        
        Description description = new Description();
        same.describeTo(description);
        
        assertEquals("same(\"" + obj.toString() + "\")", description.toString());
    }
    
    @Test
    public void testAppendQuoting() {
        Same same = new Same("");
        
        Description description = new Description();
        same.appendQuoting(description);
        
        assertEquals("\"", description.toString());
        
        same = new Same('c');
        description = new Description();
        same.appendQuoting(description);
        
        assertEquals("'", description.toString());
    }
}