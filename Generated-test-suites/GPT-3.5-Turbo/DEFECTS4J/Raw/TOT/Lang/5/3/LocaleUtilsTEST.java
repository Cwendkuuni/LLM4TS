import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;


public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullString_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_GB_xxx_");
    }
    
    @Test
    public void testToLocale_ValidLanguageString_ReturnsLocaleWithLanguage() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryString_ReturnsLocaleWithLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("", locale.getVariant());
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryVariantString_ReturnsLocaleWithLanguageCountryAndVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> lookupList = LocaleUtils.localeLookupList(null);
        assertTrue(lookupList.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsCorrectLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));
        assertEquals(Locale.getDefault(), lookupList.get(3));
    }
    
    @Test
    public void testLocaleLookupList_ValidLocaleDefaultLocale_ReturnsCorrectLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));
        assertEquals(defaultLocale, lookupList.get(3));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        try {
            availableLocales.add(new Locale("es"));
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        try {
            availableLocales.add(new Locale("es"));
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es")));
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListWithAvailableLanguages() {
        List<Locale> languages = LocaleUtils.languagesByCountry("GB");
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(new Locale("en")));
        assertFalse(languages.contains(new Locale("es")));
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListWithAvailableCountries() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("GB")));
        assertFalse(countries.contains(new Locale("ES")));
    }
}