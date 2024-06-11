package org.jsoup.helper;
import org.junit.*;
import java.util.*;
import java.net.*;

public class StringUtilTest {
    // Join a collection of strings by a separator
    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String joined = StringUtil.join(strings, ",");
        Assert.assertEquals("Hello,World,!", joined);
    }

    // Join an array of strings by a separator
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String joined = StringUtil.join(strings, ",");
        Assert.assertEquals("Hello,World,!", joined);
    }

    // Returns space padding
    @Test
    public void testPadding() {
        String padding = StringUtil.padding(5);
        Assert.assertEquals("     ", padding);
    }

    // Tests if a string is blank
    @Test
    public void testIsBlank() {
        boolean isBlank = StringUtil.isBlank("");
        Assert.assertTrue(isBlank);

        isBlank = StringUtil.isBlank("  ");
        Assert.assertTrue(isBlank);

        isBlank = StringUtil.isBlank("Hello");
        Assert.assertFalse(isBlank);
    }

    // Tests if a string is numeric
    @Test
    public void testIsNumeric() {
        boolean isNumeric = StringUtil.isNumeric("");
        Assert.assertFalse(isNumeric);

        isNumeric = StringUtil.isNumeric("123");
        Assert.assertTrue(isNumeric);

        isNumeric = StringUtil.isNumeric("ABC");
        Assert.assertFalse(isNumeric);
    }

    // Tests if a code point is whitespace
    @Test
    public void testIsWhitespace() {
        boolean isWhitespace = StringUtil.isWhitespace(' ');
        Assert.assertTrue(isWhitespace);

        isWhitespace = StringUtil.isWhitespace('\t');
        Assert.assertTrue(isWhitespace);

        isWhitespace = StringUtil.isWhitespace('\n');
        Assert.assertTrue(isWhitespace);

        isWhitespace = StringUtil.isWhitespace('A');
        Assert.assertFalse(isWhitespace);
    }

    // Tests if a code point is actually whitespace
    @Test
    public void testIsActuallyWhitespace() {
        boolean isActuallyWhitespace = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(isActuallyWhitespace);

        isActuallyWhitespace = StringUtil.isActuallyWhitespace('\t');
        Assert.assertTrue(isActuallyWhitespace);

        isActuallyWhitespace = StringUtil.isActuallyWhitespace('\n');
        Assert.assertTrue(isActuallyWhitespace);

        isActuallyWhitespace = StringUtil.isActuallyWhitespace('A');
        Assert.assertFalse(isActuallyWhitespace);
    }

    // Normalise the whitespace within a string
    @Test
    public void testNormaliseWhitespace() {
        String normalized = StringUtil.normaliseWhitespace("Hello   World  !");
        Assert.assertEquals("Hello World !", normalized);
    }

    // Tests if a string is in a collection of strings
    @Test
    public void testIn() {
        String needle = "World";
        String[] haystack = {"Hello", "World", "!"};
        boolean in = StringUtil.in(needle, haystack);
        Assert.assertTrue(in);

        needle = "Goodbye";
        in = StringUtil.in(needle, haystack);
        Assert.assertFalse(in);
    }

    // Tests if a string is in a sorted array of strings
    @Test
    public void testInSorted() {
        String needle = "World";
        String[] haystack = {"!", "Hello", "World"};
        boolean inSorted = StringUtil.inSorted(needle, haystack);
        Assert.assertTrue(inSorted);

        needle = "Goodbye";
        inSorted = StringUtil.inSorted(needle, haystack);
        Assert.assertFalse(inSorted);
    }

    // Create a new absolute URL
    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "path";
        URL resolved = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(new URL("http://example.com/path"), resolved);

        relUrl = "?query=value";
        resolved = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(new URL("http://example.com/path?query=value"), resolved);
    }

    // Create a new absolute URL
    @Test
    public void testResolveString() {
        String baseUrl = "http://example.com";
        String relUrl = "path";
        String resolved = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("http://example.com/path", resolved);

        relUrl = "?query=value";
        resolved = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("http://example.com/path?query=value", resolved);
    }
}