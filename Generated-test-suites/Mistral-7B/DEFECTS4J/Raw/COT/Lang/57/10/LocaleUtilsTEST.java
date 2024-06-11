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

        // Edge case: empty string
        assertNull(LocaleUtils.toLocale(""));

        // Edge case: invalid length
        try {
            LocaleUtils.toLocale("en_");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxxxxxxxxxxxxxxxx");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale expected = new Locale("fr", "CA", "xxx");
        List<Locale> actual = LocaleUtils.localeLookupList(expected);
        List<Locale> expectedList = Arrays.asList(expected, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList, actual);

        // Edge case: null input
        List<Locale> actualEmpty = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), actualEmpty);

        // Edge case: empty input
        Locale emptyLocale = new Locale("");
        List<Locale> actualEmptyLocale = LocaleUtils.localeLookupList(emptyLocale);
        assertEquals(Collections.singletonList(emptyLocale), actualEmptyLocale);

        // Edge case: defaultLocale is the same as inputLocale
        Locale defaultLocale = new Locale("en");
        List<Locale> actualSame = LocaleUtils.localeLookupList(defaultLocale, defaultLocale);
        assertEquals(Collections.singletonList(defaultLocale), actualSame);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales().toArray(new Locale[0]));
        assertEquals(expected, actual);

        // Edge case: empty list
        List<Locale> actualEmpty = LocaleUtils.availableLocaleList();
        assertEquals(Collections.emptyList(), actualEmpty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAvailableLocaleWithInvalidLocale() {
        // Test with an invalid Locale
        Locale invalidLocale = new Locale("en", "GB", "invalid");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "CA";
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        assertEquals(expected, actual);

        // Edge case: null input
        String nullCountryCode = null;
        List<Locale> actualEmpty = LocaleUtils.languagesByCountry(nullCountryCode);
        assertEquals(Collections.emptyList(), actualEmpty);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "fr";
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "CH"));
        assertEquals(expected, actual);

        // Edge case: null input
        String nullLanguageCode = null;
        List<Locale> actualEmpty = LocaleUtils.countriesByLanguage(nullLanguageCode);
        assertEquals(Collections.emptyList(), actualEmpty);
    }
}