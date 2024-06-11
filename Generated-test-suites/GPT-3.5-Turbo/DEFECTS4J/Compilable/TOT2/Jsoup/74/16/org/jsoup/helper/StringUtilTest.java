package org.jsoup.helper;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import java.net.MalformedURLException;
import java.net.URL;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        String result = StringUtil.join(Arrays.asList("a", "b", "c"), ",");
        assertEquals("a,b,c", result);
    }

    @Test
    public void testJoinIterator() {
        String result = StringUtil.join(Arrays.asList("a", "b", "c").iterator(), ",");
        assertEquals("a,b,c", result);
    }

    @Test
    public void testJoinArray() {
        String result = StringUtil.join(new String[]{"a", "b", "c"}, ",");
        assertEquals("a,b,c", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("    a"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("abc12"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertFalse(StringUtil.isWhitespace('c'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("  a  b   c  ");
        assertEquals("a b c", result);
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("b", "a", "b", "c"));
        assertFalse(StringUtil.in("d", "a", "b", "c"));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("b", new String[]{"a", "b", "c"}));
        assertFalse(StringUtil.inSorted("d", new String[]{"a", "b", "c"}));
    }

    @Test
    public void testResolveUrl() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String result = StringUtil.resolve(base, "path/to/page.html").toString();
        assertEquals("https://www.example.com/path/to/page.html", result);
    }

    @Test
    public void testResolveString() {
        String result = StringUtil.resolve("https://www.example.com/", "path/to/page.html");
        assertEquals("https://www.example.com/path/to/page.html", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("test");
        assertEquals("test", sb.toString());
    }

}