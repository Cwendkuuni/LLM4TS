package org.jsoup.helper;
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("one", "two", "three");
        assertEquals("one,two,three", StringUtil.join(strings, ","));
    }
    
    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = strings.iterator();
        assertEquals("one,two,three", StringUtil.join(iterator, ","));
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"one", "two", "three"};
        assertEquals("one,two,three", StringUtil.join(strings, ","));
    }
    
    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("          ", StringUtil.padding(10));
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\r\n"));
        assertTrue(StringUtil.isBlank("\t"));
        assertFalse(StringUtil.isBlank("  123  "));
    }
    
    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("abc"));
        assertFalse(StringUtil.isNumeric("12345a"));
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
        assertEquals("hello world", StringUtil.normaliseWhitespace("hello   world"));
        assertEquals("hello world", StringUtil.normaliseWhitespace("hello\r\nworld"));
        assertEquals(" hello world ", StringUtil.normaliseWhitespace(" hello \r\n world "));
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("a", "a", "b", "c"));
        assertFalse(StringUtil.in("d", "a", "b", "c"));
    }
    
    @Test
    public void testInSorted() {
        String[] haystack = {"a", "b", "c"};
        assertTrue(StringUtil.inSorted("b", haystack));
        assertFalse(StringUtil.inSorted("d", haystack));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com/");
        String relUrl = "/path";
        assertEquals("http://example.com/path", StringUtil.resolve(base, relUrl).toString());
    }
    
    @Test
    public void testResolveString() {
        String baseUrl = "http://example.com/";
        String relUrl = "/path";
        assertEquals("http://example.com/path", StringUtil.resolve(baseUrl, relUrl));
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        sb.append("Hello World");
        assertEquals("Hello World", sb.toString());
    }
}