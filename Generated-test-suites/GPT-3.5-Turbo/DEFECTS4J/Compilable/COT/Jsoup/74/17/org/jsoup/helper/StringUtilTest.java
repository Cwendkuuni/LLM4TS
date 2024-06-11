package org.jsoup.helper;
import org.junit.Assert;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollectionWithSeparator() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        String sep = "-";
        String expected = "Hello-World";
        String result = StringUtil.join(strings, sep);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorWithSeparator() {
        Iterator<String> strings = Arrays.asList("Hello", "World").iterator();
        String sep = "-";
        String expected = "Hello-World";
        String result = StringUtil.join(strings, sep);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayWithSeparator() {
        String[] strings = {"Hello", "World"};
        String sep = "-";
        String expected = "Hello-World";
        String result = StringUtil.join(strings, sep);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        int width = 5;
        String expected = "     ";
        String result = StringUtil.padding(width);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlankWithNullInput() {
        String input = null;
        boolean expected = true;
        boolean result = StringUtil.isBlank(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlankWithEmptyStringInput() {
        String input = "";
        boolean expected = true;
        boolean result = StringUtil.isBlank(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlankWithWhitespaceInput() {
        String input = "  ";
        boolean expected = true;
        boolean result = StringUtil.isBlank(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBlankWithNonBlankInput() {
        String input = "Hello";
        boolean expected = false;
        boolean result = StringUtil.isBlank(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumericWithNullInput() {
        String input = null;
        boolean expected = false;
        boolean result = StringUtil.isNumeric(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumericWithEmptyStringInput() {
        String input = "";
        boolean expected = false;
        boolean result = StringUtil.isNumeric(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumericWithNonNumericInput() {
        String input = "Hello";
        boolean expected = false;
        boolean result = StringUtil.isNumeric(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNumericWithNumericInput() {
        String input = "12345";
        boolean expected = true;
        boolean result = StringUtil.isNumeric(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceWithWhitespaceChar() {
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceWithNonWhitespaceChar() {
        int c = 'A';
        boolean expected = false;
        boolean result = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsActuallyWhitespaceWithWhitespaceChar() {
        int c = ' ';
        boolean expected = true;
        boolean result = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsActuallyWhitespaceWithNonWhitespaceChar() {
        int c = 'A';
        boolean expected = false;
        boolean result = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "Hello   World";
        String expected = "Hello World";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAppendNormalisedWhitespaceWithLeadingWhitespace() {
        StringBuilder accum = new StringBuilder();
        String input = "  Hello World";
        String expected = "Hello World";
        StringUtil.appendNormalisedWhitespace(accum, input, true);
        String result = accum.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAppendNormalisedWhitespaceWithoutLeadingWhitespace() {
        StringBuilder accum = new StringBuilder();
        String input = "Hello World";
        String expected = "Hello World";
        StringUtil.appendNormalisedWhitespace(accum, input, false);
        String result = accum.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInWithExistingString() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInWithNonExistingString() {
        String needle = "Hello";
        String[] haystack = {"Hi", "World"};
        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSortedWithExistingString() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSortedWithNonExistingString() {
        String needle = "Hello";
        String[] haystack = {"Hi", "World"};
        boolean expected = false;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolve() {
        try {
            URL base = new URL("http://www.example.com");
            String relUrl = "/path";
            URL expected = new URL("http://www.example.com/path");
            URL result = StringUtil.resolve(base, relUrl);
            Assert.assertEquals(expected, result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResolveWithStringResult() {
        String base = "http://www.example.com";
        String relUrl = "/path";
        String expected = "http://www.example.com/path";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveWithInvalidUrls() {
        String base = "invalidUrl";
        String relUrl = "/path";
        String expected = "";
        String result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        Assert.assertNotSame(sb1, sb2);
        Assert.assertEquals(0, sb1.length());
        Assert.assertEquals(0, sb2.length());
    }
}