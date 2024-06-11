import org.junit.Test;
import org.jsoup.helper.StringUtil;

public class StringUtilTest {

    @Test
    public void testJoin() {
        // Test with collection of strings
        String[] strings = {"Hello", "World"};
        String joined = StringUtil.join(strings, " ");
        assertEquals("Hello World", joined);
        
        // Test with iterator of strings
        List<String> stringList = Arrays.asList("Hello", "World");
        joined = StringUtil.join(stringList.iterator(), " ");
        assertEquals("Hello World", joined);
        
        // Test with array of strings
        joined = StringUtil.join(strings, " ");
        assertEquals("Hello World", joined);
    }

    @Test
    public void testPadding() {
        // Test with width < padding.length
        String padded = StringUtil.padding(5);
        assertEquals("     ", padded);
        
        // Test with width > padding.length
        padded = StringUtil.padding(25);
        assertEquals("                         ", padded);
    }

    @Test
    public void testIsBlank() {
        // Test with null string
        boolean isBlank = StringUtil.isBlank(null);
        assertTrue(isBlank);
        
        // Test with empty string
        isBlank = StringUtil.isBlank("");
        assertTrue(isBlank);
        
        // Test with only whitespace string
        isBlank = StringUtil.isBlank("  ");
        assertTrue(isBlank);
        
        // Test with non-blank string
        isBlank = StringUtil.isBlank("Hello");
        assertFalse(isBlank);
    }

    @Test
    public void testIsNumeric() {
        // Test with null string
        boolean isNumeric = StringUtil.isNumeric(null);
        assertFalse(isNumeric);
        
        // Test with empty string
        isNumeric = StringUtil.isNumeric("");
        assertFalse(isNumeric);
        
        // Test with non-numeric string
        isNumeric = StringUtil.isNumeric("123a");
        assertFalse(isNumeric);
        
        // Test with numeric string
        isNumeric = StringUtil.isNumeric("123");
        assertTrue(isNumeric);
    }

    @Test
    public void testIsWhitespace() {
        // Test with whitespace character
        boolean isWhitespace = StringUtil.isWhitespace(' ');
        assertTrue(isWhitespace);
        
        // Test with non-whitespace character
        isWhitespace = StringUtil.isWhitespace('a');
        assertFalse(isWhitespace);
    }

    @Test
    public void testIsActuallyWhitespace() {
        // Test with whitespace character
        boolean isWhitespace = StringUtil.isActuallyWhitespace(' ');
        assertTrue(isWhitespace);
        
        // Test with non-whitespace character
        isWhitespace = StringUtil.isActuallyWhitespace('a');
        assertFalse(isWhitespace);
        
        // Test with non-breaking space character
        isWhitespace = StringUtil.isActuallyWhitespace('\u00A0');
        assertTrue(isWhitespace);
    }

    @Test
    public void testNormaliseWhitespace() {
        // Test with string containing multiple spaces and newline characters
        String normalised = StringUtil.normaliseWhitespace("Hello    \n\tWorld");
        assertEquals("Hello World", normalised);
    }

    @Test
    public void testIn() {
        // Test with needle not in haystack
        boolean isIn = StringUtil.in("c", "a", "b");
        assertFalse(isIn);
        
        // Test with needle in haystack
        isIn = StringUtil.in("c", "a", "b", "c");
        assertTrue(isIn);
    }

    @Test
    public void testInSorted() {
        // Test with needle not in sorted haystack
        boolean isIn = StringUtil.inSorted("c", new String[]{"a", "b"});
        assertFalse(isIn);
        
        // Test with needle in sorted haystack
        isIn = StringUtil.inSorted("c", new String[]{"a", "b", "c"});
        assertTrue(isIn);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        // Test with relative URL
        URL base = new URL("https://www.example.com");
        String resolved = StringUtil.resolve(base, "/path/file");
        assertEquals("https://www.example.com/path/file", resolved);
        
        // Test with absolute URL
        base = new URL("https://www.example.com");
        resolved = StringUtil.resolve(base, "http://www.google.com");
        assertEquals("http://www.google.com", resolved);
    }

    @Test
    public void testResolveString() {
        // Test with valid URLs
        String resolved = StringUtil.resolve("https://www.example.com", "/path/file");
        assertEquals("https://www.example.com/path/file", resolved);
        
        resolved = StringUtil.resolve("https://www.example.com", "http://www.google.com");
        assertEquals("http://www.google.com", resolved);
        
        // Test with invalid URL
        resolved = StringUtil.resolve("invalidurl", "path/file");
        assertEquals("", resolved);
    }
}