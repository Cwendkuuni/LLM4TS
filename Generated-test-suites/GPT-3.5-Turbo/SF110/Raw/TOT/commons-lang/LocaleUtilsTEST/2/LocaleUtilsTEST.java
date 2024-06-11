import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    // Test cases for toLocale() method
    @Test
    public void testToLocale_NullString_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals(new Locale("", ""), locale);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("invalid#format");
    }
    
    @Test
    public void testToLocale_TwoLetterLanguageCode_ReturnsLocaleWithTwoLetterLanguageCode() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en"), locale);
    }
    
    @Test
    public void testToLocale_ThreeLetterLanguageCode_ReturnsLocaleWithThreeLetterLanguageCode() {
        Locale locale = LocaleUtils.toLocale("eng");
        assertEquals(new Locale("eng"), locale);
    }

    @Test
    public void testToLocale_TwoLetterLanguageCodeAndTwoLetterCountryCode_ReturnsLocaleWithTwoLetterLanguageCodeAndTwoLetterCountryCode() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals(new Locale("en", "US"), locale);
    }
    
    @Test
    public void testToLocale_ThreeLetterLanguageCodeAndTwoLetterCountryCode_ReturnsLocaleWithThreeLetterLanguageCodeAndTwoLetterCountryCode() {
        Locale locale = LocaleUtils.toLocale("eng_US");
        assertEquals(new Locale("eng", "US"), locale);
    }
    
    @Test
    public void testToLocale_ThreeLetterLanguageCodeAndEmptyStringCountryCode_ReturnsLocaleWithThreeLetterLanguageCodeAndEmptyCountryCode() {
        Locale locale = LocaleUtils.toLocale("eng_");
        assertEquals(new Locale("eng", ""), locale);
    }
    
    @Test
    public void testToLocale_ThreeLetterLanguageCodeAndVariant_ReturnsLocaleWithThreeLetterLanguageCodeAndVariant() {
        Locale locale = LocaleUtils.toLocale("eng_variant");
        assertEquals(new Locale("eng", "", "variant"), locale);
    }
    
    @Test
    public void testToLocale_ThreeLetterLanguageCodeAndTwoLetterCountryCodeAndVariant_ReturnsLocaleWithThreeLetterLanguageCodeAndTwoLetterCountryCodeAndVariant() {
        Locale locale = LocaleUtils.toLocale("eng_US_variant");
        assertEquals(new Locale("eng", "US", "variant"), locale);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatStartingWithUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_US");
    }
    
    // Test cases for localeLookupList() method
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithVariant_ReturnsListWithVariantLanguageAndLanguage() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("en", "US"), list.get(1));
        assertEquals(new Locale("en", ""), list.get(2));
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithCountry_ReturnsListWithCountryLanguageAndLanguage() {
        Locale locale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("en", ""), list.get(1));
        assertEquals(new Locale("", ""), list.get(2));
    }
    
    @Test
    public void testLocaleLookupList_DefaultLocaleNotInList_ReturnsListWithDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("es", "ES");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertTrue(list.contains(defaultLocale));
    }
    
    // Test case for availableLocaleList() method
    @Test
    public void testAvailableLocaleList_ReturnsListWithAvailableLocales() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertFalse(list.isEmpty());
    }
    
    // Test case for availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet_ReturnsSetWithAvailableLocales() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertFalse(set.isEmpty());
    }
    
    // Test case for isAvailableLocale() method
    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        Locale locale = Locale.getDefault();
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xx", "YY");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    // Test case for languagesByCountry() method
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListWithMatchingLocales() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertFalse(list.isEmpty());
    }
    
    // Test case for countriesByLanguage() method
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListWithMatchingLocales() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertFalse(list.isEmpty());
    }
    
}