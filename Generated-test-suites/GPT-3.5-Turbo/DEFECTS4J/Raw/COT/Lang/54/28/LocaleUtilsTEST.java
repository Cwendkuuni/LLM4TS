import org.junit.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test basic functionality
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("eng");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test with locale and default locale
        assertNotNull(LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH));
        assertEquals(4, LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH).size());
        assertEquals(Locale.ENGLISH, LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH).get(0));
        assertEquals(Locale.ENGLISH, LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH).get(1));
        assertEquals(new Locale("en", ""), LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH).get(2));
        assertEquals(Locale.FRENCH, LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH).get(3));

        // Test with null locale
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertEquals(0, LocaleUtils.localeLookupList(null).size());
        
        // Test with null locale and default locale
        assertNotNull(LocaleUtils.localeLookupList(null, Locale.FRENCH));
        assertEquals(1, LocaleUtils.localeLookupList(null, Locale.FRENCH).size());
        assertEquals(Locale.FRENCH, LocaleUtils.localeLookupList(null, Locale.FRENCH).get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with valid country code
        assertNotNull(LocaleUtils.languagesByCountry("GB"));
        assertFalse(LocaleUtils.languagesByCountry("GB").isEmpty());
        assertEquals(Locale.ENGLISH, LocaleUtils.languagesByCountry("GB").get(0));

        // Test with invalid country code
        assertNotNull(LocaleUtils.languagesByCountry("xx"));
        assertTrue(LocaleUtils.languagesByCountry("xx").isEmpty());
        
        // Test with null country code
        assertNotNull(LocaleUtils.languagesByCountry(null));
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with valid language code
        assertNotNull(LocaleUtils.countriesByLanguage("en"));
        assertFalse(LocaleUtils.countriesByLanguage("en").isEmpty());
        assertEquals(new Locale("en", "GB"), LocaleUtils.countriesByLanguage("en").get(0));

        // Test with invalid language code
        assertNotNull(LocaleUtils.countriesByLanguage("xx"));
        assertTrue(LocaleUtils.countriesByLanguage("xx").isEmpty());
        
        // Test with null language code
        assertNotNull(LocaleUtils.countriesByLanguage(null));
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }
}