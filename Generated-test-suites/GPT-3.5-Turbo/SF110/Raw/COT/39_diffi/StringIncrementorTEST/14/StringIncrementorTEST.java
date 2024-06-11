
package de.beiri22.stringincrementor;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1: basic input
        String a1 = "abc";
        String b1 = "def";
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        // check if result is empty
        assertTrue(result1.linksCount() == 0);
        assertEquals(result1.getAbsolute(), "def");

        // Test case 2: input with common substring
        String a2 = "abcde";
        String b2 = "cdefg";
        RelativeString result2 = StringIncrementor.diff(a2, b2);
        // check if result has one link with correct values
        assertTrue(result2.linksCount() == 1);
        StringLink link2 = result2.getLink(0);
        assertTrue(link2.getPosOrig() == 2);
        assertTrue(link2.getPosNew() == 1);
        assertTrue(link2.getLen() == 4);
        assertEquals(result2.getAbsolute(), "cd");
    }

    @Test
    public void testDiffVerbose() {
        // Test case 1: basic input
        String a1 = "abc";
        String b1 = "def";
        RelativeString result1 = StringIncrementor.diff(a1, b1, true);
        // check if result is empty
        assertTrue(result1.linksCount() == 0);
        assertEquals(result1.getAbsolute(), "def");

        // Test case 2: input with common substring
        String a2 = "abcde";
        String b2 = "cdefg";
        RelativeString result2 = StringIncrementor.diff(a2, b2, true);
        // check if result has one link with correct values
        assertTrue(result2.linksCount() == 1);
        StringLink link2 = result2.getLink(0);
        assertTrue(link2.getPosOrig() == 2);
        assertTrue(link2.getPosNew() == 1);
        assertTrue(link2.getLen() == 4);
        assertEquals(result2.getAbsolute(), "cd");
    }

    @Test
    public void testPatch() {
        // Test case 1: basic input
        String a1 = "abc";
        RelativeString rs1 = new RelativeString();
        rs1.setAbsolute("def");
        String result1 = StringIncrementor.patch(a1, rs1);
        assertEquals(result1, "def");

        // Test case 2: input with relative links
        String a2 = "abcde";
        RelativeString rs2 = new RelativeString();
        rs2.addLink(new StringLink(2, 1, 4));
        rs2.setAbsolute("cd");
        String result2 = StringIncrementor.patch(a2, rs2);
        assertEquals(result2, "abcdefg");
    }

    @Test
    public void testPatchVerbose() {
        // Test case 1: basic input
        String a1 = "abc";
        RelativeString rs1 = new RelativeString();
        rs1.setAbsolute("def");
        String result1 = StringIncrementor.patch(a1, rs1, true);
        assertEquals(result1, "def");

        // Test case 2: input with relative links
        String a2 = "abcde";
        RelativeString rs2 = new RelativeString();
        rs2.addLink(new StringLink(2, 1, 4));
        rs2.setAbsolute("cd");
        String result2 = StringIncrementor.patch(a2, rs2, true);
        assertEquals(result2, "abcdefg");
    }
}
