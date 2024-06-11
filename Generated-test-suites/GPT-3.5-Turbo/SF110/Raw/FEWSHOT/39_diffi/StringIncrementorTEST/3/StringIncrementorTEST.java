
package de.beiri22.stringincrementor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcdefg";
        String b = "abcdefghij";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 7));
        expected.setAbsolute("hij");
        assertEquals(expected, StringIncrementor.diff(a, b));

        a = "abc";
        b = "def";
        expected = new RelativeString();
        expected.setAbsolute("def");
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiffVerbose() {
        String a = "abcdefg";
        String b = "abcdefghij";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 7));
        expected.setAbsolute("hij");
        // Add additional assertions for verbose mode
        // For example output statements
        assertEquals(expected, StringIncrementor.diff(a, b, true));

        a = "abc";
        b = "def";
        expected = new RelativeString();
        expected.setAbsolute("def");
        // Add additional assertions for verbose mode
        // For example output statements
        assertEquals(expected, StringIncrementor.diff(a, b, true));
    }

    @Test
    public void testPatch() {
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 7));
        r.setAbsolute("hij");
        String expected = "abcdefghij";
        assertEquals(expected, StringIncrementor.patch(a, r));

        a = "abcdefghij";
        r = new RelativeString();
        r.setAbsolute("def");
        expected = "defghij";
        assertEquals(expected, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatchVerbose() {
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 7));
        r.setAbsolute("hij");
        String expected = "abcdefghij";
        // Add additional assertions for verbose mode
        // For example output statements
        assertEquals(expected, StringIncrementor.patch(a, r, true));

        a = "abcdefghij";
        r = new RelativeString();
        r.setAbsolute("def");
        expected = "defghij";
        // Add additional assertions for verbose mode
        // For example output statements
        assertEquals(expected, StringIncrementor.patch(a, r, true));
    }
}
