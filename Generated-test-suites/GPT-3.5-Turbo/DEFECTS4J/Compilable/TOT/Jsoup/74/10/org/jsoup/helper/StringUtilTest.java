package org.jsoup.helper;
import org.jsoup.helper.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String sep = "-";
        String expectedResult = "Hello-World-!";
        
        String result = StringUtil.join(strings, sep);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String sep = "+";
        String expectedResult = "Hello+World+!";
        
        String result = StringUtil.join(strings.iterator(), sep);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testJoinStringArray() {
        String[] strings = {"Hello", "World", "!"};
        String sep = "*";
        String expectedResult = "Hello*World*!";
        
        String result = StringUtil.join(strings, sep);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testPadding() {
        int width = 5;
        String expectedResult = "     ";
        
        String result = StringUtil.padding(width);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsBlank() {
        String blankString = "";
        String nonBlankString = "Hello";
        
        boolean isBlankResult = StringUtil.isBlank(blankString);
        boolean isNonBlankResult = StringUtil.isBlank(nonBlankString);
        
        Assert.assertTrue(isBlankResult);
        Assert.assertFalse(isNonBlankResult);
    }
    
    @Test
    public void testIsNumeric() {
        String numericString = "12345";
        String nonNumericString = "12a45";
        
        boolean isNumericResult = StringUtil.isNumeric(numericString);
        boolean isNonNumericResult = StringUtil.isNumeric(nonNumericString);
        
        Assert.assertTrue(isNumericResult);
        Assert.assertFalse(isNonNumericResult);
    }
    
    @Test
    public void testIsWhitespace() {
        int whitespaceCodePoint = ' ';
        int nonWhitespaceCodePoint = 'a';
        
        boolean isWhitespaceResult = StringUtil.isWhitespace(whitespaceCodePoint);
        boolean isNonWhitespaceResult = StringUtil.isWhitespace(nonWhitespaceCodePoint);
        
        Assert.assertTrue(isWhitespaceResult);
        Assert.assertFalse(isNonWhitespaceResult);
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        int whitespaceCodePoint = ' ';
        int nonWhitespaceCodePoint = 'a';
        
        boolean isWhitespaceResult = StringUtil.isActuallyWhitespace(whitespaceCodePoint);
        boolean isNonWhitespaceResult = StringUtil.isActuallyWhitespace(nonWhitespaceCodePoint);
        
        Assert.assertTrue(isWhitespaceResult);
        Assert.assertFalse(isNonWhitespaceResult);
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String input = " Hello   World  !  ";
        String expectedResult = "Hello World !";
        
        String result = StringUtil.normaliseWhitespace(input);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "/path";
        URL expectedResult = new URL("http://www.example.com/path");
        
        URL result = StringUtil.resolve(base, relUrl);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testResolveString() {
        String baseUrl = "http://www.example.com";
        String relUrl = "/path";
        String expectedResult = "http://www.example.com/path";
        
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIn() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World", "!"};
        
        boolean isInResult = StringUtil.in(needle, haystack);
        
        Assert.assertTrue(isInResult);
    }
    
    @Test
    public void testInSorted() {
        String needle = "Hello";
        String[] haystack = {"!", "Hello", "World"};
        
        boolean isInResult = StringUtil.inSorted(needle, haystack);
        
        Assert.assertTrue(isInResult);
    }
}