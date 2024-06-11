import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String str = "en";
        Locale expected = new Locale("en", "");
        Locale actual = LocaleUtils.toLocale(str);
        assertEquals(expected, actual);

        str = "en_GB";
        expected = new Locale("en", "GB");
        actual = LocaleUtils.toLocale(str);
        assertEquals(expected, actual);

        str = "en_GB_xxx";
        expected = new Locale("en", "GB", "xxx");
        actual = LocaleUtils.toLocale(str);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        String str = "invalid";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = new java.util.ArrayList<>();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        assertNotNull(expected);
        assertFalse(expected.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        assertNotNull(expected);
        assertFalse(expected.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        assertTrue(actual);
    }

    @Test
    public void testIsAvailableLocaleNotAvailable() {
        Locale locale = new Locale("xx", "YY", "zzz");
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        assertFalse(actual);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expected = new java.util.ArrayList<>();
        expected.add(new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountryNullCountryCode() {
        String countryCode = null;
        List<Locale> expected = java.util.Collections.EMPTY_LIST;
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expected = new java.util.ArrayList<>();
        expected.add(new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        String languageCode = null;
        List<Locale> expected = java.util.Collections.EMPTY_LIST;
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}