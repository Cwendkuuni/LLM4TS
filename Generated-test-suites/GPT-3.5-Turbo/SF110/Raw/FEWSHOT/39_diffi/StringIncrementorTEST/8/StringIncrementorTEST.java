
package de.beiri22.stringincrementor;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        assertEquals(expected, StringIncrementor.diff("a", "a"));

        expected = new RelativeString();
        expected.addLink(new StringLink(1, 1, 2));
        assertEquals(expected, StringIncrementor.diff("ab", "ab"));

        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        expected.addLink(new StringLink(2, 2, 1));
        assertEquals(expected, StringIncrementor.diff("abc", "abc"));

        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        expected.addLink(new StringLink(2, 2, 1));
        expected.addLink(new StringLink(4, 4, 1));
        assertEquals(expected, StringIncrementor.diff("abcd", "abcd"));

        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        expected.addLink(new StringLink(2, 2, 1));
        expected.addLink(new StringLink(6, 6, 1));
        assertEquals(expected, StringIncrementor.diff("abcegd", "abcegd"));
    }

    @Test
    public void testDiff_verbose() {
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        assertEquals(expected, StringIncrementor.diff("a", "a", true));

        expected = new RelativeString();
        expected.addLink(new StringLink(1, 1, 2));
        assertEquals(expected, StringIncrementor.diff("ab", "ab", true));

        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        expected.addLink(new StringLink(2, 2, 1));
        assertEquals(expected, StringIncrementor.diff("abc", "abc", true));

        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        expected.addLink(new StringLink(2, 2, 1));
        expected.addLink(new StringLink(4, 4, 1));
        assertEquals(expected, StringIncrementor.diff("abcd", "abcd", true));

        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 1));
        expected.addLink(new StringLink(2, 2, 1));
        expected.addLink(new StringLink(6, 6, 1));
        assertEquals(expected, StringIncrementor.diff("abcegd", "abcegd", true));
    }

    @Test
    public void testPatch() {
        assertEquals("a", StringIncrementor.patch("", new RelativeString().addLink(new StringLink(0, 0, 1))));

        assertEquals("ab", StringIncrementor.patch("", new RelativeString().addLink(new StringLink(0, 0, 1)).addLink(new StringLink(1, 1, 1))));

        assertEquals("abc", StringIncrementor.patch("", new RelativeString().addLink(new StringLink(0, 0, 1), new StringLink(1, 1, 1), new StringLink(2, 2, 1))));
    }

    @Test
    public void testPatch_verbose() {
        assertEquals("a", StringIncrementor.patch("", new RelativeString().addLink(new StringLink(0, 0, 1))), true);

        assertEquals("ab", StringIncrementor.patch("", new RelativeString().addLink(new StringLink(0, 0, 1)).addLink(new StringLink(1, 1, 1))), true);

        assertEquals("abc", StringIncrementor.patch("", new RelativeString().addLink(new StringLink(0, 0, 1), new StringLink(1, 1, 1), new StringLink(2, 2, 1))), true);
    }
}
