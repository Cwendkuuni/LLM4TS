package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Objects;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        assertTrue(sameMatcher.matches(actual));
        sameMatcher.describeTo(new Description() {});
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        assertFalse(new Same(wanted).matches(actual));
        assertFalse(new Same(actual).matches(wanted));
    }

    @Test
    public void testNullAndSameObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(null));
        sameMatcher.describeTo(new Description() {});
    }

    @Test
    public void testNullAndDifferentObject() {
        Object wanted = new Object();

        assertFalse(new Same(null).matches(wanted));
        assertFalse(new Same(wanted).matches(null));
    }

    @Test
    public void testStringQuoting() {
        String wanted = "test";
        Same sameMatcher = new Same(wanted);

        assertEquals("same(test)", sameMatcher.description().toString());
    }

    @Test
    public void testCharacterQuoting() {
        char wanted = 'a';
        Same sameMatcher = new Same(wanted);

        assertEquals("same(a)", sameMatcher.description().toString());
    }

    @Test
    public void testSameString() {
        String wanted = "test";
        Object actual = "test";

        assertTrue(new Same(wanted).matches(actual));
        Same sameMatcher = new Same(wanted);
        sameMatcher.describeTo(new Description() {});
    }

    @Test
    public void testSameCharacter() {
        char wanted = 'a';
        Object actual = 'a';

        assertTrue(new Same(wanted).matches(actual));
        Same sameMatcher = new Same(wanted);
        sameMatcher.describeTo(new Description() {});
    }

    @Test(expected = NullPointerException.class)
    public void testNullWanted() {
        new Same(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullWantedDescription() {
        new Same(new Object()) {
            @Override
            public void describeTo(Description description) {
                // do nothing
            }
        }.description();
    }
}