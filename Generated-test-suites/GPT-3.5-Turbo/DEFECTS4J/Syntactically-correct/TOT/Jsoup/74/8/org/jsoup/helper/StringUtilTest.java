import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinWithCollectionAndSeparator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("JUnit");
        strings.add("4");

        String result = StringUtil.join(strings, "-");

        assertEquals("Hello-World-JUnit-4", result);
    }

    @Test
    public void testJoinWithIteratorAndSeparator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("JUnit");
        strings.add("4");

        Iterator<String> iterator = strings.iterator();
        String result = StringUtil.join(iterator, "-");

        assertEquals("Hello-World-JUnit-4", result);
    }

    @Test
    public void testJoinWithArrayAndSeparator() {
        String[] strings = {"Hello", "World", "JUnit", "4"};

        String result = StringUtil.join(strings, "-");

        assertEquals("Hello-World-JUnit-4", result);
    }

    @Test
    public void testPaddingWithPositiveWidth() {
        int width = 10;

        String result = StringUtil.padding(width);

        assertEquals("          ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingWithNegativeWidth() {
        int width = -1;

        StringUtil.padding(width);
    }

    @Test
    public void testIsBlankWithNullString() {
        String string = null;

        boolean result = StringUtil.isBlank(string);

        assertTrue(result);
    }

    @Test
    public void testIsBlankWithEmptyString() {
        String string = "";

        boolean result = StringUtil.isBlank(string);

        assertTrue(result);
    }

    @Test
    public void testIsBlankWithWhitespace() {
        String string = "   ";

        boolean result = StringUtil.isBlank(string);

        assertTrue(result);
    }

    @Test
    public void testIsBlankWithNonBlankString() {
        String string = "Hello World";

        boolean result = StringUtil.isBlank(string);

        assertFalse(result);
    }

    @Test
    public void testIsNumericWithNullString() {
        String string = null;

        boolean result = StringUtil.isNumeric(string);

        assertFalse(result);
    }

    @Test
    public void testIsNumericWithEmptyString() {
        String string = "";

        boolean result = StringUtil.isNumeric(string);

        assertFalse(result);
    }

    @Test
    public void testIsNumericWithNumericString() {
        String string = "12345";

        boolean result = StringUtil.isNumeric(string);

        assertTrue(result);
    }

    @Test
    public void testIsNumericWithNonNumericString() {
        String string = "Hello123";

        boolean result = StringUtil.isNumeric(string);

        assertFalse(result);
    }

    @Test
    public void testIsWhitespaceWithWhitespaceCharacter() {
        int c = ' ';

        boolean result = StringUtil.isWhitespace(c);

        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithNonWhitespaceCharacter() {
        int c = 'a';

        boolean result = StringUtil.isWhitespace(c);

        assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithWhitespaceCharacter() {
        int c = ' ';

        boolean result = StringUtil.isActuallyWhitespace(c);

        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithNonWhitespaceCharacter() {
        int c = 'a';

        boolean result = StringUtil.isActuallyWhitespace(c);

        assertFalse(result);
    }

    @Test
    public void testNormaliseWhitespaceWithNormalString() {
        String string = "Hello World";

        String result = StringUtil.normaliseWhitespace(string);

        assertEquals("Hello World", result);
    }

    @Test
    public void testNormaliseWhitespaceWithLeadingWhitespace() {
        String string = "  Hello World";

        String result = StringUtil.normaliseWhitespace(string);

        assertEquals("Hello World", result);
    }

    @Test
    public void testNormaliseWhitespaceWithMultipleSpaces() {
        String string = "Hello     World";

        String result = StringUtil.normaliseWhitespace(string);

        assertEquals("Hello World", result);
    }

    @Test
    public void testAppendNormalisedWhitespaceWithNormalString() {
        StringBuilder accum = new StringBuilder();
        String string = "Hello World";

        StringUtil.appendNormalisedWhitespace(accum, string, false);

        assertEquals("Hello World", accum.toString());
    }

    @Test
    public void testAppendNormalisedWhitespaceWithLeadingWhitespace() {
        StringBuilder accum = new StringBuilder();
        String string = "  Hello World";

        StringUtil.appendNormalisedWhitespace(accum, string, true);

        assertEquals("Hello World", accum.toString());
    }

    @Test
    public void testAppendNormalisedWhitespaceWithMultipleSpaces() {
        StringBuilder accum = new StringBuilder();
        String string = "Hello     World";

        StringUtil.appendNormalisedWhitespace(accum, string, false);

        assertEquals("Hello World", accum.toString());
    }

    @Test
    public void testInWithMatchingString() {
        String needle = "World";
        String[] haystack = {"Hello", "World", "JUnit", "4"};

        boolean result = StringUtil.in(needle, haystack);

        assertTrue(result);
    }

    @Test
    public void testInWithNonMatchingString() {
        String needle = "Hello";
        String[] haystack = {"World", "JUnit", "4"};

        boolean result = StringUtil.in(needle, haystack);

        assertFalse(result);
    }

    @Test
    public void testInSortedWithMatchingString() {
        String needle = "World";
        String[] haystack = {"Hello", "JUnit", "World", "4"};

        boolean result = StringUtil.inSorted(needle, haystack);

        assertTrue(result);
    }

    @Test
    public void testInSortedWithNonMatchingString() {
        String needle = "Hello";
        String[] haystack = {"World", "JUnit", "4"};

        boolean result = StringUtil.inSorted(needle, haystack);

        assertFalse(result);
    }

    @Test
    public void testResolveWithValidRelativeURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "about";

        URL result = StringUtil.resolve(base, relUrl);

        assertNotNull(result);
        assertEquals("https://www.example.com/about", result.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithInvalidRelativeURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com/");
        String relUrl = "invalid";

        StringUtil.resolve(base, relUrl);
    }

    @Test
    public void testResolveStringWithValidRelativeURL() {
        String baseUrl = "https://www.example.com/";
        String relUrl = "about";

        String result = StringUtil.resolve(baseUrl, relUrl);

        assertNotNull(result);
        assertEquals("https://www.example.com/about", result);
    }

    @Test
    public void testResolveStringWithInvalidRelativeURL() {
        String baseUrl = "https://www.example.com/";
        String relUrl = "invalid";

        String result = StringUtil.resolve(baseUrl, relUrl);

        assertEquals("", result);
    }

    @Test
    public void testStringBuilderWithEmptyStringBuilder() {
        StringBuilder sb = new StringBuilder();

        StringBuilder result = StringUtil.stringBuilder();

        assertSame(sb, result);
        assertEquals(0, sb.length());
     }

    @Test
    public void testStringBuilderWithNonEmptyStringBuilder() {
        StringBuilder sb = new StringBuilder("Hello World");

        StringBuilder result = StringUtil.stringBuilder();

        assertSame(sb, result);
        assertEquals(0, sb.length());
    }

}