import org.jsoup.helper.StringUtil;
import org.junit.Test;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Test", "String", "Join");
        String result = StringUtil.join(strings, ",");
        assertEquals("Test,String,Join", result);
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("Test", "String", "Join").iterator();
        String result = StringUtil.join(strings, ",");
        assertEquals("Test,String,Join", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Test", "String", "Join"};
        String result = StringUtil.join(strings, ",");
        assertEquals("Test,String,Join", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
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
        assertFalse(StringUtil.isBlank("Test"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("1234"));
        assertFalse(StringUtil.isNumeric("1234a"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Test  String");
        assertEquals("Test String", result);
    }

    @Test
    public void testIn() {
        String[] haystack = {"Test", "String", "Join"};
        assertTrue(StringUtil.in("Test", haystack));
        assertFalse(StringUtil.in("Missing", haystack));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"Join", "String", "Test"};
        assertTrue(StringUtil.inSorted("Test", haystack));
        assertFalse(StringUtil.inSorted("Missing", haystack));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://www.example.com/path/");
        String relUrl = "../file.html";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("http://www.example.com/file.html", result.toString());
    }

}