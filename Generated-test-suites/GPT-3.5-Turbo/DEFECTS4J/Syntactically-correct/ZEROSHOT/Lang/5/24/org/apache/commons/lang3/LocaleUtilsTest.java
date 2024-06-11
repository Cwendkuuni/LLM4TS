package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_nullInput_returnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        Assert.assertNull(result);
    }
    
    @Test
    public void testToLocale_invalidFormat_throwsIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("invalid");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testToLocale_validFormat_returnsLocaleObject() {
        Locale result = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en", ""), result);
    }
    
    @Test
    public void testLocaleLookupList_localeNull_returnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testLocaleLookupList_localeNotEmpty_returnsList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLocaleLookupList_localeAndDefaultLocaleNull_returnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null, null);
        Assert.assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testLocaleLookupList_localeAndDefaultLocaleNotEmpty_returnsList() {
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testAvailableLocaleList_returnsExpectedList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testAvailableLocaleSet_returnsExpectedSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testIsAvailableLocale_knownLocale_returnsTrue() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_unknownLocale_returnsFalse() {
        Locale locale = new Locale("xx", "XX");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_countryCodeNull_returnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testLanguagesByCountry_countryCodeValid_returnsListOfLanguages() {
        String countryCode = "CA";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testCountriesByLanguage_languageCodeNull_returnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testCountriesByLanguage_languageCodeValid_returnsListOfCountries() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "GB"),
                new Locale("en", "CA"),
                new Locale("en", "AU")
        );
        Assert.assertEquals(expected, result);
    }
}