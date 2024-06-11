import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locales
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de", LocaleUtils.toLocale("de").toString());

        // Test empty locale
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid locale
        try {
            LocaleUtils.toLocale("xyz");
            assert False();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = LocaleUtils.toLocale("fr_CA_QC");
        List lookupList = LocaleUtils.localeLookupList(locale);

        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals("fr_CA", lookupList.get(1).toString());
        assertEquals("fr", lookupList.get(2).toString());
        assertEquals("en", lookupList.get(3).toString());
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(331, availableLocales.size());

        // Check some known locales
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.GERMAN));

        // Check some invalid locales
        assertFalse(availableLocales.contains(Locale.ITALIAN));
        assertFalse(availableLocales.contains(Locale.JAPANESE));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));

        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE));

        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        List languages = LocaleUtils.languagesByCountry("CA");
        assertEquals(11, languages.size());

        // Check some known languages
        assertTrue(languages.contains(Locale.FRENCH));
        assertTrue(languages.contains(Locale.ENGLISH));

        // Check some invalid languages
        assertFalse(languages.contains(Locale.ITALIAN));
        assertFalse(languages.contains(Locale.JAPANESE));

        // Check empty country
        List emptyCountryLanguages = LocaleUtils.languagesByCountry(null);
        assertEquals(0, emptyCountryLanguages.size());

        // Check invalid country
        List invalidCountryLanguages = LocaleUtils.languagesByCountry("XY");
        assertEquals(0, invalidCountryLanguages.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(12, countries.size());

        // Check some known countries
        assertTrue(countries.contains(Locale.FRENCH));
        assertTrue(countries.contains(Locale.CANADIAN_FRENCH));

        // Check some invalid countries
        assertFalse(countries.contains(Locale.ITALIAN));
        assertFalse(countries.contains(Locale.JAPANESE));

        // Check empty language
        List emptyLanguageCountries = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, emptyLanguageCountries.size());

        // Check invalid language
        List invalidLanguageCountries = LocaleUtils.countriesByLanguage("XY");
        assertEquals(0, invalidLanguageCountries.size());
    }
}