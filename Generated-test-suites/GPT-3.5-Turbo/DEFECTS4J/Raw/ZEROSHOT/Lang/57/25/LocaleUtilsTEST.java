import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(locale.getLanguage(), "en");
        assertEquals(locale.getCountry(), "");
        
        locale = LocaleUtils.toLocale("en_GB");
        assertEquals(locale.getLanguage(), "en");
        assertEquals(locale.getCountry(), "GB");
        
        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(locale.getLanguage(), "en");
        assertEquals(locale.getCountry(), "GB");
        assertEquals(locale.getVariant(), "xxx");
        
        locale = LocaleUtils.toLocale(null);
        assertNull(locale);
        
        try {
            locale = LocaleUtils.toLocale("eN");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            locale = LocaleUtils.toLocale("en_gb");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(list.size(), 3);
        assertEquals(list.get(0), locale);
        assertEquals(list.get(1), new Locale("fr", "CA"));
        assertEquals(list.get(2), new Locale("fr"));
        
        Locale defaultLocale = new Locale("en");
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(list.size(), 4);
        assertEquals(list.get(0), locale);
        assertEquals(list.get(1), new Locale("fr", "CA"));
        assertEquals(list.get(2), new Locale("fr"));
        assertEquals(list.get(3), defaultLocale);
        
        list = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), defaultLocale);
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
        Locale locale = LocaleUtils.toLocale("en");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        
        locale = LocaleUtils.toLocale("fr_FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        
        locale = new Locale("xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        assertNotNull(list);
        assertTrue(list.size() > 0);
        
        list = LocaleUtils.languagesByCountry(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(list);
        assertTrue(list.size() > 0);
        
        list = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}