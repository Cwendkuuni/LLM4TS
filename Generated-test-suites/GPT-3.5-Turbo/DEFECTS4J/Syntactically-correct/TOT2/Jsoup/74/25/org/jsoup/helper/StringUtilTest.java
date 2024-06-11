import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        String joined = StringUtil.join(strings, "-");
        assertEquals("Hello-World", joined);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        Iterator<String> iterator = strings.iterator();
        String joined = StringUtil.join(iterator, "-");
        assertEquals("Hello-World", joined);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String joined = StringUtil.join(strings, "-");
        assertEquals("Hello-World", joined);
    }

    @Test
    public void testPadding() {
        String padding = StringUtil.padding(5);
        assertEquals("     ", padding);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("abc123"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalized = StringUtil.normaliseWhitespace("Hello   World");
        assertEquals("Hello World", normalized);
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("Hello", "Hello", "World"));
        assertFalse(StringUtil.in("Hello", "World", "Java"));
    }

    @Test
    public void testInSorted() {
        String[] strings = {"Apple", "Banana", "Orange"};
        assertTrue(StringUtil.inSorted("Banana", strings));
        assertFalse(StringUtil.inSorted("Grape", strings));
    }

    @Test
    public void testResolveWithURL() throws MalformedURLException {
        String resolved = StringUtil.resolve(new URL("http://example.com"), "images/pic.jpg").toExternalForm();
        assertEquals("http://example.com/images/pic.jpg", resolved);
    }

    @Test
    public void testResolveWithString() {
        String resolved = StringUtil.resolve("http://example.com", "images/pic.jpg");
        assertEquals("http://example.com/images/pic.jpg", resolved);
    }

}