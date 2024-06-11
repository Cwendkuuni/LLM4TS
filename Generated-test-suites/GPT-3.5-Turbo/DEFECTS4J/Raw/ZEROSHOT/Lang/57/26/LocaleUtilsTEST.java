import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_InvalidFormat() {
        try {
            LocaleUtils.toLocale("en_USA");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_USA", e.getMessage());
        }
    }
    
    @Test
    public void testToLocale_LanguageOnly() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale);
    }
    
    @Test
    public void testToLocale_LanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);
    }
    
    @Test
    public void testToLocale_LanguageCountryAndVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
    }
    
    @Test
    public void testLocaleLookupList_StartFromNullLocale() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_StartFromLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
    }
    
    @Test
    public void testLocaleLookupList_StartFromLocaleWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(defaultLocale, list.get(3));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertFalse(set.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale_True() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA));
    }
    
    @Test
    public void testIsAvailableLocale_False() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xxx")));
    }
    
    @Test
    public void testLanguagesByCountry_NullInput() {
        List<Locale> langs = LocaleUtils.languagesByCountry(null);
        assertTrue(langs.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> langs = LocaleUtils.languagesByCountry("GB");
        assertFalse(langs.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode() {
        List<Locale> langs = LocaleUtils.languagesByCountry("XXX");
        assertTrue(langs.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_NullInput() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertFalse(countries.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("XXX");
        assertTrue(countries.isEmpty());
    }
}