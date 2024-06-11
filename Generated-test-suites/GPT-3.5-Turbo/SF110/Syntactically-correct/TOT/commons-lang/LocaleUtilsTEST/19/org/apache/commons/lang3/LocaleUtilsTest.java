
import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void toLocale_typicalString_returnsLocaleObject() {
        // Arrange
        String str = "en_US";

        // Act
        Locale result = LocaleUtils.toLocale(str);

        // Assert
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void toLocale_emptyString_returnsEmptyLocaleObject() {
        // Arrange
        String str = "";

        // Act
        Locale result = LocaleUtils.toLocale(str);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_invalidFormat_throwIllegalArgumentException() {
        // Arrange
        String str = "invalid_format";

        // Act
        LocaleUtils.toLocale(str);

        // Assert
        // IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shortString_throwIllegalArgumentException() {
        // Arrange
        String str = "a";

        // Act
        LocaleUtils.toLocale(str);

        // Assert
        // IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_invalidUnderscoreFormat_throwIllegalArgumentException() {
        // Arrange
        String str = "_invalid";

        // Act
        LocaleUtils.toLocale(str);

        // Assert
        // IllegalArgumentException should be thrown
    }

    @Test
    public void localeLookupList_localeNotNull_returnsLookupListWithDefaults() {
        // Arrange
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");

        // Act
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "")));
        assertTrue(result.contains(new Locale("", "US")));
        assertTrue(result.contains(defaultLocale));
    }

    @Test
    public void availableLocaleList_returnsNonEmptyList() {
        // Arrange & Act
        List<Locale> result = LocaleUtils.availableLocaleList();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void availableLocaleSet_returnsNonEmptySet() {
        // Arrange & Act
        Set<Locale> result = LocaleUtils.availableLocaleSet();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void isAvailableLocale_availableLocale_returnsTrue() {
        // Arrange
        Locale locale = new Locale("en", "US");

        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);

        // Assert
        assertTrue(result);
    }

    @Test
    public void isAvailableLocale_unavailableLocale_returnsFalse() {
        // Arrange
        Locale locale = new Locale("xx", "XX");

        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);

        // Assert
        assertFalse(result);
    }

    @Test
    public void languagesByCountry_emptyCountryCode_returnsEmptyList() {
        // Arrange
        String countryCode = "";

        // Act
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void countriesByLanguage_emptyLanguageCode_returnsEmptyList() {
        // Arrange
        String languageCode = "";

        // Act
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

