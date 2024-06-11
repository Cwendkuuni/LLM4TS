package org.mockito.internal.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {

    @Test
    public void testMatches() {
        // Test when wanted and actual are the same object
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));

        // Test when wanted and actual are different objects
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same1 = new Same(obj1);
        assertFalse(same1.matches(obj2));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        Description description = mock(Description.class);
        same.describeTo(description);

        verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testAppendQuoting() {
        Same same1 = new Same("test");
        Description description1 = mock(Description.class);
        same1.appendQuoting(description1);
        verify(description1).appendText("\"");

        Same same2 = new Same('c');
        Description description2 = mock(Description.class);
        same2.appendQuoting(description2);
        verify(description2).appendText("'");
    }
}