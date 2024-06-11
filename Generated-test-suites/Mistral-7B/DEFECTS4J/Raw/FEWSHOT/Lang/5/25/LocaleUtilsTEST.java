package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_LanguageCodeMissing() {
        assertNull(LocaleUtils.toLocale("_"));
    }

    @Test
    public void testToLocale_InvalidFormat_LanguageCodeUppercase() {
        assertNull(LocaleUtils.toLocale("EN"));
    }

    @Test
    public void testToLocale_InvalidFormat_CountryCodeMissing() {
        assertNull(LocaleUtils.toLocale("en_"));
    }

    @Test
    public void testToLocale_InvalidFormat_CountryCodeLowercase() {
        assertNull(LocaleUtils.toLocale("en_gb"));
    }

    @Test
    public void testToLocale_InvalidFormat_SeparatorMissing() {
        assertNull(LocaleUtils.toLocale("enGb"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLength() {
        assertNull(LocaleUtils.toLocale("en_GB_xyz"));
    }

    @Test
    public void testToLocale_ValidFormat() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertNull(locale.getVariant());
    }

    @Test
    public void testToLocale_ValidFormat_WithVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_variant");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("variant", locale.getVariant());
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testLocaleLookupList_SimpleLocale() {
        Locale locale = new Locale("en", "GB");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("en"), new Locale("en_GB"));
        assertEquals(expectedLocaleList, localeList);
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        Locale locale = new Locale("en", "GB", "variant");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("en", "GB"), new Locale("en"), new Locale("en_GB"));
        assertEquals(expectedLocaleList, localeList);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expectedLanguages = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("es", "ES"), new Locale("it", "IT"));
        assertEquals(expectedLanguages, languages);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expectedCountries = Arrays.asList(new Locale("FR", "CA"), new Locale("FR", "BE"), new Locale("FR", "CH"), new Locale("FR", "FR"));
        assertEquals(expectedCountries, countries);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale availableLocale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(availableLocale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale invalidLocale = new Locale("en", "US", "xyz");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }
}