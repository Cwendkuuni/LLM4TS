import org.junit.Test;
import org.jsoup.helper.StringUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("hello", "world", "!");
        String sep = ", ";
        String expected = "hello, world, !";
        String result = StringUtil.join(strings, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("hello", "world", "!");
        Iterator<String> iterator = strings.iterator();
        String sep = ", ";
        String expected = "hello, world, !";
        String result = StringUtil.join(iterator, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world", "!"};
        String sep = ", ";
        String expected = "hello, world, !";
        String result = StringUtil.join(strings, sep);
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        int width = 10;
        String expected = "          ";
        String result = StringUtil.padding(width);
        assertEquals(expected, result);
    }

    @Test
    public void testIsBlank() {
        String str1 = null;
        String str2 = "";
        String str3 = "    ";
        String str4 = "hello";
        assertTrue(StringUtil.isBlank(str1));
        assertTrue(StringUtil.isBlank(str2));
        assertTrue(StringUtil.isBlank(str3));
        assertFalse(StringUtil.isBlank(str4));
    }

    @Test
    public void testIsNumeric() {
        String str1 = null;
        String str2 = "";
        String str3 = "123";
        String str4 = "abc123";
        assertFalse(StringUtil.isNumeric(str1));
        assertFalse(StringUtil.isNumeric(str2));
        assertTrue(StringUtil.isNumeric(str3));
        assertFalse(StringUtil.isNumeric(str4));
    }

    @Test
    public void testIsWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\f';
        int c5 = '\r';
        int c6 = 160;
        assertTrue(StringUtil.isWhitespace(c1));
        assertTrue(StringUtil.isWhitespace(c2));
        assertTrue(StringUtil.isWhitespace(c3));
        assertTrue(StringUtil.isWhitespace(c4));
        assertTrue(StringUtil.isWhitespace(c5));
        assertTrue(StringUtil.isWhitespace(c6));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c1 = ' ';
        int c2 = '\t';
        int c3 = '\n';
        int c4 = '\f';
        int c5 = '\r';
        int c6 = 160;
        assertTrue(StringUtil.isActuallyWhitespace(c1));
        assertTrue(StringUtil.isActuallyWhitespace(c2));
        assertTrue(StringUtil.isActuallyWhitespace(c3));
        assertTrue(StringUtil.isActuallyWhitespace(c4));
        assertTrue(StringUtil.isActuallyWhitespace(c5));
        assertTrue(StringUtil.isActuallyWhitespace(c6));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   hello   world   ";
        String expected = " hello world ";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(expected, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "   hello   world   ";
        String expected = " hello world ";
        StringUtil.appendNormalisedWhitespace(accum, string, false);
        String result = accum.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "hello";
        String[] haystack = {"hello", "world", "!"};
        assertTrue(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "hello";
        String[] haystack = {"!", "hello", "world"};
        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolve() {
        try {
            URL base = new URL("https://www.example.com");
            String relUrl = "/test";
            String expected = "https://www.example.com/test";
            URL result = StringUtil.resolve(base, relUrl);
            assertEquals(expected, result.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/test";
        String expected = "https://www.example.com/test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }
}