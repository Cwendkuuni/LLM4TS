
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = ", ";
        String expectedResult = "Hello, World";

        String result = StringUtil.join(strings, sep);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        Iterator<String> iterator = strings.iterator();
        String sep = ", ";
        String expectedResult = "Hello, World";

        String result = StringUtil.join(iterator, sep);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String sep = ", ";
        String expectedResult = "Hello, World";

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
        String string1 = null;
        boolean expected1 = true;

        boolean result1 = StringUtil.isBlank(string1);
        assertEquals(expected1, result1);

        String string2 = "";
        boolean expected2 = true;

        boolean result2 = StringUtil.isBlank(string2);
        assertEquals(expected2, result2);

        String string3 = "  \t  ";
        boolean expected3 = true;

        boolean result3 = StringUtil.isBlank(string3);
        assertEquals(expected3, result3);

        String string4 = "Hello";
        boolean expected4 = false;

        boolean result4 = StringUtil.isBlank(string4);
        assertEquals(expected4, result4);
    }

    @Test
    public void testIsNumeric() {
        String string1 = null;
        boolean expected1 = false;

        boolean result1 = StringUtil.isNumeric(string1);
        assertEquals(expected1, result1);

        String string2 = "";
        boolean expected2 = false;

        boolean result2 = StringUtil.isNumeric(string2);
        assertEquals(expected2, result2);

        String string3 = "12345";
        boolean expected3 = true;

        boolean result3 = StringUtil.isNumeric(string3);
        assertEquals(expected3, result3);

        String string4 = "Hello123";
        boolean expected4 = false;

        boolean result4 = StringUtil.isNumeric(string4);
        assertEquals(expected4, result4);
    }

    @Test
    public void testIsWhitespace() {
        int c1 = ' ';
        boolean expected1 = true;

        boolean result1 = StringUtil.isWhitespace(c1);
        assertEquals(expected1, result1);

        int c2 = '\t';
        boolean expected2 = true;

        boolean result2 = StringUtil.isWhitespace(c2);
        assertEquals(expected2, result2);

        int c3 = '\n';
        boolean expected3 = true;

        boolean result3 = StringUtil.isWhitespace(c3);
        assertEquals(expected3, result3);

        int c4 = '\f';
        boolean expected4 = true;

        boolean result4 = StringUtil.isWhitespace(c4);
        assertEquals(expected4, result4);

        int c5 = '\r';
        boolean expected5 = true;

        boolean result5 = StringUtil.isWhitespace(c5);
        assertEquals(expected5, result5);
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c1 = ' ';
        boolean expected1 = true;

        boolean result1 = StringUtil.isActuallyWhitespace(c1);
        assertEquals(expected1, result1);

        int c2 = '\t';
        boolean expected2 = true;

        boolean result2 = StringUtil.isActuallyWhitespace(c2);
        assertEquals(expected2, result2);

        int c3 = '\n';
        boolean expected3 = true;

        boolean result3 = StringUtil.isActuallyWhitespace(c3);
        assertEquals(expected3, result3);

        int c4 = '\f';
        boolean expected4 = true;

        boolean result4 = StringUtil.isActuallyWhitespace(c4);
        assertEquals(expected4, result4);

        int c5 = '\r';
        boolean expected5 = true;

        boolean result5 = StringUtil.isActuallyWhitespace(c5);
        assertEquals(expected5, result5);

        int c6 = 160;
        boolean expected6 = true;

        boolean result6 = StringUtil.isActuallyWhitespace(c6);
        assertEquals(expected6, result6);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "Hello   World\nHow\tare\fyou?";
        String expectedResult = "Hello World How are you?";

        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIn() {
        String needle = "apple";
        String[] haystack = {"apple", "banana", "orange"};

        boolean result1 = StringUtil.in(needle, haystack);
        assertTrue(result1);

        String needle2 = "grape";
        String[] haystack2 = {"apple", "banana", "orange"};

        boolean result2 = StringUtil.in(needle2, haystack2);
        assertFalse(result2);
    }

    @Test
    public void testInSorted() {
        String needle = "banana";
        String[] haystack = {"apple", "banana", "orange"};

        boolean result1 = StringUtil.inSorted(needle, haystack);
        assertTrue(result1);

        String needle2 = "grape";
        String[] haystack2 = {"apple", "banana", "orange"};

        boolean result2 = StringUtil.inSorted(needle2, haystack2);
        assertFalse(result2);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com/path/");
        String relUrl = "../file.html";
        String expectedResult = "http://www.example.com/file.html";

        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(expectedResult, result.toExternalForm());
    }

    @Test
    public void testResolve2() {
        String baseUrl = "http://www.example.com/path/";
        String relUrl = "../file.html";
        String expectedResult = "http://www.example.com/file.html";

        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        assertEquals(0, sb.length());

        sb.append("Hello");
        assertEquals("Hello", sb.toString());
    }
}

