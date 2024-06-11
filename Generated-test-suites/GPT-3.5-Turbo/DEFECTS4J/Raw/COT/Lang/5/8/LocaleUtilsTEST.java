import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleInvalidFormat() {
        try {
            LocaleUtils.toLocale("abc");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: abc", e.getMessage());
        }
    }

    @Test
    public void testToLocaleLanguageOnly() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocaleLanguageAndCountry() {
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    }
    
    @Test
    public void testToLocaleLanguageCountryAndVariant() {
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test
    public void testLocaleLookupListWithLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
    }

    @Test
    public void testLocaleLookupListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertTrue(localeSet.contains(Locale.ENGLISH));
        assertTrue(localeSet.contains(Locale.FRENCH));
    }

    @Test
    public void testIsAvailableLocaleWithAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocaleWithUnavailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es")));
    }

    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void testLanguagesByCountryWithValidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }

    @Test
    public void testCountriesByLanguageWithValidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertTrue(countries.contains(new Locale("en", "US")));
        assertTrue(countries.contains(new Locale("en", "GB")));
    }
}