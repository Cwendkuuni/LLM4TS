import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        // Typical use case
        assertEquals("one,two,three", StringUtil.join(Arrays.asList("one", "two", "three"), ","));

        // Edge case - empty collection
        assertEquals("", StringUtil.join(Arrays.asList(), ","));

        // Error scenario - null collection
        assertNull(StringUtil.join(null, ","));
    }

    @Test
    public void testJoinIterator() {
        // Typical use case
        assertEquals("4,5,6", StringUtil.join(Arrays.asList(4, 5, 6).iterator(), ","));

        // Edge case - empty iterator
        assertEquals("", StringUtil.join(Arrays.asList().iterator(), ","));

        // Error scenario - null iterator
        assertNull(StringUtil.join((Iterator)null, ","));
    }

    @Test
    public void testJoinArray() {
        // Typical use case
        assertEquals("a,b,c", StringUtil.join(new String[]{"a", "b", "c"}, ","));

        // Edge case - empty array
        assertEquals("", StringUtil.join(new String[]{}, ","));

        // Error scenario - null array
        assertNull(StringUtil.join((String[])null, ","));
    }

    @Test
    public void testPadding() {
        // Typical use case
        assertEquals("        ", StringUtil.padding(8));

        // Edge case - padding width equals 0
        assertEquals("", StringUtil.padding(0));

        // Error scenario - negative padding width
        try {
            StringUtil.padding(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testIsBlank() {
        // Typical use case - blank string
        assertTrue(StringUtil.isBlank("     "));

        // Typical use case - non-blank string
        assertFalse(StringUtil.isBlank("example"));

        // Edge case - empty string
        assertTrue(StringUtil.isBlank(""));

        // Edge case - null string
        assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsNumeric() {
        // Typical use case - numeric string
        assertTrue(StringUtil.isNumeric("12345"));

        // Typical use case - non-numeric string
        assertFalse(StringUtil.isNumeric("example"));

        // Edge case - empty string
        assertFalse(StringUtil.isNumeric(""));

        // Edge case - null string
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsWhitespace() {
        // Typical use case - whitespace character
        assertTrue(StringUtil.isWhitespace(' '));

        // Typical use case - non-whitespace character
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        // Typical use case - whitespace character
        assertTrue(StringUtil.isActuallyWhitespace(' '));

        // Typical use case - non-whitespace character
        assertFalse(StringUtil.isActuallyWhitespace('a'));

        // Edge case - non-breaking space character
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespace() {
        // Typical use case
        assertEquals("one two", StringUtil.normaliseWhitespace("one    two"));

        // Edge case - empty string
        assertEquals("", StringUtil.normaliseWhitespace(""));

        // Edge case - null string
        assertEquals("", StringUtil.normaliseWhitespace(null));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();

        // Typical use case
        StringUtil.appendNormalisedWhitespace(sb, "one    two", false);
        assertEquals("one two", sb.toString());

        // Edge case - empty string
        StringUtil.appendNormalisedWhitespace(sb, "", false);
        assertEquals("one two", sb.toString());

        // Edge case - null string
        StringUtil.appendNormalisedWhitespace(sb, null, false);
        assertEquals("one two", sb.toString());
    }

    @Test
    public void testIn() {
        // Typical use case - string is in haystack
        assertTrue(StringUtil.in("example", "one", "example", "three"));

        // Typical use case - string is not in haystack
        assertFalse(StringUtil.in("example", "one", "two", "three"));

        // Edge case - empty haystack
        assertFalse(StringUtil.in("example"));

        // Edge case - null haystack
        assertFalse(StringUtil.in("example", (String[])null));
    }

    @Test
    public void testInSorted() {
        // Typical use case - string is in sorted haystack
        assertTrue(StringUtil.inSorted("example", new String[]{"apple", "example", "orange"}));

        // Typical use case - string is not in sorted haystack
        assertFalse(StringUtil.inSorted("example", new String[]{"apple", "banana", "orange"}));

        // Edge case - empty sorted haystack
        assertFalse(StringUtil.inSorted("example", new String[]{}));

        // Edge case - null sorted haystack
        assertFalse(StringUtil.inSorted("example", (String[])null));
    }

    @Test
    public void testResolveURL() {
        // Typical use case - relative URL
        try {
            assertEquals(new URL("http://example.com/test/path"), StringUtil.resolve(new URL("http://example.com"), "test/path"));
        } catch (MalformedURLException e) {
            fail("Unexpected MalformedURLException");
        }

        // Typical use case - absolute URL
        try {
            assertEquals(new URL("http://example.com"), StringUtil.resolve(new URL("http://example.com"), "http://example.com"));
        } catch (MalformedURLException e) {
            fail("Unexpected MalformedURLException");
        }

        // Edge case - empty relative URL
        try {
            assertEquals(new URL("http://example.com"), StringUtil.resolve(new URL("http://example.com"), ""));
        } catch (MalformedURLException e) {
            fail("Unexpected MalformedURLException");
        }

        // Edge case - empty base URL
        try {
            assertEquals(new URL("http://example.com"), StringUtil.resolve(new URL(""), "http://example.com"));
        } catch (MalformedURLException e) {
            fail("Unexpected MalformedURLException");
        }

        // Error scenario - MalformedURLException
        try {
            StringUtil.resolve(new URL("http://example.com"), "test/path#");
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // pass
        }
    }

    @Test
    public void testResolveString() {
        // Typical use case - relative URL
        assertEquals("http://example.com/test/path", StringUtil.resolve("http://example.com", "test/path"));

        // Typical use case - absolute URL
        assertEquals("http://example.com", StringUtil.resolve("http://example.com", "http://example.com"));

        // Edge case - empty relative URL
        assertEquals("http://example.com", StringUtil.resolve("http://example.com", ""));

        // Edge case - empty base URL
        assertEquals("http://example.com", StringUtil.resolve("", "http://example.com"));

        // Error scenario - MalformedURLException
        assertEquals("", StringUtil.resolve("http://example.com", "test/path#"));
    }

    @Test
    public void testStringBuilder() {
        // Typical use case
        StringBuilder sb1 = StringUtil.stringBuilder();
        sb1.append("example");
        assertEquals("example", sb1.toString());

        // Edge case - multiple calls on same thread
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertNotSame(sb1, sb2);
    }

}