
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("foo", "bar", "baz");
        String joined = StringUtil.join(strings, ",");
        assertEquals("foo,bar,baz", joined);
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("foo", "bar", "baz");
        Iterator<String> iterator = strings.iterator();
        String joined = StringUtil.join(iterator, ",");
        assertEquals("foo,bar,baz", joined);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"foo", "bar", "baz"};
        String joined = StringUtil.join(strings, ",");
        assertEquals("foo,bar,baz", joined);
    }

    @Test
    public void testPadding() {
        String padded = StringUtil.padding(5);
        assertEquals("     ", padded);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\t"));
        assertTrue(StringUtil.isBlank("\n"));
        assertFalse(StringUtil.isBlank("foo"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("abc"));
        assertFalse(StringUtil.isNumeric("12a3"));
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
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalized = StringUtil.normaliseWhitespace("    foo   \t  bar \n   baz   ");
        assertEquals(" foo bar baz ", normalized);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "    foo   \t  bar \n   baz   ", false);
        assertEquals(" foo bar baz ", sb.toString());
    }

    @Test
    public void testIn() {
        String[] haystack = {"foo", "bar", "baz"};
        assertTrue(StringUtil.in("bar", haystack));
        assertFalse(StringUtil.in("qux", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"bar", "baz", "foo"};
        assertTrue(StringUtil.inSorted("bar", haystack));
        assertFalse(StringUtil.inSorted("qux", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "/foo";
        URL resolved = StringUtil.resolve(base, relUrl);
        assertEquals("http://example.com/foo", resolved.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "http://example.com";
        String relUrl = "/foo";
        String resolved = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/foo", resolved);
    }
}
