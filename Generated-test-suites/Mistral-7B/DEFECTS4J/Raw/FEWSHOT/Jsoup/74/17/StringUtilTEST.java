package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class StringUtilTest {

    private static StringBuilder stringBuilder;

    @BeforeClass
    public static void setUp() {
        stringBuilder = StringUtil.stringBuilder();
    }

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("", "a", "b", "c");
        assertEquals("", StringUtil.join(Collections.emptyList(), ""));
        assertEquals("a", StringUtil.join(strings.iterator(), ""));
        assertEquals("a b c", StringUtil.join(strings, " "));
        assertEquals("a b c", StringUtil.join(strings.toArray(new String[0]), " "));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < 22; i++) {
            assertEquals(padding(i), StringUtil.padding(i));
        }
        try {
            StringUtil.padding(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("foobar\t\n\r\f "));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("foo "));
        assertFalse(StringUtil.isBlank("foo\t\n\r\f"));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("123 "));
        assertFalse(StringUtil.isNumeric("123\t\n\r\f"));
        assertFalse(StringUtil.isNumeric("123a"));
        assertFalse(StringUtil.isNumeric("a123"));
        assertFalse(StringUtil.isNumeric("123a"));
        assertFalse(StringUtil.isNumeric("a123"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("1234567890"));
        assertTrue(StringUtil.isNumeric("12345678901234567890"));
    }

    @Test
    public void testIsWhitespace() {
        assertEquals(true, StringUtil.isWhitespace(' '));
        assertEquals(true, StringUtil.isWhitespace('\t'));
        assertEquals(true, StringUtil.isWhitespace('\n'));
        assertEquals(true, StringUtil.isWhitespace('\r'));
        assertEquals(true, StringUtil.isWhitespace('\f'));
        assertEquals(false, StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertEquals(true, StringUtil.isActuallyWhitespace(' '));
        assertEquals(true, StringUtil.isActuallyWhitespace('\t'));
        assertEquals(true, StringUtil.isActuallyWhitespace('\n'));
        assertEquals(true, StringUtil.isActuallyWhitespace('\r'));
        assertEquals(true, StringUtil.isActuallyWhitespace('\f'));
        assertEquals(true, StringUtil.isActuallyWhitespace('\u00A0')); // non-breaking space
        assertEquals(false, StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("", StringUtil.normaliseWhitespace(" \t\n\r\f "));
        assertEquals("foo", StringUtil.normaliseWhitespace("foo"));
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo bar"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\nbaz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\n baz"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\n baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\n baz\t"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\n baz \t"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar\n baz\t "));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder builder = StringUtil.stringBuilder();
        StringUtil.appendNormalisedWhitespace(builder, "", false);
        assertEquals("", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, " ", false);
        assertEquals(" ", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "\t", false);
        assertEquals("\t", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "\n", false);
        assertEquals("\n", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "\r", false);
        assertEquals("\r", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "\f", false);
        assertEquals("\f", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "\u00A0", false);
        assertEquals("\u00A0", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "foo", false);
        assertEquals("foo", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "foo \t", false);
        assertEquals("foo ", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "foo \t\n", false);
        assertEquals("foo \n", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "foo \t\n\r", false);
        assertEquals("foo \r", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "foo \t\n\r\f", false);
        assertEquals("foo \f", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "foo \t\n\r\f\u00A0", false);
        assertEquals("foo \u00A0", builder.toString());
        StringUtil.appendNormalisedWhitespace(builder, "foo bar", false);
        assertEquals("foo bar", builder.toString());
    }

    @Test
    public void testIn() {
        assertFalse(StringUtil.in("needle", new String[0]));
        assertFalse(StringUtil.in("needle", new String[]{"apple"}));
        assertFalse(StringUtil.in("needle", new String[]{"apple", "banana"}));
        assertTrue(StringUtil.in("needle", new String[]{"apple", "needle", "banana"}));
    }

    @Test
    public void testInSorted() {
        assertFalse(StringUtil.inSorted("needle", new String[]{ "apple", "banana" }));
        assertTrue(StringUtil.inSorted("needle", new String[]{ "needle", "apple", "banana" }));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com/index.html");
        assertEquals("http://example.com/index.html/./foo", StringUtil.resolve(base, "./foo"));
        assertEquals("http://example.com/index.html/./foo/bar", StringUtil.resolve(base, "./foo/bar"));
        assertEquals("http://example.com/index.html/./", StringUtil.resolve(base, "./"));
        assertEquals("http://example.com/index.html/./", StringUtil.resolve(base, ""));
        assertEquals("http://example.com/index.html/./foo", StringUtil.resolve(base, "foo"));
        assertEquals("http://example.com/index.html/./foo/", StringUtil.resolve(base, "foo/"));
        assertEquals("http://example.com/index.html/./foo/", StringUtil.resolve(base, "/foo/"));
        assertEquals("http://example.com/index.html/./foo/bar", StringUtil.resolve(base, "/foo/bar"));
        assertEquals("http://example.com/index.html/./foo/bar/baz", StringUtil.resolve(base, "/foo/bar/baz"));
        assertEquals("http://example.com/index.html/./foo/bar/baz", StringUtil.resolve(base, "./foo/bar/baz"));
        assertEquals("http://example.com/index.html/./foo/bar/baz", StringUtil.resolve(base, "./foo/bar/baz/"));
        assertEquals("http://example.com/index.html/./foo/bar/baz", StringUtil.resolve(base, "./foo/bar/baz/."));
        assertEquals("http://example.com/index.html/./foo/bar/baz", StringUtil.resolve(base, "./foo/bar/baz/.."));
        assertEquals("http://example.com/index.html/./foo/bar/baz", StringUtil.resolve(base, "./foo/bar/baz/../"));
        assertEquals("http://example.com/index.html/./foo/bar/baz", StringUtil.resolve(base, "./foo/bar/baz/../../"));
        assertEquals("http://example.com/", StringUtil.resolve(base, "./../"));
        assertEquals("http://example.com/", StringUtil.resolve(base, "./../../"));
        assertEquals("http://example.com/index.html/./", StringUtil.resolve(base, "../"));
        assertEquals("http://example.com/index.html/./", StringUtil.resolve(base, "../../"));
        assertEquals("http://example.com/", StringUtil.resolve(new URL("http://example.com/index.html"), ""));
    }

    @Test
    public void testResolveUrls() throws MalformedURLException {
        URL base = new URL("http://example.com/index.html");
        URL base2 = new URL("http://example.com/index.html");
        URL relUrl = new URL("http://example.com/subpage.html");
        URL relUrl2 = new URL("http://example.com/subpage.html");
        URL relUrl3 = new URL("./subpage.html");
        URL relUrl4 = new URL("./subpage.html");
        URL resolvedUrl = StringUtil.resolve(base, relUrl.toExternalForm());
        assertEquals(resolvedUrl.toExternalForm(), StringUtil.resolve(base2, relUrl2.toExternalForm()));
        assertEquals(resolvedUrl.toExternalForm(), StringUtil.resolve(base, relUrl3.toExternalForm()));
        assertEquals(resolvedUrl.toExternalForm(), StringUtil.resolve(base, relUrl4.toExternalForm()));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertEquals("", sb.toString());
        sb.append("foo");
        assertEquals("foo", sb.toString());
        sb.append(" bar");
        assertEquals("foo bar", sb.toString());
        stringBuilder = StringUtil.stringBuilder();
        StringUtil.appendNormalisedWhitespace(stringBuilder, "foo bar baz quux", false);
        assertEquals("foo bar baz quux", stringBuilder.toString());
    }
}