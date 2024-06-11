package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo,bar,baz", StringUtil.join(strings, ", "));
        assertEquals("foo,bar,baz", StringUtil.join(strings.iterator(), ", "));
        assertEquals("foo,bar,baz", StringUtil.join(strings.toArray(new String[0]), ", "));

        List<String> emptyStrings = Collections.emptyList();
        assertEquals("", StringUtil.join(emptyStrings, ", "));
        assertEquals("", StringUtil.join(emptyStrings.iterator(), ", "));
        assertEquals("", StringUtil.join(new String[0], ", "));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < StringUtil.padding.length; i++) {
            assertEquals(Character.isWhitespace(StringUtil.padding[i].charAt(0)), i <= 8);
            assertEquals(StringUtil.padding(i).length(), i + 1);
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\n"));
        assertTrue(StringUtil.isBlank("\t"));
        assertTrue(StringUtil.isBlank("\r"));
        assertTrue(StringUtil.isBlank(" foo\t\n\r "));

        assertFalse(StringUtil.isBlank(" foo"));
        assertFalse(StringUtil.isBlank(" foo "));
        assertFalse(StringUtil.isBlank("foo "));
        assertFalse(StringUtil.isBlank(" foo\n"));
        assertFalse(StringUtil.isBlank(" foo\r"));
        assertFalse(StringUtil.isBlank(" foo\t"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("1a"));
        assertFalse(StringUtil.isNumeric("1_2"));
        assertFalse(StringUtil.isNumeric("10e10"));

        assertTrue(StringUtil.isNumeric("1"));
        assertTrue(StringUtil.isNumeric("12"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("1234"));
        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("123456"));
        assertTrue(StringUtil.isNumeric("1234567"));
        assertTrue(StringUtil.isNumeric("12345678"));
        assertTrue(StringUtil.isNumeric("123456789"));
        assertTrue(StringUtil.isNumeric("1234567890"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('\u0000'));
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('z'));
        assertFalse(StringUtil.isWhitespace('!'));
        assertFalse(StringUtil.isWhitespace('?'));
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
        assertFalse(StringUtil.isActuallyWhitespace('z'));
        assertFalse(StringUtil.isActuallyWhitespace('!'));
        assertFalse(StringUtil.isActuallyWhitespace('?'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("foo", StringUtil.normaliseWhitespace("foo"));
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo bar"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz"));
        assertEquals(" foo bar baz", StringUtil.normaliseWhitespace(" foo bar baz"));
        assertEquals("foo\nbar baz", StringUtil.normaliseWhitespace("foo\nbar baz"));
        assertEquals("foo\n bar baz", StringUtil.normaliseWhitespace("foo\n bar baz"));
        assertEquals("foo\r bar baz", StringUtil.normaliseWhitespace("foo\r bar baz"));
        assertEquals("foo\r\n bar baz", StringUtil.normaliseWhitespace("foo\r\n bar baz"));
        assertEquals("foo   bar\t baz", StringUtil.normaliseWhitespace("foo   bar\t baz"));
        assertEquals("foo   bar\t  baz", StringUtil.normaliseWhitespace("foo   bar\t  baz"));
        assertEquals("foo   bar\t  baz\n", StringUtil.normaliseWhitespace("foo   bar\t  baz\n"));
        assertEquals("foo   bar\t  baz\n", StringUtil.normaliseWhitespace("foo   bar\t  baz\n"));
        assertEquals(" foo   bar\t  baz\n", StringUtil.normaliseWhitespace(" foo   bar\t  baz\n"));
        assertEquals(" foo   bar\t  baz\n", StringUtil.normaliseWhitespace(" foo   bar\t  baz\n"));
        assertEquals("foo   bar\t  baz\n", StringUtil.normaliseWhitespace("foo   bar\t  baz\n"));
        assertEquals("foo   bar\t  baz\n", StringUtil.normaliseWhitespace("foo   bar\t  baz\n"));
        assertEquals("foo   bar\t  baz", StringUtil.normaliseWhitespace("foo   bar\t  baz"));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", "foo", "bar", "baz"));
        assertTrue(StringUtil.in("bar", "foo", "bar", "baz"));
        assertTrue(StringUtil.in("baz", "foo", "bar", "baz"));
        assertFalse(StringUtil.in("qux", "foo", "bar", "baz"));

        assertTrue(StringUtil.inSorted("foo", "bar", "baz"));
        assertTrue(StringUtil.inSorted("baz", "bar", "baz"));
        assertFalse(StringUtil.inSorted("qux", "bar", "baz"));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com/");
        assertEquals("http://example.com/path/file.html", StringUtil.resolve(base, "/path/file.html"));
        assertEquals("http://example.com/path/file.html", StringUtil.resolve(base, "path/file.html"));
        assertEquals("http://example.com/path/file.html", StringUtil.resolve(base, "/path/file.html?"));
        assertEquals("http://example.com/path/file.html", StringUtil.resolve(base, "path/file.html?"));
        assertEquals("http://example.com/path/file.html", StringUtil.resolve(base, "/path/file.html#fragment"));
        assertEquals("http://example.com/path/file.html#fragment", StringUtil.resolve(base, "path/file.html#fragment"));

        assertNull(StringUtil.resolve(null, ""));
        assertNull(StringUtil.resolve(null, "/"));
        assertNull(StringUtil.resolve("http://example.com", ""));
        assertNull(StringUtil.resolve("http://example.com", "/"));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("foo");
        assertEquals("foo", sb.toString());

        sb.setLength(0);
        sb.append("bar");
        assertEquals("bar", sb.toString());

        sb.setLength(0);
        sb.append("foo");
        sb.append(" bar");
        assertEquals("foo bar", sb.toString());

        sb.setLength(0);
        sb.appendCodePoint(1234);
        assertEquals("\u000Cfoo", sb.toString());

        sb.setLength(0);
        sb.appendCodePoint(1234);
        sb.appendCodePoint(5678);
        assertEquals("\u000C\u000Efoo", sb.toString());
    }
}