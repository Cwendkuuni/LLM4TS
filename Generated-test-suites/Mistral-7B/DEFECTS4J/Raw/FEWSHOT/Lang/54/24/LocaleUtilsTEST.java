package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale(null));

        try {
            LocaleUtils.toLocale("xx_YY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(4, localeList.size());
        assertTrue(localeList.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(localeList.contains(new Locale("fr", "CA")));
        assertTrue(localeList.contains(new Locale("fr", "")));
        assertTrue(localeList.contains(new Locale("en")));

        localeList = LocaleUtils.localeLookupList(new Locale("en"));
        assertEquals(3, localeList.size());
        assertTrue(localeList.contains(new Locale("en")));
        assertTrue(localeList.contains(new Locale("en", "")));
        assertTrue(localeList.contains(new Locale("fr")));

        localeList = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(1, localeList.size());
        assertTrue(localeList.contains(new Locale("en")));

        localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA"), new Locale("en"));
        assertEquals(5, localeList.size());
        assertTrue(localeList.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(localeList.contains(new Locale("fr", "CA")));
        assertTrue(localeList.contains(new Locale("fr", "")));
        assertTrue(localeList.contains(new Locale("en")));
        assertTrue(localeList.contains(new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, availableLocaleList.size());
        assertTrue(availableLocaleList.containsAll(Locale.getAvailableLocales()));

        List<Locale> localeList = new ArrayList<>();
        localeList.addAll(availableLocaleList);
        Collections.shuffle(localeList);

        List<Locale> localeListCopy = new ArrayList<>(availableLocaleList);
        Collections.shuffle(localeListCopy);

        for (int i = 0; i < 10; i++) {
            assertEquals(localeList.get(i), localeListCopy.get(i));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFR = LocaleUtils.languagesByCountry("FR");
        assertEquals(3, languagesByCountryFR.size());
        assertEquals(new Locale("fr", "CA"), languagesByCountryFR.get(0));
        assertEquals(new Locale("fr", "FR"), languagesByCountryFR.get(1));
        assertEquals(new Locale("fr", "BE"), languagesByCountryFR.get(2));

        List<Locale> languagesByCountryEN = LocaleUtils.languagesByCountry("EN");
        assertEquals(2, languagesByCountryEN.size());
        assertEquals(new Locale("en", "GB"), languagesByCountryEN.get(0));
        assertEquals(new Locale("en", "US"), languagesByCountryEN.get(1));

        List<Locale> languagesByCountryNull = LocaleUtils.languagesByCountry(null);
        assertEquals(0, languagesByCountryNull.size());

        List<Locale> localeList = new ArrayList<>();
        localeList.addAll(LocaleUtils.availableLocaleList());
        LocaleUtils.cLanguagesByCountry.clear();
        List<Locale> languagesByCountryEmpty = LocaleUtils.languagesByCountry("XX");
        assertEquals(0, languagesByCountryEmpty.size());

        // Test for performance
        for (int i = 0; i < 10000; i++) {
            List<Locale> languagesByCountryFRTest = LocaleUtils.languagesByCountry("FR");
            List<Locale> languagesByCountryFRCopy = new ArrayList<>(languagesByCountryFR);
            assertEquals(languagesByCountryFR.get(0), languagesByCountryFRCopy.get(0));
            assertEquals(languagesByCountryFR.get(1), languagesByCountryFRCopy.get(1));
            assertEquals(languagesByCountryFR.get(2), languagesByCountryFRCopy.get(2));
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguageEN = LocaleUtils.countriesByLanguage("EN");
        assertEquals(12, countriesByLanguageEN.size());
        assertEquals(new Locale("EN", "AU"), countriesByLanguageEN.get(0));
        assertEquals(new Locale("EN", "GB"), countriesByLanguageEN.get(1));
        assertEquals(new Locale("EN", "US"), countriesByLanguageEN.get(2));
        assertEquals(new Locale("EN", "CA"), countriesByLanguageEN.get(3));
        assertEquals(new Locale("EN", "IE"), countriesByLanguageEN.get(4));
        assertEquals(new Locale("EN", "IN"), countriesByLanguageEN.get(5));
        assertEquals(new Locale("EN", "NZ"), countriesByLanguageEN.get(6));
        assertEquals(new Locale("EN", "SG"), countriesByLanguageEN.get(7));
        assertEquals(new Locale("EN", "ZA"), countriesByLanguageEN.get(8));
        assertEquals(new Locale("EN", "JM"), countriesByLanguageEN.get(9));
        assertEquals(new Locale("EN", "TT"), countriesByLanguageEN.get(10));
        assertEquals(new Locale("EN", "BM"), countriesByLanguageEN.get(11));

        List<Locale> countriesByLanguageFR = LocaleUtils.countriesByLanguage("FR");
        assertEquals(27, countriesByLanguageFR.size());
        // ...

        List<Locale> countriesByLanguageNull = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countriesByLanguageNull.size());

        List<Locale> localeList = new ArrayList<>();
        localeList.addAll(LocaleUtils.availableLocaleList());
        LocaleUtils.cCountriesByLanguage.clear();
        List<Locale> countriesByLanguageEmpty = LocaleUtils.countriesByLanguage("XX");
        assertEquals(0, countriesByLanguageEmpty.size());

        // Test for performance
        for (int i = 0; i < 10000; i++) {
            List<Locale> countriesByLanguageENTest = LocaleUtils.countriesByLanguage("EN");
            List<Locale> countriesByLanguageENCopy = new ArrayList<>(countriesByLanguageEN);
            assertEquals(countriesByLanguageEN.get(0), countriesByLanguageENCopy.get(0));
            assertEquals(countriesByLanguageEN.get(1), countriesByLanguageENCopy.get(1));
            assertEquals(countriesByLanguageEN.get(2), countriesByLanguageENCopy.get(2));
            // ...
        }
    }
}