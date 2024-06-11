package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = Locale.ENGLISH;
        Locale actual = LocaleUtils.toLocale("en");
        Assert.assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        Assert.assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.toLocale("fr_CA_xxx");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_a");
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List expected = Arrays.asList(
                baseLocale,
                new Locale("fr", "CA"),
                new Locale("fr", ""),
                Locale.getDefault()
        );
        List actual = LocaleUtils.localeLookupList(baseLocale);
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());

        baseLocale = new Locale("en");
        expected = Collections.singletonList(Locale.ENGLISH);
        actual = LocaleUtils.localeLookupList(baseLocale, Locale.getDefault());
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testAvailableLocaleList() {
        List expected = LocaleUtils.availableLocaleList();
        List actual = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set expected = LocaleUtils.availableLocaleSet();
        Set actual = new HashSet(LocaleUtils.availableLocaleList());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "FR");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "GB");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("nonexistent", "nonexistent");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List expected = LocaleUtils.languagesByCountry("CA");
        List actual = Arrays.asList(Locale.CANADA, new Locale("fr", "CA"), new Locale("en", "CA"));
        Assert.assertEquals(expected, actual);

        expected = Collections.singletonList(Locale.getDefault());
        actual = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List expected = LocaleUtils.countriesByLanguage("fr");
        List actual = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"));
        Assert.assertEquals(expected, actual);

        expected = Collections.singletonList(Locale.getDefault());
        actual = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(expected, actual);
    }
}