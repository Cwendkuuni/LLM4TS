import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locales
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Test invalid locales
        try {
            LocaleUtils.toLocale("eng");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("eng_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test localeLookupList(Locale locale)
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), 
                     LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
                     
        // Test localeLookupList(Locale locale, Locale defaultLocale)
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en")),
                     LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test availableLocaleList()
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test availableLocaleSet()
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test isAvailableLocale(Locale locale)
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("eng")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test languagesByCountry(String countryCode)
        assertEquals(Arrays.asList(new Locale("en")), LocaleUtils.languagesByCountry("GB"));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("ZZ"));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test countriesByLanguage(String languageCode)
        assertEquals(Arrays.asList(new Locale("GB")), LocaleUtils.countriesByLanguage("en"));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("zz"));
    }
}