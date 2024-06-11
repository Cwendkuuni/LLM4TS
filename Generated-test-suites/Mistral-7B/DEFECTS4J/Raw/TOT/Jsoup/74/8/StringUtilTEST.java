package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String expected = "apple,banana,cherry";
        String result = StringUtil.join(strings, ", ");
        Assert.assertEquals(expected, result);

        strings = Collections.emptyList();
        expected = "";
        result = StringUtil.join(strings, ", ");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"apple", "banana", "cherry"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "apple,banana,cherry";
        String result = StringUtil.join(iterator, ", ");
        Assert.assertEquals(expected, result);

        iterator = Collections.emptyIterator();
        expected = "";
        result = StringUtil.join(iterator, ", ");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"apple", "banana", "cherry"};
        String expected = "apple,banana,cherry";
        String result = StringUtil.join(strings, ", ");
        Assert.assertEquals(expected, result);

        strings = new String[0];
        expected = "";
        result = StringUtil.join(strings, ", ");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        String expected = "    ";
        Assert.assertEquals(expected, result);

        result = StringUtil.padding(-5);
        Assert.assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(result));
    }

    @Test
    public void testIsBlank() {
        Assert.assertTrue(StringUtil.isBlank(null));
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank(" "));
        Assert.assertTrue(StringUtil.isBlank("\r\n"));
        Assert.assertTrue(StringUtil.isBlank("\t"));
        Assert.assertTrue(StringUtil.isBlank("   "));
        Assert.assertTrue(StringUtil.isBlank("         "));
        Assert.assertTrue(StringUtil.isBlank("  \r\n\t\t   "));

        String str = "  hi there,  how are you today? ";
        Assert.assertFalse(StringUtil.isBlank(str));
    }

    @Test
    public void testIsNumeric() {
        Assert.assertFalse(StringUtil.isNumeric(null));
        Assert.assertFalse(StringUtil.isNumeric(""));
        Assert.assertFalse(StringUtil.isNumeric("abc"));
        Assert.assertFalse(StringUtil.isNumeric("1a"));
        Assert.assertFalse(StringUtil.isNumeric("10.5"));
        Assert.assertFalse(StringUtil.isNumeric("1_2"));
        Assert.assertFalse(StringUtil.isNumeric("1000"));

        Assert.assertTrue(StringUtil.isNumeric("0"));
        Assert.assertTrue(StringUtil.isNumeric("1"));
        Assert.assertTrue(StringUtil.isNumeric("123"));
        Assert.assertTrue(StringUtil.isNumeric("1234"));
        Assert.assertTrue(StringUtil.isNumeric("12345"));
        Assert.assertTrue(StringUtil.isNumeric("123456"));
        Assert.assertTrue(StringUtil.isNumeric("1234567"));
        Assert.assertTrue(StringUtil.isNumeric("12345678"));
        Assert.assertTrue(StringUtil.isNumeric("123456789"));
        Assert.assertTrue(StringUtil.isNumeric("1234567890"));
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(StringUtil.isWhitespace(' '));
        Assert.assertTrue(StringUtil.isWhitespace('\t'));
        Assert.assertTrue(StringUtil.isWhitespace('\n'));
        Assert.assertTrue(StringUtil.isWhitespace('\f'));
        Assert.assertTrue(StringUtil.isWhitespace('\r'));
        Assert.assertFalse(StringUtil.isWhitespace('a'));
        Assert.assertFalse(StringUtil.isWhitespace('z'));
        Assert.assertFalse(StringUtil.isWhitespace('1'));
        Assert.assertFalse(StringUtil.isWhitespace('?'));
        Assert.assertFalse(StringUtil.isWhitespace('.'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(' '));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\t'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\n'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\f'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\r'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(160));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('a'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('z'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('1'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('?'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('.'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("   hi there, \n   how are you today?   ");
        String expected = "hi there, \n how are you today?";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        Assert.assertTrue(StringUtil.in(
            "cherry", haystack
        ));
        Assert.assertTrue(StringUtil.in(
            "banana", haystack
        ));
        Assert.assertTrue(StringUtil.in(
            "cherry", Arrays.asList("apple", "banana", "cherry").toArray(new String[0])
        ));
        Assert.assertTrue(StringUtil.in(
            "banana", Arrays.asList("apple", "banana", "cherry").toArray(new String[0])
        ));
        Assert.assertFalse(StringUtil.in(
            "orange", new String[0]
        ));
        Assert.assertFalse(StringUtil.in(
            "orange", new String[0]
        ));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry"};
        Assert.assertTrue(StringUtil.inSorted("cherry", haystack));
        Assert.assertTrue(StringUtil.inSorted("banana", haystack));
        Assert.assertFalse(StringUtil.inSorted("orange", haystack));
    }

    @Test
    public void testResolve() {
        String baseUrl = "http://example.com/path/file.html";
        String relUrl = "/relative/path.html";
        String expected = "http://example.com/relative/path.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "/";
        expected = "http://example.com/";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "..";
        expected = "http://example.com/path/";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "../relative/path.html";
        expected = "http://example.com/relative/path.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "../relative/path/file.html";
        expected = "http://example.com/relative/path/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "../../relative/path/file.html";
        expected = "http://example.com/relative/path/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "relative/path.html";
        expected = "http://example.com/relative/path.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "relative/path/file.html";
        expected = "http://example.com/relative/path/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "relative/path/file.html#fragment";
        expected = "http://example.com/relative/path/file.html#fragment";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "relative/path/file.html?query=param";
        expected = "http://example.com/relative/path/file.html?query=param";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "relative/path/file.html?query=param#fragment";
        expected = "http://example.com/relative/path/file.html?query=param#fragment";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/file.html";
        relUrl = "http://example.org/other/path.html";
        Assert.assertNull(StringUtil.resolve(baseUrl, relUrl));
    }
}