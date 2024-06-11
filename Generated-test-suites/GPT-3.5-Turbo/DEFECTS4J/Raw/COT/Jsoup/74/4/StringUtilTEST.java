import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    // Test join(Collection strings, String sep)
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";
        String expected = "Hello-World";
        String actual = StringUtil.join(strings, sep);
        Assert.assertEquals(expected, actual);
    }

    // Test join(Iterator strings, String sep)
    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        Iterator<String> iterator = strings.iterator();
        String sep = "-";
        String expected = "Hello-World";
        String actual = StringUtil.join(iterator, sep);
        Assert.assertEquals(expected, actual);
    }

    // Test join(String[] strings, String sep)
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        String expected = "Hello-World";
        String actual = StringUtil.join(strings, sep);
        Assert.assertEquals(expected, actual);
    }

    // Test padding(int width)
    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        String actual = StringUtil.padding(width);
        Assert.assertEquals(expected, actual);
    }

    // Test isBlank(String string)
    @Test
    public void testIsBlank() {
        String string = "  ";
        boolean expected = true;
        boolean actual = StringUtil.isBlank(string);
        Assert.assertEquals(expected, actual);
    }

    // Test isNumeric(String string)
    @Test
    public void testIsNumeric() {
        String string = "123";
        boolean expected = true;
        boolean actual = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, actual);
    }

    // Test isWhitespace(int c)
    @Test
    public void testIsWhitespace() {
        int c = ' ';
        boolean expected = true;
        boolean actual = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    // Test isActuallyWhitespace(int c)
    @Test
    public void testIsActuallyWhitespace() {
        int c = ' ';
        boolean expected = true;
        boolean actual = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    // Test normaliseWhitespace(String string)
    @Test
    public void testNormaliseWhitespace() {
        String string = "  hello  world  ";
        String expected = " hello world ";
        String actual = StringUtil.normaliseWhitespace(string);
        Assert.assertEquals(expected, actual);
    }

    // Test in(String needle, String... haystack)
    @Test
    public void testIn() {
        String needle = "hello";
        String[] haystack = {"hello", "world"};
        boolean expected = true;
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    // Test inSorted(String needle, String[] haystack)
    @Test
    public void testInSorted() {
        String needle = "hello";
        String[] haystack = {"apple", "banana", "hello", "world"};
        boolean expected = true;
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    // Test resolve(URL base, String relUrl)
    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "../page.html";
        String expected = "http://example.com/page.html";
        URL resolvedUrl = StringUtil.resolve(base, relUrl);
        String actual = resolvedUrl.toString();
        Assert.assertEquals(expected, actual);
    }

    // Test resolve(String baseUrl, String relUrl)
    @Test
    public void testResolveString() {
        String baseUrl = "http://example.com";
        String relUrl = "../page.html";
        String expected = "http://example.com/page.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    // Test stringBuilder()
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        int expected = 0;
        int actual = sb.length();
        Assert.assertEquals(expected, actual);
    }

}