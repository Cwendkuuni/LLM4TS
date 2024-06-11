package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinWithCollectionAndSeparator() {
        Collection<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String result = StringUtil.join(strings, ",");
        Assert.assertEquals("Hello,World,!", result);
    }

    @Test
    public void testJoinWithIteratorAndSeparator() {
        List<String> strings = Arrays.asList("Hello", "World", "!");
        Iterator<String> iterator = strings.iterator();

        String result = StringUtil.join(iterator, ",");
        Assert.assertEquals("Hello,World,!", result);
    }

    @Test
    public void testJoinWithArrayAndSeparator() {
        String[] strings = {"Hello", "World", "!"};

        String result = StringUtil.join(strings, ",");
        Assert.assertEquals("Hello,World,!", result);
    }

    @Test
    public void testPaddingWithPositiveWidth() {
        String result = StringUtil.padding(5);
        Assert.assertEquals("     ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingWithNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIsBlankWithNullString() {
        boolean result = StringUtil.isBlank(null);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWithEmptyString() {
        boolean result = StringUtil.isBlank("");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWithWhitespaceString() {
        boolean result = StringUtil.isBlank("    ");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWithNonBlankString() {
        boolean result = StringUtil.isBlank("Hello World!");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithNullString() {
        boolean result = StringUtil.isNumeric(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithEmptyString() {
        boolean result = StringUtil.isNumeric("");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithNonNumericString() {
        boolean result = StringUtil.isNumeric("Hello");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericWithNumericString() {
        boolean result = StringUtil.isNumeric("12345");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithWhitespaceCharacters() {
        boolean result = StringUtil.isWhitespace(' ');
        Assert.assertTrue(result);

        result = StringUtil.isWhitespace('\t');
        Assert.assertTrue(result);

        result = StringUtil.isWhitespace('\n');
        Assert.assertTrue(result);

        result = StringUtil.isWhitespace('\f');
        Assert.assertTrue(result);

        result = StringUtil.isWhitespace('\r');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceWithNonWhitespaceCharacter() {
        boolean result = StringUtil.isWhitespace('A');
        Assert.assertFalse(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithWhitespaceCharacters() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\t');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\n');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\f');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace('\r');
        Assert.assertTrue(result);

        result = StringUtil.isActuallyWhitespace(160);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceWithNonWhitespaceCharacter() {
        boolean result = StringUtil.isActuallyWhitespace('A');
        Assert.assertFalse(result);
    }

    @Test
    public void testNormaliseWhitespaceWithStringContainingMultipleSpaces() {
        String result = StringUtil.normaliseWhitespace("Hello   World!");
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testAppendNormalisedWhitespaceWithStripLeadingTrue() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  Hello World!", true);
        Assert.assertEquals("Hello World!", sb.toString());
    }

    @Test
    public void testAppendNormalisedWhitespaceWithStripLeadingFalse() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "  Hello World!", false);
        Assert.assertEquals("  Hello World!", sb.toString());
    }

    @Test
    public void testInWithExistingNeedleInHaystack() {
        String[] haystack = {"Hello", "World", "!"};

        boolean result = StringUtil.in("Hello", haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInWithNonExistingNeedleInHaystack() {
        String[] haystack = {"Hello", "World", "!"};

        boolean result = StringUtil.in("Foo", haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSortedWithExistingNeedleInSortedHaystack() {
        String[] haystack = {"!", "Hello", "World"};

        boolean result = StringUtil.inSorted("Hello", haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInSortedWithNonExistingNeedleInSortedHaystack() {
        String[] haystack = {"!", "Hello", "World"};

        boolean result = StringUtil.inSorted("Foo", haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testResolveWithValidUrls() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "foo/bar.html";

        URL result = StringUtil.resolve(base, relUrl);
        URL expected = new URL("http://example.com/foo/bar.html");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveWithInvalidBaseUrl() {
        String baseUrl = "example.com";
        String relUrl = "foo/bar.html";

        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("", result);
    }

    @Test
    public void testResolveWithInvalidRelativeUrl() {
        String baseUrl = "http://example.com";
        String relUrl = "http://example.com/foo/bar.html";

        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(relUrl, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("Hello World!");

        Assert.assertEquals("Hello World!", sb.toString());
    }
}