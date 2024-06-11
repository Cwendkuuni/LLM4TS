package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;

public class SameTest {
    @Test
    public void testMatches() {
        Same matcher = new Same("hello");
        assertTrue(matcher.matches("hello"));
        assertFalse(matcher.matches("world"));
    }

    @Test
    public void testDescribeTo() {
        Same matcher = new Same("hello");
        Description description = new Description();
        matcher.describeTo(description);
        assertEquals("same(\"hello\")", description.toString());
    }
}