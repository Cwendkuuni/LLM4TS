package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "test");
        String expected = "hello world test";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = Collections.singletonList("hello");
        expected = "hello";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = Collections.emptyList();
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"hello", "world", "test"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "hello world test";
        String result = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, result);

        String[] emptyStrings = new String[0];
        iterator = Arrays.asList(emptyStrings).iterator();
        expected = "";
        result = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"hello", "world", "test"};
        String expected = "hello world test";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = new String[0];
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < 21; i++) {
            String expected = padding(i);
            String result = StringUtil.padding(i);
            Assert.assertEquals(expected, result);
        }

        try {
            StringUtil.padding(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        String[] strings = new String[]{null, "", " ", "   ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          ", "           ", "            ", "             ", "              ", "               ", "                ", "                 ", "                  ", "                   ", "                    "};
        for (String string : strings) {
            boolean expected = StringUtil.isBlank(string);
            boolean result = StringUtil.isBlank(string);
            Assert.assertEquals(expected, result);
        }

        String string = "hello world";
        expected = false;
        result = StringUtil.isBlank(string);
        Assert.assertFalse(result);

        string = "   hello world   ";
        expected = false;
        result = StringUtil.isBlank(string);
        Assert.assertFalse(result);

        string = "hello world\n";
        expected = false;
        result = StringUtil.isBlank(string);
        Assert.assertFalse(result);

        string = "\n";
        expected = true;
        result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);

        string = " \t\r\f";
        expected = true;
        result = StringUtil.isBlank(string);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumeric() {
        String[] strings = new String[]{"123", "1234", "", "abc", "123a", "123.4", "123e4", "123-4", "123_4", "123."};
        for (String string : strings) {
            boolean expected = StringUtil.isNumeric(string);
            boolean result = StringUtil.isNumeric(string);
            Assert.assertEquals(expected, result);
        }

        String string = null;
        expected = false;
        result = StringUtil.isNumeric(string);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsWhitespace() {
        for (int i = 0; i < 32; i++) {
            int codePoint = i;
            boolean expected = StringUtil.isWhitespace(codePoint);
            boolean result = StringUtil.isWhitespace(codePoint);
            Assert.assertEquals(expected, result);
        }

        codePoint = 160;
        expected = true;
        result = StringUtil.isWhitespace(codePoint);
        Assert.assertEquals(expected, result);

        codePoint = 32767;
        expected = false;
        result = StringUtil.isWhitespace(codePoint);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        for (int i = 0; i < 33; i++) {
            int codePoint = i;
            boolean expected = StringUtil.isActuallyWhitespace(codePoint);
            boolean result = StringUtil.isActuallyWhitespace(codePoint);
            Assert.assertEquals(expected, result);
        }

        codePoint = 160;
        expected = true;
        result = StringUtil.isActuallyWhitespace(codePoint);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String[] strings = new String[]{"   hello world   ", "hello world", "hello\nworld\t", " \thello world\n", " \t\n\r\f\nhello world\n", "hello world\t\t\t\t\t\t\t\t\t"};
        for (String string : strings) {
            String expected = "hello world";
            String result = StringUtil.normaliseWhitespace(string);
            Assert.assertEquals(expected, result);
        }
    }

    @Test
    public void testIn() {
        String[] strings = new String[]{"hello", "world", "test", "needle"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, strings);
        Assert.assertEquals(expected, result);

        strings = new String[]{null, "world", "test", "needle"};
        expected = false;
        result = StringUtil.in(needle, strings);
        Assert.assertEquals(expected, result);

        strings = new String[0];
        expected = false;
        result = StringUtil.in(needle, strings);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSorted() {
        String[] strings = new String[]{"needle", "hello", "world", "test"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, strings);
        Assert.assertEquals(expected, result);

        strings = new String[]{"needle", "hello", "test", "world"};
        expected = false;
        result = StringUtil.inSorted(needle, strings);
        Assert.assertEquals(expected, result);

        strings = new String[0];
        expected = false;
        result = StringUtil.inSorted(needle, strings);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "https://www.example.com/";
        String relUrl = "/index.html";
        String expected = "https://www.example.com/index.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://www.example.com/path/to/file.html";
        relUrl = "relative/path/to/file.html";
        expected = "https://www.example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://www.example.com/path/to/file.html";
        relUrl = "../relative/path/to/file.html";
        expected = "https://www.example.com/relative/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://www.example.com/path/to/file.html";
        relUrl = "../../relative/path/to/file.html";
        expected = "https://www.example.com/relative/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://www.example.com/path/to/file.html";
        relUrl = "./file.html";
        expected = "https://www.example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://www.example.com/path/to/file.html";
        relUrl = "./";
        expected = "https://www.example.com/path/to/";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://www.example.com/path/to/file.html";
        relUrl = "../";
        expected = "https://www.example.com/path/";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://www.example.com/path/to/file.html";
        relUrl = "";
        expected = "https://www.example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "https://www.example.com/path/to/file.html";
        relUrl = null;
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = null;
        relUrl = "/index.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);

        baseUrl = "";
        relUrl = "/index.html";
        expected = "/index.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }
}