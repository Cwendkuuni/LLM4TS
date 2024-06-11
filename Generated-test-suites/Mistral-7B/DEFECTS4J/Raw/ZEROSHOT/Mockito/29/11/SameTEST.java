package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Same sameInstance = new Same(wanted);
        Object actual = wanted;

        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Same sameInstance = new Same(wanted);
        Object actual = new Object();

        assertFalse(sameInstance.matches(actual));
    }

    @Test
    public void testNullValues() {
        Same sameInstance = new Same(null);
        Object actual = null;

        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testStringValues() {
        Same sameInstance = new Same("test");
        Object actual = "test";

        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testStringValuesDifferent() {
        Same sameInstance = new Same("test1");
        Object actual = "test2";

        assertFalse(sameInstance.matches(actual));
    }

    @Test
    public void testCharacterValues() {
        Same sameInstance = new Same('a');
        Object actual = 'a';

        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testCharacterValuesDifferent() {
        Same sameInstance = new Same('a');
        Object actual = 'b';

        assertFalse(sameInstance.matches(actual));
    }

    @Test
    public void testMatchesDescription() {
        Object wanted = "test";
        Same sameInstance = new Same(wanted);

        Description description = new Description();
        sameInstance.describeTo(description);

        Matcher expectedMatcher = new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return wanted.equals(argument);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("argument equal to ");
                description.appendValue(wanted);
            }
        };

        assertEquals(expectedMatcher.description().toString(), description.toString());
    }

    @Test
    public void testMatchesWithNullDescription() {
        Object wanted = null;
        Same sameInstance = new Same(wanted);

        Description description = new Description();
        sameInstance.describeTo(description);

        assertNull(description.toString());
    }
}