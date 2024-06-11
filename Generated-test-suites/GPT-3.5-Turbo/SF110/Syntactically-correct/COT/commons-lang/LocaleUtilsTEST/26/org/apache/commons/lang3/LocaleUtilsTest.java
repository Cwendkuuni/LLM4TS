import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {



    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThanTwoCharacters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_StartsWithUnderscoreButNotFollowedByTwoCharacters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_StartsWithUnderscoreButFollowedByNonUpperCaseCharacters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_ab");
    }
    
    @Test
    public void testToLocale_ValidFormatWithTwoLowerCaseCharacters_ReturnLocaleWithLanguage() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ValidFormatWithTwoLowerCaseCharacters_ThrowException() {
        Locale result = LocaleUtils.toLocale("abc");
    }
    
    @Test
    public void testToLocale_ValidFormatWithTwoLowerCaseCharactersAndTwoUpperCaseCharacters_ReturnLocaleWithLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ValidFormatWithTwoLowerCaseCharactersAndTwoUpperCaseCharacters_ThrowException() {
        Locale result = LocaleUtils.toLocale("en_USa");
    }
    
    @Test
    public void testToLocale_ValidFormatWithTwoLowerCaseCharactersAndUpperCaseCountry_ReturnLocaleWithLanguageAndEmptyCountry() {
        Locale result = LocaleUtils.toLocale("en_US_");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithTwoLowerCaseCharactersAndUpperCaseCountryAndNonEmptyVariant_ReturnLocaleWithLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("en_US_var");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("var", result.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ValidFormatWithUnderscoreButNotEnoughCharacters_AfterUnderscore_ThrowException() {
        Locale result = LocaleUtils.toLocale("_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ValidFormatWithUnderscoreButNotFollowedByTwoUpperCaseCharacters_ThrowException() {
        LocaleUtils.toLocale("_Us");
    }
    
    @Test
    public void testLocaleLookupList_LocaleIsNull_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_LocaleIsNotNullAndVariantIsEmpty_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(defaultLocale));
    }
    
    @Test
    public void testLocaleLookupList_LocaleIsNotNullAndVariantIsNotEmpty_ReturnListWithLocaleAndLocaleWithoutVariant() {
        Locale locale = new Locale("en", "US", "var");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "US")));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnListWithAllAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnSetWithAllAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testIsAvailableLocale_LocaleIsAvailable_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_LocaleIsNotAvailable_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }
    
    @Test
    public void testLanguagesByCountry_CountryCodeIsNull_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_CountryCodeDoesNotExist_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("xx");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_CountryCodeExists_ReturnListWithLocalesMatchingCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        for (Locale locale : result) {
            assertEquals("US", locale.getCountry());
        }
    }
    
    @Test
    public void testCountriesByLanguage_LanguageCodeIsNull_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_LanguageCodeDoesNotExist_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("xx");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_LanguageCodeExists_ReturnListWithLocalesMatchingLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        for (Locale locale : result) {
            assertEquals("en", locale.getLanguage());
        }
    }



}