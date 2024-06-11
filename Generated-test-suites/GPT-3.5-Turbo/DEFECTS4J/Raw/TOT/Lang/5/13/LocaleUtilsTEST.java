import static org.junit.Assert.*;
import java.util.List;
import java.util.Locale;
import org.junit.BeforeClass;
import org.junit.Test;

public class LocaleUtilsTest {
    
    private static LocaleUtils localeUtils;
    
    @BeforeClass
    public static void setUp() {
        localeUtils = new LocaleUtils();
    }
    
    // Test cases for toLocale() method
    
    @Test
    public void testToLocaleValidLanguageCode_ReturnsLocale() {
        Locale locale = localeUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
    }
    
    @Test
    public void testToLocaleValidLanguageCodeAndCountryCode_ReturnsLocale() {
        Locale locale = localeUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }
    
    @Test
    public void testToLocaleValidLanguageCodeAndCountryCodeAndVariant_ReturnsLocale() {
        Locale locale = localeUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat_ThrowsIllegalArgumentException() {
        localeUtils.toLocale("invalid");
    }
    
    // Test cases for localeLookupList() method
    
    @Test
    public void testLocaleLookupListWithLocaleAndDefaultLocale_ReturnsList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = localeUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(defaultLocale, list.get(3));
    }
    
    @Test
    public void testLocaleLookupListWithNullLocale_ReturnsEmptyList() {
        List<Locale> list = localeUtils.localeLookupList(null);
        assertTrue(list.isEmpty());
    }
    
    // Test cases for availableLocaleList() method
    
    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List<Locale> list = localeUtils.availableLocaleList();
        try {
            list.add(new Locale("es"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
    
    // Test cases for availableLocaleSet() method
    
    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set<Locale> set = localeUtils.availableLocaleSet();
        try {
            set.add(new Locale("es"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
    
    // Test cases for isAvailableLocale() method
    
    @Test
    public void testIsAvailableLocaleWithAvailableLocale_ReturnsTrue() {
        Locale locale = new Locale("es", "ES");
        assertTrue(localeUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocaleWithUnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("fr", "FR");
        assertFalse(localeUtils.isAvailableLocale(locale));
    }
    
    // Test cases for languagesByCountry() method
    
    @Test
    public void testLanguagesByCountryWithValidCountryCode_ReturnsList() {
        List<Locale> languages = localeUtils.languagesByCountry("GB");
        assertEquals(10, languages.size());
        assertEquals(new Locale("ar"), languages.get(0));
        assertEquals(new Locale("en"), languages.get(1));
        // ...
        assertEquals(new Locale("zh"), languages.get(9));
    }
    
    @Test
    public void testLanguagesByCountryWithNullCountryCode_ReturnsEmptyList() {
        List<Locale> languages = localeUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }
    
    // Test cases for countriesByLanguage() method
    
    @Test
    public void testCountriesByLanguageWithValidLanguageCode_ReturnsList() {
        List<Locale> countries = localeUtils.countriesByLanguage("en");
        assertEquals(135, countries.size());
        assertEquals(new Locale("AR", "en"), countries.get(0));
        assertEquals(new Locale("AU", "en"), countries.get(1));
        // ...
        assertEquals(new Locale("ZW", "en"), countries.get(134));
    }
    
    @Test
    public void testCountriesByLanguageWithNullLanguageCode_ReturnsEmptyList() {
        List<Locale> countries = localeUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
}