
import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String result = StringUtil.join(strings, " ");
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("Hello", "World").iterator();
        String result = StringUtil.join(strings, " ");
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String result = StringUtil.join(strings, " ");
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\t"));
        assertFalse(StringUtil.isBlank("Hello"));
    }
    
    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("1234"));
        assertFalse(StringUtil.isNumeric("12A4"));
    }
    
    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertFalse(StringUtil.isWhitespace('A'));
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertFalse(StringUtil.isActuallyWhitespace('A'));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello   World");
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("Hello", "Hello", "World"));
        assertFalse(StringUtil.in("Goodbye", "Hello", "World"));
    }
    
    @Test
    public void testInSorted() {
        String[] haystack = {"Apple", "Banana", "Grapes"};
        assertTrue(StringUtil.inSorted("Banana", haystack));
        assertFalse(StringUtil.inSorted("Orange", haystack));
    }
    
    @Test
    public void testResolveURL() {
        try {
            URL base = new URL("https://www.example.com");
            String relUrl = "/path/example.html";
            URL result = StringUtil.resolve(base, relUrl);
            assertEquals("https://www.example.com/path/example.html", result.toString());
        } catch (MalformedURLException e) {
            fail("MalformedURLException: " + e.getMessage());
        }
    }
    
    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/path/example.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com/path/example.html", result);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        assertEquals("", sb.toString());
    }
}
