import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validFormat() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale);
        
        locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);
        
        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
        
        locale = LocaleUtils.toLocale("fr_FR");
        assertEquals(new Locale("fr", "FR"), locale);
        
        locale = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(new Locale("fr", "CA", "xxx"), locale);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));
        
        Locale defaultLocale = new Locale("en");
        lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));
        assertEquals(defaultLocale, lookupList.get(3));
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
    public void testIsAvailableLocale_true() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }
    
    @Test
    public void testIsAvailableLocale_false() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertNotNull(languages);
        assertTrue(languages.size() > 0);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertTrue(countries.size() > 0);
    }
}