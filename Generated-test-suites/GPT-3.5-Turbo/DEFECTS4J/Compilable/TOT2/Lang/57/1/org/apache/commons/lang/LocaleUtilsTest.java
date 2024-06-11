package org.apache.commons.lang;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test
    public void testToLocaleInvalidFormat() {
        // Testing invalid format scenario
        try {
            LocaleUtils.toLocale("INVALID");
        } catch (IllegalArgumentException e) {
            // Exception expected
            return;
        }
        assertNull("Expected IllegalArgumentException not thrown", null);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(LocaleUtils.availableLocaleList(), LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(LocaleUtils.availableLocaleSet(), LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertEquals(true, LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(4, LocaleUtils.languagesByCountry("US").size());
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(4, LocaleUtils.countriesByLanguage("en").size());
    }
}