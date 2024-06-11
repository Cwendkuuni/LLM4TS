import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String result = StringUtil.join(strings, " ");
        Assert.assertEquals("Hello World !", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("This", "is", "an", "Iterator");
        String result = StringUtil.join(strings.iterator(), "-");
        Assert.assertEquals("This-is-an-Iterator", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"One", "Two", "Three"};
        String result = StringUtil.join(strings, ",");
        Assert.assertEquals("One,Two,Three", result);
    }

    @Test
    public void testPaddingValidWidth() {
        String result = StringUtil.padding(5);
        Assert.assertEquals("     ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingInvalidWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIsBlank() {
        Assert.assertTrue(StringUtil.isBlank(null));
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank("   "));
        Assert.assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        Assert.assertTrue(StringUtil.isNumeric("12345"));
        Assert.assertFalse(StringUtil.isNumeric("abc123"));
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(StringUtil.isWhitespace(' '));
        Assert.assertTrue(StringUtil.isWhitespace('\t'));
        Assert.assertTrue(StringUtil.isWhitespace('\n'));
        Assert.assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(' '));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\t'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\n'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello     World  !");
        Assert.assertEquals("Hello World !", result);
    }

    @Test
    public void testIn() {
        Assert.assertTrue(StringUtil.in("One", "One", "Two", "Three"));
        Assert.assertFalse(StringUtil.in("Four", "One", "Two", "Three"));
    }

    @Test
    public void testInSorted() {
        String[] sortedArray = {"Apple", "Banana", "Orange"};
        Assert.assertTrue(StringUtil.inSorted("Banana", sortedArray));
        Assert.assertFalse(StringUtil.inSorted("Grapes", sortedArray));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "/page";
        URL resolvedUrl = StringUtil.resolve(base, relUrl);
        Assert.assertEquals("https://www.example.com/page", resolvedUrl.toString());
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com";
        String relUrl = "./page";
        String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("https://www.example.com/page", resolvedUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        Assert.assertEquals("Hello World", sb.toString());
    }
}