package de.beiri22.stringincrementor;

import org.junit.Assert;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff_withEmptyStrings_shouldReturnEmptyRelativeString() {
        // Arrange
        String a = "";
        String b = "";

        // Act
        RelativeString result = StringIncrementor.diff(a, b);

        // Assert
        Assert.assertTrue(result.getLinks().isEmpty());
        Assert.assertEquals("", result.getAbsolute());
    }

    @Test
    public void testDiff_withEqualStrings_shouldReturnEmptyRelativeString() {
        // Arrange
        String a = "abc";
        String b = "abc";

        // Act
        RelativeString result = StringIncrementor.diff(a, b);

        // Assert
        Assert.assertTrue(result.getLinks().isEmpty());
        Assert.assertEquals("", result.getAbsolute());
    }

    @Test
    public void testDiff_withOneCharacterDiff_shouldReturnRelativeStringWithSingleLink() {
        // Arrange
        String a = "abc";
        String b = "abd";

        // Act
        RelativeString result = StringIncrementor.diff(a, b);

        // Assert
        Assert.assertEquals(1, result.getLinks().size());
        StringLink link = result.getLinks().get(0);
        Assert.assertEquals(2, link.getPosOrig());
        Assert.assertEquals(2, link.getPosNew());
        Assert.assertEquals(1, link.getLen());
        Assert.assertEquals("", result.getAbsolute());
    }

    @Test
    public void testDiff_withPartialMatch_shouldReturnRelativeStringWithLink() {
        // Arrange
        String a = "abcdef";
        String b = "abddef";

        // Act
        RelativeString result = StringIncrementor.diff(a, b);

        // Assert
        Assert.assertEquals(1, result.getLinks().size());
        StringLink link = result.getLinks().get(0);
        Assert.assertEquals(2, link.getPosOrig());
        Assert.assertEquals(2, link.getPosNew());
        Assert.assertEquals(4, link.getLen());
        Assert.assertEquals("", result.getAbsolute());
    }

    @Test
    public void testDiff_withNoMatch_shouldReturnFullAbsoluteStringInRelativeString() {
        // Arrange
        String a = "abc";
        String b = "def";

        // Act
        RelativeString result = StringIncrementor.diff(a, b);

        // Assert
        Assert.assertTrue(result.getLinks().isEmpty());
        Assert.assertEquals("def", result.getAbsolute());
    }

    @Test
    public void testPatch_withEmptyStringAndEmptyRelativeString_shouldReturnEmptyString() {
        // Arrange
        String a = "";
        RelativeString r = new RelativeString();

        // Act
        String result = StringIncrementor.patch(a, r);

        // Assert
        Assert.assertEquals("", result);
    }

    @Test
    public void testPatch_withStringAndEmptyRelativeString_shouldReturnSameString() {
        // Arrange
        String a = "abc";
        RelativeString r = new RelativeString();

        // Act
        String result = StringIncrementor.patch(a, r);

        // Assert
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testPatch_withStringAndRelativeString_shouldReturnPatchedString() {
        // Arrange
        String a = "abcdef";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(3, 0, 3);
        r.addLink(link);

        // Act
        String result = StringIncrementor.patch(a, r);

        // Assert
        Assert.assertEquals("abcabc", result);
    }

}