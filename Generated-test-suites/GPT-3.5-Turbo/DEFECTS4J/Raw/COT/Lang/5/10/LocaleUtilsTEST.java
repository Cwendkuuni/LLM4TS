import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Test invalid input
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
        
        // Test null input
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
        
        // Test empty default locale
        assertEquals(Arrays.asList(locale), LocaleUtils.localeLookupList(locale, null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
        assertFalse(locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.getDefault()));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        assertNotNull(langs);
        assertFalse(langs.isEmpty());
        assertTrue(langs.contains(new Locale("en", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("en", "CA")));
    }
    
    // Additional test cases to cover edge cases and exception handling scenarios

    @Test
    public void testToLocaleWithInvalidFormat() {
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testLocaleLookupListWithNullDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr")
        );
        assertEquals(expected, LocaleUtils.localeLookupList(locale, null));
    }

}