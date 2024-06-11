import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Edge case: empty input
        assertNull(LocaleUtils.toLocale(""));

        // Edge case: invalid input length
        try {
            LocaleUtils.toLocale("xx");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge case: invalid input characters
        try {
            LocaleUtils.toLocale("En_GB");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge case: invalid separator
        try {
            LocaleUtils.toLocale("en_GB_XXX");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge case: variant is not empty in JDK1.3
//        Locale expectedJDK13 = new Locale("en", "GB", "EN");
//        Locale actualJDK13 = LocaleUtils.toLocale("en_GB_EN");
//        assertEquals(expectedJDK13, actualJDK13);
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "XXX");
        List expected = LocaleUtils.localeLookupList(locale);
        List actual = new ArrayList();
        actual.add(locale);
        actual.add(new Locale("fr", "CA"));
        actual.add(new Locale("fr", ""));
        actual.add(new Locale("en"));
        assertEquals(actual, expected);

        // Edge case: null input
        Locale defaultLocale = new Locale("en");
        assertEmpty(LocaleUtils.localeLookupList(null, defaultLocale));

        // Edge case: empty list
        List emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.localeLookupList(defaultLocale, emptyList));

        // Edge case: default locale is the same as input locale
        assertEquals(Collections.singletonList(locale), LocaleUtils.localeLookupList(locale, locale));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List expected = LocaleUtils.availableLocaleList();
        List actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(actual, expected);

        // Edge case: empty list
        assertEmpty(LocaleUtils.availableLocaleList());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "XXX");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge case: null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Edge case: invalid locale
        Locale invalidLocale = new Locale("fr", "CA", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));

        // Edge case: empty list
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ROOT));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "CA";
        List expected = LocaleUtils.languagesByCountry(countryCode);
        List actual = Arrays.asList(Locale.getLocales(new String[]{"ca", "en"}));
        assertEquals(actual, expected);

        // Edge case: null input
        List emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.languagesByCountry(null));

        // Edge case: empty list
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));

        // Edge case: no languages available for the country
        String emptyCountryCode = "";
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(emptyCountryCode));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "fr";
        List expected = LocaleUtils.countriesByLanguage(languageCode);
        List actual = Arrays.asList(Locale.getLocales(new String[]{"CA", "BE", "CH"}));
        assertEquals(actual, expected);

        // Edge case: null input
        List emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.countriesByLanguage(null));

        // Edge case: empty list
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));

        // Edge case: no countries available for the language
        String emptyLanguageCode = "";
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(emptyLanguageCode));
    }

    private void assertEmpty(List list) {
        assertTrue(list.isEmpty());
    }
}