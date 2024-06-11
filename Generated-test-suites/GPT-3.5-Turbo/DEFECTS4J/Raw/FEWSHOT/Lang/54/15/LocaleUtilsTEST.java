import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        //Invalid format
        try {
            LocaleUtils.toLocale("en_gb_xxx");
            fail("IllegalArgumentException was not thrown for invalid format");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        List expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            defaultLocale
        );
        
        List result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List expected = Arrays.asList(
            Locale.getAvailableLocales()
        );
        
        List result = LocaleUtils.availableLocaleList();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set expected = new HashSet(LocaleUtils.availableLocaleList());
        
        Set result = LocaleUtils.availableLocaleSet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List expected = Arrays.asList(
            new Locale("en", "US"),
            new Locale("es", "US")
        );
        
        List result = LocaleUtils.languagesByCountry("US");
        
        assertEquals(expected, result);
        
        //Invalid country code
        List result = LocaleUtils.languagesByCountry("ZZ");
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List expected = Arrays.asList(
            new Locale("en", "US"),
            new Locale("en", "GB")
        );
        
        List result = LocaleUtils.countriesByLanguage("en");
        
        assertEquals(expected, result);
        
        //Invalid language code
        List result = LocaleUtils.countriesByLanguage("ZZ");
        
        assertTrue(result.isEmpty());
    }
}