
package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "FR", "xxx"), LocaleUtils.toLocale("fr_FR_xxx"));
        assertEquals(new Locale("fr", ""), LocaleUtils.toLocale("fr"));
        assertNull(LocaleUtils.toLocale(null));
        //Invalid locale format
        try {
            LocaleUtils.toLocale("en-GB");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en-GB", e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedResult = new ArrayList<>();
        expectedResult.add(locale);
        expectedResult.add(new Locale("fr", "CA"));
        expectedResult.add(new Locale("fr"));
        expectedResult.add(defaultLocale);
        assertEquals(expectedResult, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedResult = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedResult, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedResult = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expectedResult, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

   @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedResult = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        assertEquals(expectedResult, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedResult = Arrays.asList(new Locale("en", "US"), new Locale("fr", "FR"));
        assertEquals(expectedResult, LocaleUtils.countriesByLanguage("en"));
    }

}
