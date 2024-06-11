import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;

import java.util.Locale;
import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // typical use case
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));

        // edge cases
        assertEquals(null, LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_error() {
        // error scenarios
        LocaleUtils.toLocale("en#US");
    }

    @Test
    public void testLocaleLookupList() {
        // typical use case
        List<Locale> expected = Arrays.asList(Locale.US, new Locale("en", ""), new Locale("", ""), Locale.GERMANY);
        List<Locale> result = LocaleUtils.localeLookupList(Locale.US, Locale.GERMANY);
        assertEquals(expected, result);

        // edge cases
        result = LocaleUtils.localeLookupList(Locale.US, null);
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        // typical use case
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.GERMANY));

        // edge cases
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        // typical use case
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.US));
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.GERMANY));

        // edge cases
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // typical use case
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

        // edge cases
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        // typical use case
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(Arrays.asList(Locale.US, Locale.ENGLISH, Locale.ENGLISH), result);

        // edge cases
        result = LocaleUtils.languagesByCountry("");
        assertEquals(Arrays.asList(), result);

        // error scenarios
        result = LocaleUtils.languagesByCountry(null);
        assertEquals(Arrays.asList(), result);
    }

    @Test
    public void testCountriesByLanguage() {
        // typical use case
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(Arrays.asList(Locale.US, Locale.CANADA, Locale.UK), result);

        // edge cases
        result = LocaleUtils.countriesByLanguage("");
        assertEquals(Arrays.asList(), result);

        // error scenarios
        result = LocaleUtils.countriesByLanguage(null);
        assertEquals(Arrays.asList(), result);
    }
}