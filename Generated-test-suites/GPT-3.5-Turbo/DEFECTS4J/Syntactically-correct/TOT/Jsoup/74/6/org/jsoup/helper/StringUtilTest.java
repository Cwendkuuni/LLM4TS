
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    // Test cases for join(Collection strings, String sep)
    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("JUnit");
        strings.add("Test");

        String sep = " ";

        String expectedResult = "Hello JUnit Test";
        String actualResult = StringUtil.join(strings, sep);

        assertEquals(expectedResult, actualResult);
    }

    // Test cases for join(Iterator strings, String sep)
    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("JUnit");
        strings.add("Test");

        Iterator<String> iterator = strings.iterator();
        String sep = " ";

        String expectedResult = "Hello JUnit Test";
        String actualResult = StringUtil.join(iterator, sep);

        assertEquals(expectedResult, actualResult);
    }

    // Test cases for join(String[] strings, String sep)
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "JUnit", "Test"};

        String sep = " ";

        String expectedResult = "Hello JUnit Test";
        String actualResult = StringUtil.join(strings, sep);

        assertEquals(expectedResult, actualResult);
    }

    // Test cases for padding(int width)
    @Test
    public void testPadding() {
        int width = 10;

        String expectedResult = "          ";
        String actualResult = StringUtil.padding(width);

        assertEquals(expectedResult, actualResult);
    }

    // Test cases for isBlank(String string)
    @Test
    public void testIsBlank() {
        String blankString = "";
        String notBlankString = "Hello";

        assertTrue(StringUtil.isBlank(blankString));
        assertFalse(StringUtil.isBlank(notBlankString));
    }

    // Test cases for isNumeric(String string)
    @Test
    public void testIsNumeric() {
        String numericString = "12345";
        String nonNumericString = "abc123";

        assertTrue(StringUtil.isNumeric(numericString));
        assertFalse(StringUtil.isNumeric(nonNumericString));
    }

    // Test cases for isWhitespace(int c)
    @Test
    public void testIsWhitespace() {
        int whitespaceCharacter = ' ';
        int nonWhitespaceCharacter = 'a';

        assertTrue(StringUtil.isWhitespace(whitespaceCharacter));
        assertFalse(StringUtil.isWhitespace(nonWhitespaceCharacter));
    }

    // Test cases for isActuallyWhitespace(int c)
    @Test
    public void testIsActuallyWhitespace() {
        int whitespaceCharacter = ' ';
        int nonWhitespaceCharacter = 'a';

        assertTrue(StringUtil.isActuallyWhitespace(whitespaceCharacter));
        assertFalse(StringUtil.isActuallyWhitespace(nonWhitespaceCharacter));
    }

    // Test cases for normaliseWhitespace(String string)
    @Test
    public void testNormaliseWhitespace() {
        String stringWithMultipleSpaces = "Hello   JUnit          Test";
        String stringWithNewlines = "Hello\nJUnit\nTest";

        String expectedResult = "Hello JUnit Test";
        String actualResult = StringUtil.normaliseWhitespace(stringWithMultipleSpaces);

        assertEquals(expectedResult, actualResult);

        expectedResult = "Hello JUnit Test";
        actualResult = StringUtil.normaliseWhitespace(stringWithNewlines);

        assertEquals(expectedResult, actualResult);
    }

    // Test cases for in(String needle, String[] haystack)
    @Test
    public void testIn() {
        String needle = "JUnit";
        String[] haystack = {"Hello", "World", "JUnit", "Test"};

        assertTrue(StringUtil.in(needle, haystack));
    }

    // Test cases for inSorted(String needle, String[] haystack)
    @Test
    public void testInSorted() {
        String needle = "JUnit";
        String[] haystack = {"Foo", "Bar", "JUnit", "Test"};

        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    // Test cases for resolve(URL base, String relUrl)
    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://example.com/");
        String relUrl = "test.html";

        URL expectedResult = new URL("http://example.com/test.html");
        URL actualResult = StringUtil.resolve(base, relUrl);

        assertEquals(expectedResult, actualResult);
    }

    // Test cases for resolve(String baseUrl, String relUrl)
    @Test
    public void testResolveString() {
        String baseUrl = "http://example.com/";
        String relUrl = "test.html";

        String expectedResult = "http://example.com/test.html";
        String actualResult = StringUtil.resolve(baseUrl, relUrl);

        assertEquals(expectedResult, actualResult);
    }

    // Test cases for stringBuilder()
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();

        assertNotNull(sb);
        assertEquals(0, sb.length());
    }
}

