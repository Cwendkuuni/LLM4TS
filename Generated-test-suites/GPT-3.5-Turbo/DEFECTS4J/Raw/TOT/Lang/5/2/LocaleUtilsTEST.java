import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(Locale.ENGLISH, locale);

        locale = LocaleUtils.toLocale("en_GB");
        assertEquals(Locale.UK, locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("en_GB_xx");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);

        assertEquals(3, locales.size());
        assertEquals(locale, locales.get(0));
        assertEquals(new Locale("fr", "CA"), locales.get(1));
        assertEquals(new Locale("fr"), locales.get(2));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(4, locales.size());
        assertEquals(locale, locales.get(0));
        assertEquals(new Locale("fr", "CA"), locales.get(1));
        assertEquals(new Locale("fr"), locales.get(2));
        assertEquals(defaultLocale, locales.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertFalse(locales.isEmpty());
        assertTrue(locales.contains(Locale.ENGLISH));
    }

    @Test
    public void testAvailableLocaleSet() {
        ConcurrentMap<String, List<Locale>> countriesByLanguage = LocaleUtils.getCountriesByLanguage();
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(locales.size(), countriesByLanguage.size());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abc")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertNotNull(languages);
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(Locale.FRENCH));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("fr", "CA")));
    }
}