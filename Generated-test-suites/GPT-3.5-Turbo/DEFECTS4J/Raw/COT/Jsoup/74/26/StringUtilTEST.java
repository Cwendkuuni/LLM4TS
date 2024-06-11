import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String joinedString = StringUtil.join(strings, ", ");
        assertEquals("Hello, World", joinedString);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("Hello", "World").iterator();
        String joinedString = StringUtil.join(strings, ", ");
        assertEquals("Hello, World", joinedString);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String joinedString = StringUtil.join(strings, ", ");
        assertEquals("Hello, World", joinedString);
    }

    @Test
    public void testPadding() {
        String padding = StringUtil.padding(5);
        assertEquals("     ", padding);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("     "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("12abc"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String normalized = StringUtil.normaliseWhitespace("  Hello   World   ");
        assertEquals("Hello World", normalized);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  Hello   World   ", true);
        assertEquals("Hello World", sb.toString());
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("Hello", "Hello", "World"));
        assertFalse(StringUtil.in("Goodbye", "Hello", "World"));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("Hello", new String[] {"Goodbye", "Hello", "World"}));
        assertFalse(StringUtil.inSorted("Goodbye", new String[] {"Hello", "World"}));
    }

    @Test
    public void testResolve() {
        try {
            URL base = new URL("http://www.example.com/");
            URL resolvedUrl = StringUtil.resolve(base, "subpage.html");
            assertEquals("http://www.example.com/subpage.html", resolvedUrl.toString());
        } catch (MalformedURLException e) {
            fail("Failed to resolve URL");
        }
    }

    @Test
    public void testResolveString() {
        String resolvedUrl = StringUtil.resolve("http://www.example.com/", "subpage.html");
        assertEquals("http://www.example.com/subpage.html", resolvedUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
    }
}