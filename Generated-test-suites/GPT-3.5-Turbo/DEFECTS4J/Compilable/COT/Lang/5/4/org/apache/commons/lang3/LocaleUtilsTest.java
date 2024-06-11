package org.apache.commons.lang3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_ValidLanguageCode_ReturnsLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en"), locale);
    }
    
    @Test
    public void testToLocale_ValidLanguageAndCountryCode_ReturnsLocale() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryAndVariantCode_ReturnsLocale() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLength_ThrowsException() {
        LocaleUtils.toLocale("e");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCode_ThrowsException() {
        LocaleUtils.toLocale("XX_GB");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCountryCode_ThrowsException() {
        LocaleUtils.toLocale("en_xxx");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidSeparator_ThrowsException() {
        LocaleUtils.toLocale("enGB");
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> locales = LocaleUtils.localeLookupList(null);
        assertTrue(locales.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsListOfLocales() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        assertEquals(3, locales.size());
        assertEquals(locale, locales.get(0));
        assertEquals(new Locale("fr", "CA"), locales.get(1));
        assertEquals(new Locale("fr"), locales.get(2));
    }
    
    @Test
    public void testLocaleLookupList_WithDefaultLocale_ReturnsListOfLocalesWithDefault() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.US;
        List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, locales.size());
        assertEquals(locale, locales.get(0));
        assertEquals(new Locale("fr", "CA"), locales.get(1));
        assertEquals(new Locale("fr"), locales.get(2));
        assertEquals(defaultLocale, locales.get(3));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        locales.add(Locale.CHINA); // should throw UnsupportedOperationException
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        locales.add(Locale.CHINA); // should throw UnsupportedOperationException
    }
    
    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfLanguages() {
        List<Locale> languages = LocaleUtils.languagesByCountry("GB");
        assertTrue(languages.contains(new Locale("en", "GB")));
        assertTrue(languages.contains(new Locale("cy", "GB")));
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfCountries() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertTrue(countries.contains(new Locale("fr", "CA")));
        assertTrue(countries.contains(new Locale("fr", "CH")));
    }
}