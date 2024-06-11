package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {
    private StringUtil stringUtil;

    @Before
    public void setUp() {
        stringUtil = new StringUtil();
    }

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>(Arrays.asList("string1", "string2", "string3"));
        String sep = ", ";

        String joinedString = stringUtil.join(strings, sep);
        assertEquals("string1, string2, string3", joinedString);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>(Arrays.asList("string1", "string2", "string3"));
        String sep = ", ";
        Iterator<String> iterator = strings.iterator();

        String joinedString = stringUtil.join(iterator, sep);
        assertEquals("string1, string2, string3", joinedString);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"string1", "string2", "string3"};
        String sep = ", ";

        String joinedString = stringUtil.join(strings, sep);
        assertEquals("string1, string2, string3", joinedString);
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expectedPadding = "     ";

        String padding = stringUtil.padding(width);
        assertEquals(expectedPadding, padding);
    }

    @Test
    public void testIsBlank() {
        String blankString = "";
        String nonBlankString = "string";

        assertTrue(stringUtil.isBlank(blankString));
        assertFalse(stringUtil.isBlank(nonBlankString));
    }

    @Test
    public void testIsNumeric() {
        String numericString = "12345";
        String nonNumericString = "string";

        assertTrue(stringUtil.isNumeric(numericString));
        assertFalse(stringUtil.isNumeric(nonNumericString));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(stringUtil.isWhitespace(' '));
        assertTrue(stringUtil.isWhitespace('\t'));
        assertTrue(stringUtil.isWhitespace('\n'));
        assertTrue(stringUtil.isWhitespace('\f'));
        assertTrue(stringUtil.isWhitespace('\r'));
        assertFalse(stringUtil.isWhitespace('a'));
        assertFalse(stringUtil.isWhitespace('1'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(stringUtil.isActuallyWhitespace(' '));
        assertTrue(stringUtil.isActuallyWhitespace('\t'));
        assertTrue(stringUtil.isActuallyWhitespace('\n'));
        assertTrue(stringUtil.isActuallyWhitespace('\f'));
        assertTrue(stringUtil.isActuallyWhitespace('\r'));
        assertTrue(stringUtil.isActuallyWhitespace(160));
        assertFalse(stringUtil.isActuallyWhitespace('a'));
        assertFalse(stringUtil.isActuallyWhitespace('1'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "This   is\na\ntest.";
        String expectedNormalisedString = "This is a test.";

        String normalisedString = stringUtil.normaliseWhitespace(string);
        assertEquals(expectedNormalisedString, normalisedString);
    }

    @Test
    public void testIn() {
        String needle = "string1";
        String[] haystack = {"string1", "string2", "string3"};

        assertTrue(stringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "string2";
        String[] haystack = {"string1", "string2", "string3"};

        assertTrue(stringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolve() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/path/to/page";

        try {
            URL resolvedUrl = stringUtil.resolve(new URL(baseUrl), relUrl);
            assertEquals(baseUrl + relUrl, resolvedUrl.toString());
        } catch (MalformedURLException e) {
            fail("MalformedURLException should not be thrown");
        }
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/path/to/page";

        String resolvedUrl = stringUtil.resolve(baseUrl, relUrl);
        assertEquals(baseUrl + relUrl, resolvedUrl);
    }
}