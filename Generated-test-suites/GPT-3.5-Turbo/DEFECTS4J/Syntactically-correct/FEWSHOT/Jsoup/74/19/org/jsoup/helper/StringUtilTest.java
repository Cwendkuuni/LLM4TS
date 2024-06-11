package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoinCollectionString() {
        assertEquals("abc,def,ghi", StringUtil.join(Arrays.asList("abc", "def", "ghi"), ","));
        assertEquals("", StringUtil.join(Collections.emptyList(), ","));
        assertEquals("abc", StringUtil.join(Collections.singletonList("abc"), ","));
    }

    @Test
    public void testJoinIteratorString() {
        assertEquals("abc,def,ghi", StringUtil.join(Arrays.asList("abc", "def", "ghi").iterator(), ","));
        assertFalse(StringUtil.join(Collections.emptyList().iterator(), ",").isEmpty());
        assertEquals("abc", StringUtil.join(Collections.singletonList("abc").iterator(), ","));
    }

    @Test
    public void testJoinStringArrayString() {
        assertEquals("abc,def,ghi", StringUtil.join(new String[]{"abc", "def", "ghi"}, ","));
        assertEquals("", StringUtil.join(new String[]{}, ","));
        assertEquals("abc", StringUtil.join(new String[]{"abc"}, ","));
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("     ", StringUtil.padding(5));
        assertEquals("                    ", StringUtil.padding(20));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("abc"));
        assertFalse(StringUtil.isBlank("  abc  "));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("12ab34"));
        assertFalse(StringUtil.isNumeric("  "));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('1'));
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
        assertFalse(StringUtil.isActuallyWhitespace('1'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("abc def ghi", StringUtil.normaliseWhitespace(" abc  def   ghi  "));
        assertEquals("abc def ghi", StringUtil.normaliseWhitespace("abc def\nghi"));
        assertEquals("abc def ghi", StringUtil.normaliseWhitespace("  abc\ndef\r\nghi \t"));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("abc", "abc", "def", "ghi"));
        assertTrue(StringUtil.in("def", "abc", "def", "ghi"));
        assertTrue(StringUtil.in("ghi", "abc", "def", "ghi"));
        assertFalse(StringUtil.in("xyz", "abc", "def", "ghi"));
    }

    @Test
    public void testInSorted() {
        String[] haystack = new String[]{"abc", "def", "ghi"};
        assertTrue(StringUtil.inSorted("abc", haystack));
        assertTrue(StringUtil.inSorted("def", haystack));
        assertTrue(StringUtil.inSorted("ghi", haystack));
        assertFalse(StringUtil.inSorted("xyz", haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        assertEquals("http://www.example.com/rel/url", StringUtil.resolve(base, "rel/url").toExternalForm());
        assertEquals("http://www.example.com/path/file?foo", StringUtil.resolve(base, "?foo").toExternalForm());
        assertEquals("http://www.example.com/./foo", StringUtil.resolve(base, "./foo").toExternalForm());
    }

    @Test
    public void testResolveString() {
        assertEquals("http://www.example.com/rel/url", StringUtil.resolve("http://www.example.com", "rel/url"));
        assertEquals("http://www.example.com/path/file?foo", StringUtil.resolve("http://www.example.com", "?foo"));
        assertEquals("http://www.example.com/./foo", StringUtil.resolve("http://www.example.com", "./foo"));
        assertEquals("", StringUtil.resolve("abc", "def"));
    }

    @Test
    public void testStringBuilder() {
        assertTrue(StringUtil.stringBuilder() instanceof StringBuilder);
        assertTrue(StringUtil.stringBuilder().length() == 0);
    }
}