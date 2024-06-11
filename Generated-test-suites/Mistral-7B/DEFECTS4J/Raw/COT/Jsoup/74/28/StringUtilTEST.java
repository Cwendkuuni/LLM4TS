package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("a", "b", "c");
        String expected = "a b c";
        String result = StringUtil.join(strings, " ");
        Assert.assertEquals(expected, result);

        strings = Collections.singletonList(null);
        expected = "";
        result = StringUtil.join(strings, " ");
        Assert.assertEquals(expected, result);

        strings = Collections.emptyList();
        expected = "";
        result = StringUtil.join(strings, " ");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = {"a", "b", "c"};
        String expected = "a b c";
        String result = StringUtil.join(Arrays.asList(strings).iterator(), " ");
        Assert.assertEquals(expected, result);

        String[] emptyStrings = {};
        String emptyResult = "";
        String resultEmpty = StringUtil.join(Arrays.asList(emptyStrings).iterator(), " ");
        Assert.assertEquals(emptyResult, resultEmpty);

        String[] nullStrings = {null};
        String nullExpected = "";
        String nullResult = StringUtil.join(Arrays.asList(nullStrings).iterator(), " ");
        Assert.assertEquals(nullExpected, nullResult);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"a", "b", "c"};
        String expected = "a b c";
        String result = StringUtil.join(strings, " ");
        Assert.assertEquals(expected, result);

        String[] emptyStrings = {};
        String emptyResult = "";
        String resultEmpty = StringUtil.join(emptyStrings, " ");
        Assert.assertEquals(emptyResult, resultEmpty);

        String[] nullStrings = {null};
        String nullExpected = "";
        String nullResult = StringUtil.join(nullStrings, " ");
        Assert.assertEquals(nullExpected, nullResult);
    }

    @Test
    public void testPadding() {
        String expected = "   test";
        String result = StringUtil.padding(4) + "test";
        Assert.assertEquals(expected, result);

        int widthNegative = -1;
        try {
            String paddingNegative = StringUtil.padding(widthNegative);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            //expected
        }

        String paddingZero = "";
        Assert.assertEquals(paddingZero, StringUtil.padding(0));
    }

    @Test
    public void testIsBlank() {
        String blank = null;
        String empty = "";
        String whitespace = "   ";
        String nonBlank = "test";

        Assert.assertTrue(StringUtil.isBlank(blank));
        Assert.assertTrue(StringUtil.isBlank(empty));
        Assert.assertTrue(StringUtil.isBlank(whitespace));
        Assert.assertFalse(StringUtil.isBlank(nonBlank));

        String stringWithLeadingWhitespace = "   test";
        Assert.assertTrue(StringUtil.isBlank(stringWithLeadingWhitespace));
    }

    @Test
    public void testIsNumeric() {
        String numeric = "123";
        String nonNumeric = "abc";
        String empty = "";

        Assert.assertTrue(StringUtil.isNumeric(numeric));
        Assert.assertFalse(StringUtil.isNumeric(nonNumeric));
        Assert.assertFalse(StringUtil.isNumeric(empty));

        String negativeNumber = "-123";
        Assert.assertTrue(StringUtil.isNumeric(negativeNumber));
    }

    @Test
    public void testIsNullOrEmpty() {
        String nullString = null;
        String emptyString = "";
        String whitespaceString = "   ";
        String testString = "test";

        Assert.assertTrue(StringUtil.isBlank(nullString));
        Assert.assertTrue(StringUtil.isBlank(emptyString));
        Assert.assertTrue(StringUtil.isBlank(whitespaceString));
        Assert.assertFalse(StringUtil.isBlank(testString));

        Assert.assertEquals(true, StringUtil.isEmpty(nullString));
        Assert.assertEquals(true, StringUtil.isEmpty(emptyString));
        Assert.assertEquals(true, StringUtil.isEmpty(whitespaceString));
        Assert.assertEquals(false, StringUtil.isEmpty(testString));
    }

    @Test
    public void testIsWhitespace() {
        int whitespaceCodePoint = ' ';
        int newlineCodePoint = '\n';
        int tabCodePoint = '\t';
        int formFeedCodePoint = '\f';
        int carriageReturnCodePoint = '\r';

        Assert.assertTrue(StringUtil.isWhitespace(whitespaceCodePoint));
        Assert.assertTrue(StringUtil.isWhitespace(newlineCodePoint));
        Assert.assertTrue(StringUtil.isWhitespace(tabCodePoint));
        Assert.assertTrue(StringUtil.isWhitespace(formFeedCodePoint));
        Assert.assertTrue(StringUtil.isWhitespace(carriageReturnCodePoint));

        int nonWhitespaceCodePoint = 'a';
        Assert.assertFalse(StringUtil.isWhitespace(nonWhitespaceCodePoint));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int whitespaceCodePoint = ' ';
        int newlineCodePoint = '\n';
        int tabCodePoint = '\t';
        int formFeedCodePoint = '\f';
        int carriageReturnCodePoint = '\r';
        int nonBreakingSpaceCodePoint = '\u00A0'; // non-breaking space

        Assert.assertTrue(StringUtil.isActuallyWhitespace(whitespaceCodePoint));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(newlineCodePoint));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(tabCodePoint));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(formFeedCodePoint));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(carriageReturnCodePoint));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpaceCodePoint));

        int nonWhitespaceCodePoint = 'a';
        Assert.assertFalse(StringUtil.isActuallyWhitespace(nonWhitespaceCodePoint));
    }

    @Test
    public void testNormaliseWhitespace() {
        String multipleSpaces = "   test";
        String multipleTabs = "	test";
        String newlineAndTab = "test\t";
        String multipleNewlines = "test\n\ntest";

        String normalisedMultipleSpaces = "test";
        String normalisedMultipleTabs = "test";
        String normalisedNewlineAndTab = "test";
        String normalisedMultipleNewlines = "test";

        String resultMultipleSpaces = StringUtil.normaliseWhitespace(multipleSpaces);
        String resultMultipleTabs = StringUtil.normaliseWhitespace(multipleTabs);
        String resultNewlineAndTab = StringUtil.normaliseWhitespace(newlineAndTab);
        String resultMultipleNewlines = StringUtil.normaliseWhitespace(multipleNewlines);

        Assert.assertEquals(normalisedMultipleSpaces, resultMultipleSpaces);
        Assert.assertEquals(normalisedMultipleTabs, resultMultipleTabs);
        Assert.assertEquals(normalisedNewlineAndTab, resultNewlineAndTab);
        Assert.assertEquals(normalisedMultipleNewlines, resultMultipleNewlines);
    }

    @Test
    public void testIn() {
        String haystack = "apple,banana,orange";
        String needle1 = "apple";
        String needle2 = "orange";
        String needle3 = "pear";

        boolean expectedTrue = true;
        boolean resultTrue1 = StringUtil.in(needle1, haystack.split(","));
        boolean resultTrue2 = StringUtil.in(needle2, haystack.split(","));
        boolean resultFalse = StringUtil.in(needle3, haystack.split(","));

        Assert.assertEquals(expectedTrue, resultTrue1);
        Assert.assertEquals(expectedTrue, resultTrue2);
        Assert.assertEquals(false, resultFalse);
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "orange"};
        String needle = "orange";

        boolean expectedTrue = true;
        boolean resultTrue = StringUtil.inSorted(needle, haystack);

        Assert.assertEquals(expectedTrue, resultTrue);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithMalformedURL() {
        String baseUrl = "http://example.com/";
        String relUrl = "//path/file";

        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test
    public void testResolve() {
        String baseUrl = "http://example.com/index.html";
        String relUrl1 = "page1.html";
        String relUrl2 = "../page2.html";

        String expectedUrl1 = "http://example.com/page1.html";
        String expectedUrl2 = "http://example.com/page2.html";

        String resultUrl1 = StringUtil.resolve(baseUrl, relUrl1);
        String resultUrl2 = StringUtil.resolve(baseUrl, relUrl2);

        Assert.assertEquals(expectedUrl1, resultUrl1);
        Assert.assertEquals(expectedUrl2, resultUrl2);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder stringBuilder = StringUtil.stringBuilder();
        StringBuilder stringBuilder2 = StringUtil.stringBuilder();

        String expected = "test";
        stringBuilder.append(expected);

        String result = stringBuilder.toString();

        Assert.assertEquals(expected, result);

        stringBuilder2.append("test2");

        Assert.assertNotSame(stringBuilder, stringBuilder2);
    }
}