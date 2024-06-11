package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.util.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String sep = ",";
        assertEquals("Hello,World,!", StringUtil.join(strings, sep));
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("Hello", "World", "!").iterator();
        String sep = ",";
        assertEquals("Hello,World,!", StringUtil.join(strings, sep));
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String sep = ",";
        assertEquals("Hello,World,!", StringUtil.join(strings, sep));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testBlankString() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testNumericString() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("123a45"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testWhitespaceCharacters() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("Hello World !", StringUtil.normaliseWhitespace("Hello   World \t!"));
    }

    @Test
    public void testInMethod() {
        String[] haystack = {"Hello", "World", "!"};
        assertTrue(StringUtil.in("World", haystack));
        assertFalse(StringUtil.in("Test", haystack));
    }

    @Test
    public void testInSortedMethod() {
        String[] haystack = {"!", "Hello", "World"};
        assertTrue(StringUtil.inSorted("World", haystack));
        assertFalse(StringUtil.inSorted("Test", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "/path";
        assertEquals(new URL("https://www.example.com/path"), StringUtil.resolve(base, relUrl));
    }

    @Test
    public void testResolveStringBaseURL() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/path";
        assertEquals("https://www.example.com/path", StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("Hello");
        assertEquals("Hello", sb.toString());
    }

}