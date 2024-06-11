package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleEmpty() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleValid() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertSame(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWrongCase() {
        LocaleUtils.toLocale("en_GB");
        LocaleUtils.toLocale("en_gb");
    }

    @Test
    public void testToLocaleWithVariant() {
        Locale expected = new Locale("en", "GB", "US");
        Locale actual = LocaleUtils.toLocale("en_GB_US");
        assertSame(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidVariant() {
        LocaleUtils.toLocale("en_GB_XXX");
    }

    @Test
    public void testLocaleLookupList() {
        List expected = Arrays.asList(
                new Locale("en", "GB", "US"),
                new Locale("en", "GB"),
                new Locale("en", ""),
                new Locale("fr", "CA"),
                new Locale("fr", "CA", "XX")
        );

        List actual = LocaleUtils.localeLookupList(new Locale("en", "GB", "US"), new Locale("fr", "CA"));
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testLocaleLookupListNull() {
        LocaleUtils.localeLookupList(null, new Locale("fr", "CA"));
    }

    @Test
    public void testAvailableLocaleList() {
        List expected = LocaleUtils.availableLocaleList();
        List actual = Arrays.asList(LocaleUtils.availableLocaleList().toArray());
        assertSame(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set expected = LocaleUtils.availableLocaleSet();
        Set actual = new HashSet<>(LocaleUtils.availableLocaleList());
        assertSame(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        Locale locale2 = new Locale("fr", "CA", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        List expected = Arrays.asList(new Locale("en", "GB"), new Locale("en", "US"));
        List actual = LocaleUtils.languagesByCountry("GB");
        assertSame(expected, actual);
    }

    @Test
    public void testLanguagesByCountryNull() {
        List expected = Collections.emptyList();
        List actual = LocaleUtils.languagesByCountry(null);
        assertSame(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List expected = Arrays.asList(new Locale("GB", "en"), new Locale("US", "en"));
        List actual = LocaleUtils.countriesByLanguage("en");
        assertSame(expected, actual);
    }

    @Test
    public void testCountriesByLanguageNull() {
        List expected = Collections.emptyList();
        List actual = LocaleUtils.countriesByLanguage(null);
        assertSame(expected, actual);
    }
}