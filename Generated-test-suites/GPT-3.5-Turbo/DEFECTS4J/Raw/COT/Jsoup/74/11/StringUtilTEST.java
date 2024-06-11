import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoin_collectionWithSeparator_returnsJoinedString() {
        String[] strings = {"Hello", "World"};
        String sep = ", ";
        Collection<String> stringList = Arrays.asList(strings);
        
        String result = StringUtil.join(stringList, sep);
        
        assertEquals("Hello, World", result);
    }

    @Test
    public void testJoin_iteratorWithSeparator_returnsJoinedString() {
        String[] strings = {"Hello", "World"};
        String sep = ", ";
        Iterator<String> stringIterator = Arrays.asList(strings).iterator();
        
        String result = StringUtil.join(stringIterator, sep);
        
        assertEquals("Hello, World", result);
    }

    @Test
    public void testJoin_arrayWithSeparator_returnsJoinedString() {
        String[] strings = {"Hello", "World"};
        String sep = ", ";
        
        String result = StringUtil.join(strings, sep);
        
        assertEquals("Hello, World", result);
    }

    @Test
    public void testPadding_positiveWidth_returnsPaddingString() {
        int width = 5;
        
        String result = StringUtil.padding(width);
        
        assertEquals("     ", result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPadding_negativeWidth_throwsIllegalArgumentException() {
        int width = -5;
        
        StringUtil.padding(width);
    }

    @Test
    public void testIsBlank_emptyString_returnsTrue() {
        String string = "";
        
        boolean result = StringUtil.isBlank(string);
        
        assertTrue(result);
    }

    @Test
    public void testIsBlank_nullString_returnsTrue() {
        String string = null;
        
        boolean result = StringUtil.isBlank(string);
        
        assertTrue(result);
    }

    @Test
    public void testIsBlank_whitespaceString_returnsTrue() {
        String string = "        ";
        
        boolean result = StringUtil.isBlank(string);
        
        assertTrue(result);
    }

    @Test
    public void testIsBlank_nonBlankString_returnsFalse() {
        String string = "Hello";
        
        boolean result = StringUtil.isBlank(string);
        
        assertFalse(result);
    }

    @Test
    public void testIsNumeric_emptyString_returnsFalse() {
        String string = "";
        
        boolean result = StringUtil.isNumeric(string);
        
        assertFalse(result);
    }

    @Test
    public void testIsNumeric_nullString_returnsFalse() {
        String string = null;
        
        boolean result = StringUtil.isNumeric(string);
        
        assertFalse(result);
    }

    @Test
    public void testIsNumeric_onlyDigitChars_returnsTrue() {
        String string = "12345";
        
        boolean result = StringUtil.isNumeric(string);
        
        assertTrue(result);
    }

    @Test
    public void testIsNumeric_containsNonDigitChars_returnsFalse() {
        String string = "12a45";
        
        boolean result = StringUtil.isNumeric(string);
        
        assertFalse(result);
    }

    @Test
    public void testIsWhitespace_spaceChar_returnsTrue() {
        int c = ' ';
        
        boolean result = StringUtil.isWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_tabChar_returnsTrue() {
        int c = '\t';
        
        boolean result = StringUtil.isWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_newlineChar_returnsTrue() {
        int c = '\n';
        
        boolean result = StringUtil.isWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_formFeedChar_returnsTrue() {
        int c = '\f';
        
        boolean result = StringUtil.isWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_carriageReturnChar_returnsTrue() {
        int c = '\r';
        
        boolean result = StringUtil.isWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace_spaceChar_returnsTrue() {
        int c = ' ';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace_tabChar_returnsTrue() {
        int c = '\t';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace_newlineChar_returnsTrue() {
        int c = '\n';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace_formFeedChar_returnsTrue() {
        int c = '\f';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace_carriageReturnChar_returnsTrue() {
        int c = '\r';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace_nonBreakingSpaceChar_returnsTrue() {
        int c = 160;
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespace_multipleSpaces_returnsSingleSpace() {
        String string = "Hello     World";
        
        String result = StringUtil.normaliseWhitespace(string);
        
        assertEquals("Hello World", result);
    }

    @Test
    public void testNormaliseWhitespace_newlineTab_returnsSingleSpace() {
        String string = "Hello\n\tWorld";
        
        String result = StringUtil.normaliseWhitespace(string);
        
        assertEquals("Hello World", result);
    }

    @Test
    public void testIn_needleInHaystack_returnsTrue() {
        String needle = "Hello";
        String[] haystack = {"Hi", "Hello", "Hey"};
        
        boolean result = StringUtil.in(needle, haystack);
        
        assertTrue(result);
    }

    @Test
    public void testIn_needleNotInHaystack_returnsFalse() {
        String needle = "Hello";
        String[] haystack = {"Hi", "Hey"};
        
        boolean result = StringUtil.in(needle, haystack);
        
        assertFalse(result);
    }

    @Test
    public void testInSorted_needleInHaystack_returnsTrue() {
        String needle = "Hello";
        String[] haystack = {"Goodbye", "Hello", "Hi"};
        
        boolean result = StringUtil.inSorted(needle, haystack);
        
        assertTrue(result);
    }

    @Test
    public void testInSorted_needleNotInHaystack_returnsFalse() {
        String needle = "Hello";
        String[] haystack = {"Hi", "Goodbye"};
        
        boolean result = StringUtil.inSorted(needle, haystack);
        
        assertFalse(result);
    }

    @Test
    public void testResolve_relativeUrl_resolvesToAbsoluteUrl() {
        String baseUrl = "http://www.example.com/";
        String relUrl = "path/to/resource";
        
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals("http://www.example.com/path/to/resource", result);
    }

    @Test
    public void testResolve_absoluteUrl_returnsOriginalUrl() {
        String baseUrl = "http://www.example.com/";
        String absUrl = "http://www.example2.com/resource";
        
        String result = StringUtil.resolve(baseUrl, absUrl);
        
        assertEquals(absUrl, result);
    }

    @Test
    public void testResolve_invalidBaseUrl_returnsEmptyString() {
        String baseUrl = "invalid-url";
        String relUrl = "path/to/resource";
        
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals("", result);
    }
    
}