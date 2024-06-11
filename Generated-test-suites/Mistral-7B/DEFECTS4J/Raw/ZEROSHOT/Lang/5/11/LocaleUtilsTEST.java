package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Assert.assertNotNull(LocaleUtils.toLocale("en"));
        Assert.assertNotNull(LocaleUtils.toLocale("en_GB"));
        Assert.assertNotNull(LocaleUtils.toLocale("en_GB_xxx"));
        Assert.assertNotNull(LocaleUtils.toLocale("fr_CA"));
        Assert.assertNotNull(LocaleUtils.toLocale("fr_CA_xxx"));

        // Test invalid locale strings
        Assert.assertNull(LocaleUtils.toLocale(null));
        Assert.assertNull(LocaleUtils.toLocale(""));
        Assert.assertNull(LocaleUtils.toLocale("en_"));
        Assert.assertNull(LocaleUtils.toLocale("en_GB_"));
        Assert.assertNull(LocaleUtils.toLocale("en_GB_xxx_"));
        Assert.assertNull(LocaleUtils.toLocale("fr_CA_xxx_"));
        Assert.assertNull(LocaleUtils.toLocale("fr_CA_xxx_xxx"));
        Assert.assertNull(LocaleUtils.toLocale("fr_CA_xxx_xxx_"));

        // Test empty locale objects
        Assert.assertNull(LocaleUtils.toLocale(""));
        Assert.assertNull(LocaleUtils.toLocale(new Locale("", "")));

        // Test locales with empty country or language
        Assert.assertNotNull(LocaleUtils.toLocale(""));
        Assert.assertNotNull(LocaleUtils.toLocale("en"));
        Assert.assertNotNull(LocaleUtils.toLocale("en_"));
        Assert.assertNotNull(LocaleUtils.toLocale(""));
        Assert.assertNotNull(LocaleUtils.toLocale("fr"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        // Test empty lookup list
        Assert.assertEquals(0, LocaleUtils.localeLookupList(null).size());

        // Test valid locale with no variants
        Assert.assertEquals(1, LocaleUtils.localeLookupList(new Locale("en")).size());
        Assert.assertEquals(1, LocaleUtils.localeLookupList(new Locale("en", "")).size());

        // Test valid locale with country
        Assert.assertEquals(2, LocaleUtils.localeLookupList(new Locale("en", "CA")).size());

        // Test valid locale with variant
        Assert.assertEquals(4, LocaleUtils.localeLookupList(locale).size());

        // Test invalid locale
        Assert.assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(true, localeList.contains(Locale.ENGLISH));
        Assert.assertEquals(true, localeList.contains(Locale.FRENCH));
        Assert.assertEquals(true, localeList.contains(Locale.GERMAN));

        // Test empty list
        Assert.assertEquals(0, LocaleUtils.availableLocaleList().size());
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");

        // Test empty list
        Assert.assertEquals(0, LocaleUtils.languagesByCountry(null).size());

        // Test valid country
        Assert.assertEquals(2, LocaleUtils.languagesByCountry("CA").size());
        Assert.assertEquals(true, LocaleUtils.languagesByCountry("CA").contains(Locale.CANADA_FRENCH));
        Assert.assertEquals(true, LocaleUtils.languagesByCountry("CA").contains(Locale.CANADA_ENGLISH));

        // Test invalid country
        Assert.assertEquals(0, LocaleUtils.languagesByCountry("ZZ").size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");

        // Test empty list
        Assert.assertEquals(0, LocaleUtils.countriesByLanguage(null).size());

        // Test valid language
        Assert.assertEquals(2, LocaleUtils.countriesByLanguage("en").size());
        Assert.assertEquals(true, LocaleUtils.countriesByLanguage("en").contains(Locale.ENGLISH_US));
        Assert.assertEquals(true, LocaleUtils.countriesByLanguage("en").contains(Locale.ENGLISH_GB));

        // Test invalid language
        Assert.assertEquals(0, LocaleUtils.countriesByLanguage("ZZ").size());
    }
}