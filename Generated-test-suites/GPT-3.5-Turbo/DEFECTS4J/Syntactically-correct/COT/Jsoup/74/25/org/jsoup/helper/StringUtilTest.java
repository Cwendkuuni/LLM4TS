
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        // Arrange
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("Hello");
        strings.add("World");

        // Act
        String result = StringUtil.join(strings, ", ");

        // Assert
        assertEquals("Hello, World", result);
    }

    @Test
    public void testJoinIterator() {
        // Arrange
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("Hello");
        strings.add("World");

        // Act
        String result = StringUtil.join(strings.iterator(), ", ");

        // Assert
        assertEquals("Hello, World", result);
    }

    @Test
    public void testJoinArray() {
        // Arrange
        String[] strings = {"Hello", "World"};

        // Act
        String result = StringUtil.join(strings, ", ");

        // Assert
        assertEquals("Hello, World", result);
    }

    @Test
    public void testPadding() {
        // Arrange
        int width = 5;

        // Act
        String result = StringUtil.padding(width);

        // Assert
        assertEquals("     ", result);
    }

    @Test
    public void testIsBlank_NullString() {
        // Arrange
        String string = null;

        // Act
        boolean result = StringUtil.isBlank(string);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsBlank_EmptyString() {
        // Arrange
        String string = "";

        // Act
        boolean result = StringUtil.isBlank(string);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsBlank_WhitespaceString() {
        // Arrange
        String string = "   ";

        // Act
        boolean result = StringUtil.isBlank(string);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsBlank_NonBlankString() {
        // Arrange
        String string = "Hello World";

        // Act
        boolean result = StringUtil.isBlank(string);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsNumeric_NullString() {
        // Arrange
        String string = null;

        // Act
        boolean result = StringUtil.isNumeric(string);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsNumeric_EmptyString() {
        // Arrange
        String string = "";

        // Act
        boolean result = StringUtil.isNumeric(string);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsNumeric_NumericString() {
        // Arrange
        String string = "12345";

        // Act
        boolean result = StringUtil.isNumeric(string);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsNumeric_NonNumericString() {
        // Arrange
        String string = "Hello World";

        // Act
        boolean result = StringUtil.isNumeric(string);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsWhitespace_WhitespaceChar() {
        // Arrange
        int c = ' ';

        // Act
        boolean result = StringUtil.isWhitespace(c);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_NonWhitespaceChar() {
        // Arrange
        int c = 'A';

        // Act
        boolean result = StringUtil.isWhitespace(c);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespace_WhitespaceChar() {
        // Arrange
        int c = ' ';

        // Act
        boolean result = StringUtil.isActuallyWhitespace(c);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace_NonWhitespaceChar() {
        // Arrange
        int c = 'A';

        // Act
        boolean result = StringUtil.isActuallyWhitespace(c);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        // Arrange
        String string = "  Hello     World   ";

        // Act
        String result = StringUtil.normaliseWhitespace(string);

        // Assert
        assertEquals(" Hello World ", result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        // Arrange
        StringBuilder accum = new StringBuilder();
        String string = "  Hello     World   ";
        boolean stripLeading = false;

        // Act
        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);

        // Assert
        assertEquals("  Hello     World   ", accum.toString());
    }

    @Test
    public void testIn_InArray() {
        // Arrange
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};

        // Act
        boolean result = StringUtil.in(needle, haystack);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIn_NotInArray() {
        // Arrange
        String needle = "Foo";
        String[] haystack = {"Hello", "World"};

        // Act
        boolean result = StringUtil.in(needle, haystack);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testInSorted_InArray() {
        // Arrange
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};

        // Act
        boolean result = StringUtil.inSorted(needle, haystack);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testInSorted_NotInArray() {
        // Arrange
        String needle = "Foo";
        String[] haystack = {"Hello", "World"};

        // Act
        boolean result = StringUtil.inSorted(needle, haystack);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testResolve_withBaseURL() throws MalformedURLException {
        // Arrange
        URL base = new URL("http://example.com");
        String relUrl = "path/";

        // Act
        URL result = StringUtil.resolve(base, relUrl);

        // Assert
        assertEquals(new URL("http://example.com/path/"), result);
    }

    @Test
    public void testResolve_withoutBaseURL() {
        // Arrange
        String baseUrl = "http://example.com";
        String relUrl = "path/";

        // Act
        String result = StringUtil.resolve(baseUrl, relUrl);

        // Assert
        assertEquals("http://example.com/path/", result);
    }

    @Test
    public void testStringBuilder() {
        // Act
        StringBuilder result = StringUtil.stringBuilder();

        // Assert
        assertNotNull(result);
    }
}
