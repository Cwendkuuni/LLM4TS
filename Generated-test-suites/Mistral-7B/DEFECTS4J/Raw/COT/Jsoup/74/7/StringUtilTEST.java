import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> strings = Collections.emptyIterator();
        String result = StringUtil.join(strings, "");
        assertEquals("", result);
    }

    @Test
    public void testJoinIteratorSingle() {
        Iterator<String> strings = Arrays.asList("hello").iterator();
        String result = StringUtil.join(strings, "");
        assertEquals("hello", result);
    }

    @Test
    public void testJoinIteratorMultiple() {
        Iterator<String> strings = Arrays.asList("hello", "world", "!").iterator();
        String expected = "helloworld!";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = {};
        String result = StringUtil.join(strings, "");
        assertEquals("", result);
    }

    @Test
    public void testJoinArraySingle() {
        String[] strings = {"hello"};
        String result = StringUtil.join(strings, "");
        assertEquals("hello", result);
    }

    @Test
    public void testJoinArrayMultiple() {
        String[] strings = {"hello", "world", "!"};
        String expected = "helloworld!";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        String result = StringUtil.padding(-1);
    }

    @Test
    public void testIsBlankNull() {
        String string = null;
        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }

    @Test
    public void testIsBlankEmpty() {
        String string = "";
        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }

    @Test
    public void testIsBlankWhitespace() {
        String string = "   ";
        boolean result = StringUtil.isBlank(string);
        assertTrue(result);
    }

    @Test
    public void testIsBlankNonBlank() {
        String string = "hello";
        boolean result = StringUtil.isBlank(string);
        assertFalse(result);
    }

    @Test
    public void testIsNumericTrue() {
        String string = "123";
        boolean result = StringUtil.isNumeric(string);
        assertTrue(result);
    }

    @Test
    public void testIsNumericFalseEmpty() {
        String string = "";
        boolean result = StringUtil.isNumeric(string);
        assertFalse(result);
    }

    @Test
    public void testIsNumericFalseNonNumeric() {
        String string = "hello";
        boolean result = StringUtil.isNumeric(string);
        assertFalse(result);
    }

    @Test
    public void testIsWhitespaceTrue() {
        int codePoint = ' ';
        boolean result = StringUtil.isWhitespace(codePoint);
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceFalse() {
        int codePoint = 'a';
        boolean result = StringUtil.isWhitespace(codePoint);
        assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceTrue() {
        int codePoint = ' ';
        boolean result = StringUtil.isActuallyWhitespace(codePoint);
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceFalse() {
        int codePoint = 'a';
        boolean result = StringUtil.isActuallyWhitespace(codePoint);
        assertFalse(result);
    }

    @Test
    public void testNormaliseWhitespaceSingle() {
        String string = " ";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceMultiple() {
        String string = "   hello world   ";
        String expected = "hello world";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(expected, result);
    }

    @Test
    public void testInArrayNullNeedle() {
        String[] haystack = {"hello", "world"};
        boolean result = StringUtil.in(null, haystack);
        assertFalse(result);
    }

    @Test
    public void testInArrayEmptyArray() {
        String[] haystack = {};
        String needle = "hello";
        boolean result = StringUtil.in(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testInArrayMatch() {
        String[] haystack = {"hello", "world"};
        String needle = "hello";
        boolean result = StringUtil.in(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInArrayNoMatch() {
        String[] haystack = {"hello", "world"};
        String needle = "world";
        boolean result = StringUtil.in(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testInSortedNullNeedle() {
        String[] haystack = {"hello", "world"};
        boolean result = StringUtil.inSorted(null, haystack);
        assertFalse(result);
    }

    @Test
    public void testInSortedEmptyHaystack() {
        String needle = "hello";
        boolean result = StringUtil.inSorted(needle, new String[0]);
        assertFalse(result);
    }

    @Test
    public void testInSortedMatch() {
        String[] haystack = {"hello", "world"};
        String needle = "hello";
        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);
    }

    @Test
    public void testInSortedNoMatch() {
        String[] haystack = {"hello", "world"};
        String needle = "world";
        boolean result = StringUtil.inSorted(needle, haystack);
        assertFalse(result);
    }

    @Test
    public void testResolveBaseNull() {
        String relUrl = "";
        String result = StringUtil.resolve(null, relUrl);
        assertEquals("", result);
    }

    @Test
    public void testResolveBaseEmpty() {
        String relUrl = "";
        String baseUrl = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("", result);
    }

    @Test
    public void testResolveBaseAbsolute() {
        String relUrl = "http://example.com";
        String baseUrl = "http://example.com/";
        String expected = relUrl;
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseRelative() {
        String relUrl = "/foo.html";
        String baseUrl = "http://example.com/bar/";
        String expected = "http://example.com/foo.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseRelativeWithTrailingSlash() {
        String relUrl = "/foo.html";
        String baseUrl = "http://example.com/bar/";
        String expected = "http://example.com/bar/foo.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseMalformed() {
        String relUrl = "http://example.com/foo.html";
        String baseUrl = "http://example.com";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test
    public void testResolveBaseWithWorkaround() {
        String relUrl = "http://example.com/foo.html";
        String baseUrl = "http://example.com/";
        String expected = "http://example.com/foo.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseWithWorkaroundMalformed() {
        String relUrl = "http://example.com/foo.html";
        String baseUrl = "http://example.com:80/";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");
        String expected = "hello";
        String result = sb.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilderMaxSize() {
        StringBuilder sb = StringUtil.stringBuilder();
        for (int i = 0; i < 10 * 1024; i++) {
            sb.append("x");
        }
        String result = sb.toString();
        assertEquals(10 * 1024 * 3, result.length()); // 3 bytes per char
    }
}