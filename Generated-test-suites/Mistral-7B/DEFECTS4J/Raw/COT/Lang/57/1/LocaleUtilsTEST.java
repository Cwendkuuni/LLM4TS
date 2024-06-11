import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("fr");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(expected, actual);

        // Edge case test with empty string
        String language = "";
        String country = "";
        Locale locale = LocaleUtils.toLocale(language + "_" + country);
        assertEquals(new Locale(language, country), locale);

        // Edge case test with empty language
        country = "US";
        locale = LocaleUtils.toLocale(language + "_" + country);
        assertEquals(Locale.US, locale);

        // Edge case test with empty country
        language = "en";
        locale = LocaleUtils.toLocale(language + "_");
        assertEquals(new Locale(language, ""), locale);

        // Exception handling test with null input
        assertNull(LocaleUtils.toLocale(null));

        // Exception handling test with invalid format
        String invalidFormat = "en_GB_XXX";
        try {
            LocaleUtils.toLocale(invalidFormat);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling test with language code not lowercase
        String upperCaseLanguage = "EN";
        try {
            LocaleUtils.toLocale(upperCaseLanguage + "_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling test with country code not uppercase
        String lowerCaseCountry = "ca";
        try {
            LocaleUtils.toLocale("en_" + lowerCaseCountry);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling test with invalid separator
        String invalidSeparator = "en_GB_";
        try {
            LocaleUtils.toLocale(invalidSeparator);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling test with invalid length
        String tooShort = "en_";
        try {
            LocaleUtils.toLocale(tooShort);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String tooLong = "en_GB_XXX_YYY";
        try {
            LocaleUtils.toLocale(tooLong);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr", ""),
                Locale.getDefault()
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);

        // Test with null input
        locale = null;
        expectedList = Collections.emptyList();
        actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);

        // Test with defaultLocale as null
        locale = new Locale("fr", "CA", "xxx");
        expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr", ""),
                Locale.getDefault()
        );
        Locale defaultLocale = null;
        actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, actualList);

        // Test with defaultLocale not null
        defaultLocale = new Locale("en");
        expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr", ""),
                Locale.getDefault()
        );
        actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = java.util.Locale.getAvailableLocalesList();
        assertEquals(expectedList, actualList);

        // Test with null input
        assertNull(LocaleUtils.availableLocaleList(null));
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualSet = java.util.Collections.unmodifiableSet(java.util.Locale.getAvailableLocales());
        assertEquals(expectedSet, actualSet);

        // Test with null input
        assertNotNull(LocaleUtils.availableLocaleSet(null));
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale1 = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("fr", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));

        // Test with null input
        Locale nullLocale = null;
        assertFalse(LocaleUtils.isAvailableLocale(nullLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> expectedList = LocaleUtils.languagesByCountry("CA");
        List<Locale> actualList = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA")
        );
        assertEquals(expectedList, actualList);

        // Test with null input
        assertNotNull(LocaleUtils.languagesByCountry(null));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> expectedList = LocaleUtils.countriesByLanguage("fr");
        List<Locale> actualList = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "BE"),
                new Locale("fr", "CH")
        );
        assertEquals(expectedList, actualList);

        // Test with null input
        assertNotNull(LocaleUtils.countriesByLanguage(null));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}