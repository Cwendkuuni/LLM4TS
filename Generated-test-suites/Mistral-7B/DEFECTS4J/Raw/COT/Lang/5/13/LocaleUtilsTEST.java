import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = {"en", "en_US", "en_GB", "fr_FR", "fr_CA", "es_ES", "es_MX"};
        for (String testCase : testCases) {
            Locale expected = new Locale(testCase.substring(0, 2), testCase.substring(3));
            if (testCase.length() > 5) {
                expected = new Locale(testCase.substring(0, 2), testCase.substring(3, 5), testCase.substring(6));
            }
            Locale actual = LocaleUtils.toLocale(testCase);
            assertEquals(expected.getDisplayName(), actual.getDisplayName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleFormat() {
        LocaleUtils.toLocale("en_GB_xx"); // invalid variant length
        LocaleUtils.toLocale("en_GB_xx_xx"); // multiple variants
        LocaleUtils.toLocale("en_GB_xx_xx_xx"); // too many parts
        LocaleUtils.toLocale("en_GB_xx_xx_xx_xx"); // too many parts
        LocaleUtils.toLocale("en_GB_xx_xx_xx_xx_xx"); // too many parts
    }

    @Test
    public void testLocaleLookupList() {
        Locale defaultLocale = new Locale("en");
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupListDefault() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertTrue(actual.size() >= 3);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        // TODO: Implement test for specific locales
        assertEquals(true, actual.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        // TODO: Implement test for specific locales
        assertEquals(true, actual.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("fr", "FR");
        Locale locale3 = new Locale("es", "MX");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        assertTrue(LocaleUtils.isAvailableLocale(locale2));
        assertTrue(LocaleUtils.isAvailableLocale(locale3));
        Locale unavailableLocale = new Locale("non-existent", " locale");
        assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}