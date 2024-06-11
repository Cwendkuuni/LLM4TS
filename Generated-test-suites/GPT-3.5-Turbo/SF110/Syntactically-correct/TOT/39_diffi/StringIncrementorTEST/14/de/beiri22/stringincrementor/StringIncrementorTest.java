
package de.beiri22.stringincrementor;

import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        // Typical use case
        RelativeString result1 = StringIncrementor.diff("abcde", "abfde");
        assertEquals(1, result1.linksCount());
        assertEquals("f", result1.getAbsolute());
        assertEquals(0, result1.getLink(0).getPosOrig());
        assertEquals(2, result1.getLink(0).getPosNew());
        assertEquals(1, result1.getLink(0).getLen());

        // Edge case: empty strings
        RelativeString result2 = StringIncrementor.diff("", "");
        assertEquals(0, result2.linksCount());
        assertEquals("", result2.getAbsolute());

        // Error scenario: null strings
        try {
            RelativeString result3 = StringIncrementor.diff(null, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Input strings cannot be null", e.getMessage());
        }
    }

    @Test
    public void testPatch() {
        // Typical use case
        String result1 = StringIncrementor.patch("abcde", new RelativeString());
        assertEquals("abcde", result1);

        // Edge case: empty string
        String result2 = StringIncrementor.patch("", new RelativeString());
        assertEquals("", result2);

        // Error scenario: null string
        try {
            String result3 = StringIncrementor.patch(null, new RelativeString());
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Original string cannot be null", e.getMessage());
        }
    }
}
