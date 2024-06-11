import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validLocaleString_returnsLocale() {
        // Arrange
        String localeString = "en_GB";

        // Act
        Locale locale = LocaleUtils.toLocale(localeString);

        // Assert
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleString_throwsIllegalArgumentException() {
        // Arrange
        String localeString = "abc";

        // Act
        LocaleUtils.toLocale(localeString);

        // Assert
        // Exception expected
    }

    @Test
    public void testLocaleLookupList_localeAndDefaultLocale_returnsListWithLocales() {
        // Arrange
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        // Act
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);

        // Assert
        assertNotNull(localeList);
        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList_returnsAvailableLocaleList() {
        // Act
        List<Locale> localeList = LocaleUtils.availableLocaleList();

        // Assert
        assertNotNull(localeList);
        assertFalse(localeList.isEmpty());
        assertTrue(localeList.contains(Locale.US));
        assertTrue(localeList.contains(Locale.UK));
        assertTrue(localeList.contains(Locale.FRANCE));
    }

    @Test
    public void testAvailableLocaleSet_returnsAvailableLocaleSet() {
        // Act
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();

        // Assert
        assertNotNull(localeSet);
        assertFalse(localeSet.isEmpty());
        assertTrue(localeSet.contains(Locale.US));
        assertTrue(localeSet.contains(Locale.UK));
        assertTrue(localeSet.contains(Locale.FRANCE));
    }

    @Test
    public void testIsAvailableLocale_knownLocale_returnsTrue() {
        // Arrange
        Locale locale = Locale.US;

        // Act
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);

        // Assert
        assertTrue(isAvailable);
    }

    @Test
    public void testIsAvailableLocale_unknownLocale_returnsFalse() {
        // Arrange
        Locale locale = new Locale("abc");

        // Act
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);

        // Assert
        assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry_knownCountryCode_returnsListOfLanguages() {
        // Arrange
        String countryCode = "CA";

        // Act
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);

        // Assert
        assertNotNull(languages);
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testLanguagesByCountry_unknownCountryCode_returnsEmptyList() {
        // Arrange
        String countryCode = "abc";

        // Act
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);

        // Assert
        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_knownLanguageCode_returnsListOfCountries() {
        // Arrange
        String languageCode = "fr";

        // Act
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);

        // Assert
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("fr", "CA")));
        assertTrue(countries.contains(new Locale("fr", "FR")));
    }

    @Test
    public void testCountriesByLanguage_unknownLanguageCode_returnsEmptyList() {
        // Arrange
        String languageCode = "abc";

        // Act
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);

        // Assert
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

}