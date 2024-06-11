import org.jsoup.helper.StringUtil;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String joined = StringUtil.join(strings, " ");
        assertEquals("Hello World !", joined);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("One", "Two", "Three");
        Iterator<String> iterator = strings.iterator();
        String joined = StringUtil.join(iterator, "-");
        assertEquals("One-Two-Three", joined);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"This", "is", "an", "array"};
        String joined = StringUtil.join(strings, "/");
        assertEquals("This/is/an/array", joined);
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("  ", StringUtil.padding(2));
        assertEquals("         ", StringUtil.padding(9));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("abc123"));
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("This is a test", StringUtil.normaliseWhitespace("This     is    a          test"));
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("apple", "apple", "banana"));
        assertFalse(StringUtil.in("orange", "apple", "banana"));
    }

    @Test
    public void testInSorted() {
        String[] array = {"apple", "banana", "cherry"};
        assertTrue(StringUtil.inSorted("banana", array));
        assertFalse(StringUtil.inSorted("kiwi", array));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "path/file.html";
        URL resolved = StringUtil.resolve(base, relUrl);
        assertEquals("http://www.example.com/path/file.html", resolved.toString());
    }

}