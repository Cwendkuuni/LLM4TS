import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("en");
        actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        expected = null;
        actual = LocaleUtils.toLocale(null);
        assertEquals(expected, actual);

        String invalidFormat = "xx_yy_zzz";
        try {
            actual = LocaleUtils.toLocale(invalidFormat);
            fail("Should have thrown IllegalArgumentException for invalid format '" + invalidFormat + "'");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        invalidFormat = "xx_yy";
        try {
            actual = LocaleUtils.toLocale(invalidFormat);
            fail("Should have thrown IllegalArgumentException for invalid format '" + invalidFormat + "'");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        invalidFormat = "xx_yy_";
        try {
            actual = LocaleUtils.toLocale(invalidFormat);
            fail("Should have thrown IllegalArgumentException for invalid format '" + invalidFormat + "'");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        invalidFormat = "xx_yy_zz";
        try {
            actual = LocaleUtils.toLocale(invalidFormat);
            fail("Should have thrown IllegalArgumentException for invalid format '" + invalidFormat + "'");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xx");
        List<Locale> expected = LocaleUtils.localeLookupList(locale);
        List<Locale> actual = new ArrayList<>();
        actual.add(locale);
        actual.add(new Locale("fr", "CA"));
        actual.add(new Locale("fr", ""));
        actual.add(new Locale("en"));
        assertEquals(expected, actual);

        locale = new Locale("en");
        expected.clear();
        expected.add(locale);
        actual = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expected, actual);

        locale = null;
        expected.clear();
        actual = LocaleUtils.localeLookupList(locale, null);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("fr", "CA"));

        String countryCode = "US";
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        countryCode = "CA";
        expected.clear();
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("en", "CA"));
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        countryCode = "GB";
        expected.clear();
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("sc", "GB"));
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        countryCode = null;
        expected.clear();
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("US", "en"));
        expected.add(new Locale("CA", "fr"));

        String languageCode = "en";
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        languageCode = "fr";
        expected.clear();
        expected.add(new Locale("CA", "fr"));
        expected.add(new Locale("CH", "fr"));
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        languageCode = "es";
        expected.clear();
        expected.add(new Locale("ES", "es"));
        expected.add(new Locale("MX", "es"));
        expected.add(new Locale("US", "es"));
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        languageCode = null;
        expected.clear();
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(Collections.emptyList(), actual);
    }
}