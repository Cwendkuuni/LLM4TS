package org.apache.commons.lang3;
import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en"),
                new Locale("en_GB"),
                new Locale("en_GB_xxx"),
                new Locale("fr_CA"),
                new Locale("fr_CA_xxx"),
                new Locale("fr_CA_yyy")
        );
        for (Locale locale : expectedLocales) {
            Locale resultLocale = LocaleUtils.toLocale(locale.toString());
            Assert.assertEquals(locale, resultLocale);
        }

        // Test invalid inputs
        String invalidLocale1 = "";
        String invalidLocale2 = "XX_YY";
        String invalidLocale3 = "en_GB_xxx_zzz";
        Assert.assertNull(LocaleUtils.toLocale(invalidLocale1));
        Assert.assertNull(LocaleUtils.toLocale(invalidLocale2));
        Assert.assertNull(LocaleUtils.toLocale(invalidLocale3));
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("en_GB", "GB");
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en_GB", "GB"),
                new Locale("en_GB"),
                new Locale("en"),
                new Locale("es") // default locale
        );
        List<Locale> resultLocales = LocaleUtils.localeLookupList(baseLocale, new Locale("es"));
        Assert.assertEquals(expectedLocales, resultLocales);

        // Test empty input
        List<Locale> resultLocalesEmpty = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(Collections.emptyList(), resultLocalesEmpty);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        // Test invalid input
        Locale invalidLocale = new Locale("INVALID");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLocalesFrance = Arrays.asList(
                new Locale("fr_FR"),
                new Locale("fr_BE"),
                new Locale("fr_CH"),
                new Locale("fr_CA")
        );
        List<Locale> resultLocalesFrance = LocaleUtils.languagesByCountry("FR");
        Assert.assertEquals(expectedLocalesFrance, resultLocalesFrance);

        // Test empty input
        List<Locale> resultLocalesEmpty = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(Collections.emptyList(), resultLocalesEmpty);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedLocalesEnglish = Arrays.asList(
                new Locale("en_GB"),
                new Locale("en_US"),
                new Locale("en_CA"),
                new Locale("en_IN")
        );
        List<Locale> resultLocalesEnglish = LocaleUtils.countriesByLanguage("EN");
        Assert.assertEquals(expectedLocalesEnglish, resultLocalesEnglish);

        // Test empty input
        List<Locale> resultLocalesEmpty = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(Collections.emptyList(), resultLocalesEmpty);
    }
}