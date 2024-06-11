
import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }
    
    @Test
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("en_GB_xxxx");
        });
    }
    
    @Test
    public void testToLocale_ValidLanguage_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), result);
    }
    
    @Test
    public void testToLocale_ValidLanguageAndCountry_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), result);
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryAndVariant_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), result);
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsListOfAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsSetOfAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        boolean result = LocaleUtils.isAvailableLocale(Locale.US);
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        boolean result = LocaleUtils.isAvailableLocale(new Locale("xx", "XX"));
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("XX");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsList() {
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        assertTrue(result.contains(new Locale("en", "GB")));
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("XX");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertTrue(result.contains(new Locale("en", "GB")));
    }
}
