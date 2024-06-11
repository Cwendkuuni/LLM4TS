
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
        String sep = "-";
        String expectedResult = "Hello-World";

        assertEquals(expectedResult, StringUtil.join(strings, sep));
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";
        String expectedResult = "Hello-World";

        assertEquals(expectedResult, StringUtil.join(strings.iterator(), sep));
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        String expectedResult = "Hello-World";

        assertEquals(expectedResult, StringUtil.join(strings, sep));
    }

    @Test
    public void testPadding() {
        int width = 3;
        String expectedResult = "   ";

        assertEquals(expectedResult, StringUtil.padding(width));
    }

    @Test
    public void testIsBlank() {
        String blankString = "";
        String nonBlankString = "Hello";
        assertTrue(StringUtil.isBlank(blankString));
        assertFalse(StringUtil.isBlank(nonBlankString));
    }

    @Test
    public void testIsNumeric() {
        String numericString = "123";
        String nonNumericString = "Hello";
        assertTrue(StringUtil.isNumeric(numericString));
        assertFalse(StringUtil.isNumeric(nonNumericString));
    }

    @Test
    public void testIsWhitespace() {
        int whitespaceCodePoint = ' ';
        int nonWhitespaceCodePoint = 'A';
        assertTrue(StringUtil.isWhitespace(whitespaceCodePoint));
        assertFalse(StringUtil.isWhitespace(nonWhitespaceCodePoint));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int whitespaceCodePoint = ' ';
        int nonWhitespaceCodePoint = 'A';
        assertTrue(StringUtil.isActuallyWhitespace(whitespaceCodePoint));
        assertFalse(StringUtil.isActuallyWhitespace(nonWhitespaceCodePoint));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "  Hello   World  ";
        String expectedResult = "Hello World";

        assertEquals(expectedResult, StringUtil.normaliseWhitespace(string));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "  Hello   World  ";
        String expectedResult = "Hello World";

        StringUtil.appendNormalisedWhitespace(accum, string, true);

        assertEquals(expectedResult, accum.toString());
    }

    @Test
    public void testIn() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolve() {
        try {
            URL base = new URL("https://www.example.com/");
            String relUrl = "../page.html";
            URL expectedResult = new URL("https://www.example.com/page.html");

            assertEquals(expectedResult, StringUtil.resolve(base, relUrl));
        } catch (MalformedURLException e) {
            fail("MalformedURLException occurred");
        }
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com/";
        String relUrl = "../page.html";
        String expectedResult = "https://www.example.com/page.html";

        assertEquals(expectedResult, StringUtil.resolve(baseUrl, relUrl));
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }

}
