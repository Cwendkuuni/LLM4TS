
package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";
        String result = StringUtil.join(strings, sep);
        assertEquals("Hello-World", result);
    }
    
    @Test
    public void testJoinIterator() {
        Iterator<String> iterator = Arrays.asList("Hello", "World").iterator();
        String sep = "-";
        String result = StringUtil.join(iterator, sep);
        assertEquals("Hello-World", result);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        String result = StringUtil.join(strings, sep);
        assertEquals("Hello-World", result);
    }
    
    @Test
    public void testPadding() {
        int width = 5;
        String result = StringUtil.padding(width);
        assertEquals("     ", result);
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }
    
    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("12a45"));
    }
    
    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertFalse(StringUtil.isWhitespace('a'));
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello  World");
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("b", "a", "b", "c"));
        assertFalse(StringUtil.in("d", "a", "b", "c"));
    }
    
    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry"};
        assertTrue(StringUtil.inSorted("banana", haystack));
        assertFalse(StringUtil.inSorted("dragonfruit", haystack));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "/page";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(new URL("https://www.example.com/page"), result);
    }
    
    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/page";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com/page", result);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("Hello");
        assertEquals("Hello", sb.toString());
    }
}

