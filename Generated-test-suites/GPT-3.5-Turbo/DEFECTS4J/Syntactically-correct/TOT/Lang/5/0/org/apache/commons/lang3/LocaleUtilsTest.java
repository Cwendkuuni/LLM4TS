import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Set up any necessary preconditions
    }

    @Test
    public void testToLocale_ValidLocaleString_ReturnsLocaleObject() {
        // Arrange
        String validLocaleString = "en_GB";
        Locale expectedLocale = new Locale("en", "GB");

        // Act
        Locale actualLocale = LocaleUtils.toLocale(validLocaleString);

        // Assert
        assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleString_ThrowsIllegalArgumentException() {
        // Arrange
        String invalidLocaleString = "abc";

        // Act
        LocaleUtils.toLocale(invalidLocaleString);

        // Assert
        // Exception is expected
    }

    @Test
    public void testLocaleLookupList_SpecificLocale_ReturnsListOfLocalesInDescendingOrderOfSpecificity() {
        // Arrange
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(defaultLocale);

        // Act
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);

        // Assert
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList_ReturnsListOfAvailableLocales() {
        // Arrange
        List<Locale> expectedList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;

        // Act
        List<Locale> actualList = LocaleUtils.availableLocaleList();

        // Assert
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet_ReturnsSetOfAvailableLocales() {
        // Arrange
        Set<Locale> expectedSet = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;

        // Act
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();

        // Assert
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale_KnownLocale_ReturnsTrue() {
        // Arrange
        Locale knownLocale = new Locale("en", "US");

        // Act
        boolean actualResult = LocaleUtils.isAvailableLocale(knownLocale);

        // Assert
        assertTrue(actualResult);
    }

    @Test
    public void testIsAvailableLocale_UnknownLocale_ReturnsFalse() {
        // Arrange
        Locale unknownLocale = new Locale("zz", "ZZ");

        // Act
        boolean actualResult = LocaleUtils.isAvailableLocale(unknownLocale);

        // Assert
        assertFalse(actualResult);
    }

    @Test
    public void testLanguagesByCountry_KnownCountry_ReturnsListofLanguages() {
        // Arrange
        String knownCountryCode = "US";
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));

        // Act
        List<Locale> actualList = LocaleUtils.languagesByCountry(knownCountryCode);

        // Assert
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountry_UnknownCountry_ReturnsEmptyList() {
        // Arrange
        String unknownCountryCode = "ZZ";

        // Act
        List<Locale> actualList = LocaleUtils.languagesByCountry(unknownCountryCode);

        // Assert
        assertTrue(actualList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_KnownLanguage_ReturnsListofCountries() {
        // Arrange
        String knownLanguageCode = "en";
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));

        // Act
        List<Locale> actualList = LocaleUtils.countriesByLanguage(knownLanguageCode);

        // Assert
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_UnknownLanguage_ReturnsEmptyList() {
        // Arrange
        String unknownLanguageCode = "zz";

        // Act
        List<Locale> actualList = LocaleUtils.countriesByLanguage(unknownLanguageCode);

        // Assert
        assertTrue(actualList.isEmpty());
    }
}