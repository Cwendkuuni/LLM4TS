import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case: valid locale string
        Locale locale1 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale1.getLanguage());
        assertEquals("GB", locale1.getCountry());
        
        // Edge case: null input
        Locale locale2 = LocaleUtils.toLocale(null);
        assertNull(locale2);
        
        // Error scenario: invalid format
        try {
            Locale locale3 = LocaleUtils.toLocale("en_USA");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        // Typical use case: locale with country code and variant
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(4, list1.size());
        assertEquals(locale1, list1.get(0));
        assertEquals(new Locale("fr", "CA"), list1.get(1));
        assertEquals(new Locale("fr"), list1.get(2));
        assertEquals(Locale.getDefault(), list1.get(3));
        
        // Edge case: null locale
        List<Locale> list2 = LocaleUtils.localeLookupList(null);
        assertEquals(1, list2.size());
        assertEquals(Locale.getDefault(), list2.get(0));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertTrue(set.size() > 0);
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        // Typical use case: valid country code
        List<Locale> list1 = LocaleUtils.languagesByCountry("US");
        assertTrue(list1.contains(new Locale("en", "US")));
        
        // Edge case: null country code
        List<Locale> list2 = LocaleUtils.languagesByCountry(null);
        assertNotNull(list2);
        assertEquals(0, list2.size());
    }
    
    @Test
    public void testCountriesByLanguage() {
        // Typical use case: valid language code
        List<Locale> list1 = LocaleUtils.countriesByLanguage("en");
        assertTrue(list1.contains(new Locale("en", "US")));
        
        // Edge case: null language code
        List<Locale> list2 = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list2);
        assertEquals(0, list2.size());
    }
}