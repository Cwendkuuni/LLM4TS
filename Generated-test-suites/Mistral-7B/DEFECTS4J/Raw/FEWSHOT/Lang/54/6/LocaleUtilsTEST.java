package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("aa"));
        assertNull(LocaleUtils.toLocale("AA"));
        assertNull(LocaleUtils.toLocale("en_GB_XXX"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx_XXX"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx_XXX_XXX"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx_XXX_XXX_XXX"));
    }

    @Test
    public void testLocaleLookupList() {
        List localeList = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(Locale.US, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        assertEquals(Locale.ROOT, localeList.get(2));

        List localeListDefault = LocaleUtils.localeLookupList(Locale.US, Locale.FRENCH);
        assertEquals(Locale.US, localeListDefault.get(0));
        assertEquals(Locale.US, localeListDefault.get(1));
        assertEquals(Locale.ENGLISH, localeListDefault.get(2));
        assertEquals(Locale.FRENCH, localeListDefault.get(3));

        List localeListNull = LocaleUtils.localeLookupList(null);
        assertTrue(localeListNull.isEmpty());

        List localeListEmpty = LocaleUtils.localeLookupList(Locale.ROOT);
        assertTrue(localeListEmpty.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(Locale.class, availableLocaleList.getClass());
        assertTrue(availableLocaleList.size() > 0);

        List availableLocaleListSubset = new ArrayList(Arrays.asList(Locale.ENGLISH, Locale.FRENCH));
        List availableLocaleListFiltered = new ArrayList<>();
        for (Object o : availableLocaleList) {
            Locale locale = (Locale) o;
            if (Locale.ENGLISH.equals(locale) || Locale.FRENCH.equals(locale)) {
                availableLocaleListFiltered.add(locale);
            }
        }
        assertEquals(availableLocaleListSubset, availableLocaleListFiltered);
    }

    @Test
    public void testLanguagesByCountry() {
        List frenchLanguages = LocaleUtils.languagesByCountry("FR");
        assertNotNull(frenchLanguages);
        assertEquals(Locale.class, frenchLanguages.getClass());
        assertTrue(frenchLanguages.size() > 0);
        assertEquals(Locale.FRENCH, frenchLanguages.get(0));

        List emptyLanguages = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyLanguages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List englishCountries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(englishCountries);
        assertEquals(Locale.class, englishCountries.getClass());
        assertTrue(englishCountries.size() > 0);
        assertEquals(Locale.US, englishCountries.get(0));

        List emptyCountries = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyCountries.isEmpty());
    }
}