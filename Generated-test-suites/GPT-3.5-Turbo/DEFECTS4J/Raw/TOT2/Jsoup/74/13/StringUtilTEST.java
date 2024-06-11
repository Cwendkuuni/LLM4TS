import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

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
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("Hello"));
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello  World");
        assertEquals("Hello World", result);
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("Apple", "Apple", "Orange", "Banana"));
        assertFalse(StringUtil.in("Grape", "Apple", "Orange", "Banana"));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("Apple", new String[]{"Apple", "Banana", "Orange"}));
        assertFalse(StringUtil.inSorted("Grape", new String[]{"Apple", "Banana", "Orange"}));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://example.com/path/");
        String relUrl = "../newpath";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("http://example.com/newpath", result.toString());
    }

    @Test
    public void testResolveUrlString() {
        String baseUrl = "http://example.com/path/";
        String relUrl = "../newpath";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/newpath", result);
    }

}