package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalid() {
        LocaleUtils.toLocale("");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(new Locale("fr","CA","xxx"), new Locale("fr","CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr","CA","xxx"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = SyncAvoid.AVAILABLE_LOCALE_SET;
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zzz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("it","CH","xxx"), new Locale("it","CH"));
        assertEquals(expected, LocaleUtils.languagesByCountry("CH"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("de","AT"), new Locale("de","CH"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("de"));
    }
}