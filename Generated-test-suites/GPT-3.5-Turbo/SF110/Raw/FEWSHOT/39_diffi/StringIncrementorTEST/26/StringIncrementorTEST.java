
package de.beiri22.stringincrementor;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: when both strings are empty
        assertEquals(new RelativeString(), StringIncrementor.diff("", ""));
        
        // Test case 2: when a and b are the same string
        String a = "abc";
        String b = "abc";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 3));
        assertEquals(expected, StringIncrementor.diff(a, b));
        
        // Test case 3: when a and b have some common characters
        a = "abcd";
        b = "bcde";
        expected = new RelativeString();
        expected.addLink(new StringLink(1, 0, 3));
        assertEquals(expected, StringIncrementor.diff(a, b));
        
        // Test case 4: when a and b have no common characters
        a = "abcd";
        b = "efgh";
        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 0));
        expected.setAbsolute("efgh");
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testPatch() {
        // Test case 1: when the patch contains no links
        String a = "abc";
        RelativeString r = new RelativeString();
        r.setAbsolute("def");
        assertEquals("def", StringIncrementor.patch(a, r));
        
        // Test case 2: when the patch contains links
        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        assertEquals("abc", StringIncrementor.patch(a, r));
        
        // Test case 3: when the patch contains both links and absolute characters
        a = "";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 0));
        r.setAbsolute("abcdefgh");
        assertEquals("abcdefgh", StringIncrementor.patch(a, r));
    }
}
