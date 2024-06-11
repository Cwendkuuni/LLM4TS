import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyStrings() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithShortString_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }
    
    @Test
    public void testToLocale_ValidLanguageCode_ReturnsLocaleWithLanguageCode() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidLanguageAndCountryCode_ReturnsLocaleWithLanguageAndCountryCode() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryAndVariantCode_ReturnsLocaleWithLanguageCountryAndVariantCode() {
        Locale result = LocaleUtils.toLocale("en_US_variant");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("variant", result.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithUnderscoreBeforeUpperCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_us");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithSingleUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThan3CharactersAfterUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_u");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithNoUnderscoreAfter3Characters_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_usa");
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsListWithGivenLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(1, result.size());
        assertEquals(locale, result.get(0));
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithVariant_ReturnsListWithLocaleAndLocaleWithoutVariant() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "US")));
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithCountry_ReturnsListWithLocaleAndLocaleWithoutCountry() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "")));
    }
    
    @Test
    public void testLocaleLookupList_DefaultLocaleNotInList_AddsDefaultLocaleToList() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("es", "ES");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(3, result.size());
        assertTrue(result.contains(defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsListWithAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        for (Locale locale : Locale.getAvailableLocales()) {
            assertTrue(result.contains(locale));
        }
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsSetWithAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        for (Locale locale : Locale.getAvailableLocales()) {
            assertTrue(result.contains(locale));
        }
    }
    
    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        boolean result = LocaleUtils.isAvailableLocale(Locale.ENGLISH);
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("zz", "ZZ");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListWithMatchingLocales() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        for (Locale locale : result) {
            assertEquals("US", locale.getCountry());
            assertTrue(locale.getVariant().isEmpty());
        }
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListWithMatchingLocales() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        for (Locale locale : result) {
            assertEquals("en", locale.getLanguage());
            assertFalse(locale.getCountry().isEmpty());
            assertTrue(locale.getVariant().isEmpty());
        }
    }
}