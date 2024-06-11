import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        // Arrange
        String str = null;

        // Act
        Locale locale = LocaleUtils.toLocale(str);

        // Assert
        Assert.assertNull(locale);
    }

    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyLanguageAndCountry() {
        // Arrange
        String str = "";

        // Act
        Locale locale = LocaleUtils.toLocale(str);

        // Assert
        Assert.assertEquals("", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "#";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_LengthLessThan2_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "a";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_NonUnderscoreCharAtBeginning_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "abc";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_InvalidLanguage_Case0_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "xy";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_InvalidLanguageCase1_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "xy_ABC";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_InvalidLanguageCase2_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "xy_BCD_ABC";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_LengthLessThan3_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "_A";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_NonUpperCaseCharAfterUnderscore_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "_ab";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_LengthLessThan5_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "_ABC";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_MissingUnderscoreAfterUnderscore_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "_ABC_";

        // Act
        LocaleUtils.toLocale(str);
    }

    // Other test methods for the remaining public methods in LocaleUtils class

}