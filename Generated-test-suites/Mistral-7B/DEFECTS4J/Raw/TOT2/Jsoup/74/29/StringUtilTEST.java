import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinCollectionEmpty() {
        List<String> strings = Collections.emptyList();
        String expected = "";
        String result = StringUtil.join(strings.iterator(), "");
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testJoinCollectionNullIterator() {
        List<String> strings = null;
        String sep = "";
        StringUtil.join(strings.iterator(), sep);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world", "!"};
        String expected = "helloworld!";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = {};
        String expected = "";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        int width = 10;
        String expected = "         hello";
        String result = StringUtil.padding(width + 5);
        result = result.substring(0, width + 5) + "hello";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlank() {
        Assert.assertTrue(StringUtil.isBlank(null));
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank("   "));
        Assert.assertFalse(StringUtil.isBlank("hello"));
    }

    @Test
    public void testIsNumeric() {
        Assert.assertFalse(StringUtil.isNumeric(null));
        Assert.assertFalse(StringUtil.isNumeric(""));
        Assert.assertFalse(StringUtil.isNumeric("abc"));
        Assert.assertTrue(StringUtil.isNumeric("123"));
        Assert.assertTrue(StringUtil.isNumeric("12345"));
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
        Assert.assertFalse(StringUtil.isWhitespace('1'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        Assert.assertFalse(StringUtil.isActuallyWhitespace(' '));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('\t'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\n'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\r'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\f'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\u00A0')); // non-breaking space
        Assert.assertFalse(StringUtil.isActuallyWhitespace('a'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('1'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "   hello world!   ";
        String expected = "hello world!";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        Assert.assertTrue(StringUtil.in("Java", "Java", "JavaScript", "Scala"));
        Assert.assertFalse(StringUtil.in("C++", "Java", "JavaScript", "Scala"));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"Java", "JavaScript", "Python", "Ruby", "Scala"};
        Assert.assertTrue(StringUtil.inSorted("Java", haystack));
        Assert.assertFalse(StringUtil.inSorted("C++", haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "https://example.com/path/to/file.html";
        String relUrl = "about.html";
        String expected = "https://example.com/about.html";
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected, result.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidBaseUrl() throws MalformedURLException {
        String baseUrl = "http://example.com:8080//path/to/file.html";
        String relUrl = "about.html";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidRelUrl() throws MalformedURLException {
        String baseUrl = "https://example.com/path/to/file.html";
        String relUrl = "://example.com/about.html";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test
    public void testResolveEmptyBaseUrl() throws MalformedURLException {
        String baseUrl = "";
        String relUrl = "about.html";
        String expected = "about.html";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testResolveEmptyRelUrl() throws MalformedURLException {
        String baseUrl = "https://example.com/path/to/file.html";
        String relUrl = "";
        String expected = "https://example.com/path/to/file.html";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");
        String expected = "hello";
        String result = sb.toString();
        Assert.assertEquals(expected, result);
    }
}