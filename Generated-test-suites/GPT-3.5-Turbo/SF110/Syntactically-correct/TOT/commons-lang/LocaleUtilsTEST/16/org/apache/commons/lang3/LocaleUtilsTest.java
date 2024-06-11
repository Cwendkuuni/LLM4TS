
package org.apache.commons.lang3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LocaleUtilsTest {

    // toLocale() method test cases
    
    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan2_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLowerCaseLanguageAndLengthIsNot2Or3_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("eng");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthIsNot2AndLanguageIsNotLowerCaseAndCountryIsNotUpperCase_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_us");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithCountryEmptyAndVariantNotEmpty_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en__variant");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan3AndLanguageCharacterIsNotUpperCase_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_us");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan5_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithCountryCharacterIsNotUpperCaseAfterLanguageCharacterIsUpperCase_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_uS_country");
    }
    
    @Test
    public void testToLocale_ValidFormatWithCountryEmpty_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("__");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithCountryEmpty_ReturnLocaleWithEmptyLanguageAndCountry2() {
        Locale locale = LocaleUtils.toLocale("_US");
        assertEquals("", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithCountryNotEmpty_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("_US_country");
        assertEquals("", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }
    
    // localeLookupList() method test cases
    
    @Test
    public void testLocaleLookupList_WithNullLocale_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLocaleLookupList_WithValidLocale_ReturnListWithLocale() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(1, result.size());
        assertTrue(result.contains(locale));
    }
    
    @Test
    public void testLocaleLookupList_WithValidLocaleWithVariant_ReturnListWithTwoLocales() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale, locale);
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale(locale.getLanguage(), locale.getCountry())));
    }
    
    @Test
    public void testLocaleLookupList_WithValidLocaleWithCountry_ReturnListWithTwoLocales() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale(locale.getLanguage(), ""));
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale(locale.getLanguage(), "")));
    }
    
    @Test
    public void testLocaleLookupList_WithValidLocaleWithoutDefaultLocale_ReturnListWithOneLocale() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale("es", "ES"));
        assertEquals(1, result.size());
        assertTrue(result.contains(locale));
    }
    
    // availableLocaleList() method test cases
    
    @Test
    public void testAvailableLocaleList_ReturnNonEmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.size() > 0);
    }
    
    // availableLocaleSet() method test cases
    
    @Test
    public void testAvailableLocaleSet_ReturnNonEmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertTrue(result.size() > 0);
    }
    
    // isAvailableLocale() method test cases
    
    @Test
    public void testIsAvailableLocale_WithNullLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }
    
    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }
    
    // languagesByCountry() method test cases
    
    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnListWithLocales() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testLanguagesByCountry_WithInvalidCountryCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("ZZ");
        assertEquals(0, result.size());
    }
    
    // countriesByLanguage() method test cases
    
    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnListWithLocales() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testCountriesByLanguage_WithInvalidLanguageCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("zz");
        assertEquals(0, result.size());
    }
}

