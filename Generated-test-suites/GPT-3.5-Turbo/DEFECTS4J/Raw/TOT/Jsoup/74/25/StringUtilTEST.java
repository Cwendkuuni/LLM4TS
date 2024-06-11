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
        String joinedString = StringUtil.join(strings, ",");
        Assert.assertEquals("Hello,World", joinedString);
    }

    @Test
    public void testJoinIteratorWithSeparator() {
        Collection<String> strings = Arrays.asList("Hello", "World");
        Iterator<String> iterator = strings.iterator();
        String joinedString = StringUtil.join(iterator, ",");
        Assert.assertEquals("Hello,World", joinedString);
    }

    @Test
    public void testJoinArrayWithSeparator() {
        String[] strings = {"Hello", "World"};
        String joinedString = StringUtil.join(strings, ",");
        Assert.assertEquals("Hello,World", joinedString);
    }

    @Test
    public void testPaddingWithPositiveWidth() {
        String padding = StringUtil.padding(5);
        Assert.assertEquals("     ", padding);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingWithNegativeWidth() {
        StringUtil.padding(-5);
    }

    @Test
    public void testIsBlankWithNullString() {
        boolean isBlank = StringUtil.isBlank(null);
        Assert.assertTrue(isBlank);
    }

    @Test
    public void testIsBlankWithEmptyString() {
        boolean isBlank = StringUtil.isBlank("");
        Assert.assertTrue(isBlank);
    }

    @Test
    public void testIsBlankWithWhitespaceString() {
        boolean isBlank = StringUtil.isBlank("         ");
        Assert.assertTrue(isBlank);
    }

    @Test
    public void testIsBlankWithNonBlankString() {
        boolean isBlank = StringUtil.isBlank("Hello");
        Assert.assertFalse(isBlank);
    }

    @Test
    public void testIsNumericWithNullString() {
        boolean isNumeric = StringUtil.isNumeric(null);
        Assert.assertFalse(isNumeric);
    }

    @Test
    public void testIsNumericWithEmptyString() {
        boolean isNumeric = StringUtil.isNumeric("");
        Assert.assertFalse(isNumeric);
    }

    @Test
    public void testIsNumericWithNumericString() {
        boolean isNumeric = StringUtil.isNumeric("12345");
        Assert.assertTrue(isNumeric);
    }

    @Test
    public void testIsNumericWithStringContainingNonNumericChars() {
        boolean isNumeric = StringUtil.isNumeric("Hello123");
        Assert.assertFalse(isNumeric);
    }

    @Test
    public void testIsWhitespaceWithWhitespaceChar() {
        boolean isWhitespace = StringUtil.isWhitespace(' ');
        Assert.assertTrue(isWhitespace);
    }

    @Test
    public void testIsWhitespaceWithNonWhitespaceChar() {
        boolean isWhitespace = StringUtil.isWhitespace('A');
        Assert.assertFalse(isWhitespace);
    }

    @Test
    public void testIsActuallyWhitespaceWithWhitespaceChar() {
        boolean isWhitespace = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(isWhitespace);
    }

    @Test
    public void testIsActuallyWhitespaceWithNonWhitespaceChar() {
        boolean isWhitespace = StringUtil.isActuallyWhitespace('A');
        Assert.assertFalse(isWhitespace);
    }

    @Test
    public void testNormaliseWhitespaceWithMultipleSpaces() {
        String normalizedString = StringUtil.normaliseWhitespace("Hello   World");
        Assert.assertEquals("Hello World", normalizedString);
    }

    @Test
    public void testNormaliseWhitespaceWithNewlineAndTab() {
        String normalizedString = StringUtil.normaliseWhitespace("Hello \n\tWorld");
        Assert.assertEquals("Hello World", normalizedString);
    }

    @Test
    public void testInWithMatchingStringInArray() {
        String[] array = {"Hello", "World"};
        boolean isInArray = StringUtil.in("Hello", array);
        Assert.assertTrue(isInArray);
    }

    @Test
    public void testInWithNonMatchingStringInArray() {
        String[] array = {"Hello", "World"};
        boolean isInArray = StringUtil.in("Foo", array);
        Assert.assertFalse(isInArray);
    }

    @Test
    public void testInSortedWithMatchingStringInArray() {
        String[] array = {"Hello", "World"};
        boolean isInArray = StringUtil.inSorted("Hello", array);
        Assert.assertTrue(isInArray);
    }

    @Test
    public void testInSortedWithNonMatchingStringInArray() {
        String[] array = {"Hello", "World"};
        boolean isInArray = StringUtil.inSorted("Foo", array);
        Assert.assertFalse(isInArray);
    }

    @Test
    public void testResolveWithAbsoluteBaseAndRelativeUrl() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relativeUrl = "/path";
        URL resolvedUrl = StringUtil.resolve(base, relativeUrl);
        Assert.assertEquals("http://example.com/path", resolvedUrl.toString());
    }

    @Test
    public void testResolveWithRelativeBaseAndRelativeUrl() {
        String base = "path";
        String relativeUrl = "/file";
        String resolvedUrl = StringUtil.resolve(base, relativeUrl);
        Assert.assertEquals("/path/file", resolvedUrl);
    }

    @Test
    public void testResolveWithInvalidBaseAndRelativeUrl() {
        String base = "invalid";
        String relativeUrl = "/file";
        String resolvedUrl = StringUtil.resolve(base, relativeUrl);
        Assert.assertEquals("", resolvedUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder builder = StringUtil.stringBuilder();
        builder.append("Hello");
        Assert.assertEquals("Hello", builder.toString());
    }
}