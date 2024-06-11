package org.jsoup.helper;

import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class StringUtilTest {
  
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("hello", "world");
        String joinedString = StringUtil.join(strings, "-");
        assertEquals("hello-world", joinedString);
    }
  
    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("hello", "world").iterator();
        String joinedString = StringUtil.join(strings, "-");
        assertEquals("hello-world", joinedString);
    }
  
    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world"};
        String joinedString = StringUtil.join(strings, "-");
        assertEquals("hello-world", joinedString);
    }
  
    @Test
    public void testPadding() {
        String padding = StringUtil.padding(5);
        assertEquals("     ", padding);
    }
  
    @Test
    public void testIsBlank() {
        assertFalse(StringUtil.isBlank("hello"));
        assertFalse(StringUtil.isBlank(" hello "));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank("         "));
    }
  
    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("1234567890"));
        assertTrue(StringUtil.isNumeric("0"));
        assertFalse(StringUtil.isNumeric("hello"));
        assertFalse(StringUtil.isNumeric("12345a67890"));
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
        String normalised = StringUtil.normaliseWhitespace("   hello   world   ");
        assertEquals(" hello world ", normalised);
    }
  
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "   hello   world   ", false);
        assertEquals(" hello world ", sb.toString());
    }
  
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("hello", "world", "hello", "foo"));
        assertFalse(StringUtil.in("bar", "world", "hello", "foo"));
    }
  
    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("hello", new String[]{"bar", "foo", "hello", "world"}));
        assertFalse(StringUtil.inSorted("bar", new String[]{"foo", "hello", "world"}));
    }
  
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String resolved = StringUtil.resolve(base, "foo/bar");
        assertEquals("http://www.example.com/foo/bar", resolved);
    }
  
    @Test
    public void testResolveString() {
        String base = "http://www.example.com";
        String resolved = StringUtil.resolve(base, "foo/bar");
        assertEquals("http://www.example.com/foo/bar", resolved);
    }
  
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");
        assertEquals("hello", sb.toString());
    }

}