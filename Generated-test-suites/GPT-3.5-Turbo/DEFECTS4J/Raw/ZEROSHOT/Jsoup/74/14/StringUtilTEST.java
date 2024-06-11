import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringUtilTest {

    private StringUtil stringUtil;

    @BeforeEach
    public void setUp() {
        stringUtil = new StringUtil();
    }

    @Test
    public void testJoinCollectionWithStringSeparator() {
        Collection<String> collection = new ArrayList<>();
        collection.add("Hello");
        collection.add("World");
        String result = stringUtil.join(collection, ", ");
        assertEquals("Hello, World", result);
    }

    @Test
    public void testJoinIteratorWithStringSeparator() {
        Collection<String> collection = new ArrayList<>();
        collection.add("Hello");
        collection.add("World");
        Iterator<String> iterator = collection.iterator();
        String result = stringUtil.join(iterator, ", ");
        assertEquals("Hello, World", result);
    }

    @Test
    public void testJoinArrayWithStringSeparator() {
        String[] array = {"Hello", "World"};
        String result = stringUtil.join(array, ", ");
        assertEquals("Hello, World", result);
    }

    @Test
    public void testPaddingWithPositiveWidth() {
        String result = stringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test
    public void testPaddingWithZeroWidth() {
        String result = stringUtil.padding(0);
        assertEquals("", result);
    }

    @Test
    public void testIsBlankWithNullString() {
        boolean result = stringUtil.isBlank(null);
        assertTrue(result);
    }

    @Test
    public void testIsBlankWithEmptyString() {
        boolean result = stringUtil.isBlank("");
        assertTrue(result);
    }

    @Test
    public void testIsBlankWithWhitespaceString() {
        boolean result = stringUtil.isBlank("     ");
        assertTrue(result);
    }

    @Test
    public void testIsBlankWithNonBlankString() {
        boolean result = stringUtil.isBlank("Hello");
        assertFalse(result);
    }

    @Test
    public void testIsNumericWithNullString() {
        boolean result = stringUtil.isNumeric(null);
        assertFalse(result);
    }

    @Test
    public void testIsNumericWithEmptyString() {
        boolean result = stringUtil.isNumeric("");
        assertFalse(result);
    }

    @Test
    public void testIsNumericWithNumericString() {
        boolean result = stringUtil.isNumeric("1234");
        assertTrue(result);
    }

    @Test
    public void testIsNumericWithNonNumericString() {
        boolean result = stringUtil.isNumeric("Hello");
        assertFalse(result);
    }

    @Test
    public void testIsWhitespaceWithSpaceCharacter() {
        boolean result = stringUtil.isWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithTabCharacter() {
        boolean result = stringUtil.isWhitespace('\t');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithNewlineCharacter() {
        boolean result = stringUtil.isWhitespace('\n');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithFormfeedCharacter() {
        boolean result = stringUtil.isWhitespace('\f');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithCarriagereturnCharacter() {
        boolean result = stringUtil.isWhitespace('\r');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithSpaceCharacter() {
        boolean result = stringUtil.isActuallyWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithTabCharacter() {
        boolean result = stringUtil.isActuallyWhitespace('\t');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithNewlineCharacter() {
        boolean result = stringUtil.isActuallyWhitespace('\n');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithFormfeedCharacter() {
        boolean result = stringUtil.isActuallyWhitespace('\f');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithCarriagereturnCharacter() {
        boolean result = stringUtil.isActuallyWhitespace('\r');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithNonBreakingSpaceCharacter() {
        boolean result = stringUtil.isActuallyWhitespace(160);
        assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespaceWithEmptyString() {
        String result = stringUtil.normaliseWhitespace("");
        assertEquals("", result);
    }

    @Test
    public void testNormaliseWhitespaceWithMultipleSpaces() {
        String result = stringUtil.normaliseWhitespace("Hello     World");
        assertEquals("Hello World", result);
    }

    @Test
    public void testAppendNormalisedWhitespaceWithEmptyString() {
        StringBuilder accum = new StringBuilder();
        stringUtil.appendNormalisedWhitespace(accum, "", false);
        assertEquals("", accum.toString());
    }

    @Test
    public void testAppendNormalisedWhitespaceWithMultipleSpaces() {
        StringBuilder accum = new StringBuilder();
        stringUtil.appendNormalisedWhitespace(accum, "Hello     World", false);
        assertEquals("Hello World", accum.toString());
    }

    @Test
    public void testInWithNeedleInHaystack() {
        boolean result = stringUtil.in("Hello", "Hello", "World");
        assertTrue(result);
    }

    @Test
    public void testInWithNeedleNotInHaystack() {
        boolean result = stringUtil.in("Hello", "Foo", "Bar");
        assertFalse(result);
    }

    @Test
    public void testInSortedWithNeedleInHaystack() {
        String[] haystack = {"Bar", "Foo", "Hello", "World"};
        boolean result = stringUtil.inSorted("Hello", haystack);
        assertTrue(result);
    }

    @Test
    public void testInSortedWithNeedleNotInHaystack() {
        String[] haystack = {"Bar", "Foo", "Goo", "Zoo"};
        boolean result = stringUtil.inSorted("Hello", haystack);
        assertFalse(result);
    }

    @Test
    public void testResolveWithURLBaseAndRelativeURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "/about";
        URL result = stringUtil.resolve(base, relUrl);
        assertEquals(new URL("https://www.example.com/about"), result);
    }

    @Test
    public void testResolveWithStringBaseAndRelativeURL() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/about";
        String result = stringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com/about", result);
    }

    @Test
    public void testResolveWithStringBaseAndAbsoluteURL() {
        String baseUrl = "https://www.example.com";
        String absUrl = "https://www.example.com/about";
        String result = stringUtil.resolve(baseUrl, absUrl);
        assertEquals("https://www.example.com/about", result);
    }

    @Test
    public void testResolveWithStringBaseAndInvalidURL() {
        String baseUrl = "https://www.example.com";
        String invalidUrl = "foo";
        String result = stringUtil.resolve(baseUrl, invalidUrl);
        assertEquals("", result);
    }

    @Test
    public void testStringBuilderWithMultipleCalls() {
        StringBuilder sb1 = stringUtil.stringBuilder();
        StringBuilder sb2 = stringUtil.stringBuilder();
        assertNotSame(sb1, sb2);
    }

    @Test
    public void testStringBuilderWithMaxCachedBuilderSize() {
        StringBuilder sb = stringUtil.stringBuilder();
        int maxSize = 8 * 1024;
        assertEquals(maxSize, sb.capacity());
    }

    @Test
    public void testResolveWithStringBaseAndRelativeURLStartingWithQuestionMark() {
        String baseUrl = "https://www.example.com";
        String relUrl = "?foo";
        String result = stringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com?foo", result);
    }
}