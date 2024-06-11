import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> stringCollection = Arrays.asList("Hello", "World");
        String result = StringUtil.join(stringCollection, ",");
        assertEquals("Hello,World", result);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> stringIterator = Arrays.asList("Hello", "World").iterator();
        String result = StringUtil.join(stringIterator, ",");
        assertEquals("Hello,World", result);
    }

    @Test
    public void testJoinArray() {
        String[] stringArray = {"Hello", "World"};
        String result = StringUtil.join(stringArray, ",");
        assertEquals("Hello,World", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(3);
        assertEquals("   ", result);
    }

    @Test
    public void testIsBlank() {
        boolean result1 = StringUtil.isBlank(null);
        assertTrue(result1);
        
        boolean result2 = StringUtil.isBlank("");
        assertTrue(result2);
        
        boolean result3 = StringUtil.isBlank("   ");
        assertTrue(result3);
        
        boolean result4 = StringUtil.isBlank("Hello");
        assertFalse(result4);
    }

    @Test
    public void testIsNumeric() {
        boolean result1 = StringUtil.isNumeric(null);
        assertFalse(result1);
        
        boolean result2 = StringUtil.isNumeric("");
        assertFalse(result2);
        
        boolean result3 = StringUtil.isNumeric("123");
        assertTrue(result3);
        
        boolean result4 = StringUtil.isNumeric("Hello");
        assertFalse(result4);
    }

    @Test
    public void testIsWhitespace() {
        boolean result1 = StringUtil.isWhitespace(' ');
        assertTrue(result1);
        
        boolean result2 = StringUtil.isWhitespace('\t');
        assertTrue(result2);
        
        boolean result3 = StringUtil.isWhitespace('\n');
        assertTrue(result3);
        
        boolean result4 = StringUtil.isWhitespace('\f');
        assertTrue(result4);
        
        boolean result5 = StringUtil.isWhitespace('\r');
        assertTrue(result5);
        
        boolean result6 = StringUtil.isWhitespace(160);
        assertTrue(result6);
        
        boolean result7 = StringUtil.isWhitespace('A');
        assertFalse(result7);
    }

    @Test
    public void testIsActuallyWhitespace() {
        boolean result1 = StringUtil.isActuallyWhitespace(' ');
        assertTrue(result1);
        
        boolean result2 = StringUtil.isActuallyWhitespace('\t');
        assertTrue(result2);
        
        boolean result3 = StringUtil.isActuallyWhitespace('\n');
        assertTrue(result3);
        
        boolean result4 = StringUtil.isActuallyWhitespace('\f');
        assertTrue(result4);
        
        boolean result5 = StringUtil.isActuallyWhitespace('\r');
        assertTrue(result5);
        
        boolean result6 = StringUtil.isActuallyWhitespace(160);
        assertTrue(result6);
        
        boolean result7 = StringUtil.isActuallyWhitespace('A');
        assertFalse(result7);
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello    World");
        assertEquals("Hello World", result);
    }

    @Test
    public void testIn() {
        boolean result1 = StringUtil.in("Hello", "Hi", "Hello", "Hey");
        assertTrue(result1);
        
        boolean result2 = StringUtil.in("Hi", "Hello", "Hey");
        assertFalse(result2);
    }

    @Test
    public void testInSorted() {
        String[] haystack1 = {"Hi", "Hello", "Hey"};
        boolean result1 = StringUtil.inSorted("Hello", haystack1);
        assertTrue(result1);
        
        String[] haystack2 = {"Hey", "Hi", "Hello"};
        boolean result2 = StringUtil.inSorted("Bye", haystack2);
        assertFalse(result2);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "/path";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("http://example.com/path", result.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "http://example.com";
        String relUrl = "/path";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/path", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder result = StringUtil.stringBuilder();
        assertNotNull(result);
    }

}