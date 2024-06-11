package org.jsoup.helper;
import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("one", "two", "three");
        String sep = "-";
        String expected = "one-two-three";
        String result = StringUtil.join(strings, sep);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = strings.iterator();
        String sep = "-";
        String expected = "one-two-three";
        String result = StringUtil.join(iterator, sep);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"one", "two", "three"};
        String sep = "-";
        String expected = "one-two-three";
        String result = StringUtil.join(strings, sep);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        String result = StringUtil.padding(width);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsBlankNull() {
        String string = null;
        boolean expected = true;
        boolean result = StringUtil.isBlank(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsBlankEmpty() {
        String string = "";
        boolean expected = true;
        boolean result = StringUtil.isBlank(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsBlankWhitespace() {
        String string = "     ";
        boolean expected = true;
        boolean result = StringUtil.isBlank(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsBlankNotWhitespace() {
        String string = "not blank";
        boolean expected = false;
        boolean result = StringUtil.isBlank(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsNumericNull() {
        String string = null;
        boolean expected = false;
        boolean result = StringUtil.isNumeric(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsNumericEmpty() {
        String string = "";
        boolean expected = false;
        boolean result = StringUtil.isNumeric(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsNumericNumeric() {
        String string = "12345";
        boolean expected = true;
        boolean result = StringUtil.isNumeric(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsNumericNotNumeric() {
        String string = "abc";
        boolean expected = false;
        boolean result = StringUtil.isNumeric(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWhitespace() {
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isWhitespace(c);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsNotWhitespace() {
        int c = 'a';
        boolean expected = false;
        boolean result = StringUtil.isWhitespace(c);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsActuallyNotWhitespace() {
        int c = 'a';
        boolean expected = false;
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String string = "  Hello\tworld!\n How    are \tyou?  ";
        String expected = " Hello world! How are you? ";
        String result = StringUtil.normaliseWhitespace(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "  Hello\tworld!\n How    are \tyou?  ";
        String expected = " Hello world! How are you? ";
        StringUtil.appendNormalisedWhitespace(accum, string, false);
        String result = accum.toString();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testInReturnTrue() {
        String needle = "two";
        String[] haystack = {"one", "two", "three"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testInReturnFalse() {
        String needle = "four";
        String[] haystack = {"one", "two", "three"};
        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testInSortedReturnTrue() {
        String needle = "two";
        String[] haystack = {"one", "two", "three"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testInSortedReturnFalse() {
        String needle = "four";
        String[] haystack = {"one", "two", "three"};
        boolean expected = false;
        boolean result = StringUtil.inSorted(needle, haystack);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "/page";
        URL expected = new URL("https://www.example.com/page");
        URL result = StringUtil.resolve(base, relUrl);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testResolveInvalidURL() {
        String baseUrl = "https://www.example.com";
        String relUrl = "##";
        String expected = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testStringBuilder() {
        int MaxCachedBuilderSize = 8 * 1024;
        StringBuilder expected = new StringBuilder(MaxCachedBuilderSize);
        StringBuilder result = StringUtil.stringBuilder();
        
        assertEquals(expected.capacity(), result.capacity());
    }
}