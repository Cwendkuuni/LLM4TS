import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        String joined = StringUtil.join(strings, "-");
        Assert.assertEquals("Hello-World", joined);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        String joined = StringUtil.join(strings.iterator(), "-");
        Assert.assertEquals("Hello-World", joined);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        String joined = StringUtil.join(strings, "-");
        Assert.assertEquals("Hello-World", joined);
    }

    @Test
    public void testPadding() {
        String padded = StringUtil.padding(5);
        Assert.assertEquals("     ", padded);
    }

    @Test
    public void testIsBlank() {
        boolean blank = StringUtil.isBlank("");
        Assert.assertTrue(blank);

        boolean notBlank = StringUtil.isBlank("Hello");
        Assert.assertFalse(notBlank);
    }

    @Test
    public void testIsNumeric() {
        boolean numeric = StringUtil.isNumeric("12345");
        Assert.assertTrue(numeric);

        boolean notNumeric = StringUtil.isNumeric("Hello");
        Assert.assertFalse(notNumeric);
    }

    @Test
    public void testIsWhitespace() {
        boolean whitespace = StringUtil.isWhitespace(' ');
        Assert.assertTrue(whitespace);

        boolean notWhitespace = StringUtil.isWhitespace('A');
        Assert.assertFalse(notWhitespace);
    }

    @Test
    public void testIsActuallyWhitespace() {
        boolean whitespace = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(whitespace);

        boolean notWhitespace = StringUtil.isActuallyWhitespace('A');
        Assert.assertFalse(notWhitespace);
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalised = StringUtil.normaliseWhitespace("Hello   World");
        Assert.assertEquals("Hello World", normalised);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "Hello   World", false);
        Assert.assertEquals("Hello World", sb.toString());
    }

    @Test
    public void testIn() {
        boolean in = StringUtil.in("Hello", "Hello", "World");
        Assert.assertTrue(in);

        boolean notIn = StringUtil.in("Foo", "Hello", "World");
        Assert.assertFalse(notIn);
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"Apple", "Banana", "Orange"};
        boolean in = StringUtil.inSorted("Banana", haystack);
        Assert.assertTrue(in);

        boolean notIn = StringUtil.inSorted("Grapes", haystack);
        Assert.assertFalse(notIn);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String resolved = StringUtil.resolve(base, "/about");
        Assert.assertEquals("https://www.example.com/about", resolved);
    }

    @Test
    public void testResolveString() {
        String base = "https://www.example.com";
        String resolved = StringUtil.resolve(base, "/about");
        Assert.assertEquals("https://www.example.com/about", resolved);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("Hello");
        sb.append("World");
        Assert.assertEquals("HelloWorld", sb.toString());
    }
}