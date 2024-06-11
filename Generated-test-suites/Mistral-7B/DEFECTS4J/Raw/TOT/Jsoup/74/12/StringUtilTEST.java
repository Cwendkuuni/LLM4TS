package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.jsoup.helper.StringUtil.join;
import static org.jsoup.helper.StringUtil.isActuallyWhitespace;
import static org.jsoup.helper.StringUtil.isBlank;
import static org.jsoup.helper.StringUtil.isNumeric;
import static org.jsoup.helper.StringUtil.join;
import static org.jsoup.helper.StringUtil.padding;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        String expected = "apple,banana,cherry";
        String actual = join(list, ",");
        Assert.assertEquals(expected, actual);

        list = Collections.singletonList(null);
        actual = join(list, ",");
        Assert.assertEquals("", actual);

        list = Collections.emptyList();
        actual = join(list, ",");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"apple", "banana", "cherry"};
        String expected = "apple,banana,cherry";
        String actual = join(Arrays.asList(strings).iterator(), ",");
        Assert.assertEquals(expected, actual);

        String[] emptyStrings = new String[0];
        actual = join(Arrays.asList(emptyStrings).iterator(), ",");
        Assert.assertEquals("", actual);

        Iterator<String> emptyIterator = Collections.emptyIterator();
        actual = join(emptyIterator, ",");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"apple", "banana", "cherry"};
        String expected = "apple,banana,cherry";
        String actual = join(strings, ",");
        Assert.assertEquals(expected, actual);

        strings = new String[0];
        actual = join(strings, ",");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testPadding() {
        String actual = padding(5);
        String expected = "     ";
        Assert.assertEquals(expected, actual);

        actual = padding(-5);
        Assert.assertNull(actual);

        actual = padding(0);
        Assert.assertEquals("", actual);
    }

    @Test
    public void testIsBlank() {
        String empty = "";
        Assert.assertTrue(isBlank(empty));

        String whitespace = "   ";
        Assert.assertTrue(isBlank(whitespace));

        String nullString = null;
        Assert.assertTrue(isBlank(nullString));

        String nonBlank = "hello world";
        Assert.assertFalse(isBlank(nonBlank));
    }

    @Test
    public void testIsNumeric() {
        String nonNumeric = "abcdef";
        Assert.assertFalse(isNumeric(nonNumeric));

        String empty = "";
        Assert.assertFalse(isNumeric(empty));

        String numeric = "12345";
        Assert.assertTrue(isNumeric(numeric));

        String numberWithLeadingZero = "012345";
        Assert.assertTrue(isNumeric(numberWithLeadingZero));
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(isWhitespace(' '));
        Assert.assertTrue(isWhitespace('\t'));
        Assert.assertTrue(isWhitespace('\n'));
        Assert.assertTrue(isWhitespace('\r'));
        Assert.assertTrue(isWhitespace('\f'));

        Assert.assertFalse(isWhitespace('a'));
        Assert.assertFalse(isWhitespace('\u00A0'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(isActuallyWhitespace(' '));
        Assert.assertTrue(isActuallyWhitespace('\t'));
        Assert.assertTrue(isActuallyWhitespace('\n'));
        Assert.assertTrue(isActuallyWhitespace('\r'));
        Assert.assertTrue(isActuallyWhitespace('\f'));
        Assert.assertTrue(isActuallyWhitespace(Character.toChars(0x00A0)[0]));

        Assert.assertFalse(isActuallyWhitespace('a'));
        Assert.assertFalse(isActuallyWhitespace('\u00AD'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "hello\tthere\r\n\tmike";
        String expected = "hellothere mike";
        String actual = normaliseWhitespace(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIn() {
        String needle = "mike";
        String[] haystack = {"tim", "tom", "mike", "jim"};
        boolean expected = true;
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);

        needle = "john";
        expected = false;
        actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInSorted() {
        String needle = "mike";
        String[] sortedHaystack = {"jim", "mike", "tom", "tim"};
        boolean expected = true;
        boolean actual = StringUtil.inSorted(needle, sortedHaystack);
        Assert.assertEquals(expected, actual);

        needle = "john";
        expected = false;
        actual = StringUtil.inSorted(needle, sortedHaystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "/relative/path";
        URL expected = new URL("http://example.com/relative/path");
        URL actual = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testResolveEmptyBaseUrl() throws MalformedURLException {
        String baseUrl = "";
        String relUrl = "/relative/path";
        URL actual = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals("http://" + relUrl, actual.toString());
    }

    @Test
    public void testResolveEmptyRelUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = "";
        URL expected = new URL(baseUrl);
        URL actual = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testResolveInvalidRelUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file";
        String relUrl = ""; // empty string
        URL actual = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals("", actual.toString());
    }
}