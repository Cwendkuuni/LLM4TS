package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("eng");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("EN_GB");
    }

    @Test
    public void testLocaleLookupList() {
        assertTrue(LocaleUtils.localeLookupList(Locale.US).contains(Locale.US));
        assertTrue(LocaleUtils.localeLookupList(Locale.US).contains(new Locale("en", "US")));
        assertTrue(LocaleUtils.localeLookupList(Locale.US).contains(new Locale("en", "")));
        assertTrue(LocaleUtils.localeLookupList(Locale.US).contains(Locale.getDefault()));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale defaultLocale = new Locale("fr", "FR");
        assertTrue(LocaleUtils.localeLookupList(Locale.US, defaultLocale).contains(Locale.US));
        assertTrue(LocaleUtils.localeLookupList(Locale.US, defaultLocale).contains(new Locale("en", "US")));
        assertTrue(LocaleUtils.localeLookupList(Locale.US, defaultLocale).contains(new Locale("en", "")));
        assertTrue(LocaleUtils.localeLookupList(Locale.US, defaultLocale).contains(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.US));
    }

    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.US));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(!LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertTrue(LocaleUtils.languagesByCountry("US").contains(new Locale("en", "US")));
    }

    @Test
    public void testCountriesByLanguage() {
        assertTrue(LocaleUtils.countriesByLanguage("en").contains(new Locale("en", "US")));
    }

}