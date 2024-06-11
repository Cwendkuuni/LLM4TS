package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.hamcrest.Description;
import org.junit.Test;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);

        assertTrue(same.matches(obj1)); // Same object
        assertFalse(same.matches(obj2)); // Different object
    }

    @Test
    public void testDescribeTo() {
        Description description = mock(Description.class);
        Same same = new Same("foo");

        same.describeTo(description);

        verify(description).appendText("same(\"foo\")");
    }
    
    @Test
    public void testAppendQuoting_withString() {
        Description description = mock(Description.class);
        Same same = new Same("foo");

        same.appendQuoting(description);

        verify(description).appendText("\"");
    }

    @Test
    public void testAppendQuoting_withCharacter() {
        Description description = mock(Description.class);
        Same same = new Same('a');

        same.appendQuoting(description);

        verify(description).appendText("'");
    }

    @Test
    public void testAppendQuoting_withOtherTypes() {
        Description description = mock(Description.class);
        Same same = new Same(123);

        same.appendQuoting(description);

        verify(description, never()).appendText("\"");
        verify(description, never()).appendText("'");
    }
}