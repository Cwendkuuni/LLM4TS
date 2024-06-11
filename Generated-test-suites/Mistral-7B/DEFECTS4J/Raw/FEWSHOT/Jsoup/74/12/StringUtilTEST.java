package org.jsoup.helper;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo,bar,baz", StringUtil.join(strings, ","));

        Iterator<String> iterator = Arrays.asList("foo", "bar", "baz").iterator();
        assertEquals("foo,bar,baz", StringUtil.join(iterator, ","));

        String[] stringArray = new String[]{"foo", "bar", "baz"};
        assertEquals("foo,bar,baz", StringUtil.join(stringArray, ","));
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < StringUtil.padding.length; i++) {
            String paddingString = StringUtil.padding(i);
            assertEquals(i + " ", paddingString);
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank(" "));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank("  foo  "));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric("123foo"));
        assertFalse(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("123"));
        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("123456"));
        assertTrue(StringUtil.isNumeric("1234567"));
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
        assertEquals("", StringUtil.normaliseWhitespace(null));
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("foo", StringUtil.normaliseWhitespace("foo"));
        assertEquals(" foo ", StringUtil.normaliseWhitespace(" foo "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz"));
        assertEquals("foo bar\tbaz", StringUtil.normaliseWhitespace("foo bar\tbaz"));
        assertEquals("foo bar\n baz", StringUtil.normaliseWhitespace("foo bar\n baz"));
        assertEquals("foo bar\r baz", StringUtil.normaliseWhitespace("foo bar\r baz"));
        assertEquals("foo bar\r\nbaz", StringUtil.normaliseWhitespace("foo bar\r\nbaz"));
        assertEquals(" foo bar baz ", StringUtil.normaliseWhitespace(" foo bar baz "));
        assertEquals(" foo bar baz ", StringUtil.normaliseWhitespace(" foo \r\nbar \t baz "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz\r\n"));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("foo bar baz\n\r"));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("needle", new String[]{"haystack1", "needle", "haystack2"}));
        assertFalse(StringUtil.in("needle", new String[]{"haystack1", "haystack2"}));
        assertFalse(StringUtil.in(null, new String[]{"haystack1", "haystack2"}));
        assertFalse(StringUtil.in("", new String[]{"haystack1", "haystack2"}));
        assertFalse(StringUtil.in("haystack1", new String[]{"haystack1", "haystack2"}));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("needle", new String[]{"needle", "haystack1", "haystack2"}));
        assertFalse(StringUtil.inSorted("haystack1", new String[]{"needle", "haystack1", "haystack2"}));
        assertFalse(StringUtil.inSorted(null, new String[]{"haystack1", "haystack2"}));
        assertFalse(StringUtil.inSorted("", new String[]{"haystack1", "haystack2"}));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String base = "http://example.com/path/to/file.html";
        String relUrl1 = "rel1.html";
        String relUrl2 = "../rel2.html";
        String relUrl3 = "./rel3.html";
        String relUrl4 = "http://example.com/rel4.html";

        URL url = new URL(base);
        assertEquals("http://example.com/path/to/file.html/rel1.html", StringUtil.resolve(url, relUrl1).toExternalForm());
        assertEquals("http://example.com/path/to/rel2.html", StringUtil.resolve(url, relUrl2).toExternalForm());
        assertEquals("http://example.com/path/to/file.html/rel3.html", StringUtil.resolve(url, relUrl3).toExternalForm());
        assertEquals("http://example.com/rel4.html", StringUtil.resolve(url, relUrl4).toExternalForm());

        assertEquals("http://example.com/rel4.html", StringUtil.resolve(base, relUrl4).trim());
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        StringBuilder sb3 = StringUtil.stringBuilder();

        assertNotNull(sb1);
        assertEquals(0, sb1.length());
        assertEquals(0, sb1.capacity());
        assertEquals(MaxCachedBuilderSize, sb1.capacity());

        sb1.append("foo");
        assertEquals("foo", sb1.toString());
        assertEquals(4, sb1.length());
        assertEquals(4, sb1.capacity());

        sb2.append("bar");
        sb2.append(sb1);
        assertEquals("barfoo", sb2.toString());
        assertEquals(7, sb2.length());
        assertEquals(MaxCachedBuilderSize, sb2.capacity());

        sb3.append("baz");
        sb3.append(sb1);
        sb3.append(sb2);
        assertEquals("bazfoobarfoo", sb3.toString());
        assertEquals(11, sb3.length());
        assertEquals(MaxCachedBuilderSize, sb3.capacity());

        // test that we don't grab more than one in the same stack
        StringBuilder sb4 = StringUtil.stringBuilder();
        StringBuilder sb5 = StringUtil.stringBuilder();
        assertEquals(sb1, sb4); // should be different instances
    }
}