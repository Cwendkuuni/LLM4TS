import org.junit.Test;

import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("enGB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode() {
        LocaleUtils.toLocale("EN_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode() {
        LocaleUtils.toLocale("en_GB!");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        List expectedList = new ArrayList();
        expectedList.add(locale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(defaultLocale);
        
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        Locale defaultLocale = new Locale("en");

        List expectedList = new ArrayList();
        expectedList.add(defaultLocale);

        assertEquals(expectedList, LocaleUtils.localeLookupList(null, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List locales = LocaleUtils.availableLocaleList();

        assertNotNull(locales);
        assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set locales = LocaleUtils.availableLocaleSet();

        assertNotNull(locales);
        assertFalse(locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List languages = LocaleUtils.languagesByCountry("CA");

        assertNotNull(languages);
        assertFalse(languages.isEmpty());
    }

    @Test
    public void testLanguagesByCountryNullCountryCode() {
        List languages = LocaleUtils.languagesByCountry(null);

        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List countries = LocaleUtils.countriesByLanguage("en");

        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        List countries = LocaleUtils.countriesByLanguage(null);

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }
}