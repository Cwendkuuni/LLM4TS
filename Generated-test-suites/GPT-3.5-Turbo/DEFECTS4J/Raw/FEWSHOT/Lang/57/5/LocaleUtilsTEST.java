import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertEquals(null, LocaleUtils.toLocale(null));
        
        try {
            LocaleUtils.toLocale("en_GB_xxxx");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_GB_xxxx", e.getMessage());
        }
        
        try {
            LocaleUtils.toLocale("en_GBxxx");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_GBxxx", e.getMessage());
        }
        
        try {
            LocaleUtils.toLocale("ENG");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: ENG", e.getMessage());
        }
        
        try {
            LocaleUtils.toLocale("ENG_GB");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: ENG_GB", e.getMessage());
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(new Locale("en"));
        
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
        
        locale = new Locale("en");
        defaultLocale = new Locale("fr");
        expected.clear();
        expected.add(new Locale("en"));
        expected.add(new Locale("fr"));
        
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
        
        locale = null;
        defaultLocale = new Locale("en");
        expected.clear();
        expected.add(new Locale("en"));
        
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "GB")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("en", "US"));
        
        assertEquals(expected, LocaleUtils.languagesByCountry("GB"));
        
        expected.clear();
        expected.add(new Locale("fr", "CA"));
        
        assertEquals(expected, LocaleUtils.languagesByCountry("CA"));
        
        expected.clear();
        
        assertEquals(expected, LocaleUtils.languagesByCountry("XX"));
        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "GB"));
        
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        
        expected.clear();
        expected.add(new Locale("fr", "CA"));
        
        assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));
        
        expected.clear();
        
        assertEquals(expected, LocaleUtils.countriesByLanguage("xx"));
        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }
}