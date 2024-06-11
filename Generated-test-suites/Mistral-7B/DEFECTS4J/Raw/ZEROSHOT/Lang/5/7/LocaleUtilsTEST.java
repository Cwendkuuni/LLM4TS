package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid cases
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "XX");
        actual = LocaleUtils.toLocale("fr_CA_XX");
        assertEquals(expected, actual);

        expected = new Locale("es");
        actual = LocaleUtils.toLocale("es");
        assertEquals(expected, actual);

        // Test invalid cases
        try {
            LocaleUtils.toLocale("en_GB_xx");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XX");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XX_xx");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_XX");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XX_xx");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "XX");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "XX"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);

        expected = Arrays.asList(new Locale("fr"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "BE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

}