package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        assertEquals("a, b, c", StringUtil.join(Arrays.asList("a", "b", "c"), ", "));
        assertEquals("", StringUtil.join(Arrays.asList(), ", "));
        assertEquals("a", StringUtil.join(Arrays.asList("a"), ", "));
    }
    
    @Test
    public void testJoinIterator() {
        assertEquals("a, b, c", StringUtil.join(Arrays.asList("a", "b", "c").iterator(), ", "));
        assertEquals("", StringUtil.join(Arrays.asList().iterator(), ", "));
        assertEquals("a", StringUtil.join(Arrays.asList("a").iterator(), ", "));
    }
    
    @Test
    public void testJoinStringArray() {
        assertEquals("a, b, c", StringUtil.join(new String[]{"a", "b", "c"}, ", "));
        assertEquals("", StringUtil.join(new String[]{}, ", "));
        assertEquals("a", StringUtil.join(new String[]{"a"}, ", "));
    }
    
    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("  ", StringUtil.padding(2));
        assertEquals("        ", StringUtil.padding(8));
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\t"));
        assertTrue(StringUtil.isBlank("\n"));
        assertTrue(StringUtil.isBlank("\r"));
        assertFalse(StringUtil.isBlank("a"));
        assertFalse(StringUtil.isBlank(" a "));
    }
    
    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("a"));
        assertTrue(StringUtil.isNumeric("0"));
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
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        assertEquals("a b c", StringUtil.normaliseWhitespace("a   b \n c"));
        assertEquals("a b c", StringUtil.normaliseWhitespace("a \r b  c"));
        assertEquals("a b c", StringUtil.normaliseWhitespace("a \t b \f c"));
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "a   b \n c", true);
        assertEquals("a b c", sb.toString());
        
        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "a \t b \f c", false);
        assertEquals("a b c", sb.toString());
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("a", "a", "b", "c"));
        assertTrue(StringUtil.in("b", "a", "b", "c"));
        assertTrue(StringUtil.in("c", "a", "b", "c"));
        assertFalse(StringUtil.in("d", "a", "b", "c"));
        assertFalse(StringUtil.in("a", "b", "c"));
    }
    
    @Test
    public void testInSorted() {
        String[] array = {"a", "b", "c"};
        assertTrue(StringUtil.inSorted("a", array));
        assertTrue(StringUtil.inSorted("b", array));
        assertTrue(StringUtil.inSorted("c", array));
        assertFalse(StringUtil.inSorted("d", array));
    }
    
    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://www.example.com/path/");
        assertEquals("http://www.example.com/path/file.html", StringUtil.resolve(base, "file.html").toString());
        assertEquals("http://www.example.com/file.html", StringUtil.resolve(base, "/file.html").toString());
        assertEquals("http://www.example.com/?foo", StringUtil.resolve(base, "?foo").toString());
        assertEquals("http://www.example.com/path/?foo", StringUtil.resolve(base, "./?foo").toString());
    }
    
    @Test
    public void testResolveString() {
        assertEquals("http://www.example.com/path/file.html", StringUtil.resolve("http://www.example.com/path/", "file.html"));
        assertEquals("http://www.example.com/file.html", StringUtil.resolve("http://www.example.com/path/", "/file.html"));
        assertEquals("http://www.example.com/?foo", StringUtil.resolve("http://www.example.com/path/", "?foo"));
        assertEquals("http://www.example.com/path/?foo", StringUtil.resolve("http://www.example.com/path/", "./?foo"));
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        
        assertEquals("", sb1.toString());
        assertEquals("", sb2.toString());
        assertEquals(sb1, sb2);
    }
}