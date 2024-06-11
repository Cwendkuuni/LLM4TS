import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test case
        String str = "en_GB";
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale(str);
        assertEquals(expected, actual);

        // Null input test case
        str = null;
        expected = null;
        actual = LocaleUtils.toLocale(str);
        assertEquals(expected, actual);

        // Invalid locale format test case
        str = "en-Gb";
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale(str);
        });
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test case
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            Locale.getDefault()
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Null locale test case
        locale = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test case
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic test case
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test case
        Locale locale = new Locale("fr", "CA");
        boolean expected = true;
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        assertEquals(expected, actual);

        // Locale not available test case
        locale = new Locale("xx", "YY");
        expected = false;
        actual = LocaleUtils.isAvailableLocale(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test case
        String countryCode = "CA";
        List<Locale> expected = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Null country code test case
        countryCode = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test case
        String languageCode = "en";
        List<Locale> expected = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Null language code test case
        languageCode = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}