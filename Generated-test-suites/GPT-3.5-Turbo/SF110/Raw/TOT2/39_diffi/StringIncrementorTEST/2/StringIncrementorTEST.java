// Merged comprehensive JUnit test file with test cases from all three experts

package de.beiri22.stringincrementor;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff_Expert1() {
        RelativeString result = StringIncrementor.diff("abc", "ab");
        assertTrue(result != null);
        assertEquals(result.getAbsolute(), "c");
    }

    @Test
    public void testPatch_Expert1() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        String result = StringIncrementor.patch(a, r);
        assertEquals(result, a);
    }

    @Test
    public void testDiff_Expert2() {
        RelativeString result = StringIncrementor.diff("abcde", "ab");
        assertTrue(result != null);
    }

    @Test
    public void testPatch_Expert2() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        String result = StringIncrementor.patch(a, r);
        assertTrue(!result.isEmpty());
    }

    @Test
    public void testDiff_Expert3() {
        RelativeString result = StringIncrementor.diff("hello", "world", true);
        assertNotNull(result);
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testPatch_Expert3() {
        String a = "hello";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 5));
        String result = StringIncrementor.patch(a, r, true);
        assertEquals(a, result);
    }
}
