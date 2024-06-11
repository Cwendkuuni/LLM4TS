import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String result = StringUtil.join(strings, ",");

        assertEquals("Hello,World,!", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        Iterator<String> iterator = strings.iterator();

        String result = StringUtil.join(iterator, ",");

        assertEquals("Hello,World,!", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};

        String result = StringUtil.join(strings, ",");

        assertEquals("Hello,World,!", result);
    }

    @Test
    public void testPadding() {
        String expected = "       ";
        String result = StringUtil.padding(7);

        assertEquals(expected, result);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("     "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("1234"));
        assertFalse(StringUtil.isNumeric("12a34"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String expected = "This is a sample text.";
        String result = StringUtil.normaliseWhitespace("This    \tis\r\na sample\ttext.");

        assertEquals(expected, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        String expected = "This is a sample text.";
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "This    \tis\r\na sample\ttext.", false);

        assertEquals(expected, sb.toString());
    }

    @Test
    public void testIn() {
        String[] haystack = {"Hello", "World", "!"};

        assertTrue(StringUtil.in("Hello", haystack));
        assertTrue(StringUtil.in("World", haystack));
        assertTrue(StringUtil.in("!", haystack));
        assertFalse(StringUtil.in("Goodbye", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"!", "Hello", "World"};

        assertTrue(StringUtil.inSorted("!", haystack));
        assertTrue(StringUtil.inSorted("Hello", haystack));
        assertTrue(StringUtil.inSorted("World", haystack));
        assertFalse(StringUtil.inSorted("Goodbye", haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "/path/to/file.html";

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(new URL("http://example.com/path/to/file.html"), result);
    }

    @Test
    public void testResolveWithQueryParams() throws MalformedURLException {
        URL base = new URL("http://example.com/path/to/file.html?q=123");
        String relUrl = "?foo=bar";

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(new URL("http://example.com/path/to/file.html?foo=bar"), result);
    }

    @Test
    public void testResolveWithRelativeFilePath() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "./path/to/file.html";

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(new URL("http://example.com/path/to/file.html"), result);
    }

    @Test
    public void testResolveWithInvalidBaseURL() {
        String baseUrl = "http://invalid.com";
        String relUrl = "/path/to/file.html";

        String result = StringUtil.resolve(baseUrl, relUrl);

        assertEquals("", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();

        assertNotNull(sb1);
        assertTrue(sb1.length() == 0);

        sb1.append("Hello");

        StringBuilder sb2 = StringUtil.stringBuilder();

        assertNotNull(sb1);
        assertNotEquals(sb1, sb2);
        assertTrue(sb2.length() == 0);
    }
}