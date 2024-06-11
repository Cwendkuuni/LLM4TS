import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

import java.util.Locale;

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
    }

    @Test
    public void testToLocaleInvalidInput() {
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("invalidLocale");
            fail("Expected IllegalArgumentException for invalid input format");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, localeList.size());
        assertTrue(localeList.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(localeList.contains(new Locale("fr", "CA")));
        assertTrue(localeList.contains(new Locale("fr")));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, localeList.size());
        assertTrue(localeList.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(localeList.contains(new Locale("fr", "CA")));
        assertTrue(localeList.contains(new Locale("fr")));
        assertTrue(localeList.contains(new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid", "locale")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        assertTrue(langs.contains(new Locale("fr", "CA")));
        assertFalse(langs.contains(new Locale("invalid", "locale")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertTrue(countries.contains(new Locale("fr", "CA")));
        assertFalse(countries.contains(new Locale("invalid", "locale")));
    }
}