import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validInput() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidInput_missingLanguageCode() {
        LocaleUtils.toLocale("_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidInput_missingCountryCode() {
        LocaleUtils.toLocale("en_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidInput_invalidLanguageCode() {
        LocaleUtils.toLocale("XX_YY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidInput_invalidCountryCode() {
        LocaleUtils.toLocale("en_xx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidInput_missingSeparator() {
        LocaleUtils.toLocale("enGB");
    }

    @Test
    public void testLocaleLookupList_validInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                locale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.getDefault()
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testLocaleLookupList_nullInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(Collections.isEmpty(actual));
    }

    @Test
    public void testLocaleLookupList_defaultLocaleInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.getDefault());
        assertTrue(Collections.isEmpty(actual));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = Collections.unmodifiableSet(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_validInput() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_invalidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_validInput() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testLanguagesByCountry_emptyInput() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertTrue(Collections.isEmpty(actual));
    }

    @Test
    public void testCountriesByLanguage_validInput() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(
                new Locale("CA"),
                new Locale("GB")
        );
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testCountriesByLanguage_emptyInput() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(Collections.isEmpty(actual));
    }
}