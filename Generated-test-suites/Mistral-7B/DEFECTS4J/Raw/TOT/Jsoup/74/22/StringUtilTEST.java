package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for StringUtil
 */
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("one", "two", "three");
        String expected = "one" + " two" + " three";
        String result = StringUtil.join(list.iterator(), "");
        Assert.assertEquals(expected, result);

        // edge case: empty collection
        list = Collections.emptyList();
        Assert.assertEquals("", StringUtil.join(list.iterator(), ""));

        // error scenario: null iterator
        Assert.assertTrue(StringUtil.join(null, "").isEmpty());
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"one", "two", "three"};
        String expected = "one" + " two" + " three";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        Assert.assertEquals("", StringUtil.padding(0));
        Assert.assertEquals(" ", StringUtil.padding(1));
        Assert.assertEquals("  ", StringUtil.padding(3));
        Assert.assertEquals("   ", StringUtil.padding(5));
        Assert.assertEquals("    ", StringUtil.padding(7));
        Assert.assertEquals("     ", StringUtil.padding(9));
        Assert.assertEquals("      ", StringUtil.padding(11));
        Assert.assertEquals("       ", StringUtil.padding(13));
        Assert.assertEquals("        ", StringUtil.padding(15));
        Assert.assertEquals("         ", StringUtil.padding(17));
        Assert.assertEquals("          ", StringUtil.padding(19));
        Assert.assertEquals("           ", StringUtil.padding(21));
        Assert.assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(-1));
    }

    @Test
    public void testIsBlank() {
        Assert.assertTrue(StringUtil.isBlank(null));
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank(" "));
        Assert.assertTrue(StringUtil.isBlank("\t"));
        Assert.assertTrue(StringUtil.isBlank("\n"));
        Assert.assertTrue(StringUtil.isBlank("\r"));
        Assert.assertTrue(StringUtil.isBlank("\f"));
        Assert.assertFalse(StringUtil.isBlank("one"));
        Assert.assertFalse(StringUtil.isBlank("one "));
        Assert.assertFalse(StringUtil.isBlank("one\t"));
        Assert.assertFalse(StringUtil.isBlank("one\n"));
        Assert.assertFalse(StringUtil.isBlank("one\r"));
        Assert.assertFalse(StringUtil.isBlank("one\f"));
    }

    @Test
    public void testIsNumeric() {
        Assert.assertFalse(StringUtil.isNumeric(null));
        Assert.assertFalse(StringUtil.isNumeric(""));
        Assert.assertFalse(StringUtil.isNumeric("one"));
        Assert.assertFalse(StringUtil.isNumeric("1two"));
        Assert.assertFalse(StringUtil.isNumeric("abc"));
        Assert.assertTrue(StringUtil.isNumeric("1"));
        Assert.assertTrue(StringUtil.isNumeric("12"));
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
        Assert.assertTrue(StringUtil.isWhitespace('\r'));
        Assert.assertTrue(StringUtil.isWhitespace('\f'));
        Assert.assertFalse(StringUtil.isWhitespace('\b'));
        Assert.assertFalse(StringUtil.isWhitespace('\u0000'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(' '));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\t'));
        Assert.assertTrue(
                StringUtil.isActuallyWhitespace('\n')
        );
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\r'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\f'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\u00A0')); // non-breaking space
        Assert.assertFalse(StringUtil.isActuallyWhitespace('\u0001'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = " hello world \t\t\n\r\f 123\ntext\t text ";
        String expected = "hello world   text text";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String[] haystack = {"one", "two", "three"};
        Assert.assertTrue(StringUtil.in(null, haystack));
        Assert.assertFalse(StringUtil.in(null, haystack));
        Assert.assertFalse(StringUtil.in("", haystack));
        Assert.assertTrue(StringUtil.in("one", haystack));
        Assert.assertTrue(StringUtil.in("two", haystack));
        Assert.assertTrue(StringUtil.in("three", haystack));
        Assert.assertFalse(StringUtil.in("four", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"one", "two", "three"};
        Assert.assertTrue(StringUtil.inSorted("one", haystack));
        Assert.assertTrue(StringUtil.inSorted("two", haystack));
        Assert.assertTrue(StringUtil.inSorted("three", haystack));
        Assert.assertFalse(StringUtil.inSorted("four", haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com";
        String relUrl = "/path/to/file.html";
        String expected = "http://example.com/path/to/file.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com";
        relUrl = "path/to/file.html";
        expected = "http://example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/to";
        relUrl = "file.html";
        expected = "http://example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/";
        relUrl = "/path/to/file.html";
        expected = "http://example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/to";
        relUrl = "file.html";
        expected = "http://example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/to";
        relUrl = "./file.html";
        expected = "http://example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/to";
        relUrl = "../file.html";
        expected = "http://example.com/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "./";
        expected = "http://example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "../";
        expected = "http://example.com/path/to/";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "../../";
        expected = "http://example.com/";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "file://localhost/path/to/file.html";
        Assert.assertEquals("", StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "http://example2.com/path/to/file.html";
        Assert.assertEquals("", StringUtil.resolve(baseUrl, relUrl));
    }
}