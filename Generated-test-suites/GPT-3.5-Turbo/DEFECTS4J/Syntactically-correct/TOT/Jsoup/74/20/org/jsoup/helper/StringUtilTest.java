import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String result = StringUtil.join(strings, " ");
        assertEquals("Hello World !", result);
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        Iterator<String> iterator = strings.iterator();
        String result = StringUtil.join(iterator, " ");
        assertEquals("Hello World !", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String result = StringUtil.join(strings, " ");
        assertEquals("Hello World !", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testPaddingZeroWidth() {
        String result = StringUtil.padding(0);
        assertEquals("", result);
    }

    @Test
    public void testPaddingPositiveWidth() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test
    public void testIsBlankNullString() {
        boolean result = StringUtil.isBlank(null);
        assertTrue(result);
    }

    @Test
    public void testIsBlankEmptyString() {
        boolean result = StringUtil.isBlank("");
        assertTrue(result);
    }

    @Test
    public void testIsBlankWhitespaceString() {
        boolean result = StringUtil.isBlank("   ");
        assertTrue(result);
    }

    @Test
    public void testIsBlankNonBlankString() {
        boolean result = StringUtil.isBlank("Hello");
        assertFalse(result);
    }

    @Test
    public void testIsNumericNullString() {
        boolean result = StringUtil.isNumeric(null);
        assertFalse(result);
    }

    @Test
    public void testIsNumericEmptyString() {
        boolean result = StringUtil.isNumeric("");
        assertFalse(result);
    }

    @Test
    public void testIsNumericValidNumericString() {
        boolean result = StringUtil.isNumeric("12345");
        assertTrue(result);
    }

    @Test
    public void testIsNumericInvalidNumericString() {
        boolean result = StringUtil.isNumeric("12.34");
        assertFalse(result);
    }

    @Test
    public void testIsWhitespace() {
        boolean result = StringUtil.isWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsNotWhitespace() {
        boolean result = StringUtil.isWhitespace('A');
        assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsNotActuallyWhitespace() {
        boolean result = StringUtil.isActuallyWhitespace('A');
        assertFalse(result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String result = StringUtil.normaliseWhitespace("Hello   World!");
        assertEquals("Hello World!", result);
    }

    @Test
    public void testNormaliseWhitespaceNewlineAndTab() {
        String result = StringUtil.normaliseWhitespace("Hello\n\tWorld!");
        assertEquals("Hello World!", result);
    }

    @Test
    public void testInStringInHaystack() {
        boolean result = StringUtil.in("World", "Hello", "World", "!");
        assertTrue(result);
    }

    @Test
    public void testInStringNotInHaystack() {
        boolean result = StringUtil.in("Goodbye", "Hello", "World", "!");
        assertFalse(result);
    }

    @Test
    public void testInSortedStringInHaystack() {
        String[] haystack = {"Hello", "World", "!"};
        boolean result = StringUtil.inSorted("World", haystack);
        assertTrue(result);
    }

    @Test
    public void testInSortedStringNotInHaystack() {
        String[] haystack = {"Hello", "World", "!"};
        boolean result = StringUtil.inSorted("Goodbye", haystack);
        assertFalse(result);
    }

    @Test
    public void testResolveRelativeUrl() throws MalformedURLException {
        URL base = new URL("https://www.example.com/path/");
        String relUrl = "page.html";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/path/page.html", result.toString());
    }

    @Test
    public void testResolveAbsoluteUrl() throws MalformedURLException {
        URL base = new URL("https://www.example.com/path/");
        String relUrl = "https://www.example.com/page.html";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/page.html", result.toString());
    }

    @Test
    public void testResolveRelativeUrlWithQuery() throws MalformedURLException {
        URL base = new URL("https://www.example.com/path/");
        String relUrl = "?id=123";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/path/?id=123", result.toString());
    }

    @Test
    public void testResolveRelativeUrlWithDot() throws MalformedURLException {
        URL base = new URL("https://www.example.com/path/");
        String relUrl = "./about.html";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("https://www.example.com/path/about.html", result.toString());
    }

    @Test
    public void testResolveInvalidBaseUrl() {
        URL base = null;
        String relUrl = "page.html";
        try {
            StringUtil.resolve(base, relUrl);
        } catch (MalformedURLException e) {
            fail("MalformedURLException should not be thrown");
        }
    }

    @Test
    public void testResolveInvalidRelUrl() {
        URL base = null;
        String relUrl = "https://www.example.com/page.html";
        try {
            StringUtil.resolve(base, relUrl);
        } catch (MalformedURLException e) {
            fail("MalformedURLException should not be thrown");
        }
    }

    @Test
    public void testResolveInvalidUrl() {
        URL base = null;
        String relUrl = "https://www.example.com/page.html";
        String result = StringUtil.resolve(base.toString(), relUrl);
        assertEquals("", result);
    }

    @Test
    public void testResolveValidUrl() {
        String baseUrl = "https://www.example.com/path/";
        String relUrl = "https://www.example.com/page.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com/page.html", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }
}