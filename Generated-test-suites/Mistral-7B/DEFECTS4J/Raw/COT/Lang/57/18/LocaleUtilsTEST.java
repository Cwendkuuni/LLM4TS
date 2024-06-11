import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("fr");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(expected, actual);

        // Test invalid locale strings
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            LocaleUtils.toLocale("en_GB_invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            LocaleUtils.toLocale("en_gb_invalid"); // lowercase 'B'
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            LocaleUtils.toLocale("en_GB_invalid_separator"); // invalid separator
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            LocaleUtils.toLocale("en_GB_invalid_length"); // incorrect length
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            LocaleUtils.toLocale("en_GB_invalid_language_case"); // language code not lowercase
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            LocaleUtils.toLocale("en_GB_invalid_country_case"); // country code not uppercase
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        Locale defaultLocale = new Locale("en");
        List actual = LocaleUtils.localeLookupList(null, defaultLocale);
        List expected = Arrays.asList(defaultLocale);
        assertEquals(expected, actual);

        // Test single locale
        Locale locale = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        expected = Arrays.asList(locale, defaultLocale);
        assertEquals(expected, actual);

        // Test multiple locales with same language and country
        locale = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("fr", "CA");
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        expected = Arrays.asList(locale, locale2, defaultLocale);
        assertEquals(expected, actual);

        // Test multiple locales with different languages and countries
        locale = new Locale("fr", "CA", "xxx");
        Locale locale3 = new Locale("es", "ES");
        actual = LocaleUtils.localeLookupList(locale, defaultLocale, locale3);
        expected = Arrays.asList(locale, locale2, locale, defaultLocale, locale3);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List actual = LocaleUtils.availableLocaleList();
        List expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set actual = LocaleUtils.availableLocaleSet();
        Set expected = new HashSet(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test invalid locale
        locale = new Locale("invalid", "invalid");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        List actual = LocaleUtils.languagesByCountry(null);
        List expected = Collections.emptyList();
        assertEquals(expected, actual);

        // Test valid country
        String countryCode = "CA";
        actual = LocaleUtils.languagesByCountry(countryCode);
        expected = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        List actual = LocaleUtils.countriesByLanguage(null);
        List expected = Collections.emptyList();
        assertEquals(expected, actual);

        // Test valid language
        String languageCode = "fr";
        actual = LocaleUtils.countriesByLanguage(languageCode);
        expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "FR"));
        assertEquals(expected, actual);
    }
}