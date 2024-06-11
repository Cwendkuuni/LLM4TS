package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoinCollectionString() {
        Collection<String> strings = new ArrayList<>();
        assertEquals("", StringUtil.join(strings, ""));
        
        strings.add("foo");
        assertEquals("foo", StringUtil.join(strings, ""));
        
        strings.add("bar");
        assertEquals("foobar", StringUtil.join(strings, ""));
        
        strings.add("baz");
        assertEquals("foo,bar,baz", StringUtil.join(strings, ","));
    }
    
    @Test
    public void testJoinIteratorString() {
        List<String> strings = Arrays.asList("foo", "bar", "baz");
        Iterator<String> iterator = strings.iterator();
        
        assertEquals("foobarbaz", StringUtil.join(iterator, ""));
    }
    
    @Test
    public void testJoinStringArrayString() {
        String[] strings = new String[]{"foo", "bar", "baz"};
        assertEquals("foobarbaz", StringUtil.join(strings, ""));
        
        assertEquals("foo,bar,baz", StringUtil.join(strings, ","));
    }
    
    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        
        for (int i = 0; i < StringUtil.padding.length; i++) {
            assertEquals(StringUtil.padding[i], StringUtil.padding(i));
        }
        
        assertEquals("          ", StringUtil.padding(10));
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertFalse(StringUtil.isBlank("foo"));
        assertFalse(StringUtil.isBlank(" bar "));
    }
    
    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric(" "));
        assertFalse(StringUtil.isNumeric("foo"));
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
        assertFalse(StringUtil.isWhitespace('0'));
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
        assertFalse(StringUtil.isActuallyWhitespace('0'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("", StringUtil.normaliseWhitespace("     "));
        assertEquals("foo bar baz", StringUtil.normaliseWhitespace("  foo   bar  baz  "));
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        
        StringUtil.appendNormalisedWhitespace(sb, "", true);
        assertEquals("", sb.toString());
        
        sb.setLength(0);
        StringUtil.appendNormalisedWhitespace(sb, "  foo   bar  baz  ", true);
        assertEquals("foo bar baz", sb.toString());
        
        sb.setLength(0);
        StringUtil.appendNormalisedWhitespace(sb, "  foo   bar  baz  ", false);
        assertEquals("  foo bar baz  ", sb.toString());
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", "foo"));
        assertTrue(StringUtil.in("bar", "foo", "bar", "baz"));
        assertFalse(StringUtil.in("qux", "foo", "bar", "baz"));
        assertFalse(StringUtil.in(null, "foo", "bar", "baz"));
    }
    
    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("foo", new String[]{"baz", "bar", "foo"}));
        assertFalse(StringUtil.inSorted("qux", new String[]{"baz", "bar", "foo"}));
    }
    
    @Test
    public void testResolveURLString() throws MalformedURLException {
        URL baseUrl = new URL("http://www.example.com/");
        assertEquals("http://www.example.com/foo", StringUtil.resolve(baseUrl, "foo").toString());
        
        assertEquals("http://www.example.com/foo?bar", StringUtil.resolve(baseUrl, "?bar").toString());
        
        URL base = new URL("http://www.example.com/path/");
        assertEquals("http://www.example.com/./foo", StringUtil.resolve(base, "./foo").toString());
        
        assertEquals("http://www.example.com/foo", StringUtil.resolve("", "http://www.example.com/foo"));
    }
    
    @Test
    public void testResolveStringString() {
        assertEquals("", StringUtil.resolve("", ""));
        assertEquals("http://www.example.com/foo", StringUtil.resolve("http://www.example.com/", "foo"));
        
        assertEquals("", StringUtil.resolve("", "http://www.example.com/foo"));
        assertEquals("", StringUtil.resolve("http://www.example.com/", ""));
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        
        assertEquals("", sb1.toString());
        assertEquals("", sb2.toString());
        
        sb1.append("foo");
        sb2.append("bar");
        
        assertEquals("foo", sb1.toString());
        assertEquals("bar", sb2.toString());
    }
}