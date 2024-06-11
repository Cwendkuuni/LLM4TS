
import static org.junit.Assert.*;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");

        String joined = StringUtil.join(strings, ",");
        assertEquals("hello,world", joined);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");

        String joined = StringUtil.join(strings.iterator(), ",");
        assertEquals("hello,world", joined);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world"};

        String joined = StringUtil.join(strings, ",");
        assertEquals("hello,world", joined);
    }

    @Test
    public void testPadding() {
        String padded = StringUtil.padding(5);
        assertEquals("     ", padded);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\r\n"));
        assertTrue(StringUtil.isBlank("\t"));

        assertFalse(StringUtil.isBlank("hello"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("hello"));

        assertTrue(StringUtil.isNumeric("123"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));

        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\u00A0'));

        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalised = StringUtil.normaliseWhitespace("hello\n\t  world");
        assertEquals("hello world", normalised);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, " hello\n\t  world", true);
        assertEquals("hello world", sb.toString());
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("hello", "hello", "world"));
        assertFalse(StringUtil.in("foo", "hello", "world"));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry"};
        assertTrue(StringUtil.inSorted("banana", haystack));
        assertFalse(StringUtil.inSorted("foo", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "/foo/bar.html";
        URL resolved = StringUtil.resolve(base, relUrl);

        assertEquals(new URL("http://www.example.com/foo/bar.html"), resolved);
    }

    @Test
    public void testResolveString() {
        String baseUrl = "http://www.example.com";
        String relUrl = "/foo/bar.html";
        String resolved = StringUtil.resolve(baseUrl, relUrl);

        assertEquals("http://www.example.com/foo/bar.html", resolved);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");
        assertEquals("hello", sb.toString());
    }
}