package org.mockito.internal.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import org.hamcrest.Description;
import org.junit.Test;


public class SameTest {
    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        
        assertTrue(same.matches(obj));
        assertFalse(same.matches(new Object()));
    }
    
    @Test
    public void testDescribeTo_String() {
        Description description = mock(Description.class);
        Same same = new Same("hello");
        
        same.describeTo(description);
        
        verify(description).appendText("same(\"hello\")");
    }
    
    @Test
    public void testDescribeTo_Character() {
        Description description = mock(Description.class);
        Same same = new Same('c');
        
        same.describeTo(description);
        
        verify(description).appendText("same('c')");
    }
    
    @Test
    public void testDescribeTo_OtherObject() {
        Description description = mock(Description.class);
        Same same = new Same(new Object());
        
        same.describeTo(description);
        
        verify(description).appendText("same(");
        verify(description).appendText(same.wanted.toString());
        verify(description).appendText(")");
    }
    
    @Test
    public void testMatchesArgumentMatcher() {
        Object obj = new Object();
        Same same = new Same(obj);
        
        assertTrue(argThat(same).matches(obj));
        assertFalse(argThat(same).matches(new Object()));
    }
}