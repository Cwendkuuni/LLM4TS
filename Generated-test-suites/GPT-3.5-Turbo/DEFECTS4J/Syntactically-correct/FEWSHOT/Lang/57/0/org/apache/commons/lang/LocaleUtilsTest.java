
package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode() {
        LocaleUtils.toLocale("EN_GB");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode() {
        LocaleUtils.toLocale("en_gb");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidSeparator() {
        LocaleUtils.toLocale("en-GB");
    }
    
    @Test
    public void testLocaleLookupListWithLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            Locale.getDefault()
        );
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
    }
    
    @Test
    public void testLocaleLookupListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testLocaleLookupListWithNullLocale() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.localeLookupList(null));
    }
    
    @Test
    public void testLocaleLookupListWithNullLocaleAndDefaultLocale() {
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Collections.singletonList(defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(null, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.getDefault()));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.getDefault()));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.getDefault()));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(
            new Locale("en", "AU"),
            new Locale("en", "CA"),
            new Locale("en", "GB"),
            new Locale("en", "IN"),
            new Locale("en", "NZ"),
            new Locale("en", "US")
        );
        assertEquals(expected, LocaleUtils.languagesByCountry("AU"));
    }
    
    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(
            new Locale("en", "AU"),
            new Locale("en", "CA"),
            new Locale("en", "GB"),
            new Locale("en", "IN"),
            new Locale("en", "NZ"),
            new Locale("en", "US")
        );
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }
    
    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }
    
}
