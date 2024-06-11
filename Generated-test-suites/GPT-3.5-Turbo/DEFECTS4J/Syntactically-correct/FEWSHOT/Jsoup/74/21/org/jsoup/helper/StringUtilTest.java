package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class StringUtilTest {
    @Test
    public void testJoin_collection() {
        Collection<String> collection = Arrays.asList("one", "two", "three");
        
        String result = StringUtil.join(collection, ",");
        
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoin_iterator() {
        Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();
        
        String result = StringUtil.join(iterator, ",");
        
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoin_array() {
        String[] array = {"one", "two", "three"};
        
        String result = StringUtil.join(array, ",");
        
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        
        assertEquals("     ", result);
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertFalse(StringUtil.isBlank("foo"));
    }
    
    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric(" "));
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("foo"));
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
        String result = StringUtil.normaliseWhitespace("  foo  bar  ");
        
        assertEquals(" foo bar ", result);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  foo  bar  ", true);
        
        assertEquals("foo bar ", sb.toString());
    }
    
    @Test
    public void testIn() {
        String[] haystack = {"one", "two", "three"};
        
        assertTrue(StringUtil.in("one", haystack));
        assertFalse(StringUtil.in("four", haystack));
    }
    
    @Test
    public void testInSorted() {
        String[] haystack = {"one", "three", "two"};
        Arrays.sort(haystack);
        
        assertTrue(StringUtil.inSorted("one", haystack));
        assertFalse(StringUtil.inSorted("four", haystack));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com");
        
        URL result = StringUtil.resolve(base, "bar");
        
        assertEquals("http://example.com/bar", result.toExternalForm());
    }
    
    @Test
    public void testResolveString() {
        String baseUrl = "http://example.com";
        
        String result = StringUtil.resolve(baseUrl, "bar");
        
        assertEquals("http://example.com/bar", result);
    }
}