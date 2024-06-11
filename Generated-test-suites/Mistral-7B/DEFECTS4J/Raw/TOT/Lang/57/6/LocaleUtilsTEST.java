package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "XX");
        actual = LocaleUtils.toLocale("fr_CA_XX");
        assertEquals(expected, actual);

        try {
            String invalidLocale = "ab_Cd_xx";
            Locale actualInvalid = LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException with invalid locale string: " + invalidLocale);
        } catch (IllegalArgumentException e) {
            // pass
        }

        try {
            String nullLocale = null;
            Locale actualNull = LocaleUtils.toLocale(nullLocale);
            fail("Expected null for null locale string");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale enUS = new Locale("en", "US");
        Locale frCA = new Locale("fr", "CA");
        Locale en = new Locale("en");
        Locale defaultLocale = new Locale("es");

        List<Locale> lookupList = LocaleUtils.localeLookupList(frCA, en);

        List<Locale> expected = Arrays.asList(frCA, en, en, defaultLocale);
        assertEquals(expected, lookupList);

        lookupList = LocaleUtils.localeLookupList(enUS);
        expected = Arrays.asList(enUS, en, defaultLocale);
        assertEquals(expected, lookupList);

        lookupList = LocaleUtils.localeLookupList(en);
        expected = Arrays.asList(en, defaultLocale);
        assertEquals(expected, lookupList);

        lookupList = LocaleUtils.localeLookupList(defaultLocale);
        assertEquals(Arrays.asList(defaultLocale), lookupList);

        lookupList = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), lookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, availableLocales);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale enUS = new Locale("en", "US");
        Locale frCA = new Locale("fr", "CA");
        Locale en = new Locale("en");
        Locale invalidLocale = new Locale("ab", "Cd");

        assertTrue(LocaleUtils.isAvailableLocale(enUS));
        assertTrue(LocaleUtils.isAvailableLocale(frCA));
        assertTrue(LocaleUtils.isAvailableLocale(en));
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedFR = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CA"));
        List<Locale> languagesByCountryFR = LocaleUtils.languagesByCountry("FR");
        assertEquals(expectedFR, languagesByCountryFR);

        List<Locale> emptyList = Collections.emptyList();
        List<Locale> languagesByCountryNull = LocaleUtils.languagesByCountry(null);
        assertEquals(emptyList, languagesByCountryNull);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedEN = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "AU"));
        List<Locale> countriesByLanguageEN = LocaleUtils.countriesByLanguage("EN");
        assertEquals(expectedEN, countriesByLanguageEN);

        List<Locale> emptyList = Collections.emptyList();
        List<Locale> countriesByLanguageNull = LocaleUtils.countriesByLanguage(null);
        assertEquals(emptyList, countriesByLanguageNull);
    }
}