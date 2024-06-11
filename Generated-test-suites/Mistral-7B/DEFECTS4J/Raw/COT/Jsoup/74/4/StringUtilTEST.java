package org.jsoup.helper.test;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.jsoup.helper.StringUtil.*;

public class StringUtilTest {

    @Test
    public void testJoin_Collection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String expected = "applebananacherry";
        String result = join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoin_Iterator() {
        String[] strings = {"apple", "banana", "cherry"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "applebananacherry";
        String result = join(iterator, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoin_Array() {
        String[] strings = {"apple", "banana", "cherry"};
        String expected = "applebananacherry";
        String result = join(strings, "");
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < padding.length; i++) {
            String paddingResult = padding(i + 1);
            String expected = padding[i + 1];
            assertEquals(expected, paddingResult);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPadding_NegativeWidth() {
        padding(-1);
    }

    @Test
    public void testIsBlank() {
        String test1 = null;
        String test2 = "";
        String test3 = " ";
        String test4 = "   ";
        String test5 = "   hello   ";
        String test6 = "hello";
        String test7 = "hello world";

        boolean[] expected = {true, true, true, true, true, false, false};
        for (int i = 0; i < expected.length; i++) {
            boolean result = isBlank(testString(i));
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIsNumeric() {
        String test1 = null;
        String test2 = "";
        String test3 = "123";
        String test4 = "123abc";
        String test5 = "ab123";
        String test6 = "123.45";
        String test7 = "123e45";

        boolean[] expected = {false, false, true, false, false, true, false};
        for (int i = 0; i < expected.length; i++) {
            boolean result = isNumeric(testString(i));
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIsWhitespace_Basic() {
        for (int i = 0; i < 9; i++) {
            String test = padding[i + 1];
            boolean expected = true;
            boolean result = isWhitespace(test.charAt(0));
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsWhitespace_NonBasic() {
        boolean[] expected = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        String[] tests = {"", "\t", "\n", "\f", "\r", "1", "a", "A", "!", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "<", "=", ">", "?", "@", "[", "]", "{", "|", "}", "\\", "^", "`", "~", " ", "\u00A0"};
        for (int i = 0; i < tests.length; i++) {
            String test = tests[i];
            boolean result = isWhitespace(test.charAt(0));
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIsActuallyWhitespace() {
        for (int i = 0; i < 9; i++) {
            String test = padding[i + 1];
            boolean expected = true;
            boolean result = isActuallyWhitespace(test.charAt(0));
            assertEquals(expected, result);
        }

        boolean[] expected = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, true};
        String[] tests = {"", "\t", "\n", "\f", "\r", "1", "a", "A", "!", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "<", "=", ">", "?", "@", "[", "]", "{", "|", "}", "\\", "^", "`", "~", " "};
        for (int i = 0; i < tests.length; i++) {
            String test = tests[i];
            boolean result = isActuallyWhitespace(test.charAt(0));
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testNormaliseWhitespace() {
        String test1 = " ";
        String test2 = "\t ";
        String test3 = "\n hello\n world ";
        String test4 = "\r\n hello\r world ";
        String test5 = "hello\tworld";
        String test6 = "hello world";
        String test7 = "hello world\n";

        String expected1 = " ";
        String expected2 = " ";
        String expected3 = "hello world";
        String expected4 = "hello world";
        String expected5 = "hello world";
        String expected6 = "hello world";
        String expected7 = "hello world";

        for (int i = 0; i < 7; i++) {
            String result = normaliseWhitespace(testString(i));
            assertEquals(expectedString(i), result);
        }
    }

    @Test
    public void testIn() {
        String[] haystack = {"hello", "world", "stringUtil", "test", "method", "Array", "iterator"};
        String needle1 = "test";
        String needle2 = "Array";
        String needle3 = "iterator";
        String needle4 = "testMethod";

        boolean[] expected = {true, true, true, false};

        for (int i = 0; i < 4; i++) {
            boolean result = in(needleString(i), haystack);
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"stringUtil", "test", "Array", "iterator", "hello", "world"};
        String needle = "Array";

        boolean expected = true;

        boolean result = inSorted(needleString(5), haystack);
        assertEquals(expected, result);
    }

    @Test
    public void testResolve_URL() {
        String base = "http://example.com";
        String relUrl1 = "http://another.com";
        String relUrl2 = "/relative.html";

        String expected1 = "http://another.com";
        String expected2 = "http://example.com/relative.html";

        String result1 = resolve(new URL(base), relUrl1);
        String result2 = resolve(base, relUrl2);

        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }

    @Test
    public void testResolve_String() {
        String baseUrl = "http://example.com";
        String relUrl1 = "http://another.com";
        String relUrl2 = "/relative.html";

        String expected1 = "http://another.com";
        String expected2 = "http://example.com/relative.html";

        String result1 = resolve(baseUrl, relUrl1);
        String result2 = resolve(baseUrl, relUrl2);

        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder stringBuilder1 = stringBuilder();
        StringBuilder stringBuilder2 = stringBuilder();

        String test = "test";

        stringBuilder1.append(test);
        stringBuilder2.append(test);

        String expected = test;
        String result1 = stringBuilder1.toString();
        String result2 = stringBuilder2.toString();

        assertEquals(expected, result1);
        assertEquals(expected, result2);

        stringBuilder1.delete(0, test.length());
        stringBuilder2.delete(0, test.length());

        int length = test.length();

        for (int i = 0; i < length; i++) {
            stringBuilder1.append(test.charAt(i));
            stringBuilder2.append(test.charAt(i));
        }

        for (int i = 0; i < length; i++) {
            char testChar = test.charAt(i);
            char resultChar1 = stringBuilder1.charAt(i);
            char resultChar2 = stringBuilder2.charAt(i);
            assertEquals(testChar, resultChar1);
            assertEquals(testChar, resultChar2);
        }
    }

    private String testString(int i) {
        if (i < 0 || i >= tests.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid test index: " + i);
        }
        return tests[i];
    }

    private String needleString(int i) {
        if (i < 0 || i >= needles.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid needle index: " + i);
        }
        return needles[i];
    }

    private String expectedString(int i) {
        if (i < 0 || i >= expectedStrings.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid expected string index: " + i);
        }
        return expectedStrings[i];
    }

    private String[] tests = {"", "\t", "\n", "\f", "\r", "1", "a", "A", "!", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "<", "=", ">", "?", "@", "[", "]", "{", "|", "}", "\\", "^", "`", "~", " ", "\u00A0"};
    private String[] needles = {"hello", "world", "stringUtil", "test", "method", "Array", "iterator"};
    private String[] expectedStrings = {" ", " ", "hello world", "hello world", "hello world", "hello world", "hello world"};

}