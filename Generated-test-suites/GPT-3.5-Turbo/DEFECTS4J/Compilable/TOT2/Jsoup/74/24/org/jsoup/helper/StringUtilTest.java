package org.jsoup.helper;
import org.junit.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");
        
        String result = StringUtil.join(strings, " ");
        
        assertEquals("Hello World !", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("Hello", "World", "!");
        Iterator<String> iterator = strings.iterator();
        
        String result = StringUtil.join(iterator, " ");
        
        assertEquals("Hello World !", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        
        String result = StringUtil.join(strings, " ");
        
        assertEquals("Hello World !", result);
    }

    @Test
    public void testPadding() {
        String paddedString = StringUtil.padding(5);
        
        assertEquals("     ", paddedString);
    }

    @Test
    public void testIsBlank() {
        boolean result = StringUtil.isBlank("     ");
        
        assertTrue(result);
    }

    @Test
    public void testIsNumeric() {
        boolean result = StringUtil.isNumeric("12345");
        
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace() {
        boolean result = StringUtil.isWhitespace('\t');
        
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        boolean result = StringUtil.isActuallyWhitespace('\t');
        
        assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalizedString = StringUtil.normaliseWhitespace("  Hello    World   ");
        
        assertEquals(" Hello World ", normalizedString);
    }

    @Test
    public void testIn() {
        boolean result = StringUtil.in("apple", "banana", "apple", "orange");
        
        assertTrue(result);
    }

    @Test
    public void testInSorted() {
        boolean result = StringUtil.inSorted("orange", new String[]{"apple", "banana", "orange"});
        
        assertTrue(result);
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL baseUrl = new URL("https://www.example.com");
        String relUrl = "/page";
        
        URL resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals("https://www.example.com/page", resolvedUrl.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com/";
        String relUrl = "page";
        
        String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals("https://www.example.com/page", resolvedUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }

}