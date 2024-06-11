
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class StringUtilTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"one", "two", "three", "four", "five"},
                {"apple", "banana", "cherry", "date"},
                {"", "", "", ""},
                {"space before", " space after ", " space at both ends ", " space in the middle "},
        });
    }

    private final String[] strings;

    public StringUtilTest(String[] strings) {
        this.strings = strings;
    }

    @Test
    public void testJoinCollection() {
        String expected = String.join(",", strings);
        String result = StringUtil.join(Arrays.asList(strings), ",");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = String.join(",", strings);
        String result = StringUtil.join(iterator, ",");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String expected = String.join(",", strings);
        String result = StringUtil.join(strings, ",");
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        int width = 10;
        String expected = StringUtil.padding(width);
        assertEquals(width, expected.length());
        for (char c : expected.toCharArray()) {
            assertEquals(' ', c);
        }
    }

    @Test
    public void testIsBlank() {
        String blank = "   ";
        assertTrue(StringUtil.isBlank(blank));
        for (String string : strings) {
            assertFalse(StringUtil.isBlank(string));
        }
    }

    @Test
    public void testIsNumeric() {
        String numeric = "1234567890";
        assertTrue(StringUtil.isNumeric(numeric));
        for (String string : strings) {
            assertFalse(StringUtil.isNumeric(string));
        }
    }

    @Test
    public void testIsWhitespace() {
        String whitespace = " \t\n\r\f";
        for (int i = 0; i < whitespace.length(); i++) {
            assertTrue(StringUtil.isWhitespace(whitespace.codePointAt(i)));
        }
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        String whitespace = " \t\n\r\f\u00A0";
        for (int i = 0; i < whitespace.length(); i++) {
            assertTrue(StringUtil.isActuallyWhitespace(whitespace.codePointAt(i)));
        }
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String separators = " \t\n\r\f";
        String string = StringUtil.join(Arrays.asList(strings), separators);

        String expected = string.replaceAll("[" + separators + "]+", " ");
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals(expected, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        String separators = " \t\n\r\f";
        StringBuilder expected = new StringBuilder();
        boolean lastWasWhite = false;
        boolean reachedNonWhite = false;

        String string = StringUtil.join(Arrays.asList(strings), separators);
        for (char c : string.toCharArray()) {
            if (StringUtil.isActuallyWhitespace(c)) {
                if (!reachedNonWhite || lastWasWhite) {
                    continue;
                }
                expected.append(' ');
                lastWasWhite = true;
            } else {
                expected.append(c);
                lastWasWhite = false;
                reachedNonWhite = true;
            }
        }

        StringBuilder result = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(result, string, true);

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testIn() {
        String[] haystack = {"apple", "banana", "cherry", "date"};

        for (String needle : haystack) {
            assertTrue(StringUtil.in(needle, haystack));
        }
        assertFalse(StringUtil.in("grape", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry", "date"};

        for (String needle : haystack) {
            assertTrue(StringUtil.inSorted(needle, haystack));
        }
        assertFalse(StringUtil.inSorted("grape", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "path/to/resource";
        URL resolved = new URL("https://www.example.com/path/to/resource");

        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(resolved, result);
    }

    @Test
    public void testResolveString() {
        String base = "https://www.example.com";
        String relUrl = "path/to/resource";
        String resolved = "https://www.example.com/path/to/resource";

        String result = StringUtil.resolve(base, relUrl);
        assertEquals(resolved, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();

        assertNotEquals(sb1, sb2);
        assertEquals(0, sb1.length());
        assertEquals(0, sb2.length());
    }
}

