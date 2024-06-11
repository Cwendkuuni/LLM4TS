import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullString_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals(new Locale("", ""), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US#");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_LessThanTwoCharacters_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }
    
    @Test
    public void testToLocale_ValidLanguageCode_ReturnsLocaleWithLanguage() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en"), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCode_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCode_ThreeCharacters_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("eng");
    }
    
    @Test
    public void testToLocale_ValidLanguageAndCountryCode_ReturnsLocaleWithLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals(new Locale("en", "US"), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountryCode_LanguageUpperCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("EN_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountryCode_LanguageLowerCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_us");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountryCode_LanguageUpperCaseAndCountryLowerCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("EN_us");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountryCode_LanguageLowerCaseAndCountryUpperCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountryCode_ThreeCharacterLanguageCode_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("z_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountryCode_EmptyCountryCode_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountryCode_EmptyCountryCodeNotAtLast_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en__US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountryCode_EmptyCountryCodeLowerCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_us_");
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryAndVariantCode_ReturnsLocaleWithLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("en_US_var");
        assertEquals(new Locale("en", "US", "var"), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCountryAndVariantCode_EmptyCountryCode_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en__var");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCountryAndVariantCode_EmptyCountryCodeNotAtLast_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en__US_var");
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsListWithLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(1, result.size());
        assertEquals(locale, result.get(0));
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithVariant_ReturnsListWithLocaleAndVariantStripped() {
        Locale locale = new Locale("en", "US", "var");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(2, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("en", "US"), result.get(1));
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithCountry_ReturnsListWithLocaleAndLanguageOnly() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, locale);
        assertEquals(2, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("en"), result.get(1));
    }
    
    @Test
    public void testLocaleLookupList_DefaultLocaleNotInList_AddsDefaultLocaleToList() {
        Locale locale = new Locale("en");
        Locale defaultLocale = new Locale("de");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(2, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(defaultLocale, result.get(1));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsNonNullList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsNonNullSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
    }
    
    @Test
    public void testIsAvailableLocale_ExistingLocale_ReturnsTrue() {
        Locale locale = Locale.US;
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocale_NonExistingLocale_ReturnsFalse() {
        Locale locale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfLanguages() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("XX");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfCountries() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("xx");
        assertTrue(result.isEmpty());
    }
}