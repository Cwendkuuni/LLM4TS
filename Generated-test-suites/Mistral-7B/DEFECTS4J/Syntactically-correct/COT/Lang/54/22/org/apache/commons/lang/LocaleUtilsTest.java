import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals("Unexpected locale returned", expected, actual);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB__");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GBxxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX_YYY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX_YYY_ZZZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB.XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX.YYY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX.YYY.ZZZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Base functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals("Unexpected list of locales returned", expected, actual);

        // Edge cases and exception handling tests
        Locale defaultLocale = new Locale("en");
        Locale expectedDefaultLocale = new Locale("en");
        List<Locale> actualDefaultLocale = LocaleUtils.localeLookupList(null);
        assertEquals("Unexpected list of locales returned for null input", Collections.singletonList(expectedDefaultLocale), actualDefaultLocale);

        List<Locale> actualEmptyList = LocaleUtils.localeLookupList(Locale.ROOT);
        assertEquals("Unexpected list of locales returned for empty list", Collections.EMPTY_LIST, actualEmptyList);

        List<Locale> actualEmptyListDefault = LocaleUtils.localeLookupList(Locale.ROOT, defaultLocale);
        assertEquals("Unexpected list of locales returned for empty list and default locale", Arrays.asList(defaultLocale), actualEmptyListDefault);

        List<Locale> actualLocaleWithNoCountry = LocaleUtils.localeLookupList(new Locale("fr", "", ""));
        assertEquals("Unexpected list of locales returned for locale with empty country", Arrays.asList(new Locale("fr")), actualLocaleWithNoCountry);

        List<Locale> actualLocaleWithNoLanguage = LocaleUtils.localeLookupList(new Locale("", "CA", ""));
        assertEquals("Unexpected list of locales returned for locale with empty language", Arrays.asList(Locale.ROOT), actualLocaleWithNoLanguage);

        List<Locale> actualLocaleWithCountryAndLanguage = LocaleUtils.localeLookupList(new Locale("fr", "CA"));
        assertEquals("Unexpected list of locales returned for locale with country and language", Arrays.asList(new Locale("fr", "CA"), new Locale("fr")), actualLocaleWithCountryAndLanguage);

        List<Locale> actualLocaleWithLanguage = LocaleUtils.localeLookupList(new Locale("fr"));
        assertEquals("Unexpected list of locales returned for locale with language", Arrays.asList(new Locale("fr"), Locale.ROOT), actualLocaleWithLanguage);
    }

    @Test
    public void testAvailableLocaleList() {
        // Base functionality test
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals("Unexpected list of locales returned", actual, expected);

        // Edge cases and exception handling tests
        List<Locale> actualEmptyList = LocaleUtils.availableLocaleList();
        assertTrue("Unexpected empty list returned", actual.isEmpty());

        List<Locale> actualNullList = LocaleUtils.availableLocaleList(null);
        assertTrue("Unexpected null list returned", actual == null);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Base functionality test
        Set<Locale> expected = new HashSet<>(Locale.getAvailableLocales());
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals("Unexpected set of locales returned", expected, actual);

        // Edge cases and exception handling tests
        Set<Locale> actualEmptySet = LocaleUtils.availableLocaleSet();
        assertTrue("Unexpected empty set returned", actual.isEmpty());

        Set<Locale> actualNullSet = LocaleUtils.availableLocaleSet(null);
        assertTrue("Unexpected null set returned", actual == null);
    }

    @Test
    public void testIsAvailableLocale() {
        // Base functionality test
        Locale locale = new Locale("fr", "CA", "");
        assertTrue("Locale should be available", LocaleUtils.isAvailableLocale(locale));

        // Edge cases and exception handling tests
        Locale localeNull = null;
        assertFalse("Null locale should not be available", LocaleUtils.isAvailableLocale(localeNull));

        Locale localeEmpty = Locale.ROOT;
        assertFalse("Empty locale should not be available", LocaleUtils.isAvailableLocale(localeEmpty));

        Locale localeUnknown = new Locale("xx", "XX");
        assertFalse("Unknown locale should not be available", LocaleUtils.isAvailableLocale(localeUnknown));
    }

    @Test
    public void testLanguagesByCountry() {
        // Base functionality test
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(Locale.CANADA, new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals("Unexpected list of locales returned", expected, actual);

        // Edge cases and exception handling tests
        String emptyCountryCode = "";
        List<Locale> actualEmptyList = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertTrue("Unexpected empty list returned", actual.isEmpty());

        String nullCountryCode = null;
        List<Locale> actualNullList = LocaleUtils.languagesByCountry(nullCountryCode);
        assertTrue("Unexpected null list returned", actual == null);
    }

    @Test
    public void testCountriesByLanguage() {
        // Base functionality test
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(Locale.FRANCE, new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals("Unexpected list of locales returned", expected, actual);

        // Edge cases and exception handling tests
        String emptyLanguageCode = "";
        List<Locale> actualEmptyList = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertTrue("Unexpected empty list returned", actual.isEmpty());

        String nullLanguageCode = null;
        List<Locale> actualNullList = LocaleUtils.countriesByLanguage(nullLanguageCode);
        assertTrue("Unexpected null list returned", actual == null);
    }
}