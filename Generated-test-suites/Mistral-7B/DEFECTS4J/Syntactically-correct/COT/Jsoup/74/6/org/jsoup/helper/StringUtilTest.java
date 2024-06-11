package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.jsoup.helper.StringUtil.join;
import static org.jsoup.helper.StringUtil.isActuallyWhitespace;
import static org.jsoup.helper.StringUtil.isBlank;
import static org.jsoup.helper.StringUtil.isNumeric;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("Hello", "World", "Test");
        String expected = "HelloWorldTest";
        String result = join(list, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        String[] array = new String[]{"Hello", "World", "Test"};
        Iterator<String> iterator = Arrays.asList(array).iterator();
        String expected = "HelloWorldTest";
        String result = join(iterator, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] array = new String[]{"Hello", "World", "Test"};
        String expected = "HelloWorldTest";
        String result = join(array, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        String expected = "       Test";
        String result = StringUtil.padding(8) + "Test";
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIsBlankNull() {
        Assert.assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlankEmptyString() {
        String str = "";
        Assert.assertTrue(StringUtil.isBlank(str));
    }

    @Test
    public void testIsBlankSpaceString() {
        String str = " ";
        Assert.assertTrue(StringUtil.isBlank(str));
    }

    @Test
    public void testIsBlankWhitespaceString() {
        String str = "\t\n\r\f ";
        Assert.assertTrue(StringUtil.isBlank(str));
    }

    @Test
    public void testIsBlankStringWithDigits() {
        String str = "123";
        Assert.assertFalse(StringUtil.isBlank(str));
    }

    @Test
    public void testIsNumericNull() {
        Assert.assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsNumericEmptyString() {
        Assert.assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsNumericAllDigits() {
        String str = "12345";
        Assert.assertTrue(StringUtil.isNumeric(str));
    }

    @Test
    public void testIsNumericStringWithLetters() {
        String str = "123abc";
        Assert.assertFalse(StringUtil.isNumeric(str));
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
    public void testIsActuallyWhitespaceNewline() {
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
        Assert.assertTrue(StringUtil.isActuallyWhitespace((int) 160));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = " \t\n     Hello World  \r\t\n\t ";
        String expected = "Hello World";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "sample";
        String[] haystack = {"apple", "banana", "sample", "orange", "sample", "grape"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInEmptyString() {
        String needle = "";
        String[] haystack = {"apple", "banana", "sample", "orange", "sample", "grape"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInNullNeedle() {
        String needle = null;
        String[] haystack = {"apple", "banana", "sample", "orange", "sample", "grape"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInEmptyHaystack() {
        String needle = "sample";
        String[] haystack = {};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSortedNullNeedle() {
        String needle = null;
        String[] haystack = {"apple", "banana", "sample", "orange", "sample", "grape"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSortedEmptyHaystack() {
        String needle = "sample";
        String[] haystack = {};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSorted() {
        String needle = "sample";
        String[] haystack = {"apple", "banana", "sample", "orange", "sample", "grape"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testResolveNullBaseUrl() {
        String relUrl = "relativeUrl";
        String result = StringUtil.resolve(null, relUrl);
        Assert.assertNull(result);
    }

    @Test
    public void testResolveNullRelUrl() {
        String baseUrl = "http://example.com/base";
        String result = StringUtil.resolve(baseUrl, null);
        Assert.assertEquals(baseUrl, result);
    }

    @Test
    public void testResolveRelativeUrl() {
        String baseUrl = "http://example.com/base";
        String relUrl = "relativeUrl";
        String expected = "http://example.com/relativeUrl";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteUrl() {
        String baseUrl = "http://example.com/base";
        String relUrl = "http://example.com/absoluteUrl";
        String expected = "http://example.com/absoluteUrl";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveMalformedUrlBase() {
        String baseUrl = "bad://example.com/base";
        String relUrl = "relativeUrl";
        String result = "";
        try {
            result = StringUtil.resolve(baseUrl, relUrl);
        } catch (MalformedURLException e) {
        }
        Assert.assertEquals("", result);
    }

    @Test
    public void testResolveMalformedUrlRelative() {
        String baseUrl = "http://example.com/base";
        String relUrl = "bad:relativeUrl";
        String result = "";
        try {
            result = StringUtil.resolve(baseUrl, relUrl);
        } catch (MalformedURLException e) {
        }
        Assert.assertEquals("", result);
    }

    @Test
    public void testResolveMalformedUrlBoth() {
        String baseUrl = "bad://example.com/base";
        String relUrl = "bad:relativeUrl";
        String result = "";
        try {
            result = StringUtil.resolve(baseUrl, relUrl);
        } catch (MalformedURLException e) {
        }
        Assert.assertEquals("", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder result = StringUtil.stringBuilder();
        Assert.assertNotNull(result);
    }

    @Test
    public void testStringBuilderMaxSize() {
        StringBuilder result = StringUtil.stringBuilder();
        result.append("123456789012345678901234567890123456789012345678901234567890");
        Assert.assertEquals(8 * 1024, result.capacity());
    }

}