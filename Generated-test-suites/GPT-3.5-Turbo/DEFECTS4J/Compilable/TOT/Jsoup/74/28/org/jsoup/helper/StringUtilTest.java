package org.jsoup.helper;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<String>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String joinedString = StringUtil.join(strings, ",");
        assertEquals("Hello,World,!", joinedString);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<String>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        Iterator<String> iterator = strings.iterator();

        String joinedString = StringUtil.join(iterator, ",");
        assertEquals("Hello,World,!", joinedString);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};

        String joinedString = StringUtil.join(strings, ",");
        assertEquals("Hello,World,!", joinedString);
    }

    @Test
    public void testPadding() {
        String padding = StringUtil.padding(5);
        assertEquals("     ", padding);
    }

    @Test
    public void testIsBlankNullString() {
        assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlankEmptyString() {
        assertTrue(StringUtil.isBlank(""));
    }

    @Test
    public void testIsBlankWhitespaceString() {
        assertTrue(StringUtil.isBlank("   "));
    }

    @Test
    public void testIsBlankNonBlankString() {
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumericNullString() {
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsNumericEmptyString() {
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsNumericNumericString() {
        assertTrue(StringUtil.isNumeric("123"));
    }

    @Test
    public void testIsNumericNonNumericString() {
        assertFalse(StringUtil.isNumeric("Hello"));
    }

    @Test
    public void testIsWhitespaceSpace() {
        assertTrue(StringUtil.isWhitespace(' '));
    }

    @Test
    public void testIsWhitespaceTab() {
        assertTrue(StringUtil.isWhitespace('\t'));
    }

    @Test
    public void testIsWhitespaceNewline() {
        assertTrue(StringUtil.isWhitespace('\n'));
    }
    
    @Test
    public void testIsWhitespaceFormFeed() {
        assertTrue(StringUtil.isWhitespace('\f'));
    }
    
    @Test
    public void testIsWhitespaceCarriageReturn() {
        assertTrue(StringUtil.isWhitespace('\r'));
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String normalized = StringUtil.normaliseWhitespace("Hello  World");
        assertEquals("Hello World", normalized);
    }

    @Test
    public void testNormaliseWhitespaceMultipleWhitespaceCharacters() {
        String normalized = StringUtil.normaliseWhitespace("Hello\nWorld\t!");
        assertEquals("Hello World !", normalized);
    }

    @Test
    public void testInStringInHaystack() {
        String[] haystack = {"Hello", "World", "!"};

        boolean isIn = StringUtil.in("Hello", haystack);
        assertTrue(isIn);
    }

    @Test
    public void testInStringNotInHaystack() {
        String[] haystack = {"Hello", "World", "!"};

        boolean isIn = StringUtil.in("Goodbye", haystack);
        assertFalse(isIn);
    }

    @Test
    public void testInSortedStringInHaystack() {
        String[] haystack = {"!", "Hello", "World"};

        boolean isIn = StringUtil.inSorted("Hello", haystack);
        assertTrue(isIn);
    }

    @Test
    public void testInSortedStringNotInHaystack() {
        String[] haystack = {"!", "Hello", "World"};

        boolean isIn = StringUtil.inSorted("Goodbye", haystack);
        assertFalse(isIn);
    }

    @Test
    public void testResolveFirstURLString() {
        String base = "https://www.example.com";
        String relUrl = "/path/to/file.html";

        String resolvedUrl = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/path/to/file.html", resolvedUrl);
    }

    @Test
    public void testResolveFirstURLMalformedURLException() {
        String base = "https://www.example.com";
        String relUrl = "https://www.example.com";

        String resolvedUrl = StringUtil.resolve(base, relUrl);
        assertEquals("", resolvedUrl);
    }

    @Test
    public void testResolveSecondURLString() {
        String base = "https://www.example.com";
        String relUrl = "https://www.example.com/path/to/file.html";

        String resolvedUrl = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/path/to/file.html", resolvedUrl);
    }

    @Test
    public void testResolveSecondURLMalformedURLException() {
        String base = "https://www.example.com";
        String relUrl = "not-a-url";

        String resolvedUrl = StringUtil.resolve(base, relUrl);
        assertEquals("", resolvedUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
    }

}