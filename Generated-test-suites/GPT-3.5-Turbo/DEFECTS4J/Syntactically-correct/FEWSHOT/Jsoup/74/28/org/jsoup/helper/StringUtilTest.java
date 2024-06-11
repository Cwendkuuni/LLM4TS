
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

import org.junit.Test;

public class StringUtilTest {
    @Test
    public void testJoin() {
        List<String> strings = new ArrayList<>();
        strings.add("one");
        strings.add("two");
        strings.add("three");
        
        assertEquals("one,two,three", StringUtil.join(strings, ","));
        
        Iterator<String> iterator = strings.iterator();
        
        assertEquals("one", StringUtil.join(iterator, ","));
        
        String[] array = {"one", "two", "three"};
        
        assertEquals("one,two,three", StringUtil.join(array, ","));
    }
    
    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("          ", StringUtil.padding(10));
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("  a "));
    }
    
    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("abc"));
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
        assertEquals("hello world", StringUtil.normaliseWhitespace("hello       world"));
        assertEquals("hello world", StringUtil.normaliseWhitespace("hello \n\r\t\fworld"));
        assertEquals(" hello world ", StringUtil.normaliseWhitespace("  \t\n \r\f hello    \n\r\t\f world    \n\r\t\f"));
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("one", "one", "two", "three"));
        assertFalse(StringUtil.in("four", "one", "two", "three"));
    }
    
    @Test
    public void testInSorted() {
        String[] sortedArray = {"one", "three", "two"};
        assertTrue(StringUtil.inSorted("one", sortedArray));
        assertFalse(StringUtil.inSorted("four", sortedArray));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com/");
        assertEquals("http://www.example.com/foo", StringUtil.resolve(base, "foo").toString());
        
        String baseUrl = "http://www.example.com/";
        assertEquals("http://www.example.com/foo", StringUtil.resolve(baseUrl, "foo"));
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertEquals("", sb.toString());
        
        sb.append("hello");
        assertEquals("hello", sb.toString());
    }
}

