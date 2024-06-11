import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocaleNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocaleEmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }
    
    @Test
    public void testToLocaleInvalidFormat() {
        try {
            LocaleUtils.toLocale("en_GB_xxx_");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_GB_xxx_", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleLanguageOnly() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test
    public void testToLocaleLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }
    
    @Test
    public void testToLocaleLanguageCountryAndVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }
    
    @Test
    public void testLocaleLookupListWithLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
    }
    
    @Test
    public void testLocaleLookupListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertEquals(4, result.size());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertEquals(4, result.size());
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountryWithNull() {
        List result = LocaleUtils.languagesByCountry(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountryWithCountryCode() {
        List result = LocaleUtils.languagesByCountry("CA");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new Locale("fr", "CA", ""), result.get(0));
    }
    
    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        List result = LocaleUtils.languagesByCountry("XXX");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguageWithNull() {
        List result = LocaleUtils.countriesByLanguage(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguageWithLanguageCode() {
        List result = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new Locale("fr", "CA", ""), result.get(0));
    }
    
    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        List result = LocaleUtils.countriesByLanguage("XXX");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
}