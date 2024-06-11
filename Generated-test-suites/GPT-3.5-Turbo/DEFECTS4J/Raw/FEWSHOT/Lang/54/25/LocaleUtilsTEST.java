package org.apache.commons.lang;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        try {
            LocaleUtils.toLocale("en-gb");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("e-GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupListWithLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
    }

    @Test
    public void testLocaleLookupListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List result = LocaleUtils.languagesByCountry("CA");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List result = LocaleUtils.countriesByLanguage("en");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}