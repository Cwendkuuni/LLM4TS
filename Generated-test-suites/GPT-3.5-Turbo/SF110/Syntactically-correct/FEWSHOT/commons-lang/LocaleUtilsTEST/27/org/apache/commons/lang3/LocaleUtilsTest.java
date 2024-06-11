package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(null, LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        try {
            LocaleUtils.toLocale("#");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("a");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("en");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("en_USA");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("_en_US");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("__US");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("__US_");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("__US_1");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("_US__");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("_US");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("_US_");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("en_US_");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("_US_1test");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        try {
            LocaleUtils.toLocale("__US_SPLIT");
            //fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
        	
        }
        assertEquals(new Locale("", "us"), LocaleUtils.toLocale("_US"));
        assertEquals(new Locale("", "us", ""), LocaleUtils.toLocale("__US"));
        assertEquals(new Locale("", "us", "split"), LocaleUtils.toLocale("__US_SPLIT"));
        assertEquals(new Locale("en", "us"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "us", ""), LocaleUtils.toLocale("en__US"));
        assertEquals(new Locale("en", "us", "split"), LocaleUtils.toLocale("en__US_SPLIT"));
        assertEquals(new Locale("en", "us", "1"), LocaleUtils.toLocale("en_US_1"));
    }

    @Test
    public void testLocaleLookupList() {
        assertEquals(Collections.singletonList(null), LocaleUtils.localeLookupList(null));
        assertEquals(Collections.singletonList(null), LocaleUtils.localeLookupList(null, null));

        Locale locale = new Locale("en", "US");
        assertEquals(Arrays.asList(locale, new Locale("en")), LocaleUtils.localeLookupList(locale));
        assertEquals(Arrays.asList(locale, new Locale("en")), LocaleUtils.localeLookupList(locale, null));

        Locale defaultLocale = new Locale("en", "GB");
        assertEquals(Arrays.asList(locale, new Locale("en"), defaultLocale), LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertEquals(true, LocaleUtils.isAvailableLocale(new Locale("en")));
        assertEquals(false, LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
        assertEquals(Arrays.asList(Locale.US), LocaleUtils.languagesByCountry("US"));
        assertEquals(Arrays.asList(Locale.FRENCH, Locale.ENGLISH), LocaleUtils.languagesByCountry("FR"));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
        assertEquals(Arrays.asList(Locale.US), LocaleUtils.countriesByLanguage("EN"));
        assertEquals(Arrays.asList(Locale.FRANCE, Locale.CANADA_FRENCH), LocaleUtils.countriesByLanguage("FR"));
    }
}