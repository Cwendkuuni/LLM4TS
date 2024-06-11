import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validFormat() {
        // Typical use case
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);

        // Edge case
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);

        // Error scenario
        try {
            LocaleUtils.toLocale("en_gb");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }

    @Test
    public void testToLocale_nullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList_withLocale() {
        // Typical use case
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(4, list1.size());
        assertEquals(locale1, list1.get(0));
        assertEquals(new Locale("fr", "CA"), list1.get(1));
        assertEquals(new Locale("fr", ""), list1.get(2));
        assertEquals(Locale.getDefault(), list1.get(3));

        // Edge case where no default locale is provided
        Locale locale2 = new Locale("en");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2, null);
        assertEquals(3, list2.size());
        assertEquals(locale2, list2.get(0));
        assertEquals(new Locale("en", ""), list2.get(1));
        assertEquals(new Locale("en", "GB"), list2.get(2));
    }

    @Test
    public void testLocaleLookupList_nullInput() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertEquals(0, list.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.contains(Locale.getDefault()));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertTrue(set.contains(Locale.getDefault()));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.getDefault()));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        // Typical use case
        List<Locale> list = LocaleUtils.languagesByCountry("FR");
        assertEquals(1, list.size());
        assertEquals(new Locale("fr", ""), list.get(0));
    }

    @Test
    public void testLanguagesByCountry_invalidCountryCode() {
        // Error scenario
        List<Locale> list = LocaleUtils.languagesByCountry("XYZ");
        assertTrue(list.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_nullInput() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        // Typical use case
        List<Locale> list = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, list.size());
        assertEquals(new Locale("fr", "CA"), list.get(0));
    }

    @Test
    public void testCountriesByLanguage_invalidLanguageCode() {
        // Error scenario
        List<Locale> list = LocaleUtils.countriesByLanguage("xyz");
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_nullInput() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertTrue(list.isEmpty());
    }
}