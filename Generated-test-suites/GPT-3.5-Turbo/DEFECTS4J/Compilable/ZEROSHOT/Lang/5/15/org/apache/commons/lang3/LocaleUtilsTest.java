package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr", ""), LocaleUtils.toLocale("fr"));
        try {
            LocaleUtils.toLocale("en_GB_xxxx");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testLocaleLookupList_locale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupList_locale_defaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("fr", "GB"));
        assertEquals(expected, LocaleUtils.languagesByCountry("GB"));
        expected.clear();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("fr", "US"));
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
        expected.clear();
        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("en", "US"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        expected.clear();
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr", "CH"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));
        expected.clear();
        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }

}