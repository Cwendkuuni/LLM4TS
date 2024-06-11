import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";
        String result = StringUtil.join(strings, sep);
        assertEquals("Hello-World", result);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("Hello", "World").iterator();
        String sep = "-";
        String result = StringUtil.join(strings, sep);
        assertEquals("Hello-World", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        String result = StringUtil.join(strings, sep);
        assertEquals("Hello-World", result);
    }

    @Test
    public void testPadding() {
        int width = 5;
        String result = StringUtil.padding(width);
        assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        String string = "";
        assertTrue(StringUtil.isBlank(string));
    }

    @Test
    public void testIsNumeric() {
        String string = "12345";
        assertTrue(StringUtil.isNumeric(string));
    }

    @Test
    public void testIsWhitespace() {
        int c = ' ';
        assertTrue(StringUtil.isWhitespace(c));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c = ' ';
        assertTrue(StringUtil.isActuallyWhitespace(c));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "  Hello  World   ";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals("Hello World", result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "  Hello  World   ";
        StringUtil.appendNormalisedWhitespace(accum, string, true);
        assertEquals("Hello World", accum.toString());
    }

    @Test
    public void testIn() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "path/to/file";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("http://www.example.com/path/to/file", result.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "http://www.example.com";
        String relUrl = "path/to/file";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://www.example.com/path/to/file", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
    }
}