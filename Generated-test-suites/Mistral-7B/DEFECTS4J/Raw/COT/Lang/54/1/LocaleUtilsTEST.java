import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] tests = new String[]{"en", "en_GB", "en_GB_XXX", "fr", "fr_CA", "fr_CA_XXX", "es_ES"};
        Locale[] expected = new Locale[]{new Locale("en", ""), new Locale("en", "GB"), new Locale("en", "GB", "XXX"),
                new Locale("fr", ""), new Locale("fr", "CA"), new Locale("fr", "CA", "XXX"), new Locale("es", "ES")};

        for (int i = 0; i < tests.length; i++) {
            Locale actual = LocaleUtils.toLocale(tests[i]);
            assertEquals("Invalid result for test:", expected[i], actual);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_XXX_XXX");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "XXX");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr", ""),
                new Locale("en"));

        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals("Invalid result for test:", expected, result);

        // Test with null input
        result = LocaleUtils.localeLookupList(null);
        assertEquals("Invalid result for test with null input:", Collections.EMPTY_LIST, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        // Check the size of the list
        assertTrue("Invalid size of available locales list:", result.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        // Check the size of the set
        assertTrue("Invalid size of available locales set:", result.size() > 0);

        // Check if the set contains an element
        assertTrue("Locale not present in the set:", result.contains(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with available locale
        assertTrue("Locale should be available:", LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Test with an invalid locale
        assertFalse("Locale should not be available:", LocaleUtils.isAvailableLocale(new Locale("foo", "bar")));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "US"));

        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertEquals("Invalid result for test:", expected, result);

        // Test with null input
        result = LocaleUtils.languagesByCountry(null);
        assertEquals("Invalid result for test with null input:", Collections.EMPTY_LIST, result);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"),
                new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "ZA"));

        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals("Invalid result for test:", expected, result);

        // Test with null input
        result = LocaleUtils.countriesByLanguage(null);
        assertEquals("Invalid result for test with null input:", Collections.EMPTY_LIST, result);
    }
}