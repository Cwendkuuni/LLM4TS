import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test null input
        assertNull(LocaleUtils.toLocale(null));
        
        // Test valid input
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(locale1.getLanguage(), "en");
        assertEquals(locale1.getCountry(), "");
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(locale2.getLanguage(), "en");
        assertEquals(locale2.getCountry(), "GB");
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(locale3.getLanguage(), "en");
        assertEquals(locale3.getCountry(), "GB");
        assertEquals(locale3.getVariant(), "xxx");
        
        // Test invalid input
        try {
            LocaleUtils.toLocale("abc");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("en");
        
        // Test localeLookupList(Locale)
        List<Locale> result1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(result1.size(), 3);
        assertEquals(result1.get(0), locale1);
        assertEquals(result1.get(1), new Locale("fr", "CA"));
        assertEquals(result1.get(2), new Locale("fr"));
        
        List<Locale> result2 = LocaleUtils.localeLookupList(locale2);
        assertEquals(result2.size(), 2);
        assertEquals(result2.get(0), locale2);
        assertEquals(result2.get(1), Locale.getDefault());
        
        // Test localeLookupList(Locale, Locale)
        List<Locale> result3 = LocaleUtils.localeLookupList(locale1, locale2);
        assertEquals(result3.size(), 4);
        assertEquals(result3.get(0), locale1);
        assertEquals(result3.get(1), new Locale("fr", "CA"));
        assertEquals(result3.get(2), new Locale("fr"));
        assertEquals(result3.get(3), locale2);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertTrue(locales.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
        assertTrue(locales.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        
        Locale locale2 = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test null input
        List<Locale> nullResult = LocaleUtils.languagesByCountry(null);
        assertNotNull(nullResult);
        assertEquals(nullResult.size(), 0);
        
        // Test valid input
        List<Locale> result1 = LocaleUtils.languagesByCountry("CA");
        assertEquals(result1.size(), 2);
        assertEquals(result1.get(0), new Locale("fr", "CA"));
        assertEquals(result1.get(1), new Locale("en", "CA"));
        
        // Test invalid input
        List<Locale> result2 = LocaleUtils.languagesByCountry("XX");
        assertNotNull(result2);
        assertEquals(result2.size(), 0);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test null input
        List<Locale> nullResult = LocaleUtils.countriesByLanguage(null);
        assertNotNull(nullResult);
        assertEquals(nullResult.size(), 0);
        
        // Test valid input
        List<Locale> result1 = LocaleUtils.countriesByLanguage("fr");
        assertEquals(result1.size(), 11);
        assertEquals(result1.get(0), new Locale("fr", "BE"));
        assertEquals(result1.get(1), new Locale("fr", "BJ"));
        
        // Test invalid input
        List<Locale> result2 = LocaleUtils.countriesByLanguage("XX");
        assertNotNull(result2);
        assertEquals(result2.size(), 0);
    }

}