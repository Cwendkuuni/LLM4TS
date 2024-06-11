package de.beiri22.stringincrementor;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Typical use case
        RelativeString result = StringIncrementor.diff("abcde", "abxyzde");
        assertEquals(result.getAbsolute(), "xyz");
        assertEquals(result.linksCount(), 1);
        StringLink link = result.getLink(0);
        assertEquals(link.getPosOrig(), 2);
        assertEquals(link.getPosNew(), 2);
        assertEquals(link.getLen(), 3);

        // Edge case: Empty strings
        result = StringIncrementor.diff("", "");
        assertEquals(result.getAbsolute(), "");
        assertEquals(result.linksCount(), 0);

        // Edge case: Empty string as second argument
        result = StringIncrementor.diff("abcde", "");
        assertEquals(result.getAbsolute(), "abcde");
        assertEquals(result.linksCount(), 0);

        // Error scenario: Null as first argument
        try {
            result = StringIncrementor.diff(null, "abc");
            fail("Expected exception not thrown!");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Error scenario: Null as second argument
        try {
            result = StringIncrementor.diff("abc", null);
            fail("Expected exception not thrown!");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testPatch() {
        // Typical use case
        String result = StringIncrementor.patch("abcde", new RelativeString("xyz"));
        assertEquals(result, "abxyzde");

        // Edge case: Empty string as second argument
        result = StringIncrementor.patch("abcde", new RelativeString(""));
        assertEquals(result, "abcde");

        // Edge case: Empty string as first argument
        result = StringIncrementor.patch("", new RelativeString("xyz"));
        assertEquals(result, "xyz");

        // Error scenario: Null as first argument
        try {
            result = StringIncrementor.patch(null, new RelativeString("xyz"));
            fail("Expected exception not thrown!");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Error scenario: Null as second argument
        try {
            result = StringIncrementor.patch("abc", null);
            fail("Expected exception not thrown!");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}