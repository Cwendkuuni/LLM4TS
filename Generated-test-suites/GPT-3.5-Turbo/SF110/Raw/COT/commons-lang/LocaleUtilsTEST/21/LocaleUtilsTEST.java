import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocaleNullString() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocaleEmptyString() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }
    
    @Test
    public void testToLocaleInvalidFormat() {
        try {
            LocaleUtils.toLocale("#");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: #", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleInvalidFormatLength() {
        try {
            LocaleUtils.toLocale("a");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: a", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleValidLanguage() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
    }
    
    @Test
    public void testToLocaleValidLanguageAndCountry() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
    }
    
    @Test
    public void testToLocaleValidLanguageAndCountryAndVariant() {
        assertEquals(new Locale("en", "US", "variant"), LocaleUtils.toLocale("en_US_variant"));
    }
    
    @Test
    public void testToLocaleValidLanguageAndCountryUpperCase() {
        assertEquals(new Locale("EN", "US"), LocaleUtils.toLocale("EN_US"));
    }
    
    @Test
    public void testToLocaleValidLanguageOnly() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en_"));
    }
    
    @Test
    public void testToLocaleInvalidFormatUnderscore() {
        try {
            LocaleUtils.toLocale("_en_US");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: _en_US", e.getMessage());
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(new Locale("en"));
        expectedList.add(new Locale("", "US"));
        expectedList.add(defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertEquals(1, LocaleUtils.availableLocaleList().size());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertEquals(1, LocaleUtils.availableLocaleSet().size());
    }
    
    @Test
    public void testIsAvailableLocaleTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }
    
    @Test
    public void testIsAvailableLocaleFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
    }
    
    @Test
    public void testLanguagesByCountryNull() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testLanguagesByCountry() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("US"));
    }
    
    @Test
    public void testCountriesByLanguageNull() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("en"));
    }
}