
package de.beiri22.stringincrementor;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test with two strings having no common subsequence
        String a = "abcde";
        String b = "fghij";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
        
        // Test with two equal strings
        a = "abcde";
        b = "abcde";
        result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());
        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(0, link.getPosOrig());
        assertEquals(0, link.getPosNew());
        assertEquals(5, link.getLen());

        // Test with one string being a substring of the other
        a = "abcdef";
        b = "cd";
        result = StringIncrementor.diff(a, b);
        assertEquals("abef", result.getAbsolute());
        assertEquals(1, result.linksCount());
        link = result.getLink(0);
        assertEquals(2, link.getPosOrig());
        assertEquals(0, link.getPosNew());
        assertEquals(2, link.getLen());

        // Test with one empty string
        a = "";
        b = "abcde";
        result = StringIncrementor.diff(a, b);
        assertEquals("abcde", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test with both empty strings
        a = "";
        b = "";
        result = StringIncrementor.diff(a, b);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testDiffVerbose() {
        // Test with verbose output and no common subsequence
        String a = "abcde";
        String b = "fghij";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test with verbose output and two equal strings
        a = "abcde";
        b = "abcde";
        result = StringIncrementor.diff(a, b, true);
        assertEquals("", result.getAbsolute());
        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(0, link.getPosOrig());
        assertEquals(0, link.getPosNew());
        assertEquals(5, link.getLen());

        // Test with verbose output and one string being a substring of the other
        a = "abcdef";
        b = "cd";
        result = StringIncrementor.diff(a, b, true);
        assertEquals("abef", result.getAbsolute());
        assertEquals(1, result.linksCount());
        link = result.getLink(0);
        assertEquals(2, link.getPosOrig());
        assertEquals(0, link.getPosNew());
        assertEquals(2, link.getLen());

        // Test with verbose output and one empty string
        a = "";
        b = "abcde";
        result = StringIncrementor.diff(a, b, true);
        assertEquals("abcde", result.getAbsolute());
        assertEquals(0, result.linksCount());

        // Test with verbose output and both empty strings
        a = "";
        b = "";
        result = StringIncrementor.diff(a, b, true);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testPatch() {
        // Test with a string and no links
        String a = "abcde";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("abcde", result);

        // Test with a string and one link
        a = "abcde";
        r = new RelativeString();
        StringLink link = new StringLink(0, 1, 3);
        r.addLink(link);
        result = StringIncrementor.patch(a, r);
        assertEquals("abadce", result);

        // Test with a string and multiple links
        a = "abcdefghij";
        r = new RelativeString();
        link = new StringLink(0, 4, 3);
        r.addLink(link);
        link = new StringLink(5, 9, 5);
        r.addLink(link);
        result = StringIncrementor.patch(a, r);
        assertEquals("abccdeghijk", result);

        // Test with an empty string and no links
        a = "";
        r = new RelativeString();
        result = StringIncrementor.patch(a, r);
        assertEquals("", result);

        // Test with an empty string and one link
        a = "";
        r = new RelativeString();
        link = new StringLink(0, 0, 3);
        r.addLink(link);
        result = StringIncrementor.patch(a, r);
        assertEquals("abc", result);

        // Test with an empty string and multiple links
        a = "";
        r = new RelativeString();
        link = new StringLink(0, 0, 3);
        r.addLink(link);
        link = new StringLink(1, 3, 2);
        r.addLink(link);
        result = StringIncrementor.patch(a, r);
        assertEquals("abcde", result);
    }

    @Test
    public void testPatchVerbose() {
        // Test with verbose output and a string and no links
        String a = "abcde";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r, true);
        assertEquals("abcde", result);

        // Test with verbose output and a string and one link
        a = "abcde";
        r = new RelativeString();
        StringLink link = new StringLink(0, 1, 3);
        r.addLink(link);
        result = StringIncrementor.patch(a, r, true);
        assertEquals("abadce", result);

        // Test with verbose output and a string and multiple links
        a = "abcdefghij";
        r = new RelativeString();
        link = new StringLink(0, 4, 3);
        r.addLink(link);
        link = new StringLink(5, 9, 5);
        r.addLink(link);
        result = StringIncrementor.patch(a, r, true);
        assertEquals("abccdeghijk", result);

        // Test with verbose output and an empty string and no links
        a = "";
        r = new RelativeString();
        result = StringIncrementor.patch(a, r, true);
        assertEquals("", result);

        // Test with verbose output and an empty string and one link
        a = "";
        r = new RelativeString();
        link = new StringLink(0, 0, 3);
        r.addLink(link);
        result = StringIncrementor.patch(a, r, true);
        assertEquals("abc", result);

        // Test with verbose output and an empty string and multiple links
        a = "";
        r = new RelativeString();
        link = new StringLink(0, 0, 3);
        r.addLink(link);
        link = new StringLink(1, 3, 2);
        r.addLink(link);
        result = StringIncrementor.patch(a, r, true);
        assertEquals("abcde", result);
    }
}

