import org.junit.Test;
import org.junit.Assert;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollectionWithSeparator() {
        Collection<String> strings = new ArrayList<>(Arrays.asList("Hello", "World"));
        String result = StringUtil.join(strings, "-");
        Assert.assertEquals("Hello-World", result);
    }

    @Test
    public void testJoinIteratorWithSeparator() {
        List<String> strings = Arrays.asList("Hello", "World");
        Iterator<String> iterator = strings.iterator();
        String result = StringUtil.join(iterator, "-");
        Assert.assertEquals("Hello-World", result);
    }

    @Test
    public void testJoinArrayWithSeparator() {
        String[] strings = {"Hello", "World"};
        String result = StringUtil.join(strings, "-");
        Assert.assertEquals("Hello-World", result);
    }

    @Test
    public void testPadding() {
        int width = 5;
        String result = StringUtil.padding(width);
        Assert.assertEquals("     ", result);
    }

    @Test
    public void testIsBlankWithNullString() {
        String string = null;
        boolean result = StringUtil.isBlank(string);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWithEmptyString() {
        String string = "";
        boolean result = StringUtil.isBlank(string);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWithWhitespaceString() {
        String string = "   ";
        boolean result = StringUtil.isBlank(string);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWithNonBlankString() {
        String string = "Hello";
        boolean result = StringUtil.isBlank(string);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithNullString() {
        String string = null;
        boolean result = StringUtil.isNumeric(string);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithEmptyString() {
        String string = "";
        boolean result = StringUtil.isNumeric(string);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithNumericString() {
        String string = "12345";
        boolean result = StringUtil.isNumeric(string);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNumericWithNonNumericString() {
        String string = "Hello";
        boolean result = StringUtil.isNumeric(string);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsWhitespace() {
        int c = ' ';
        boolean result = StringUtil.isWhitespace(c);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c = ' ';
        boolean result = StringUtil.isActuallyWhitespace(c);
        Assert.assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   Hello    World  ";
        String result = StringUtil.normaliseWhitespace(string);
        Assert.assertEquals(" Hello World ", result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        String string = "   Hello    World  ";
        StringUtil.appendNormalisedWhitespace(sb, string, false);
        Assert.assertEquals(" Hello World ", sb.toString());
    }

    @Test
    public void testInWithExistElement() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInWithNotExistElement() {
        String needle = "Hello";
        String[] haystack = {"Hi", "Hey"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSortedWithExistElement() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInSortedWithNotExistElement() {
        String needle = "Hello";
        String[] haystack = {"Hi", "Hey"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://example.com/");
        String relUrl = "path/to/file";
        URL result = StringUtil.resolve(base, relUrl);
        URL expected = new URL("https://example.com/path/to/file");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveWithInvalidBaseUrl() {
        String baseUrl = "invalidUrl";
        String relUrl = "path/to/file";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("", result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        Assert.assertNotNull(sb);
        Assert.assertTrue(sb instanceof StringBuilder);
    }
}