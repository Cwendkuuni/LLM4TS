package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());
        assertEquals("en_GB_xxx", LocaleUtils.toLocale("en_GB_xxx").toString());
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(""));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(" "));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB__"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_XX"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xxx_yyy"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xxx_yyy_zzz"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = Locale.UK;
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        List<Locale> localeListDefault = LocaleUtils.localeLookupList(locale, Locale.US);
        assertEquals(locale, localeListDefault.get(0));
        assertEquals(Locale.US, localeListDefault.get(localeListDefault.size() - 1));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.size() > 0);
        for (Locale locale : localeList) {
            assertFalse(locale.equals(null));
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertTrue(localeSet.size() > 0);
        for (Locale locale : localeSet) {
            assertFalse(locale.equals(null));
        }
        for (Locale locale : LocaleUtils.availableLocaleList()) {
            assertTrue(localeSet.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = Locale.UK;
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        Locale localeInvalid = new Locale("invalid", "invalid");
        assertFalse(LocaleUtils.isAvailableLocale(localeInvalid));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFrance = LocaleUtils.languagesByCountry("FR");
        assertEquals("fr", languagesByCountryFrance.get(0).getLanguage());
        List<Locale> languagesByCountryUSA = LocaleUtils.languagesByCountry("US");
        assertEquals("en", languagesByCountryUSA.get(0).getLanguage());
        List<Locale> languagesByCountryEmpty = LocaleUtils.languagesByCountry(null);
        assertEquals(0, languagesByCountryEmpty.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguageEnglish = LocaleUtils.countriesByLanguage("en");
        assertEquals("US", countriesByLanguageEnglish.get(0).getCountry());
        List<Locale> countriesByLanguageFrench = LocaleUtils.countriesByLanguage("fr");
        assertEquals("FR", countriesByLanguageFrench.get(0).getCountry());
        List<Locale> countriesByLanguageEmpty = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countriesByLanguageEmpty.size());
    }
}