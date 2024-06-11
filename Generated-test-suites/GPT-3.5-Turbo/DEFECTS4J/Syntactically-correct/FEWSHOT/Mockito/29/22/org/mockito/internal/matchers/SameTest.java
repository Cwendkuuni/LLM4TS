package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

public class SameTest {
    @Mock
    private Description description;

    @Test
    public void testMatchesWithSameObject() {
        Object obj = new Object();
        Same same = new Same(obj);

        assertTrue(same.matches(obj));
    }

    @Test
    public void testMatchesWithDifferentObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);

        assertFalse(same.matches(obj2));
    }

    @Test
    public void testDescribeToWithStringObject() {
        Same same = new Same("hello");

        same.describeTo(description);

        assertEquals("same(\"hello\")", description.toString());
    }

    @Test
    public void testDescribeToWithCharacterObject() {
        Same same = new Same('c');

        same.describeTo(description);

        assertEquals("same('c')", description.toString());
    }

    @Test
    public void testDescribeToWithOtherObject() {
        Object obj = new Object();
        Same same = new Same(obj);

        same.describeTo(description);

        assertEquals("same(" + obj.toString() + ")", description.toString());
    }
}