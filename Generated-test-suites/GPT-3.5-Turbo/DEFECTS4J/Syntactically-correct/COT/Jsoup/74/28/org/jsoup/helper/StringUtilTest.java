import org.junit.Test;
import org.jsoup.helper.StringUtil;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        String result = StringUtil.join(Arrays.asList("Hello", "World"), ",");
        assertEquals("Hello,World", result);
    }

    @Test
    public void testJoinIterator() {
        String result = StringUtil.join(Arrays.asList("Hello", "World").iterator(), ",");
        assertEquals("Hello,World", result);
    }

    @Test
    public void testJoinArray() {
        String result = StringUtil.join(new String[]{"Hello", "World"}, ",");
        assertEquals("Hello,World", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        boolean result = StringUtil.isBlank("");
        assertTrue(result);

        result = StringUtil.isBlank(null);
        assertTrue(result);

        result = StringUtil.isBlank(" ");
        assertTrue(result);

        result = StringUtil.isBlank("Hello");
        assertFalse(result);
    }

    @Test
    public void testIsNumeric() {
        boolean result = StringUtil.isNumeric("");
        assertFalse(result);

        result = StringUtil.isNumeric(null);
        assertFalse(result);

        result = StringUtil.isNumeric("12345");
        assertTrue(result);

        result = StringUtil.isNumeric("1a2b3c");
        assertFalse(result);
    }

    @Test
    public void testIsWhitespace() {
        boolean result = StringUtil.isWhitespace(' ');
        assertTrue(result);

        result = StringUtil.isWhitespace('\t');
        assertTrue(result);

        result = StringUtil.isWhitespace('\n');
        assertTrue(result);

        result = StringUtil.isWhitespace('\f');
        assertTrue(result);

        result = StringUtil.isWhitespace('\r');
        assertTrue(result);

        result = StringUtil.isWhitespace('\u00A0');
        assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\t');
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\n');
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\f');
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\r');
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\u00A0');
        assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\u200B');
        assertFalse(result);

        result = StringUtil.isActuallyWhitespace('\u200C');
        assertFalse(result);

        result = StringUtil.isActuallyWhitespace('\u200D');
        assertFalse(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello   World");
        assertEquals("Hello World", result);
    }

    @Test
    public void testIn() {
        boolean result = StringUtil.in("Hello", "Hello", "World");
        assertTrue(result);

        result = StringUtil.in("Hello", "World");
        assertFalse(result);
    }

    @Test
    public void testInSorted() {
        boolean result = StringUtil.inSorted("Hello", new String[]{"Hello", "World"});
        assertTrue(result);

        result = StringUtil.inSorted("Hello", new String[]{"World"});
        assertFalse(result);
    }

    @Test
    public void testResolve() {
        try {
            String result = StringUtil.resolve(new URL("http://www.example.com"), "/path/file");
            assertEquals("http://www.example.com/path/file", result);
        } catch (MalformedURLException e) {
            fail(e.getMessage());
        }
    }
}