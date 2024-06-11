package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testToLocale_emptyString() {
        Locale locale = LocaleUtils.toLocale("");
        assertNotNull(locale);
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_invalidFormat() {
        try {
            LocaleUtils.toLocale("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> list = LocaleUtils.localeLookupList(Locale.US, Locale.US);
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(Locale.US, list.get(0));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertFalse(set.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale_true() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_false() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }
    
    @Test
    public void testLanguagesByCountry_nullCountryCode() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_nullLanguageCode() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

}