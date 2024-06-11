package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        Collection<String> collection = Arrays.asList("Hello", "World");
        assertEquals("Hello, World", StringUtil.join(collection, ", "));
        
        Collection<String> emptyCollection = new ArrayList<>();
        assertEquals("", StringUtil.join(emptyCollection, ", "));
    }
    
    @Test
    public void testJoinIterator() {
        Collection<String> collection = Arrays.asList("Hello", "World");
        Iterator<String> iterator = collection.iterator();
        assertEquals("Hello, World", StringUtil.join(iterator, ", "));
        
        Collection<String> emptyCollection = new ArrayList<>();
        Iterator<String> emptyIterator = emptyCollection.iterator();
        assertEquals("", StringUtil.join(emptyIterator, ", "));
    }
    
    @Test
    public void testJoinArray() {
        String[] array = {"Hello", "World"};
        assertEquals("Hello, World", StringUtil.join(array, ", "));
        
        String[] emptyArray = {};
        assertEquals("", StringUtil.join(emptyArray, ", "));
    }
    
    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("        ", StringUtil.padding(8));
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
        assertFalse(StringUtil.isBlank(" World "));
    }
    
    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("abc"));
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("12.3"));
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
        assertEquals("Hello World", StringUtil.normaliseWhitespace("Hello    World"));
        assertEquals("Hello World", StringUtil.normaliseWhitespace("Hello\tWorld"));
        assertEquals("Hello World", StringUtil.normaliseWhitespace("Hello\nWorld"));
        assertEquals("Hello World", StringUtil.normaliseWhitespace("Hello\rWorld"));
        assertEquals("Hello World", StringUtil.normaliseWhitespace("Hello\fWorld"));
        assertEquals("Hello World", StringUtil.normaliseWhitespace("Hello\u00A0World"));
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("Hello", "Hello", "World"));
        assertTrue(StringUtil.in("World", "Hello", "World"));
        assertFalse(StringUtil.in("Foo", "Hello", "World"));
        assertFalse(StringUtil.in("Hello", "World"));
    }
    
    @Test
    public void testInSorted() {
        String[] array = {"Hello", "World"};
        assertTrue(StringUtil.inSorted("Hello", array));
        assertTrue(StringUtil.inSorted("World", array));
        assertFalse(StringUtil.inSorted("Foo", array));
    }
    
    @Test
    public void testResolveUrl() throws MalformedURLException {
        URL base = new URL("http://www.example.com/folder/");
        assertEquals("http://www.example.com/folder/file.html", StringUtil.resolve(base, "file.html").toExternalForm());
        
        assertEquals("", StringUtil.resolve("http://www.example.com/", null));
        assertEquals("", StringUtil.resolve("http://www.example.com/folder/", ""));
        
        assertEquals("http://www.example.com/file.html", StringUtil.resolve("http://www.example.com/", "/file.html"));
    }
    
    @Test
    public void testResolveString() {
        assertEquals("http://www.example.com/folder/file.html", StringUtil.resolve("http://www.example.com/folder/", "file.html"));
        
        assertEquals("", StringUtil.resolve("http://www.example.com/", null));
        assertEquals("", StringUtil.resolve("http://www.example.com/folder/", ""));
        
        assertEquals("http://www.example.com/file.html", StringUtil.resolve("http://www.example.com/", "/file.html"));
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertEquals(0, sb.length());
        
        sb.append("Hello");
        assertEquals(5, sb.length());
    }
}