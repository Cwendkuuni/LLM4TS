import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class StringUtilTest {
    
    private StringUtil stringUtil;

    @Before
    public void setUp() {
        stringUtil = new StringUtil();
    }

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("hello", "world");
        String sep = "-";
        String result = stringUtil.join(strings, sep);
        assertEquals("hello-world", result);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("hello", "world").iterator();
        String sep = "-";
        String result = stringUtil.join(strings, sep);
        assertEquals("hello-world", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world"};
        String sep = "-";
        String result = stringUtil.join(strings, sep);
        assertEquals("hello-world", result);
    }

    @Test
    public void testPadding() {
        assertEquals("          ", stringUtil.padding(10));
    }

    @Test
    public void testIsBlank() {
        assertTrue(stringUtil.isBlank(""));
        assertTrue(stringUtil.isBlank("   "));
        assertFalse(stringUtil.isBlank("hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(stringUtil.isNumeric("12345"));
        assertFalse(stringUtil.isNumeric("abc123"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(stringUtil.isWhitespace(' '));
        assertTrue(stringUtil.isWhitespace('\t'));
        assertFalse(stringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(stringUtil.isActuallyWhitespace(' '));
        assertTrue(stringUtil.isActuallyWhitespace('\n'));
        assertFalse(stringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("hello world", stringUtil.normaliseWhitespace("hello      world"));
    }

    @Test
    public void testIn() {
        assertTrue(stringUtil.in("hello", "hello", "world"));
        assertFalse(stringUtil.in("abc", "def", "ghi"));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"abc", "def", "ghi"};
        assertTrue(stringUtil.inSorted("abc", haystack));
        assertFalse(stringUtil.inSorted("xyz", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "images/pic.jpg";
        URL result = stringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/images/pic.jpg", result.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com/";
        String relUrl = "images/pic.jpg";
        String result = stringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com/images/pic.jpg", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = stringUtil.stringBuilder();
        sb.append("hello");
        assertEquals("hello", sb.toString());
    }
}