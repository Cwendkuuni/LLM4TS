import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
        
        IllegalArgumentException thrown = null;
        try {
            LocaleUtils.toLocale("invalid_locale");
        } catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals("Invalid locale format: invalid_locale", thrown.getMessage());
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        assertEquals(3, locales.size());
        assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
        assertEquals(new Locale("fr", "CA"), locales.get(1));
        assertEquals(new Locale("fr"), locales.get(2));
        
        Locale defaultLocale = new Locale("en");
        locales = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, locales.size());
        assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
        assertEquals(new Locale("fr", "CA"), locales.get(1));
        assertEquals(new Locale("fr"), locales.get(2));
        assertEquals(new Locale("en"), locales.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINESE));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages1 = LocaleUtils.languagesByCountry("CA");
        assertNotNull(languages1);
        assertTrue(languages1.contains(Locale.ENGLISH));
        assertTrue(languages1.contains(Locale.FRENCH));
        
        List<Locale> languages2 = LocaleUtils.languagesByCountry("US");
        assertNotNull(languages2);
        assertTrue(languages2.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries1);
        assertTrue(countries1.contains(Locale.CANADA));
        
        List<Locale> countries2 = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries2);
        assertTrue(countries2.contains(Locale.CANADA));
        assertFalse(countries2.contains(Locale.US));
    }

}