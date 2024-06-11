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
    public void testToLocaleNullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleInvalidFormat() {
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("aa"));
        assertNull(LocaleUtils.toLocale("aa_"));
        assertNull(LocaleUtils.toLocale("aa_CC"));
        assertNull(LocaleUtils.toLocale("aa_CC_xx"));
        assertNull(LocaleUtils.toLocale("aa_CC_xx_xx"));
        assertNull(LocaleUtils.toLocale("aa-CC"));
        assertNull(LocaleUtils.toLocale("aa-CC-xx"));
        assertNull(LocaleUtils.toLocale("aa-CC-xx-xx"));
        assertNull(LocaleUtils.toLocale("aa-CC--"));

        String[] invalidLocales = { "aa_", "aa_CC_", "aa_CC_xx_", "aa-CC-", "aa-CC-xx-", "aa-CC--" };
        for (String locale : invalidLocales) {
            try {
                LocaleUtils.toLocale(locale);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    @Test
    public void testToLocaleValidFormat() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE_XX"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE_XX_XX"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE--"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE-XX"));

        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_XX"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB---"));

        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_XX"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR--"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR-XX"));

        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XX"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA--"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA-XX"));

        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XX_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XX_CA_XX"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XX_CA--"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XX_CA-XX"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "XX");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleLookupList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expectedLocaleLookupList, localeLookupList);

        locale = new Locale("en");
        localeLookupList = LocaleUtils.localeLookupList(locale);
        expectedLocaleLookupList = Arrays.asList(locale, new Locale("en"));
        assertEquals(expectedLocaleLookupList, localeLookupList);

        locale = null;
        localeLookupList = LocaleUtils.localeLookupList(locale);
        expectedLocaleLookupList = Collections.emptyList();
        assertEquals(expectedLocaleLookupList, localeLookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.ROOT, Locale.CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.JAPANESE, Locale.KOREAN, Locale.SPANISH);
        assertEquals(expectedAvailableLocaleList, availableLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedAvailableLocaleSet = new HashSet<>(Arrays.asList(Locale.ROOT, Locale.CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.JAPANESE, Locale.KOREAN, Locale.SPANISH));
        assertEquals(expectedAvailableLocaleSet, availableLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "XX");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("non-existent-locale");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFrance = LocaleUtils.languagesByCountry("FR");
        List<Locale> expectedLanguagesByCountryFrance = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CA"), new Locale("fr", "CH"), new Locale("fr", "LU"));
        assertEquals(expectedLanguagesByCountryFrance, languagesByCountryFrance);

        List<Locale> languagesByCountryUnitedStates = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedLanguagesByCountryUnitedStates = Arrays.asList(new Locale("en", "US"));
        assertEquals(expectedLanguagesByCountryUnitedStates, languagesByCountryUnitedStates);

        List<Locale> languagesByCountryNull = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), languagesByCountryNull);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguageEnglish = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguageEnglish = Arrays.asList(new Locale("en", "GB"), new Locale("en", "IN"), new Locale("en", "US"));
        assertEquals(expectedCountriesByLanguageEnglish, countriesByLanguageEnglish);

        List<Locale> countriesByLanguageFrench = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expectedCountriesByLanguageFrench = Arrays.asList(new Locale("fr", "BE"), new Locale("fr", "CA"), new Locale("fr", "CH"), new Locale("fr", "FR"), new Locale("fr", "LU"));
        assertEquals(expectedCountriesByLanguageFrench, countriesByLanguageFrench);

        List<Locale> countriesByLanguageNull = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), countriesByLanguageNull);
    }

}