import org.jsoup.helper.StringUtil;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
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
        strings.add("!");
        String result = StringUtil.join(strings, ",");
        assertEquals("Hello,World,!", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("Hello", "World", "!");
        Iterator<String> iterator = strings.iterator();
        String result = StringUtil.join(iterator, ",");
        assertEquals("Hello,World,!", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String result = StringUtil.join(strings, ",");
        assertEquals("Hello,World,!", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-5);
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
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("12abc"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertFalse(StringUtil.isWhitespace('A'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello   World");
        assertEquals("Hello World", result);
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("dog", "cat", "dog", "bird"));
        assertFalse(StringUtil.in("snake", "cat", "dog", "bird"));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("dog", new String[]{"bird", "cat", "dog"}));
        assertFalse(StringUtil.inSorted("snake", new String[]{"bird", "cat", "dog"}));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "/path";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals(new URL("https://www.example.com/path"), result);
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/path";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("https://www.example.com/path", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
    }
}