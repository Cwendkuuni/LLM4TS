import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        // Typical use case
        String expected = "one,two,three";
        String actual = StringUtil.join(Arrays.asList("one", "two", "three"), ",");
        assertEquals(expected, actual);

        // Edge case: Empty collection
        expected = "";
        actual = StringUtil.join(Arrays.asList(), ",");
        assertEquals(expected, actual);

        // Error scenario: Null collection
        actual = StringUtil.join(null, ",");
        assertNull(actual);
    }

    @Test
    public void testJoinIterator() {
        // Typical use case
        String expected = "one,two,three";
        String actual = StringUtil.join(Arrays.asList("one", "two", "three").iterator(), ",");
        assertEquals(expected, actual);

        // Edge case: Empty iterator
        expected = "";
        actual = StringUtil.join(Arrays.asList().iterator(), ",");
        assertEquals(expected, actual);

        // Error scenario: Null iterator
        actual = StringUtil.join((Iterator<String>) null, ",");
        assertNull(actual);
    }

    @Test
    public void testJoinStringArray() {
        // Typical use case
        String expected = "one,two,three";
        String actual = StringUtil.join(new String[]{"one", "two", "three"}, ",");
        assertEquals(expected, actual);

        // Edge case: Empty array
        expected = "";
        actual = StringUtil.join(new String[]{}, ",");
        assertEquals(expected, actual);

        // Error scenario: Null array
        actual = StringUtil.join((String[]) null, ",");
        assertNull(actual);
    }

    @Test
    public void testPadding() {
        // Typical use case
        String expected = "       ";
        String actual = StringUtil.padding(7);
        assertEquals(expected, actual);

        // Edge case: Zero padding
        expected = "";
        actual = StringUtil.padding(0);
        assertEquals(expected, actual);

        // Error scenario: Negative padding
        try {
            StringUtil.padding(-5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testIsBlank() {
        // Typical use case
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("text"));

        // Edge case: Null string
        assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsNumeric() {
        // Typical use case
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("12.345"));
        assertFalse(StringUtil.isNumeric("text"));

        // Edge case: Empty string
        assertFalse(StringUtil.isNumeric(""));

        // Edge case: Null string
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsWhitespace() {
        // Typical use case
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));

        // Edge case: Non-breaking space
        assertTrue(StringUtil.isWhitespace(160));
    }

    @Test
    public void testIsActuallyWhitespace() {
        // Typical use case
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));

        // Edge case: Non-breaking space
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespace() {
        // Typical use case
        String expected = "This is a sentence.";
        String actual = StringUtil.normaliseWhitespace("  This     is   a    sentence.  ");
        assertEquals(expected, actual);

        // Edge case: Empty string
        expected = "";
        actual = StringUtil.normaliseWhitespace("");
        assertEquals(expected, actual);

        // Error scenario: Null string
        actual = StringUtil.normaliseWhitespace(null);
        assertNull(actual);
    }

    @Test
    public void testIn() {
        // Typical use case
        assertTrue(StringUtil.in("one", "one", "two", "three"));
        assertFalse(StringUtil.in("four", "one", "two", "three"));

        // Edge case: Empty haystack
        assertFalse(StringUtil.in("one"));

        // Edge case: Null haystack
        assertFalse(StringUtil.in("one", (String[]) null));
    }

    @Test
    public void testInSorted() {
        // Typical use case
        assertTrue(StringUtil.inSorted("one", new String[]{"one", "two", "three"}));
        assertFalse(StringUtil.inSorted("four", new String[]{"one", "two", "three"}));

        // Edge case: Empty haystack
        assertFalse(StringUtil.inSorted("one", new String[]{}));

        // Error scenario: Null haystack
        assertFalse(StringUtil.inSorted("one", (String[]) null));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        // Typical use case
        URL base = new URL("http://example.com");
        String relUrl = "./path/file";
        URL expected = new URL("http://example.com/path/file");
        URL actual = StringUtil.resolve(base, relUrl);
        assertEquals(expected, actual);

        // Edge case: Absolute relative URL
        base = new URL("http://example.com");
        relUrl = "http://other-example.com";
        expected = new URL("http://other-example.com");
        actual = StringUtil.resolve(base, relUrl);
        assertEquals(expected, actual);

        // Error scenario: Malformed relative URL
        base = new URL("http://example.com");
        relUrl = ":relativeUrl";
        try {
            StringUtil.resolve(base, relUrl);
            fail("Should throw MalformedURLException");
        } catch (MalformedURLException e) {
            // Pass
        }
    }

    @Test
    public void testResolveString() {
        // Typical use case
        String baseUrl = "http://example.com";
        String relUrl = "./path/file";
        String expected = "http://example.com/path/file";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);

        // Edge case: Absolute relative URL
        baseUrl = "http://example.com";
        relUrl = "http://other-example.com";
        expected = "http://other-example.com";
        actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, actual);

        // Error scenario: Malformed URL
        baseUrl = "http\\:\\malformed-url";
        relUrl = "./path/file";
        actual = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("", actual);
    }

    @Test
    public void testStringBuilder() {
        // Typical use case
        StringBuilder sb = StringUtil.stringBuilder();
        assertEquals(0, sb.length());

        // Edge case: Cached StringBuilder
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertSame(sb, sb2);

        // Edge case: Too long cached StringBuilder
        sb.append("This is a long string");
        sb2 = StringUtil.stringBuilder();
        assertNotSame(sb, sb2);

        // Edge case: Clear cached StringBuilder
        sb.delete(0, sb.length());
        assertEquals(0, sb.length());
    }
}