
package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void join_CollectionAndString_JoinedString() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";
        
        String result = StringUtil.join(strings, sep);
        
        Assert.assertEquals("Hello-World", result);
    }

    @Test
    public void join_IteratorAndString_JoinedString() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        Iterator<String> iterator = strings.iterator();
        String sep = "-";
        
        String result = StringUtil.join(iterator, sep);
        
        Assert.assertEquals("Hello-World", result);
    }

    @Test
    public void join_StringArrayAndString_JoinedString() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        
        String result = StringUtil.join(strings, sep);
        
        Assert.assertEquals("Hello-World", result);
    }

    @Test
    public void padding_PositiveWidth_PaddedString() {
        int width = 5;
        
        String result = StringUtil.padding(width);
        
        Assert.assertEquals("     ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void padding_NegativeWidth_IllegalArgumentException() {
        int width = -1;
        
        StringUtil.padding(width);
    }

    @Test
    public void isBlank_NullString_True() {
        String string = null;
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isBlank_EmptyString_True() {
        String string = "";
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isBlank_WhitespaceString_True() {
        String string = "   ";
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isBlank_NonBlankString_False() {
        String string = "Hello World";
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void isNumeric_NullString_False() {
        String string = null;
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void isNumeric_EmptyString_False() {
        String string = "";
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void isNumeric_NonNumericString_False() {
        String string = "Hello123";
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void isNumeric_NumericString_True() {
        String string = "123456";
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isWhitespace_WhitespaceCodePoint_True() {
        int c = ' ';
        
        boolean result = StringUtil.isWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isWhitespace_NonWhitespaceCodePoint_False() {
        int c = 'A';
        
        boolean result = StringUtil.isWhitespace(c);
        
        Assert.assertFalse(result);
    }

    @Test
    public void isActuallyWhitespace_WhitespaceCodePoint_True() {
        int c = ' ';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isActuallyWhitespace_NonWhitespaceCodePoint_False() {
        int c = 'A';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertFalse(result);
    }

    @Test
    public void normaliseWhitespace_UnnormalisedString_NormalisedString() {
        String string = "Hello   World";

        String result = StringUtil.normaliseWhitespace(string);

        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void appendNormalisedWhitespace_UnnormalisedStringNoLeadingWhitespaces_StrippedLeadingWhitespaces() {
        StringBuilder accum = new StringBuilder();
        String string = " Hello World";

        StringUtil.appendNormalisedWhitespace(accum, string, true);

        Assert.assertEquals("Hello World", accum.toString());
    }

    @Test
    public void appendNormalisedWhitespace_UnnormalisedStringWithLeadingWhitespaces_NoStrippedLeadingWhitespaces() {
        StringBuilder accum = new StringBuilder();
        String string = " Hello World";

        StringUtil.appendNormalisedWhitespace(accum, string, false);

        Assert.assertEquals(" Hello World", accum.toString());
    }

    @Test
    public void in_StringInHaystack_True() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};

        boolean result = StringUtil.in(needle, haystack);

        Assert.assertTrue(result);
    }

    @Test
    public void in_StringNotInHaystack_False() {
        String needle = "Foo";
        String[] haystack = {"Hello", "World"};

        boolean result = StringUtil.in(needle, haystack);

        Assert.assertFalse(result);
    }

    @Test
    public void inSorted_SortedStringInHaystack_True() {
        String needle = "Hello";
        String[] haystack = {"Apple", "Hello", "World"};

        boolean result = StringUtil.inSorted(needle, haystack);

        Assert.assertTrue(result);
    }

    @Test
    public void inSorted_SortedStringNotInHaystack_False() {
        String needle = "Foo";
        String[] haystack = {"Apple", "Hello", "World"};

        boolean result = StringUtil.inSorted(needle, haystack);

        Assert.assertFalse(result);
    }

    @Test
    public void resolve_BaseUrlAndRelUrl_RelativeUrlResolvedToAbsoluteUrl() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "/foo/bar";

        URL result = StringUtil.resolve(base, relUrl);

        URL expected = new URL("http://example.com/foo/bar");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void resolve_BaseUrlAndRelUrl_RelativeUrlIsAlreadyAbsoluteUrl_ReturnsRelUrl() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "http://example2.com/foo/bar";

        URL result = StringUtil.resolve(base, relUrl);

        URL expected = new URL(relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void resolve_BaseUrlAndRelUrl_RelativeUrlSecuredToAbsoluteUrl() throws MalformedURLException {
        URL base = new URL("https://example.com");
        String relUrl = "//example.com/foo/bar";

        URL result = StringUtil.resolve(base, relUrl);

        URL expected = new URL("https://example.com/foo/bar");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void resolve_BaseUrlAndRelUrl_RelativeUrlSecuredWithBaseUrlStartingWithSlash_ReturnsAbsoluteUrl() throws MalformedURLException {
        URL base = new URL("https://example.com/");
        String relUrl = "./foo/bar";

        URL result = StringUtil.resolve(base, relUrl);

        URL expected = new URL("https://example.com/foo/bar");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void resolve_BaseUrlAndRelUrl_BaseUrlIsInvalid_ReturnsRelUrl() {
        String baseUrl = "invalidUrl";
        String relUrl = "http://example.com/foo/bar";

        String result = StringUtil.resolve(baseUrl, relUrl);

        String expected = relUrl;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void stringBuilder_EmptyBuilderReturned() {
        StringBuilder sb = StringUtil.stringBuilder();

        Assert.assertTrue(sb.length() == 0);
    }

}
