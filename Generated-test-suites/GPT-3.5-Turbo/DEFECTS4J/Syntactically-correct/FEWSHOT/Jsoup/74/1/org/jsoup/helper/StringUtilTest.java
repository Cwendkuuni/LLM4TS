
package org.jsoup.helper;

import static org.junit.Assert.*;

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
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        
        String result = StringUtil.join(strings, ",");
        
        assertEquals("Hello,World", result);
    }

    @Test
    public void testJoinIteratorString() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        
        Iterator<String> iterator = strings.iterator();
        
        String result = StringUtil.join(iterator, ",");
        
        assertEquals("Hello,World", result);
    }

    @Test
    public void testJoinStringArrayString() {
        String[] strings = {"Hello", "World"};
        
        String result = StringUtil.join(strings, ",");
        
        assertEquals("Hello,World", result);
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
        assertTrue(StringUtil.isBlank("\r\n"));
        assertTrue(StringUtil.isBlank("\t"));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("Hello"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('X'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('X'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello   World");
        
        assertEquals("Hello World", result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "Hello   World", false);
        
        assertEquals("Hello World", sb.toString());
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("Hello", "Hello", "World"));
        assertFalse(StringUtil.in("Foo", "Hello", "World"));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("Hello", new String[]{"Hello", "World"}));
        assertFalse(StringUtil.inSorted("Foo", new String[]{"Hello", "World"}));
    }

    @Test
    public void testResolveURLString() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "/subpage";
        
        String result = StringUtil.resolve(base, relUrl).toString();
        
        assertEquals("https://www.example.com/subpage", result);
    }

    @Test
    public void testResolveStringString() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/subpage";
        
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals("https://www.example.com/subpage", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }

}

