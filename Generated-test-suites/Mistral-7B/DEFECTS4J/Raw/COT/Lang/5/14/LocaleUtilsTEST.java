import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test case
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);

        // Test null input
        String nullLocaleString = null;
        Locale nullLocale = LocaleUtils.toLocale(nullLocaleString);
        assertNull(nullLocale);

        // Test empty string input
        String emptyLocaleString = "";
        Locale emptyLocale = LocaleUtils.toLocale(emptyLocaleString);
        assertNull(emptyLocale);

        // Test invalid format
        String invalidLocaleString = "en_US_Invalid";
        Locale invalidLocale = LocaleUtils.toLocale(invalidLocaleString);
        assertNull(invalidLocale);
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test case
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Test null input
        Locale nullLocale = null;
        List<Locale> nullLocaleList = LocaleUtils.localeLookupList(nullLocale);
        assertTrue(nullLocaleList.isEmpty());

        // Test empty list input
        List<Locale> emptyLocaleList = Collections.emptyList();
        Locale emptyDefaultLocale = new Locale("en");
        List<Locale> actualEmptyLocaleList = LocaleUtils.localeLookupList(emptyLocaleList, emptyDefaultLocale);
        assertTrue(actualEmptyLocaleList.isEmpty());

        // Test default locale only input
        Locale defaultLocale = new Locale("en");
        List<Locale> actualDefaultLocaleList = LocaleUtils.localeLookupList(defaultLocale);
        assertTrue(actualDefaultLocaleList.contains(defaultLocale));
    }

    @Test
    public void testAvailableLocaleListAndSet() {
        // Basic test case
        List<Locale> expectedLocaleList = Locale.getAvailableLocales();
        Set<Locale> expectedLocaleSet = new HashSet<>(expectedLocaleList);
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        Set<Locale> actualLocaleSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedLocaleList, actualLocaleList);
        assertEquals(expectedLocaleSet, actualLocaleSet);

        // Test empty list and set
        List<Locale> emptyLocaleList = Collections.emptyList();
        Set<Locale> emptyLocaleSet = Collections.emptySet();
        assertTrue(LocaleUtils.availableLocaleList().isEmpty());
        assertTrue(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleStringFormat() {
        String invalidLocaleString = "en_US_Invalid";
        LocaleUtils.toLocale(invalidLocaleString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLanguageCodeFormat() {
        String invalidLocaleString = "en_US_aa";
        LocaleUtils.toLocale(invalidLocaleString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountryCodeFormat() {
        String invalidLocaleString = "en_us_AA";
        LocaleUtils.toLocale(invalidLocaleString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSeparator() {
        String invalidLocaleString = "en_US:xx";
        LocaleUtils.toLocale(invalidLocaleString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleLength() {
        String invalidLocaleString = "en_US_xxxx";
        LocaleUtils.toLocale(invalidLocaleString);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("fr", "CA");
        Locale locale3 = new Locale("fr");
        Locale locale4 = new Locale("en");

        List<Locale> localeList = Arrays.asList(locale1, locale2, locale3, locale4);
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale1);

        for (Locale locale : localeList) {
            boolean actualResult = LocaleUtils.isAvailableLocale(locale);
            assertTrue(localeLookupList.contains(locale) || actualResult);
        }

        Locale invalidLocale = new Locale("fr", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";

        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA")
        );

        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        String emptyCountryCode = "";
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> actualEmptyLocaleList = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertTrue(actualEmptyLocaleList.isEmpty());

        String invalidCountryCode = "XX";
        List<Locale> actualInvalidLocaleList = LocaleUtils.languagesByCountry(invalidCountryCode);
        assertTrue(actualInvalidLocaleList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";

        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "BE"),
                new Locale("fr", "CH")
        );

        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        String emptyLanguageCode = "";
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> actualEmptyLocaleList = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertTrue(actualEmptyLocaleList.isEmpty());

        String invalidLanguageCode = "XX";
        List<Locale> actualInvalidLocaleList = LocaleUtils.countriesByLanguage(invalidLanguageCode);
        assertTrue(actualInvalidLocaleList.isEmpty());
    }
}