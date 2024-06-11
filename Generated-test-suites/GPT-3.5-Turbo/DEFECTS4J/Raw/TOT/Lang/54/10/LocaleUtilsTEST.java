import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_ValidLanguageCode_ReturnsLocaleObject() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test
    public void testToLocale_ValidLanguageAndCountryCode_ReturnsLocaleObject() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryAndVariantCode_ReturnsLocaleObject() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_GB_xx");
    }
    
    @Test
    public void testLocaleLookupList_SpecificLocale_ReturnsExpectedList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
    }
    
    @Test
    public void testLocaleLookupList_SpecificAndDefaultLocale_ReturnsExpectedList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLocaleLookupList_NullLocaleAndDefaultLocale_ReturnsEmptyList() {
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        try {
            result.add(Locale.JAPAN);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        try {
            result.add(Locale.JAPAN);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testIsAvailableLocale_KnownLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_UnknownLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfLanguages() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("es", "US")));
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("XX");
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfCountries() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("en", "GB")));
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("xx");
        assertEquals(0, result.size());
    }
}