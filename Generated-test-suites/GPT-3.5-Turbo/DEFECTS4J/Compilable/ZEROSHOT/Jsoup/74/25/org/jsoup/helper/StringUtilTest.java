package org.jsoup.helper;
import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.jsoup.helper.StringUtil;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("one", "two", "three");
        String sep = "-";
        String expectedResult = "one-two-three";

        String result = StringUtil.join(strings, sep);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = strings.iterator();
        String sep = "-";
        String expectedResult = "one-two-three";

        String result = StringUtil.join(iterator, sep);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoinStringArray() {
        String[] strings = {"one", "two", "three"};
        String sep = "-";
        String expectedResult = "one-two-three";

        String result = StringUtil.join(strings, sep);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expectedResult = "     ";

        String result = StringUtil.padding(width);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsBlank() {
        String string = " ";
        boolean expectedResult = true;

        boolean result = StringUtil.isBlank(string);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsNumeric() {
        String string = "123";
        boolean expectedResult = true;

        boolean result = StringUtil.isNumeric(string);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsWhitespace() {
        int c = ' ';
        boolean expectedResult = true;

        boolean result = StringUtil.isWhitespace(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c = ' ';
        boolean expectedResult = true;

        boolean result = StringUtil.isActuallyWhitespace(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "a   b\n\tc";
        String expectedResult = "a b c";

        String result = StringUtil.normaliseWhitespace(string);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "a   b\n\tc";
        boolean stripLeading = false;
        String expectedResult = "a b c";

        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
        String result = accum.toString();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIn() {
        String needle = "two";
        String[] haystack = {"one", "two", "three"};
        boolean expectedResult = true;

        boolean result = StringUtil.in(needle, haystack);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testInSorted() {
        String needle = "two";
        String[] haystack = {"one", "two", "three"};
        boolean expectedResult = true;

        boolean result = StringUtil.inSorted(needle, haystack);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com/");
        String relUrl = "test.html";
        URL expectedResult = new URL("http://www.example.com/test.html");

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testResolveString() {
        String baseUrl = "http://www.example.com/";
        String relUrl = "test.html";
        String expectedResult = "http://www.example.com/test.html";

        String result = StringUtil.resolve(baseUrl, relUrl);

        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();

        assertNotNull(sb);
        assertEquals("", sb.toString());
    }
}