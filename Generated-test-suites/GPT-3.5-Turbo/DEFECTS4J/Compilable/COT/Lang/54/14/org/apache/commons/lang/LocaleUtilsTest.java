package org.apache.commons.lang;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals("en", locale1.getLanguage());
        assertEquals("", locale1.getCountry());
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());
        
        Locale locale4 = LocaleUtils.toLocale("fr");
        assertEquals("fr", locale4.getLanguage());
        assertEquals("", locale4.getCountry());
        
        Locale locale5 = LocaleUtils.toLocale("fr_CA");
        assertEquals("fr", locale5.getLanguage());
        assertEquals("CA", locale5.getCountry());
        
        Locale locale6 = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals("fr", locale6.getLanguage());
        assertEquals("CA", locale6.getCountry());
        assertEquals("xxx", locale6.getVariant());
        
        try {
            LocaleUtils.toLocale("invalidLocale");
            fail("IllegalArgumentException should be thrown for invalid locale");
        } catch (IllegalArgumentException e) {
            //exception expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(3, list1.size());
        assertEquals(locale1, list1.get(0));
        assertEquals(new Locale("fr", "CA"), list1.get(1));
        assertEquals(new Locale("fr"), list1.get(2));
        
        List<Locale> list2 = LocaleUtils.localeLookupList(locale1, new Locale("en"));
        assertEquals(4, list2.size());
        assertEquals(locale1, list2.get(0));
        assertEquals(new Locale("fr", "CA"), list2.get(1));
        assertEquals(new Locale("fr"), list2.get(2));
        assertEquals(new Locale("en"), list2.get(3));
        
        List<Locale> list3 = LocaleUtils.localeLookupList(null);
        assertEquals(0, list3.size());
        
        List<Locale> list4 = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(1, list4.size());
        assertEquals(new Locale("en"), list4.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertTrue(set.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("invalidLanguage", "invalidCountry");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs1 = LocaleUtils.languagesByCountry("GB");
        assertNotNull(langs1);
        assertTrue(langs1.contains(new Locale("en", "GB")));
        assertTrue(langs1.contains(new Locale("gd", "GB")));
        assertTrue(langs1.contains(new Locale("cy", "GB")));
        
        List<Locale> langs2 = LocaleUtils.languagesByCountry("invalidCountry");
        assertNotNull(langs2);
        assertEquals(0, langs2.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries1);
        assertTrue(countries1.contains(new Locale("fr", "CA")));
        assertTrue(countries1.contains(new Locale("fr", "CH")));
        assertTrue(countries1.contains(new Locale("fr", "BE")));
        
        List<Locale> countries2 = LocaleUtils.countriesByLanguage("invalidLanguage");
        assertNotNull(countries2);
        assertEquals(0, countries2.size());
    }

}