package org.jsoup.helper;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorEmpty() {
        String result = StringUtil.join(Collections.emptyIterator(), "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinIteratorSingle() {
        String result = StringUtil.join(Collections.singletonList("test").iterator(), "");
        Assert.assertEquals("test", result);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = new String[0];
        String result = StringUtil.join(strings, "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinArraySingle() {
        String[] strings = new String[]{"test"};
        String result = StringUtil.join(strings, "");
        Assert.assertEquals("test", result);
    }

    @Test
    public void testJoinArrayMultiple() {
        String[] strings = new String[]{"hello", "world", "!"};
        String expected = "helloworld!";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        String result = StringUtil.padding(-1);
    }

    @Test
    public void testIsBlankNull() {
        Assert.assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlankEmpty() {
        Assert.assertTrue(StringUtil.isBlank(""));
    }

    @Test
    public void testIsBlankWhitespace() {
        String s = "   ";
        Assert.assertTrue(StringUtil.isBlank(s));
    }

    @Test
    public void testIsBlankNonBlank() {
        String s = "hello";
        Assert.assertFalse(StringUtil.isBlank(s));
    }

    @Test
    public void testIsNumericEmpty() {
        Assert.assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsNumericSingleDigit() {
        String s = "1";
        Assert.assertTrue(StringUtil.isNumeric(s));
    }

    @Test
    public void testIsNumericMultipleDigits() {
        String s = "123";
        Assert.assertTrue(StringUtil.isNumeric(s));
    }

    @Test
    public void testIsNumericNonNumeric() {
        String s = "abc";
        Assert.assertFalse(StringUtil.isNumeric(s));
    }

    @Test
    public void testIsWhitespaceSpace() {
        Assert.assertTrue(StringUtil.isWhitespace(' '));
    }

    @Test
    public void testIsWhitespaceTab() {
        Assert.assertTrue(StringUtil.isWhitespace('\t'));
    }

    @Test
    public void testIsWhitespaceNewLine() {
        Assert.assertTrue(StringUtil.isWhitespace('\n'));
    }

    @Test
    public void testIsWhitespaceFormFeed() {
        Assert.assertTrue(StringUtil.isWhitespace('\f'));
    }

    @Test
    public void testIsWhitespaceCarriageReturn() {
        Assert.assertTrue(StringUtil.isWhitespace('\r'));
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(' '));
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\t'));
    }

    @Test
    public void testIsActuallyWhitespaceNewLine() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\n'));
    }

    @Test
    public void testIsActuallyWhitespaceFormFeed() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\f'));
    }

    @Test
    public void testIsActuallyWhitespaceCarriageReturn() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\r'));
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        String input = "";
        String expected = "";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        String input = " ";
        String expected = " ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String input = "   ";
        String expected = " ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceTabAndSpace() {
        String input = "\t   ";
        String expected = " \t";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceNewlineAndTab() {
        String input = "\n\t";
        String expected = "\n\t";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceFormFeedAndTab() {
        String input = "\f\t";
        String expected = "\f\t";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleNewlines() {
        String input = "\n\n";
        String expected = "\n";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingTab() {
        String input = "\thello";
        String expected = " hello";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceTrailingTab() {
        String input = "hello\t";
        String expected = "hello";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingAndTrailingTabs() {
        String input = "\thello\t";
        String expected = "hello";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingAndTrailingSpaces() {
        String input = " hello ";
        String expected = "hello";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingAndTrailingNewlines() {
        String input = "\nhello\n";
        String expected = "hello";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingAndTrailingFormFeeds() {
        String input = "\fhello\f";
        String expected = "hello";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingAndTrailingCarriageReturns() {
        String input = "\rhello\r";
        String expected = "hello";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceMixed() {
        String input = "\n\t hello \n\t";
        String expected = "hello";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInNull() {
        List<String> haystack = Arrays.asList("hello", "world");
        Assert.assertFalse(StringUtil.in(null, haystack.toArray(new String[0])));
    }

    @Test
    public void testInEmptyList() {
        List<String> haystack = Collections.emptyList();
        String needle = "needle";
        Assert.assertFalse(StringUtil.in(needle, haystack.toArray(new String[0])));
    }

    @Test
    public void testInSingleElementList() {
        List<String> haystack = Collections.singletonList("needle");
        String needle = "needle";
        Assert.assertTrue(StringUtil.in(needle, haystack.toArray(new String[0])));
    }

    @Test
    public void testInMultipleElementsList() {
        List<String> haystack = Arrays.asList("hello", "world");
        String needle = "hello";
        Assert.assertTrue(StringUtil.in(needle, haystack.toArray(new String[0])));
    }

    @Test
    public void testInSortedEmpty() {
        String[] haystack = {};
        String needle = "needle";
        Assert.assertFalse(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testInSortedSingleElement() {
        String[] haystack = new String[]{"needle"};
        String needle = "needle";
        Assert.assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testInSortedMultipleElements() {
        String[] haystack = new String[]{"hello", "world"};
        String needle = "hello";
        Assert.assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolveBaseNull() {
        String base = null;
        String relUrl = "/path/to/file.html";
        String expected = "";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseEmpty() {
        String base = "";
        String relUrl = "/path/to/file.html";
        String expected = "/path/to/file.html";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseAbsolute() {
        String base = "http://www.example.com/";
        String relUrl = "/path/to/file.html";
        String expected = "http://www.example.com/path/to/file.html";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseRelative() {
        String base = "http://www.example.com/";
        String relUrl = "/path/to/file.html";
        String expected = "http://www.example.com/path/to/file.html";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseRelativeWithHash() {
        String base = "http://www.example.com/";
        String relUrl = "/path/to/file.html#fragment";
        String expected = "http://www.example.com/path/to/file.html#fragment";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseRelativeWithTrailingSlash() {
        String base = "http://www.example.com/";
        String relUrl = "/path/to/file.html";
        String expected = "http://www.example.com/path/to/file.html";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseRelativeWithTrailingSlashInRelUrl() {
        String base = "http://www.example.com/";
        String relUrl = "/path/to/file.html/";
        String expected = "http://www.example.com/path/to/file.html";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseAbsoluteWithHash() {
        String base = "http://www.example.com/path/to/file.html#fragment";
        String relUrl = "/";
        String expected = "http://www.example.com/path/to/file.html#fragment";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseMalformedUrl() {
        String base = "http://www.example.com/path/to/file.html";
        String relUrl = "//path/to/file.html";
        String expected = "";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseMalformedUrl2() {
        String base = "http://www.example.com/path/to/file.html";
        String relUrl = "//path/to/file.html#fragment";
        String expected = "";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseMalformedUrl3() {
        String base = "http://www.example.com/path/to/file.html";
        String relUrl = "//path/to/file.html?fragment";
        String expected = "";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseMalformedUrl4() {
        String base = "http://www.example.com/path/to/file.html";
        String relUrl = "//path/to/file.html";
        String expected = "";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }
}