import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case: converting a valid locale String with language and country
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Edge case: converting a valid locale String with language and country and variant
        expected = new Locale("en", "GB", "xxx");
        actual = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(expected, actual);

        // Error scenario: converting an invalid locale String with invalid format
        try {
            LocaleUtils.toLocale("english_gb");
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case: creating a list of locales with a starting locale and a default locale
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);

        // Edge case: creating a list of locales with null starting locale and a default locale
        expected = new ArrayList<>();
        expected.add(defaultLocale);
        actual = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case: obtaining a list of available locales
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case: obtaining a set of available locales
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case: checking if an available locale is known
        boolean expected = true;
        boolean actual = LocaleUtils.isAvailableLocale(Locale.CANADA);
        assertEquals(expected, actual);

        // Edge case: checking if an unavailable locale is known
        expected = false;
        actual = LocaleUtils.isAvailableLocale(new Locale("xx", "XX"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case: obtaining a list of languages supported for a given country
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);

        // Edge case: obtaining a list of languages supported for a non-existent country
        expected = Collections.EMPTY_LIST;
        actual = LocaleUtils.languagesByCountry("XX");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case: obtaining a list of countries supported for a given language
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("US"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);

        // Edge case: obtaining a list of countries supported for a non-existent language
        expected = Collections.EMPTY_LIST;
        actual = LocaleUtils.countriesByLanguage("xx");
        assertEquals(expected, actual);
    }

}