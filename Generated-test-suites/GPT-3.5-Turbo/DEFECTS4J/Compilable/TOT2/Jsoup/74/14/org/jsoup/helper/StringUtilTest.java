package org.jsoup.helper;
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollectionWithSeparator() {
        Collection<String> collection = Arrays.asList("Hello", "World");
        String joinedString = StringUtil.join(collection, ",");
        assertEquals("Hello,World", joinedString);
    }

    @Test
    public void testJoinIteratorWithSeparator() {
        Iterator<String> iterator = Arrays.asList("Hello", "World").iterator();
        String joinedString = StringUtil.join(iterator, ",");
        assertEquals("Hello,World", joinedString);
    }

    @Test
    public void testJoinArrayWithSeparator() {
        String[] strings = {"Hello", "World"};
        String joinedString = StringUtil.join(strings, ",");
        assertEquals("Hello,World", joinedString);
    }

    @Test
    public void testPadding() {
        String paddedString = StringUtil.padding(5);
        assertEquals("     ", paddedString);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("Hello123"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertFalse(StringUtil.isWhitespace('A'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalizedString = StringUtil.normaliseWhitespace("  Hello   World     ");
        assertEquals(" Hello World ", normalizedString);
    }

    @Test
    public void testIn() {
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.in("Hello", haystack));
        assertFalse(StringUtil.in("Java", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.inSorted("Hello", haystack));
        assertFalse(StringUtil.inSorted("Java", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("https://example.com");
        String relUrl = "/page";
        URL resolvedUrl = StringUtil.resolve(base, relUrl);
        assertEquals("https://example.com/page", resolvedUrl.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://example.com";
        String relUrl = "/page";
        String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://example.com/page", resolvedUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("Hello");
        assertEquals("Hello", sb.toString());
    }

}