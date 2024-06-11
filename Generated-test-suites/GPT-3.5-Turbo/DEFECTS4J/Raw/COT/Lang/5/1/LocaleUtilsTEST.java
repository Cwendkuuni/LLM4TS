import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.JAPAN, LocaleUtils.toLocale("ja_JP"));

        // Test invalid locale strings
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            LocaleUtils.toLocale("EN");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            LocaleUtils.toLocale("frfr");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            LocaleUtils.toLocale("fr_FR_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test localeLookupList with only language
        Locale locale = new Locale("en");
        assertEquals(Arrays.asList(locale, new Locale("en"), Locale.getDefault()), 
                     LocaleUtils.localeLookupList(locale));

        // Test localeLookupList with language and country
        locale = new Locale("en", "GB");
        assertEquals(Arrays.asList(locale, new Locale("en", "GB"), new Locale("en"), Locale.getDefault()),
                     LocaleUtils.localeLookupList(locale));

        // Test localeLookupList with language, country, and variant
        locale = new Locale("en", "GB", "xxx");
        assertEquals(Arrays.asList(locale, new Locale("en", "GB"), new Locale("en"), Locale.getDefault()),
                     LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test that availableLocaleList returns a non-empty list
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());

        // Test that all returned locales are valid
        for (Locale locale : LocaleUtils.availableLocaleList()) {
            assertNotNull(locale);
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test that availableLocaleSet returns a non-empty set
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());

        // Test that all returned locales are valid
        for (Locale locale : LocaleUtils.availableLocaleSet()) {
            assertNotNull(locale);
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with a known locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Test with an unknown locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xy")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with a known country code
        assertEquals(Arrays.asList(Locale.ENGLISH), LocaleUtils.languagesByCountry("GB"));

        // Test with an unknown country code
        assertTrue(LocaleUtils.languagesByCountry("XX").isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with a known language code
        assertEquals(Arrays.asList(Locale.UK), LocaleUtils.countriesByLanguage("en"));

        // Test with an unknown language code
        assertTrue(LocaleUtils.countriesByLanguage("xx").isEmpty());
    }
}