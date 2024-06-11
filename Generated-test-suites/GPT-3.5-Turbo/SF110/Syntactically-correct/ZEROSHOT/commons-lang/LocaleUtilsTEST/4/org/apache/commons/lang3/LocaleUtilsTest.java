import org.junit.*;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import java.util.*;
import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullString_ReturnNull() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        assertNull(result);
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageAndCountry() {
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        String str = "en#US";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithTwoCharacters_ThrowIllegalArgumentException() {
        String str = "e";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLanguageOnly_ThrowIllegalArgumentException() {
        String str = "english";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLanguageAndCountryUpperCase_ThrowIllegalArgumentException() {
        String str = "EN_US";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLanguageCountryAndVariantLowerCase_ThrowIllegalArgumentException() {
        String str = "en_us_variant";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithUnderscoreAtSecondIndex_ThrowIllegalArgumentException() {
        String str = "a_BB";
        LocaleUtils.toLocale(str);
    }
    
    @Test
    public void testToLocale_ValidFormatWithLanguageAndCountry_ReturnLocaleWithLanguageAndCountry() {
        String str = "en_US";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithLanguageCountryAndVariant_ReturnLocaleWithLanguageCountryAndVariant() {
        String str = "en_US_variant";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("variant", result.getVariant());
    }
    
    @Test
    public void testToLocale_ValidFormatWithUnderscoreNullCountry_ReturnLocaleWithLanguageAndEmptyCountry() {
        String str = "_US";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("", result.getLanguage());
        assertEquals("US", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithUnderscoreAndNullCountry_ReturnLocaleWithLanguageAndCountry() {
        String str = "_";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US", "variant");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(locale, result.get(0));
        assertEquals(defaultLocale, result.get(1));
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnListWithDefaultLocale() {
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(defaultLocale, result.get(0));
    }
    
    @Test
    public void testLocaleLookupList_NullLocaleAndDefaultLocale_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null, null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleList_ReturnListWithAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnSetWithAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
    }
    
    @Test
    public void testIsAvailableLocale_ValidAvailableLocale_ReturnTrue() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_MissingAvailableLocale_ReturnFalse() {
        Locale locale = new Locale("xx", "YY");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListOfLocalesWithMatchingCountryCode() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(result);
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListOfLocalesWithMatchingLanguageCode() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(result);
    }
}