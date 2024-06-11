
import org.junit.Test;
import org.junit.Assert;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;

public class StringUtilTest {

    @Test
    public void testJoinCollectionWithSeparator() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String sep = ",";
        
        String result = StringUtil.join(strings, sep);
        
        Assert.assertEquals("Hello,World,!", result);
    }

    @Test
    public void testJoinIteratorWithSeparator() {
        Iterator<String> strings = Arrays.asList("Hello", "World", "!").iterator();
        String sep = ",";
        
        String result = StringUtil.join(strings, sep);
        
        Assert.assertEquals("Hello,World,!", result);
    }

    @Test
    public void testJoinArrayWithSeparator() {
        String[] strings = {"Hello", "World", "!"};
        String sep = ",";
        
        String result = StringUtil.join(strings, sep);
        
        Assert.assertEquals("Hello,World,!", result);
    }

    @Test
    public void testPaddingWithPositiveWidth() {
        int width = 5;
        
        String result = StringUtil.padding(width);
        
        Assert.assertEquals("     ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingWithNegativeWidth() {
        int width = -5;
        
        StringUtil.padding(width);
    }

    @Test
    public void testIsBlankWithStringNull() {
        String string = null;
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWithStringEmpty() {
        String string = "";
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWithStringOnlyWhitespace() {
        String string = "   ";
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWithStringNotEmpty() {
        String string = "Hello";
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithStringNull() {
        String string = null;
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithStringEmpty() {
        String string = "";
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithStringOnlyDigits() {
        String string = "12345";
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNumericWithStringContainsNonDigits() {
        String string = "12a45";
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testIsWhitespaceWithWhitespaceCodePoint() {
        int c = ' ';
        
        boolean result = StringUtil.isWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithNonWhitespaceCodePoint() {
        int c = 'a';
        
        boolean result = StringUtil.isWhitespace(c);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithWhitespaceCodePoint() {
        int c = ' ';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithNonWhitespaceCodePoint() {
        int c = 'a';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testNormaliseWhitespaceWithMultipleSpaces() {
        String string = "Hello  World!";
        
        String result = StringUtil.normaliseWhitespace(string);
        
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testNormaliseWhitespaceWithNewlinesAndTabs() {
        String string = "Hello\n\tWorld!";
        
        String result = StringUtil.normaliseWhitespace(string);
        
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testAppendNormalisedWhitespaceWithStripLeading() {
        StringBuilder accum = new StringBuilder();
        String string = "  Hello  World!";
        boolean stripLeading = true;
        
        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
        
        Assert.assertEquals("Hello World!", accum.toString());
    }

    @Test
    public void testAppendNormalisedWhitespaceWithoutStripLeading() {
        StringBuilder accum = new StringBuilder();
        String string = "  Hello  World!";
        boolean stripLeading = false;
        
        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
        
        Assert.assertEquals("  Hello World!", accum.toString());
    }

    @Test
    public void testInWithNeedleInHaystack() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World", "!"};
        
        boolean result = StringUtil.in(needle, haystack);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testInWithNeedleNotInHaystack() {
        String needle = "Foo";
        String[] haystack = {"Hello", "World", "!"};
        
        boolean result = StringUtil.in(needle, haystack);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testInSortedWithNeedleInHaystack() {
        String needle = "Hello";
        String[] haystack = {"!", "Hello", "World"};
        
        boolean result = StringUtil.inSorted(needle, haystack);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testInSortedWithNeedleNotInHaystack() {
        String needle = "Foo";
        String[] haystack = {"Hello", "World", "!"};
        
        boolean result = StringUtil.inSorted(needle, haystack);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testResolveWithStringRelUrlAbsolute() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "http://example2.com";
        
        URL result = StringUtil.resolve(base, relUrl);
        
        Assert.assertEquals(new URL(relUrl), result);
    }

    @Test
    public void testResolveWithStringRelUrlRelative() throws MalformedURLException {
        URL base = new URL("http://example.com/path");
        String relUrl = "../file.html";
        
        URL result = StringUtil.resolve(base, relUrl);
        
        Assert.assertEquals(new URL("http://example.com/file.html"), result);
    }

    @Test
    public void testResolveWithStringRelUrlInvalid() {
        String baseUrl = "http://example.com";
        String relUrl = "invalid";
        
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        Assert.assertEquals("", result);
    }

    @Test
    public void testResolveWithStringRelUrlAbsoluteException() {
        String baseUrl = "http://example.com";
        String relUrl = "http:/invalid";
        
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        Assert.assertEquals("", result);
    }

    @Test
    public void testStringBuilderWithEmptyBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        
        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testStringBuilderWithBuilderExceedingMaxSize() {
        StringBuilder sb = new StringBuilder(StringUtil.MaxCachedBuilderSize + 1);
        
        StringBuilder result = StringUtil.stringBuilder();
        
        Assert.assertEquals("", result.toString());
    }
}

