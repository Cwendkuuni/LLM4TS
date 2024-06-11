import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    /*  */

    @Test
    public void testJoin_collectionAndString() {
        Collection<String> strings = new ArrayList<>(Arrays.asList("hello", "world"));
        String sep = ",";
        String expectedResult = "hello,world";
        String result = StringUtil.join(strings, sep);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoin_iteratorAndString() {
        Collection<String> strings = new ArrayList<>(Arrays.asList("hello", "world"));
        Iterator<String> iterator = strings.iterator();
        String sep = ",";
        String expectedResult = "hello,world";
        String result = StringUtil.join(iterator, sep);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoin_arrayAndString() {
        String[] strings = {"hello", "world"};
        String sep = ",";
        String expectedResult = "hello,world";
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
    public void testBlank_stringIsNull() {
        String string = null;
        boolean expectedResult = true;
        boolean result = StringUtil.isBlank(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testBlank_stringIsEmpty() {
        String string = "";
        boolean expectedResult = true;
        boolean result = StringUtil.isBlank(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testBlank_stringIsWhitespace() {
        String string = "    ";
        boolean expectedResult = true;
        boolean result = StringUtil.isBlank(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testBlank_stringIsNotBlank() {
        String string = "hello";
        boolean expectedResult = false;
        boolean result = StringUtil.isBlank(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testNumeric_stringIsNull() {
        String string = null;
        boolean expectedResult = false;
        boolean result = StringUtil.isNumeric(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testNumeric_stringIsEmpty() {
        String string = "";
        boolean expectedResult = false;
        boolean result = StringUtil.isNumeric(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testNumeric_stringContainsNonDigitChars() {
        String string = "123a";
        boolean expectedResult = false;
        boolean result = StringUtil.isNumeric(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testNumeric_stringIsNumeric() {
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
        String string = "hello  world";
        String expectedResult = "hello world";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "hello  world";
        boolean stripLeading = false;
        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
        String expectedResult = "hello world";
        String result = accum.toString();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIn_needleInHaystack() {
        String needle = "world";
        String[] haystack = {"hello", "world"};
        boolean expectedResult = true;
        boolean result = StringUtil.in(needle, haystack);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIn_needleNotInHaystack() {
        String needle = "foo";
        String[] haystack = {"hello", "world"};
        boolean expectedResult = false;
        boolean result = StringUtil.in(needle, haystack);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testInSorted_needleInHaystack() {
        String needle = "world";
        String[] haystack = {"hello", "world"};
        boolean expectedResult = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testInSorted_needleNotInHaystack() {
        String needle = "foo";
        String[] haystack = {"hello", "world"};
        boolean expectedResult = false;
        boolean result = StringUtil.inSorted(needle, haystack);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://example.com/");
        String relUrl = "foo";
        URL expectedResult = new URL("https://example.com/foo");
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testResolve_baseUrlMalformedURLException() {
        String baseUrl = "htt:/example.com/";
        String relUrl = "foo";
        String expectedResult = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testResolve_relUrlMalformedURLException() {
        String baseUrl = "https://example.com/";
        String relUrl = "htt:/example.com/";
        String expectedResult = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder result1 = StringUtil.stringBuilder();
        StringBuilder result2 = StringUtil.stringBuilder();
        assertSame(result1, result2);
    }

    /*  */

}