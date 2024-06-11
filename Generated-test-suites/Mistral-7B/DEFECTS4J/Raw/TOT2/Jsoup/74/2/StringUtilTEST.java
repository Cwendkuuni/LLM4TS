package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    // Test for join method with Collection
    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("Hello", "World", "Java");
        String expected = "HelloWorldJava";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    // Test for join method with Iterator
    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"Hello", "World", "Java"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "HelloWorldJava";
        String actual = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, actual);
    }

    // Test for join method with Array
    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"Hello", "World", "Java"};
        String expected = "HelloWorldJava";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    // Test for padding method with positive width
    @Test
    public void testPaddingPositive() {
        String expected = "   Hello";
        String actual = StringUtil.padding(4, "Hello");
        Assert.assertEquals(expected, actual);
    }

    // Test for padding method with negative width
    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegative() {
        StringUtil.padding(-1, "");
    }

    // Test for isBlank method with null string
    @Test
    public void testIsBlankNull() {
        boolean actual = StringUtil.isBlank(null);
        Assert.assertTrue(actual);
    }

    // Test for isBlank method with empty string
    @Test
    public void testIsBlankEmpty() {
        String actual = "";
        boolean expected = StringUtil.isBlank(actual);
        Assert.assertTrue(expected);
    }

    // Test for isBlank method with whitespace string
    @Test
    public void testIsBlankWhitespace() {
        String actual = "   ";
        boolean expected = StringUtil.isBlank(actual);
        Assert.assertTrue(expected);
    }

    // Test for isBlank method with non-blank string
    @Test
    public void testIsBlankNonBlank() {
        String actual = "HelloWorld";
        boolean expected = !StringUtil.isBlank(actual);
        Assert.assertTrue(expected);
    }

    // Test for isNumeric method with numeric string
    @Test
    public void testIsNumericNumeric() {
        String actual = "12345";
        boolean expected = StringUtil.isNumeric(actual);
        Assert.assertTrue(expected);
    }

    // Test for isNumeric method with non-numeric string
    @Test
    public void testIsNumericNonNumeric() {
        String actual = "abcdef";
        boolean expected = !StringUtil.isNumeric(actual);
        Assert.assertTrue(expected);
    }

    // Test for isWhitespace method with whitespace code point
    @Test
    public void testIsWhitespaceWhiteSpace() {
        boolean actual = StringUtil.isWhitespace(' ');
        Assert.assertTrue(actual);
    }

    // Test for isWhitespace method with non-whitespace code point
    @Test
    public void testIsWhitespaceNonWhiteSpace() {
        boolean actual = StringUtil.isWhitespace('A');
        Assert.assertFalse(actual);
    }

    // Test for isActuallyWhitespace method with whitespace character
    @Test
    public void testIsActuallyWhitespaceWhiteSpace() {
        boolean actual = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(actual);
    }

    // Test for isActuallyWhitespace method with non-whitespace character
    @Test
    public void testIsActuallyWhitespaceNonWhiteSpace() {
        boolean actual = StringUtil.isActuallyWhitespace('A');
        Assert.assertFalse(actual);
    }

    // Test for isActuallyWhitespace method with non-breaking space
    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        boolean actual = StringUtil.isActuallyWhitespace('\u00A0'); // non-breaking space
        Assert.assertTrue(actual);
    }

    // Test for normaliseWhitespace method with empty string
    @Test
    public void testNormaliseWhitespaceEmpty() {
        String actual = StringUtil.normaliseWhitespace("");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    // Test for normaliseWhitespace method with single whitespace character
    @Test
    public void testNormaliseWhitespaceSingleWhitespace() {
        String actual = StringUtil.normaliseWhitespace(" ");
        String expected = " ";
        Assert.assertEquals(expected, actual);
    }

    // Test for normaliseWhitespace method with multiple whitespace characters
    @Test
    public void testNormaliseWhitespaceMultipleWhitespace() {
        String actual = StringUtil.normaliseWhitespace("   ");
        String expected = " ";
        Assert.assertEquals(expected, actual);
    }

    // Test for normaliseWhitespace method with leading whitespace
    @Test
    public void testNormaliseWhitespaceLeadingWhitespace() {
        String actual = StringUtil.normaliseWhitespace("   Hello");
        String expected = "Hello";
        Assert.assertEquals(expected, actual);
    }

    // Test for in method with null needle
    @Test
    public void testInNullNeedle() {
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean actual = StringUtil.in(null, haystack);
        Assert.assertFalse(actual);
    }

    // Test for in method with empty haystack
    @Test
    public void testInEmptyHaystack() {
        String needle = "apple";
        boolean actual = StringUtil.in(needle, new String[0]);
        Assert.assertFalse(actual);
    }

    // Test for in method with needle in haystack
    @Test
    public void testInNeedleInHaystack() {
        String needle = "apple";
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertTrue(actual);
    }

    // Test for in method with needle not in haystack
    @Test
    public void testInNeedleNotInHaystack() {
        String needle = "cherry";
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertFalse(actual);
    }

    // Test for inSorted method with null needle
    @Test(expected = NullPointerException.class)
    public void testInSortedNullNeedle() {
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        StringUtil.inSorted(null, haystack);
    }

    // Test for inSorted method with empty haystack
    @Test
    public void testInSortedEmptyHaystack() {
        String needle = "apple";
        boolean actual = StringUtil.inSorted(needle, new String[0]);
        Assert.assertFalse(actual);
    }

    // Test for inSorted method with needle in sorted haystack
    @Test
    public void testInSortedNeedleInSortedHaystack() {
        String needle = "apple";
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertTrue(actual);
    }

    // Test for inSorted method with needle not in sorted haystack
    @Test
    public void testInSortedNeedleNotInSortedHaystack() {
        String needle = "cherry";
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertFalse(actual);
    }

    // Test for resolve method with base and relative URL that resolve
    @Test
    public void testResolveValid() throws MalformedURLException {
        String baseUrl = "http://example.com";
        String relUrl = "/about.html";
        String expected = "http://example.com/about.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    // Test for resolve method with base and relative URL that don't resolve
    @Test
    public void testResolveInvalid() throws MalformedURLException {
        String baseUrl = "http://example.com";
        String relUrl = "javascript:alert('Hello')";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("", actual);
    }
}