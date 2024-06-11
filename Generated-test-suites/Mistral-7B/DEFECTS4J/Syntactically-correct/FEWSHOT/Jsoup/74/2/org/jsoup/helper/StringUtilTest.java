package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo, bar, baz", StringUtil.join(strings, ", "));
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"foo", "bar", "baz"};
        Iterator<String> it = Arrays.asList(strings).iterator();
        assertEquals("foo, bar, baz", StringUtil.join(it, ", "));
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"foo", "bar", "baz"};
        assertEquals("foo, bar, baz", StringUtil.join(strings, ", "));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < 22; i++) {
            assertEquals(padding(i), StringUtil.padding(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("  "));
        assertTrue(StringUtil.isBlank(" \r\n"));
        assertTrue(StringUtil.isBlank(" \t"));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("foo "));
        assertFalse(StringUtil.isBlank("foo\r\n"));
        assertFalse(StringUtil.isBlank("foo\t"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("1a"));
        assertFalse(StringUtil.isNumeric(" 123 "));
        assertFalse(StringUtil.isNumeric("123 "));
        assertFalse(StringUtil.isNumeric("123\t"));
        assertFalse(StringUtil.isNumeric("123\r\n"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("1234"));
        assertTrue(StringUtil.isNumeric("12345"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('\u00A0')); // non-breaking space
        assertFalse(StringUtil.isWhitespace('A'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\u00A0')); // non-breaking space
        assertFalse(StringUtil.isActuallyWhitespace('A'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo   bar    baz    "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\n bar\r baz   "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\t bar\f baz   "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\u00A0 bar\u00A0 baz   "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo\u00A0\u00A0 bar\u00A0\u00A0 baz   "));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", new String[]{"foo", "bar", "baz"}));
        assertFalse(StringUtil.in("bar", new String[]{"foo", "bar", "baz"}));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("foo", new String[]{"foo", "bar", "baz"}));
        assertFalse(StringUtil.inSorted("bar", new String[]{"foo", "bar", "baz"}));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com/");
        URL rel = new URL("path/to/file.html");
        assertEquals("http://example.com/path/to/file.html", StringUtil.resolve(base, rel.toExternalForm()));
        URL base2 = new URL("http://example.com/path/to/file.html");
        assertEquals("http://example.com/path/to/file.html", StringUtil.resolve(base2, rel.toExternalForm()));
        URL base3 = new URL("http://example.com");
        assertEquals("http://example.com/path/to/file.html", StringUtil.resolve(base3, rel.toExternalForm()));
        URL base4 = new URL("http://example.com/path/to/");
        assertEquals("http://example.com/path/to/file.html", StringUtil.resolve(base4, rel.toExternalForm()));
        assertEquals("http://example.com/path/to/file.html", StringUtil.resolve(base4, "./file.html".toExternalForm()));
        assertEquals("http://example.com/path/to/file.html", StringUtil.resolve(base4, "../file.html".toExternalForm()));
        assertEquals("http://example.com/file.html", StringUtil.resolve(base4, "../path/to/file.html".toExternalForm()));
        URL base5 = new URL("http://example.com/file.html");
        assertEquals("", StringUtil.resolve(base5, "../path/to/file.html".toExternalForm()));
    }

    @Test
    public void testResolveEmptyString() {
        assertEquals("", StringUtil.resolve(null, ""));
        assertEquals("", StringUtil.resolve(null, " "));
        assertEquals("", StringUtil.resolve(null, "foo"));
        assertEquals("", StringUtil.resolve("", ""));
        assertEquals("", StringUtil.resolve("", " "));
        assertEquals("", StringUtil.resolve("", "foo"));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("foo");
        assertEquals("foo", sb.toString());

        sb.setLength(0);
        assertEquals("", sb.toString());

        sb.append("foo");
        sb.append(" bar");
        assertEquals("foobar", sb.toString());

        sb.setLength(0);
        assertEquals("", sb.toString());
    }

}