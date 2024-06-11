package org.jsoup.helper;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Test class for StringUtil.
 */
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("hello", "world");
        String expected = "helloworld";
        String result = StringUtil.join(list, "");
        assertEquals(expected, result);

        list = Collections.emptyList();
        expected = "";
        result = StringUtil.join(list, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        String[] array = {"hello", "world"};
        List<String> list = Arrays.asList(array);
        String expected = "helloworld";
        String result = StringUtil.join(list.iterator(), "");
        assertEquals(expected, result);

        list = Collections.emptyList();
        expected = "";
        result = StringUtil.join(list.iterator(), "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] array = {"hello", "world"};
        String expected = "helloworld";
        String result = StringUtil.join(array, "");
        assertEquals(expected, result);

        array = new String[0];
        expected = "";
        result = StringUtil.join(array, "");
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < 21; i++) {
            String expected = padding(i);
            String result = StringUtil.padding(i);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsBlank() {
        String test0 = null;
        String test1 = "";
        String test2 = " ";
        String test3 = "   ";
        String test4 = "Hello";
        String test5 = "Hello  ";
        String test6 = "Hello\n";
        String test7 = "Hello\nWorld";
        String test8 = "Hello\n World";
        String test9 = "Hello World";
        String test10 = "Hello World!";

        boolean[] expected = {true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        for (int i = 0; i < expected.length; i++) {
            boolean result = StringUtil.isBlank(testString(i));
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIsNumeric() {
        String[] tests = {"123", "1A2B3C", "123abc", "123", "123", "123!", "123.", "123.4", "123.45", "123.456", "123.456.", "123.456.7", "123e4", "0x123"};
        boolean[] expected = {true, false, false, true, true, true, true, true, true, true, true, true, true, true, true};
        for (int i = 0; i < tests.length; i++) {
            boolean result = StringUtil.isNumeric(tests[i]);
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIsWhitespace() {
        int[] codes = {0x0020, 0x0009, 0x000A, 0x000B, 0x000C, 0x000D, 0x0085};
        boolean[] expected = {true, true, true, true, true, true, true};
        for (int i = 0; i < codes.length; i++) {
            boolean result = StringUtil.isWhitespace(codes[i]);
            assertEquals(expected[i], result);
        }

        int codePoint = '\u00A0'; // no-break space
        boolean result = StringUtil.isWhitespace(codePoint);
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        int[] codes = {0x0020, 0x0009, 0x000A, 0x000B, 0x000C, 0x000D, 0x0085, 0x00A0};
        boolean[] expected = {true, true, true, true, true, true, true, true};
        for (int i = 0; i < codes.length; i++) {
            boolean result = StringUtil.isActuallyWhitespace(codes[i]);
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testNormaliseWhitespace() {
        String[] inputs = {"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ",
            "         ", "          ", "           ", "            ", "             ", "              ", "               ",
            "                ", "                 ", "                  ", "                   ", "                    ",
            "a", "a b", "a b  c", "a b c d", "a b c d e", "a b c d e f", "a b c d e f g h i j k l m n o p q r s t u v w x y z", "a b c d e f g h i j k l m n o p q r s t u v w x y z  ",
            "a\nab\tc\r\n b\td\r\n c\td\r\n"};
        String[] expected = {"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ",
            "         ", "          ", "           ", "            ", "             ", "              ", "               ",
            "                ", "                 ", "                  ", "                   ", "                    ",
            "a", "ab", "ab c", "abcde", "abcdef", "abcdefg", "abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz",
            "a\nb\tc\rd\n b\td\rd\n c\td\rd\n"};
        for (int i = 0; i < inputs.length; i++) {
            String result = StringUtil.normaliseWhitespace(inputs[i]);
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testIn() {
        String[] haystack = {"apple", "banana", "cherry"};
        String[] needles = {"apple", "banana", "cherry", "orange"};
        boolean[] expected = {true, true, true, false};
        for (int i = 0; i < needles.length; i++) {
            boolean result = StringUtil.in(needles[i], haystack);
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry"};
        String[] needles = {"cherry", "banana", "apple"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needles[0], haystack);
        assertEquals(expected, result);
    }

    @Test
    public void testResolve() {
        String baseUrl = "http://www.example.com/";
        String relUrl1 = "about.html";
        String relUrl2 = "../index.html";
        String relUrl3 = "/path/to/file.html";
        String relUrl4 = "/path/to/file.html?query=test";
        String relUrl5 = "./path/to/file.html";
        String relUrl6 = "//subdomain.example.com/path/to/file.html";
        String relUrl7 = "//subdomain.example.com/path/to/file.html?query=test";

        String expected1 = "http://www.example.com/about.html";
        String expected2 = "http://www.example.com/index.html";
        String expected3 = "http://www.example.com/path/to/file.html";
        String expected4 = "http://www.example.com/path/to/file.html?query=test";
        String expected5 = "http://www.example.com/path/to/file.html";
        String expected6 = "http://subdomain.example.com/path/to/file.html";
        String expected7 = "http://subdomain.example.com/path/to/file.html?query=test";

        try {
            URL result1 = StringUtil.resolve(new URL(baseUrl), relUrl1);
            assertEquals(expected1, result1.toExternalForm());

            URL result2 = StringUtil.resolve(new URL(baseUrl), relUrl2);
            assertEquals(expected2, result2.toExternalForm());

            URL result3 = StringUtil.resolve(new URL(baseUrl), relUrl3);
            assertEquals(expected3, result3.toExternalForm());

            URL result4 = StringUtil.resolve(new URL(baseUrl), relUrl4);
            assertEquals(expected4, result4.toExternalForm());

            URL result5 = StringUtil.resolve(new URL(baseUrl), relUrl5);
            assertEquals(expected5, result5.toExternalForm());

            URL result6 = StringUtil.resolve(new URL(baseUrl), relUrl6);
            assertEquals(expected6, result6.toExternalForm());

            URL result7 = StringUtil.resolve(new URL(baseUrl), relUrl7);
            assertEquals(expected7, result7.toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("Malformed URLException thrown");
        }
    }

    private String testString(int i) {
        switch (i) {
            case 0: return null;
            case 1: return "";
            case 2: return " ";
            case 3: return "   ";
            case 4: return "Hello";
            case 5: return "Hello  ";
            case 6: return "Hello\n";
            case 7: return "Hello\nWorld";
            case 8: return "Hello\n World";
            case 9: return "Hello World";
            case 10: return "Hello World!";
            default: throw new RuntimeException("Out of bounds");
        }
    }

}