
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoin_collectionWithStrings_success() {
        List<String> strings = Arrays.asList("hello", "world", "example");
        String joinedString = StringUtil.join(strings, ",");
        assertEquals("hello,world,example", joinedString);
    }

    @Test
    public void testJoin_emptyCollection_success() {
        List<String> strings = new ArrayList<>();
        String joinedString = StringUtil.join(strings, ",");
        assertEquals("", joinedString);
    }

    @Test
    public void testJoin_iteratorWithStrings_success() {
        List<String> strings = Arrays.asList("hello", "world", "example");
        Iterator<String> iterator = strings.iterator();
        String joinedString = StringUtil.join(iterator, ",");
        assertEquals("hello,world,example", joinedString);
    }

    @Test
    public void testJoin_singleStringInCollection_success() {
        List<String> strings = Arrays.asList("hello");
        String joinedString = StringUtil.join(strings, ",");
        assertEquals("hello", joinedString);
    }

    @Test
    public void testJoin_arrayWithStrings_success() {
        String[] strings = {"hello", "world", "example"};
        String joinedString = StringUtil.join(strings, ",");
        assertEquals("hello,world,example", joinedString);
    }

    @Test
    public void testPadding_positiveWidth_success() {
        String padding = StringUtil.padding(5);
        assertEquals("     ", padding);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPadding_negativeWidth_exceptionThrown() {
        StringUtil.padding(-5);
    }

    @Test
    public void testIsBlank_nullString_returnsTrue() {
        boolean isBlank = StringUtil.isBlank(null);
        assertTrue(isBlank);
    }

    @Test
    public void testIsBlank_emptyString_returnsTrue() {
        boolean isBlank = StringUtil.isBlank("");
        assertTrue(isBlank);
    }

    @Test
    public void testIsBlank_stringWithOnlyWhitespace_returnsTrue() {
        boolean isBlank = StringUtil.isBlank("   ");
        assertTrue(isBlank);
    }

    @Test
    public void testIsBlank_stringWithNonWhitespace_returnsFalse() {
        boolean isBlank = StringUtil.isBlank("hello");
        assertFalse(isBlank);
    }

    @Test
    public void testIsNumeric_nullString_returnsFalse() {
        boolean isNumeric = StringUtil.isNumeric(null);
        assertFalse(isNumeric);
    }

    @Test
    public void testIsNumeric_emptyString_returnsFalse() {
        boolean isNumeric = StringUtil.isNumeric("");
        assertFalse(isNumeric);
    }

    @Test
    public void testIsNumeric_stringWithNonDigitChar_returnsFalse() {
        boolean isNumeric = StringUtil.isNumeric("123a");
        assertFalse(isNumeric);
    }

    @Test
    public void testIsNumeric_stringWithOnlyDigitChars_returnsTrue() {
        boolean isNumeric = StringUtil.isNumeric("123");
        assertTrue(isNumeric);
    }

    @Test
    public void testIsWhitespace_whitespaceCharacter_returnsTrue() {
        boolean isWhitespace = StringUtil.isWhitespace('\r');
        assertTrue(isWhitespace);
    }

    @Test
    public void testIsWhitespace_nonWhitespaceCharacter_returnsFalse() {
        boolean isWhitespace = StringUtil.isWhitespace('a');
        assertFalse(isWhitespace);
    }

    @Test
    public void testIsActuallyWhitespace_whitespaceCharacter_returnsTrue() {
        boolean isActuallyWhitespace = StringUtil.isActuallyWhitespace('\n');
        assertTrue(isActuallyWhitespace);
    }

    @Test
    public void testIsActuallyWhitespace_nonWhitespaceCharacter_returnsFalse() {
        boolean isActuallyWhitespace = StringUtil.isActuallyWhitespace('a');
        assertFalse(isActuallyWhitespace);
    }

    @Test
    public void testNormaliseWhitespace_multipleSpacesToSingle_success() {
        String normalizedString = StringUtil.normaliseWhitespace("Hello      world");
        assertEquals("Hello world", normalizedString);
    }

    @Test
    public void testNormaliseWhitespace_convertWhitespaceToSpace_success() {
        String normalizedString = StringUtil.normaliseWhitespace("Hello\nworld\texample");
        assertEquals("Hello world example", normalizedString);
    }

    @Test
    public void testIn_stringInHaystack_returnsTrue() {
        boolean isIn = StringUtil.in("hello", "world", "example", "hello");
        assertTrue(isIn);
    }

    @Test
    public void testIn_stringNotInHaystack_returnsFalse() {
        boolean isIn = StringUtil.in("goodbye", "world", "example", "hello");
        assertFalse(isIn);
    }

    @Test
    public void testInSorted_stringInHaystack_returnsTrue() {
        boolean isInSorted = StringUtil.inSorted("apple", new String[]{"apple", "banana", "orange"});
        assertTrue(isInSorted);
    }

    @Test
    public void testInSorted_stringNotInHaystack_returnsFalse() {
        boolean isInSorted = StringUtil.inSorted("grape", new String[]{"apple", "banana", "orange"});
        assertFalse(isInSorted);
    }

    @Test
    public void testResolve_validBaseAndRelativeURL_success() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relativeUrl = "/path/to/resource";
        URL resolvedUrl = StringUtil.resolve(base, relativeUrl);
        assertEquals("https://www.example.com/path/to/resource", resolvedUrl.toString());
    }

    @Test
    public void testResolve_relativeUrlStartingWithQuestionMark_success() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relativeUrl = "?param=value";
        URL resolvedUrl = StringUtil.resolve(base, relativeUrl);
        assertEquals("https://www.example.com?param=value", resolvedUrl.toString());
    }

    @Test
    public void testResolve_relativeUrlStartingWithDot_success() throws MalformedURLException {
        URL base = new URL("https://www.example.com/path/");
        String relativeUrl = "./resource";
        URL resolvedUrl = StringUtil.resolve(base, relativeUrl);
        assertEquals("https://www.example.com/path/resource", resolvedUrl.toString());
    }

    @Test
    public void testResolve_invalidURL_emptyStringReturned() {
        String baseUrl = "https://www.example.com";
        String relativeUrl = "/resource";
        String resolvedUrl = StringUtil.resolve(baseUrl, relativeUrl);
        assertEquals("", resolvedUrl);
    }

    @Test
    public void testStringBuilder_reusedStringBuilder_success() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("Hello");
        sb.append(" World");
        String result = sb.toString();
        assertEquals("Hello World", result);
    }

    @Test
    public void testStringBuilder_reachedMaxSize_success() {
        StringBuilder sb = StringUtil.stringBuilder();
        for (int i = 0; i < 2000; i++) {
            sb.append("a");
        }
        assertEquals(8192, sb.length()); // MaxCachedBuilderSize is 8192
    }

}
