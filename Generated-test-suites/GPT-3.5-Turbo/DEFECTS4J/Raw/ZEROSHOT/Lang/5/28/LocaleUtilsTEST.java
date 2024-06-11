import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void toLocale_NullInput_ReturnsNull() {
        // Arrange

        // Act
        Locale result = LocaleUtils.toLocale(null);

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        // Arrange
        String str = "invalid";

        // Act
        LocaleUtils.toLocale(str);

        // Assert
        // Exception expected
    }

    @Test
    public void toLocale_ValidLanguageCode_ReturnsLocaleWithLanguageOnly() {
        // Arrange
        String str = "en";

        // Act
        Locale result = LocaleUtils.toLocale(str);

        // Assert
        assertEquals(new Locale("en"), result);
    }

    @Test
    public void toLocale_ValidLanguageAndCountryCode_ReturnsLocaleWithLanguageAndCountry() {
        // Arrange
        String str = "en_GB";

        // Act
        Locale result = LocaleUtils.toLocale(str);

        // Assert
        assertEquals(new Locale("en", "GB"), result);
    }

    @Test
    public void toLocale_ValidLanguageCountryAndVariantCode_ReturnsLocaleWithLanguageCountryAndVariant() {
        // Arrange
        String str = "en_GB_xxx";

        // Act
        Locale result = LocaleUtils.toLocale(str);

        // Assert
        assertEquals(new Locale("en", "GB", "xxx"), result);
    }

    @Test
    public void localeLookupList_NullLocale_ReturnsEmptyList() {
        // Arrange

        // Act
        List<Locale> result = LocaleUtils.localeLookupList(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void localeLookupList_ValidLocale_ReturnsListWithMostSpecificLocaleFirst() {
        // Arrange
        Locale locale = new Locale("fr", "CA", "xxx");

        // Act
        List<Locale> result = LocaleUtils.localeLookupList(locale);

        // Assert
        assertEquals(4, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(Locale.getDefault(), result.get(3));
    }

    @Test
    public void localeLookupList_ValidLocaleWithDefault_ReturnsListWithMostSpecificLocaleFirstAndDefaultLast() {
        // Arrange
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        // Act
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);

        // Assert
        assertEquals(5, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
        assertEquals(Locale.getDefault(), result.get(4));
    }

    @Test
    public void availableLocaleList_ReturnsUnmodifiableList() {
        // Arrange

        // Act
        List<Locale> result = LocaleUtils.availableLocaleList();

        // Assert
        assertTrue(result instanceof java.util.Collections$UnmodifiableList);
    }

    @Test
    public void availableLocaleSet_ReturnsUnmodifiableSet() {
        // Arrange

        // Act
        Set<Locale> result = LocaleUtils.availableLocaleSet();

        // Assert
        assertTrue(result instanceof java.util.Collections$UnmodifiableSet);
    }

    @Test
    public void isAvailableLocale_AvailableLocale_ReturnsTrue() {
        // Arrange
        Locale locale = Locale.ENGLISH;

        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);

        // Assert
        assertTrue(result);
    }

    @Test
    public void isAvailableLocale_NotAvailableLocale_ReturnsFalse() {
        // Arrange
        Locale locale = new Locale("xx");

        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);

        // Assert
        assertFalse(result);
    }

    @Test
    public void languagesByCountry_NullCountryCode_ReturnsEmptyList() {
        // Arrange

        // Act
        List<Locale> result = LocaleUtils.languagesByCountry(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void languagesByCountry_ValidCountryCode_ReturnsListOfLanguagesForCountry() {
        // Arrange
        String countryCode = "GB";

        // Act
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);

        // Assert
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(x -> x.getCountry().equals(countryCode)));
    }

    @Test
    public void countriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        // Arrange

        // Act
        List<Locale> result = LocaleUtils.countriesByLanguage(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void countriesByLanguage_ValidLanguageCode_ReturnsListOfCountriesForLanguage() {
        // Arrange
        String languageCode = "fr";

        // Act
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);

        // Assert
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(x -> x.getLanguage().equals(languageCode)));
    }

    // Additional Tests

    @Test
    public void LocaleUtils_Constructor_DoesNotThrowException() {
        // Arrange

        // Act
        LocaleUtils utils = new LocaleUtils();

        // Assert
        assertNotNull(utils);
    }

    @Test
    public void localeLookupList_ValidLocaleWithDefault_ReturnsListWithoutDuplicates() {
        // Arrange
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("fr");

        // Act
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);

        // Assert
        assertEquals(4, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }

    @Test
    public void toLocale_ValidLanguageCountryAndVariantCode_ReturnsLocaleWithLanguageCountryAndVariantVariantInLowerCase() {
        // Arrange
        String str = "en_GB_XXX";

        // Act
        Locale result = LocaleUtils.toLocale(str);

        // Assert
        assertEquals(new Locale("en", "GB", "xxx"), result);
    }
}