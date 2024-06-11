import org.junit.Test;
import java.util.Locale;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void toLocale_ValidLocaleString_ReturnsLocaleObject() {
        // Typical use cases
        Locale locale1 = LocaleUtils.toLocale("en"); // language only
        assertEquals(new Locale("en", ""), locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB"); // language and country
        assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx"); // language, country, and variant
        assertEquals(new Locale("en", "GB", "xxx"), locale3);

        // Edge case
        Locale locale4 = LocaleUtils.toLocale("fr_FR_"); // empty variant
        assertEquals(new Locale("fr", "FR", ""), locale4);

        // Error scenarios
        try {
            LocaleUtils.toLocale(null); // null input
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }

        try {
            LocaleUtils.toLocale("enGB"); // invalid format, missing underscore
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }

        try {
            LocaleUtils.toLocale("E1_GB"); // invalid format, invalid language code
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }

        try {
            LocaleUtils.toLocale("en_GB1"); // invalid format, invalid country code
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void localeLookupList_SingleLocale_ReturnsListWithLocaleAndDefaultLocale() {
        // Typical use case
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));

        // Edge case
        Locale locale2 = new Locale("en");
        List<Locale> result2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        assertEquals(2, result2.size());
        assertEquals(locale2, result2.get(0));
        assertEquals(defaultLocale, result2.get(1));

        // Error scenario
        assertEquals(0, LocaleUtils.localeLookupList(null, defaultLocale).size()); // null input
    }

    @Test
    public void availableLocaleList_ReturnsListOfInstalledLocales() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        // Check if sample locales are present
        assertTrue(availableLocales.contains(new Locale("en")));
        assertTrue(availableLocales.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(availableLocales.contains(new Locale("es", "MX")));
    }

    @Test
    public void availableLocaleSet_ReturnsSetOfInstalledLocales() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        // Check if sample locales are present
        assertTrue(availableLocales.contains(new Locale("en")));
        assertTrue(availableLocales.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(availableLocales.contains(new Locale("es", "MX")));
    }

    @Test
    public void isAvailableLocale_KnownLocale_ReturnsTrue() {
        // Typical use cases
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "CA", "xxx")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es", "MX")));

        // Error scenario
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de"))); // Unknown locale
    }

    @Test
    public void languagesByCountry_ValidCountryCode_ReturnsListOfLanguages() {
        // Typical use case
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertNotNull(languages);
        assertEquals(3, languages.size());
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("fr", "CA")));
        assertTrue(languages.contains(new Locale("tl")));

        // Edge case
        List<Locale> languages2 = LocaleUtils.languagesByCountry(""); // Empty country code
        assertEquals(0, languages2.size());

        // Error scenario
        List<Locale> languages3 = LocaleUtils.languagesByCountry("ZZ"); // Unknown country code
        assertEquals(0, languages3.size());
    }

    @Test
    public void countriesByLanguage_ValidLanguageCode_ReturnsListOfCountries() {
        // Typical use case
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries);
        assertEquals(35, countries.size());
        assertTrue(countries.contains(new Locale("fr", "CA")));
        assertTrue(countries.contains(new Locale("fr", "FR")));
        assertTrue(countries.contains(new Locale("fr", "MC")));
        
        // Edge case
        List<Locale> countries2 = LocaleUtils.countriesByLanguage(""); // Empty language code
        assertEquals(0, countries2.size());

        // Error scenario
        List<Locale> countries3 = LocaleUtils.countriesByLanguage("ZZ"); // Unknown language code
        assertEquals(0, countries3.size());
    }
}