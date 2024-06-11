package org.apache.commons.lang;
import org.junit.Test;

import java.util.Locale;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");

        assertEquals(Locale.ENGLISH, locale1);
        assertEquals(new Locale("en", "GB"), locale2);
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> locales1 = LocaleUtils.localeLookupList(Locale.US);
        List<Locale> locales2 = LocaleUtils.localeLookupList(Locale.US, Locale.ENGLISH);

        assertNotNull(locales1);
        assertNotNull(locales2);
        assertFalse(locales1.isEmpty());
        assertFalse(locales2.isEmpty());
        assertTrue(locales1.contains(Locale.US));
        assertTrue(locales2.contains(Locale.US));
        assertTrue(locales2.contains(Locale.ENGLISH));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.ENGLISH));
        assertFalse(LocaleUtils.availableLocaleSet().contains(Locale.CHINESE));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
        assertNotNull(languagesByCountry);
        assertFalse(languagesByCountry.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countriesByLanguage);
        assertFalse(countriesByLanguage.isEmpty());
    }

}