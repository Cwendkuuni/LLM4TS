import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es_ES"));

        // Test invalid locale strings
        try {
            LocaleUtils.toLocale("en_GB_ invalid_variant");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_ invalid_separator");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_ US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale(null);
            fail("Expected null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test basic functionality
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertTrue(list.contains(locale));
        assertTrue(list.contains(new Locale("fr", "CA")));
        assertTrue(list.contains(new Locale("fr", "")));

        // Test empty list
        locale = null;
        list = LocaleUtils.localeLookupList(locale);
        assertTrue(list.isEmpty());

        // Test default locale
        Locale defaultLocale = new Locale("en");
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertTrue(list.contains(defaultLocale));
        assertTrue(list.size() > 3); // Guaranteed to be at least 3

        // Test null defaultLocale
        defaultLocale = null;
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.isEmpty());

        // Test non-empty list
        List<Locale> locales = Arrays.asList(Locale.getAvailableLocales());
        list = LocaleUtils.availableLocaleList();
        assertEquals(locales.size(), list.size());
        for (int i = 0; i < locales.size(); i++) {
            assertEquals(locales.get(i), list.get(i));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        assertEquals(true, LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Test unavailable locale
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(false, LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        String countryCode = null;
        List<Locale> list = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(list.isEmpty());

        // Test non-empty list
        String validCountryCode = "CA";
        list = LocaleUtils.languagesByCountry(validCountryCode);
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("es", "CA")
        );
        assertEquals(expectedList.size(), list.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), list.get(i));
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        String languageCode = null;
        List<Locale> list = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(list.isEmpty());

        // Test non-empty list
        String validLanguageCode = "fr";
        list = LocaleUtils.countriesByLanguage(validLanguageCode);
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "BE"),
                new Locale("fr", "CH"),
                new Locale("fr", "FR")
        );
        assertEquals(expectedList.size(), list.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), list.get(i));
        }
    }
}