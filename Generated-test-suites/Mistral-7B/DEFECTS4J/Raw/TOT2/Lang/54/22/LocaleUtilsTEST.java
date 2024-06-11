package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.forLanguageTag("en-GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.forLanguageTag("en-GB-xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Edge cases and exception handling test
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("xx_XX_XXX");
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge cases test with variant locale
        assertEquals(Locale.forLanguageTag("en-GB-London"), LocaleUtils.toLocale("en_GB_London"));
        assertEquals(Locale.forLanguageTag("en-GB-London-FOO"), LocaleUtils.toLocale("en_GB_London_FOO"));

        // Test with default locale
        List<Locale> lookupList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(Locale.FRENCH, lookupList.get(0));
        assertEquals(Locale.FRENCH, lookupList.get(1));
        assertEquals(Locale.FRENCH, lookupList.get(2));
        assertEquals(Locale.ENGLISH, lookupList.get(3));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale localeFR_CA = new Locale("fr", "CA");
        List<Locale> lookupList = LocaleUtils.localeLookupList(localeFR_CA);
        assertEquals(localeFR_CA, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr", ""), lookupList.get(2));

        // Test with default locale
        List<Locale> lookupListDefault = LocaleUtils.localeLookupList(localeFR_CA, Locale.ENGLISH);
        assertEquals(localeFR_CA, lookupListDefault.get(0));
        assertEquals(new Locale("fr", "CA"), lookupListDefault.get(1));
        assertEquals(new Locale("fr", ""), lookupListDefault.get(2));
        assertEquals(Locale.ENGLISH, lookupListDefault.get(3));

        // Test with empty list
        List<Locale> emptyLookupList = LocaleUtils.localeLookupList(null);
        assertTrue(emptyLookupList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertTrue(availableLocaleList.size() > 0);

        // Test with empty list
        List<Locale> emptyAvailableLocaleList = LocaleUtils.availableLocaleList();
        assertTrue(emptyAvailableLocaleList.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));

        // Test with empty list
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> languagesByCountryFR = LocaleUtils.languagesByCountry("FR");
        assertTrue(languagesByCountryFR.size() > 0);
        assertEquals(Locale.FRENCH, languagesByCountryFR.get(0));

        // Test with empty list
        List<Locale> emptyLanguagesByCountry = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyLanguagesByCountry.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countriesByLanguageEN = LocaleUtils.countriesByLanguage("EN");
        assertTrue(countriesByLanguageEN.size() > 0);
        assertEquals(Locale.ENGLISH, countriesByLanguageEN.get(0));

        // Test with empty list
        List<Locale> emptyCountriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyCountriesByLanguage.isEmpty());
    }

    @Test
    public void testToString() {
        Locale locale = new Locale("fr", "CA");
        String toString = LocaleUtils.toLocale("fr_CA").toString();
        assertEquals(locale.toString(), toString);
    }

    @Test
    public void testHashCode() {
        Locale locale = new Locale("fr", "CA");
        int hashCode = LocaleUtils.toLocale("fr_CA").hashCode();
        assertEquals(locale.hashCode(), hashCode);
    }

    @Test
    public void testEquals() {
        Locale locale1 = new Locale("fr", "CA");
        Locale locale2 = new Locale("fr", "CA");
        assertTrue(new EqualsBuilder()
                .append(locale1, locale2)
                .isEquals());
        assertFalse(new EqualsBuilder()
                .append(locale1, new Locale("fr", "US"))
                .isEquals());
    }
}