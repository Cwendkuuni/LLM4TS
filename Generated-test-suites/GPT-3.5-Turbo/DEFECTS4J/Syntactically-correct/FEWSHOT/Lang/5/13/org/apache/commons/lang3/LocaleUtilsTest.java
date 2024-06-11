package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("EN_gb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat3() {
        LocaleUtils.toLocale("en_GB_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat4() {
        LocaleUtils.toLocale("en_GB_@");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat5() {
        LocaleUtils.toLocale("en_GB@");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat6() {
        LocaleUtils.toLocale("en_GB_@#");
    }

    @Test
    public void testLocaleLookupList() {
        assertEquals(Collections.singletonList(new Locale("fr", "CA", "xxx")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }

    @Test
    public void testLocaleLookupListDefaultLocale() {
        assertEquals(Collections.singletonList(new Locale("fr", "CA", "xxx")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(new HashSet<>(Arrays.asList(Locale.getAvailableLocales())), LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA")), LocaleUtils.languagesByCountry("CA"));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XX"));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx")), LocaleUtils.countriesByLanguage("fr"));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("xx"));
    }
}