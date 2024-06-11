import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case: Convert a valid locale string
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());

        // Edge case: Convert a valid locale string with variant
        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());

        // Error scenario: Convert an invalid locale string
        try {
            locale = LocaleUtils.toLocale("invalid");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case: Create locale lookup list from a specific locale
        Locale locale = new Locale("fr", "CA", "xxx");
        List lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));

        // Edge case: Create locale lookup list from a specific locale and default locale
        Locale defaultLocale = new Locale("en");
        lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(defaultLocale, lookupList.get(3));

        // Error scenario: Create locale lookup list from null locale
        lookupList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(defaultLocale, lookupList.get(0));
        assertEquals(1, lookupList.size());
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case: Check if available locale list is not empty
        List availableLocales = LocaleUtils.availableLocaleList();
        assertFalse(availableLocales.isEmpty());

        // Edge case: Check if available locale list does not contain duplicate locales
        assertEquals(availableLocales.size(), new HashSet<>(availableLocales).size());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case: Check if available locale set is not empty
        Set availableLocales = LocaleUtils.availableLocaleSet();
        assertFalse(availableLocales.isEmpty());

        // Edge case: Check if available locale set does not contain duplicate locales
        assertEquals(availableLocales.size(), new HashSet<>(availableLocales).size());
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case: Check if a known locale is available
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

        // Typical use case: Check if an unknown locale is not available
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case: Get languages supported for a given country
        List languages = LocaleUtils.languagesByCountry("CA");
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("fr", "CA")));

        // Edge case: Get languages supported for a country with no corresponding locale
        languages = LocaleUtils.languagesByCountry("XX");
        assertTrue(languages.isEmpty());

        // Error scenario: Get languages for null country code
        languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case: Get countries supported for a given language
        List countries = LocaleUtils.countriesByLanguage("fr");
        assertTrue(countries.contains(new Locale("fr", "CA")));
        assertTrue(countries.contains(new Locale("fr", "FR")));

        // Edge case: Get countries supported for a language with no corresponding locale
        countries = LocaleUtils.countriesByLanguage("xx");
        assertTrue(countries.isEmpty());

        // Error scenario: Get countries for null language code
        countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
}