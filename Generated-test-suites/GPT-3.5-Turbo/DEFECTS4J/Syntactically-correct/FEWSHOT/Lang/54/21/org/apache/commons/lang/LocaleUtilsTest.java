package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.Locale;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalid() {
        LocaleUtils.toLocale("en_GB_xxx_");
    }

    @Test
    public void testLocaleLookupList() {
        assertEquals(Arrays.asList(Locale.getDefault(), new Locale("fr", "FR"), Locale.ENGLISH), LocaleUtils.localeLookupList(Locale.FRANCE, Locale.ENGLISH));
        assertEquals(Arrays.asList(Locale.getDefault(), new Locale("fr", "FR"), Locale.ENGLISH), LocaleUtils.localeLookupList(new Locale("fr", "CA"), Locale.ENGLISH));
        assertEquals(Arrays.asList(Locale.getDefault(), Locale.ENGLISH), LocaleUtils.localeLookupList(null, Locale.ENGLISH));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.GERMAN));
        assertTrue(availableLocales.contains(Locale.ITALIAN));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.GERMAN));
        assertTrue(availableLocales.contains(Locale.ITALIAN));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("FR");
        assertEquals(Arrays.asList(Locale.FRENCH), languagesByCountry);
        languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assertEquals(Arrays.asList(Locale.ENGLISH, Locale.FRENCH), languagesByCountry);
        languagesByCountry = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountry.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("fr");
        assertEquals(Arrays.asList(Locale.CANADA, Locale.FRANCE), countriesByLanguage);
        countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertEquals(Arrays.asList(Locale.CANADA, Locale.UK), countriesByLanguage);
        countriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguage.isEmpty());
    }

}