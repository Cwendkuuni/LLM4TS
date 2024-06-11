import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtilTest {

    // Test join collection with separator
    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("one");
        strings.add("two");
        strings.add("three");

        String result = StringUtil.join(strings, "-");

        assertEquals("one-two-three", result);
    }

    // Test join array with separator
    @Test
    public void testJoinArray() {
        String[] strings = {"apple", "banana", "cherry"};

        String result = StringUtil.join(strings, "-");

        assertEquals("apple-banana-cherry", result);
    }

    // Test padding
    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);

        assertEquals("     ", result);
    }

    // Test isBlank
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("not blank"));
    }

    // Test isNumeric
    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("abc123"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric(null));
    }

    // Test isWhitespace
    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    // Test normaliseWhitespace
    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("   Hello    World   ");

        assertEquals(" Hello World ", result);
    }

    // Test in
    @Test
    public void testIn() {
        String[] haystack = {"apple", "banana", "cherry"};

        assertTrue(StringUtil.in("banana", haystack));
        assertFalse(StringUtil.in("orange", haystack));
    }

    // Test inSorted
    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry"};

        assertTrue(StringUtil.inSorted("banana", haystack));
        assertFalse(StringUtil.inSorted("orange", haystack));
    }

    // Test resolve URL
    @Test
    public void testResolveURL() throws MalformedURLException {
        String base = "http://www.example.com";
        String relUrl = "images/picture.jpg";

        String result = StringUtil.resolve(base, relUrl).toString();

        assertEquals("http://www.example.com/images/picture.jpg", result);
    }

    // Test resolve URL with exception handling
    @Test
    public void testResolveURLWithMalformedURLException() {
        String base = "http://www.example.com";
        String relUrl = "invalid url";

        String result = StringUtil.resolve(base, relUrl);

        assertEquals("", result);
    }
}