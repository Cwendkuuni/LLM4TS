package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocale() {
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals("LocaleUtils.toLocale(en_US) should return Locale[en, US]",
                expectedLocale, actualLocale);

        expectedLocale = new Locale("fr", "CA");
        actualLocale = LocaleUtils.toLocale("fr_CA");
        assertEquals("LocaleUtils.toLocale(fr_CA) should return Locale[fr, CA]",
                expectedLocale, actualLocale);

        expectedLocale = new Locale("es", "ES");
        actualLocale = LocaleUtils.toLocale("es_ES");
        assertEquals("LocaleUtils.toLocale(es_ES) should return Locale[es, ES]",
                expectedLocale, actualLocale);
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("enUS"); // missing underscore
        LocaleUtils.toLocale("en_US_"); // extra underscore
        LocaleUtils.toLocale("en_GB_en"); // language code is not lowercase
        LocaleUtils.toLocale("en_GB_EN"); // country code is not uppercase
        LocaleUtils.toLocale("en_GB_xx"); // invalid country code
        LocaleUtils.toLocale("en_GB_xx_yy"); // more than 3 parts
        LocaleUtils.toLocale("en_GB_xx_yy_zz"); // more than 3 parts
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr", ""),
                new Locale("en")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
        assertEquals("LocaleUtils.localeLookupList(fr_CA_xxx, en) should return " +
                "[Locale[fr, CA, xxx], Locale[fr, CA], Locale[fr], Locale[en]]",
                expectedList, actualList);

        // test empty list and defaultLocale as null
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> actualEmptyList = LocaleUtils.localeLookupList(null, null);
        assertEquals("LocaleUtils.localeLookupList(null, null) should return empty list",
                emptyList, actualEmptyList);
    }

    /**
     * Test localeLookupList method with valid input and null defaultLocale.
     */
    @Test
    public void testLocaleLookupListWithNullDefaultLocale() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                null
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(baseLocale, null);
        assertEquals("LocaleUtils.localeLookupList(fr_CA_xxx, null) should return " +
                "[Locale[fr, CA, xxx], Locale[fr, CA], Locale[fr], null]",
                expectedList, actualList);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals("LocaleUtils.availableLocaleList() should return the same list as " +
                "Locale.getAvailableLocales()", expectedList, actualList);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = Collections.newHashSet(Locale.getAvailableLocales());
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        assertTrue("LocaleUtils.availableLocaleSet() should contain all available locales",
                expectedSet.containsAll(actualSet));
    }

    /**
     * Test isAvailableLocale method with valid input.
     */
    @Test
    public void testIsAvailableLocale() {
        assertTrue("LocaleUtils.isAvailableLocale(en_US) should return true",
                LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        assertTrue("LocaleUtils.isAvailableLocale(fr_CA) should return true",
                LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));

        assertTrue("LocaleUtils.isAvailableLocale(es_ES) should return true",
                LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
    }

    /**
     * Test isAvailableLocale method with invalid input.
     */
    @Test
    public void testIsAvailableLocaleInvalidInput() {
        assertFalse("LocaleUtils.isAvailableLocale(en_US_) should return false",
                LocaleUtils.isAvailableLocale(new Locale("en", "US", "")));

        assertFalse("LocaleUtils.isAvailableLocale(en_US_xxx) should return false",
                LocaleUtils.isAvailableLocale(new Locale("en", "US", "xxx")));

        assertFalse("LocaleUtils.isAvailableLocale(en_US_XX) should return false",
                LocaleUtils.isAvailableLocale(new Locale("en", "US", "XX")));

        assertFalse("LocaleUtils.isAvailableLocale(en_US__xx) should return false",
                LocaleUtils.isAvailableLocale(new Locale("en", "US", "__xx")));

        assertFalse("LocaleUtils.isAvailableLocale(null) should return false",
                LocaleUtils.isAvailableLocale(null));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("nl", "CA")
        );
        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        assertEquals("LocaleUtils.languagesByCountry(CA) should return [en_CA, fr_CA, nl_CA]",
                expectedList, actualList);

        List<Locale> emptyList = Collections.emptyList();
        List<Locale> actualEmptyList = LocaleUtils.languagesByCountry(null);
        assertEquals("LocaleUtils.languagesByCountry(null) should return an empty list",
                emptyList, actualEmptyList);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("CA", "en"),
                new Locale("CA", "fr"),
                new Locale("CA", "nl")
        );
        List<Locale> actualList = LocaleUtils.countriesByLanguage("en");
        assertEquals("LocaleUtils.countriesByLanguage(en) should return [CA_en, CA_fr, CA_nl]",
                expectedList, actualList);

        List<Locale> emptyList = Collections.emptyList();
        List<Locale> actualEmptyList = LocaleUtils.countriesByLanguage(null);
        assertEquals("LocaleUtils.countriesByLanguage(null) should return an empty list",
                emptyList, actualEmptyList);
    }
}