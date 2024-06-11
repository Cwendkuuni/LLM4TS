import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.CHINA, LocaleUtils.toLocale("zh_CN"));
        assertEquals(new Locale("en", "", "BAZ"), LocaleUtils.toLocale("en__BAZ"));
        assertEquals(new Locale("en", "US", "BAZ"), LocaleUtils.toLocale("en_US_BAZ"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("enB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat3() {
        LocaleUtils.toLocale("en__");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat4() {
        LocaleUtils.toLocale("en_US_");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList1 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(4, localeList1.size());
        assertEquals(new Locale("fr", "CA", "xxx"), localeList1.get(0));
        assertEquals(new Locale("fr", "CA"), localeList1.get(1));
        assertEquals(new Locale("fr"), localeList1.get(2));
        assertEquals(Locale.getDefault(), localeList1.get(3));

        List<Locale> localeList2 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(4, localeList2.size());
        assertEquals(new Locale("fr", "CA", "xxx"), localeList2.get(0));
        assertEquals(new Locale("fr", "CA"), localeList2.get(1));
        assertEquals(new Locale("fr"), localeList2.get(2));
        assertEquals(new Locale("en"), localeList2.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertFalse(availableLocales.isEmpty());
        assertTrue(availableLocales.contains(Locale.US));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertFalse(availableLocales.isEmpty());
        assertTrue(availableLocales.contains(Locale.US));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(Locale.ENGLISH));
    }

    @Test
    public void testLanguagesByCountryNull() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(Locale.US));
    }

    @Test
    public void testCountriesByLanguageNull() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
}