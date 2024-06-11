package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for StringUtil
 */
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("Hello", "World", "!", "Test");
        String expected = "HelloWorld!Test";
        String result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"Hello", "World", "!", "Test"};
        String expected = "HelloWorld!Test";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyCollection() {
        List<String> strings = Collections.emptyList();
        String result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinNullCollection() {
        List<String> strings = null;
        String result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinNullIterator() {
        Iterator<String> strings = null;
        String result = StringUtil.join(strings, "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinEmptyIterator() {
        Iterator<String> strings = Collections.emptyIterator();
        String result = StringUtil.join(strings, "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testPaddingNegativeValue() {
        Assert.throwsException(IllegalArgumentException.class, () -> StringUtil.padding(-1));
    }

    @Test
    public void testPaddingWidthBiggerThanPaddingArraySize() {
        String result = StringUtil.padding(StringUtil.padding.length + 1);
        Assert.assertEquals(Character.isWhitespace(result.charAt(0)), true);
    }

    @Test
    public void testIsBlankNullString() {
        boolean result = StringUtil.isBlank(null);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsBlankEmptyString() {
        boolean result = StringUtil.isBlank("");
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsBlankWhitespaceString() {
        String string = "    ";
        boolean result = StringUtil.isBlank(string);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsBlankNonBlankString() {
        String string = "This is not blank.";
        boolean result = StringUtil.isBlank(string);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testIsNumericNullString() {
        boolean result = StringUtil.isNumeric(null);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testIsNumericEmptyString() {
        boolean result = StringUtil.isNumeric("");
        Assert.assertEquals(result, false);
    }

    @Test
    public void testIsNumericStringWithDigits() {
        String string = "12345";
        boolean result = StringUtil.isNumeric(string);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsNumericStringWithLetters() {
        String string = "abcdef";
        boolean result = StringUtil.isNumeric(string);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testIsNumericStringWithMixed() {
        String string = "1a2b3";
        boolean result = StringUtil.isNumeric(string);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testIsWhitespaceSpace() {
        boolean result = StringUtil.isWhitespace(' ');
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsWhitespaceTab() {
        boolean result = StringUtil.isWhitespace('\t');
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsWhitespaceNewline() {
        boolean result = StringUtil.isWhitespace('\n');
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsWhitespaceCarriageReturn() {
        boolean result = StringUtil.isWhitespace('\r');
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsWhitespaceOther() {
        boolean result = StringUtil.isWhitespace('x');
        Assert.assertEquals(result, false);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        boolean result = StringUtil.isActuallyWhitespace('\t');
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        boolean result = StringUtil.isActuallyWhitespace('\n');
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsActuallyWhitespaceCarriageReturn() {
        boolean result = StringUtil.isActuallyWhitespace('\r');
        Assert.assertEquals(result, true);
    }

    @Test
    public void testIsActuallyWhitespaceNonWhitespace() {
        boolean result = StringUtil.isActuallyWhitespace('x');
        Assert.assertEquals(result, false);
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        boolean result = StringUtil.isActuallyWhitespace(160);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testNormaliseWhitespaceEmptyString() {
        String result = StringUtil.normaliseWhitespace("");
        Assert.assertEquals(result, "");
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
    public void testNormaliseWhitespaceLeadingAndTrailingSpaces() {
        String input = "   Hello World   ";
        String expected = "Hello World";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceNewlineTabAndSpace() {
        String input = "\n\t Hello\n World\t";
        String expected = " Hello\nWorld";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInNullNeedle() {
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.in(null, haystack);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testInNullHaystack() {
        String needle = "Hello";
        boolean result = StringUtil.in(needle, null);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testInEmptyHaystack() {
        String needle = "Hello";
        boolean result = StringUtil.in(needle, new String[0]);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testInEmptyNeedle() {
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.in(null, haystack);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testInNeedlePresentInHaystack() {
        String needle = "World";
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testInNeedleNotPresentInHaystack() {
        String needle = "Foo";
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testInSortedNullNeedle() {
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.inSorted(null, haystack);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testInSortedEmptyHaystack() {
        String needle = "Hello";
        boolean result = StringUtil.inSorted(needle, new String[0]);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testInSortedEmptyNeedle() {
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.inSorted(null, haystack);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testInSortedNeedlePresentInHaystack() {
        String needle = "World";
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testInSortedNeedleNotPresentInHaystack() {
        String needle = "Foo";
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(result, false);
    }

    @Test
    public void testInSortedNeedlePresentAtBeginning() {
        String needle = "Hello";
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testInSortedNeedlePresentAtEnd() {
        String needle = "Test";
        String[] haystack = new String[]{"Hello", "World", "!", "Test"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testResolveBaseUrlNull() {
        String relUrl = "/foo";
        String result = StringUtil.resolve(null, relUrl);
        Assert.assertEquals(result, "");
    }

    @Test
    public void testResolveBaseUrlEmpty() {
        String baseUrl = "";
        String relUrl = "/foo";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(result, relUrl);
    }

    @Test
    public void testResolveBaseUrlAbsoluteRelUrl() {
        String baseUrl = "http://example.com/bar/baz";
        String relUrl = "/qux";
        String expected = "http://example.com/qux";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseUrlRelativeUrl() {
        String baseUrl = "http://example.com/bar/baz";
        String relUrl = "qux";
        String expected = "http://example.com/bar/baz/qux";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseUrlRelativeUrlWithDots() {
        String baseUrl = "http://example.com/bar/baz";
        String relUrl = "..";
        String expected = "http://example.com/bar";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseUrlRelativeUrlWithDotsAndSlashes() {
        String baseUrl = "http://example.com/bar/baz";
        String relUrl = "../qux/";
        String expected = "http://example.com/qux";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseUrlMalformedRelativeUrl() {
        String baseUrl = "http://example.com/bar/baz";
        String relUrl = "//qux";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(result, "");
    }

    @Test
    public void testResolveBaseUrlMalformedBaseUrl() {
        String baseUrl = "http://example.com/bar/baz";
        String relUrl = "/qux";
        String result = StringUtil.resolve(null, relUrl);
        Assert.assertEquals(result, "");
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        String expected = "";
        String result = sb.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStringBuilderSize() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("Hello");
        String result = sb.toString();
        Assert.assertEquals("Hello", result);
    }

}