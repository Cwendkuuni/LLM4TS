import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.JAPAN, LocaleUtils.toLocale("ja_JP_JP"));
        assertNotNull(LocaleUtils.toLocale("fr_CA"));
        assertNull(LocaleUtils.toLocale(null));
        // Test edge cases
        try {
            LocaleUtils.toLocale("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            LocaleUtils.toLocale("en_GB_invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
        assertTrue(LocaleUtils.localeLookupList(locale).contains(locale));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(new Locale("fr", "CA")));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(new Locale("fr")));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(Locale.getDefault()));
        // Test edge cases
        assertNotNull(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
        // Test edge cases
        assertEquals(LocaleUtils.availableLocaleList().size(), new HashSet<>(LocaleUtils.availableLocaleList()).size());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
        // Test edge cases
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertNotNull(LocaleUtils.languagesByCountry("US"));
        assertFalse(LocaleUtils.languagesByCountry("US").isEmpty());
        // Test edge cases
        assertTrue(LocaleUtils.languagesByCountry("US").contains(Locale.ENGLISH));
        assertTrue(LocaleUtils.languagesByCountry("XY").isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        assertNotNull(LocaleUtils.countriesByLanguage("en"));
        assertFalse(LocaleUtils.countriesByLanguage("en").isEmpty());
        // Test edge cases
        assertTrue(LocaleUtils.countriesByLanguage("en").contains(Locale.US));
        assertTrue(LocaleUtils.countriesByLanguage("xx").isEmpty());
    }

}