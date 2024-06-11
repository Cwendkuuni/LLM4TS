package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SameTest {
    @Test
    public void testMatches() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Same same = new Same(obj1);

        assertTrue(same.matches(obj2));
    }

    @Test
    public void testMatches_NotSame() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);

        assertFalse(same.matches(obj2));
    }

    @Test
    public void testDescribeTo_String() {
        Object obj = "string";
        Same same = new Same(obj);

        Description description = mock(Description.class);
        same.describeTo(description);

        verify(description).appendText("same(\"string\")");
    }

    @Test
    public void testDescribeTo_Character() {
        Object obj = 'c';
        Same same = new Same(obj);

        Description description = mock(Description.class);
        same.describeTo(description);

        verify(description).appendText("same('c')");
    }

    @Test
    public void testDescribeTo_NotStringOrCharacter() {
        Object obj = 123;
        Same same = new Same(obj);

        Description description = mock(Description.class);
        same.describeTo(description);

        verify(description).appendText("same(123)");
    }
}