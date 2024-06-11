package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    private StringUtil stringUtil;

    @Before
    public void setUp() {
        stringUtil = new StringUtil();
    }

    @Test
    public void testJoinCollectionWithSeparator() {
        Collection<String> strings = new ArrayList<>();
        strings.add("one");
        strings.add("two");
        strings.add("three");

        String result = StringUtil.join(strings, ",");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinIteratorWithSeparator() {
        Collection<String> strings = new ArrayList<>();
        strings.add("one");
        strings.add("two");
        strings.add("three");

        Iterator<String> iterator = strings.iterator();

        String result = StringUtil.join(iterator, ",");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinArrayWithSeparator() {
        String[] strings = {"one", "two", "three"};

        String result = StringUtil.join(strings, ",");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testPaddingWithPositiveWidth() {
        int width = 5;

        String result = StringUtil.padding(width);
        assertEquals("     ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingWithNegativeWidth() {
        int width = -5;

        StringUtil.padding(width);
    }

    @Test
    public void testIsBlankWithNullString() {
        String string = null;

        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }

    @Test
    public void testIsBlankWithEmptyString() {
        String string = "";

        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }

    @Test
    public void testIsBlankWithWhitespaceString() {
        String string = "      ";

        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }

    @Test
    public void testIsBlankWithNonBlankString() {
        String string = "hello";

        boolean result = StringUtil.isBlank(string);
        assertFalse(result);
    }

    @Test
    public void testIsNumericWithNullString() {
        String string = null;

        boolean result = StringUtil.isNumeric(string);
        assertFalse(result);
    }

    @Test
    public void testIsNumericWithEmptyString() {
        String string = "";

        boolean result = StringUtil.isNumeric(string);
        assertFalse(result);
    }

    @Test
    public void testIsNumericWithNumericString() {
        String string = "12345";

        boolean result = StringUtil.isNumeric(string);
        assertTrue(result);
    }

    @Test
    public void testIsNumericWithNonNumericString() {
        String string = "hello";

        boolean result = StringUtil.isNumeric(string);
        assertFalse(result);
    }

    @Test
    public void testIsWhitespaceWithSpace() {
        int c = ' ';

        boolean result = StringUtil.isWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithTab() {
        int c = '\t';

        boolean result = StringUtil.isWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithNewline() {
        int c = '\n';

        boolean result = StringUtil.isWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithFormFeed() {
        int c = '\f';

        boolean result = StringUtil.isWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithCarriageReturn() {
        int c = '\r';

        boolean result = StringUtil.isWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithSpace() {
        int c = ' ';

        boolean result = StringUtil.isActuallyWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithTab() {
        int c = '\t';

        boolean result = StringUtil.isActuallyWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithNewline() {
        int c = '\n';

        boolean result = StringUtil.isActuallyWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithFormFeed() {
        int c = '\f';

        boolean result = StringUtil.isActuallyWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithCarriageReturn() {
        int c = '\r';

        boolean result = StringUtil.isActuallyWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithNonBreakingSpace() {
        int c = 160;

        boolean result = StringUtil.isActuallyWhitespace(c);
        assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   hello    world   ";

        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(" hello world ", result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        String string = "   hello    world   ";

        StringUtil.appendNormalisedWhitespace(sb, string, false);
        String result = sb.toString();
        assertEquals(" hello world ", result);
    }

    @Test
    public void testInWithMatch() {
        String needle = "one";
        String[] haystack = {"one", "two", "three"};

        boolean result = StringUtil.in(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInWithNoMatch() {
        String needle = "four";
        String[] haystack = {"one", "two", "three"};

        boolean result = StringUtil.in(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testInSortedWithMatch() {
        String needle = "one";
        String[] haystack = {"one", "two", "three"};

        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInSortedWithNoMatch() {
        String needle = "four";
        String[] haystack = {"one", "two", "three"};

        boolean result = StringUtil.inSorted(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "foo/bar";

        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(new URL("http://www.example.com/foo/bar"), result);
    }

    @Test
    public void testResolveWithAbsoluteUrl() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "http://www.google.com";

        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(new URL("http://www.google.com"), result);
    }

    @Test
    public void testResolveWithStringBaseUrl() {
        String baseUrl = "http://www.example.com";
        String relUrl = "foo/bar";

        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://www.example.com/foo/bar", result);
    }

    @Test
    public void testResolveWithStringBaseUrlWithMalformedURLException() {
        String baseUrl = "example.com";
        String relUrl = "foo/bar";

        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();

        assertNotNull(sb);
        assertTrue(sb.length() == 0);
    }
}