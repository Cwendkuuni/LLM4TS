import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Edge cases
        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("", "es");
        actual = LocaleUtils.toLocale("");
        assertEquals(expected, actual);

        expected = new Locale("en", "GB", "London");
        actual = LocaleUtils.toLocale("en_GB_London");
        assertEquals(expected, actual);

        // Exception scenarios
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US__");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality
        Locale locale = new Locale("fr", "CA", "Vancouver");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = Arrays.asList(
                locale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.getDefault()
        );
        assertEquals(expected, result);

        // Edge cases
        locale = new Locale("es_ES");
        result = LocaleUtils.localeLookupList(locale, new Locale("en"));
        expected = Arrays.asList(
                locale,
                new Locale("es"),
                Locale.getDefault()
        );
        assertEquals(expected, result);

        locale = null;
        result = LocaleUtils.localeLookupList(locale, new Locale("es"));
        assertEquals(Collections.emptyList(), result);

        // Exception scenarios
        try {
            LocaleUtils.localeLookupList(null, new Locale("es"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("en"), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, result);

        // Edge cases
        Set<Locale> set = new HashSet<Locale>(LocaleUtils.availableLocaleSet());
        assertEquals(Locale.getAvailableLocales().length, set.size());

        // Exception scenarios
        try {
            LocaleUtils.availableLocaleList();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "Vancouver");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge cases
        locale = new Locale("", "es");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        // Exception scenarios
        try {
            LocaleUtils.isAvailableLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality
        String countryCode = "CA";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        assertEquals(expected, result);

        // Edge cases
        countryCode = "JP";
        result = LocaleUtils.languagesByCountry(countryCode);
        expected = Arrays.asList(
                new Locale("ja", "JP")
        );
        assertEquals(expected, result);

        countryCode = "";
        result = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(Collections.emptyList(), result);

        // Exception scenarios
        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality
        String languageCode = "fr";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(
                new Locale("ca", "CA"),
                new Locale("ch", "CH"),
                new Locale("fr", "BE"),
                new Locale("fr", "CA"),
                new Locale("fr", "CH"),
                new Locale("fr", "FR")
        );
        assertEquals(expected, result);

        // Edge cases
        languageCode = "es";
        result = LocaleUtils.countriesByLanguage(languageCode);
        expected = Arrays.asList(
                new Locale("ar", "EG"),
                new Locale("ar", "SA"),
                new Locale("es", "AR"),
                new Locale("es", "BO"),
                new Locale("es", "CL"),
                new Locale("es", "CO"),
                new Locale("es", "CR"),
                new Locale("es", "CU"),
                new Locale("es", "DO"),
                new Locale("es", "EC"),
                new Locale("es", "ES"),
                new Locale("es", "GT"),
                new Locale("es", "HN"),
                new Locale("es", "MX"),
                new Locale("es", "NI"),
                new Locale("es", "PA"),
                new Locale("es", "PE"),
                new Locale("es", "PR"),
                new Locale("es", "PY"),
                new Locale("es", "SV"),
                new Locale("es", "US"),
                new Locale("es", "UY"),
                new Locale("es", "VE"),
                new Locale("es", "GT"),
                new Locale("es", "HN")
        );
        assertEquals(expected, result);

        languageCode = "";
        result = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(Collections.emptyList(), result);

        // Exception scenarios
        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}