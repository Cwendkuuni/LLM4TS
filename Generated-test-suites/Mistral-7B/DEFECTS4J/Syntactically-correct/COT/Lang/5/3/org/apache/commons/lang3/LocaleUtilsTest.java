import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);

        // Edge cases and exception handling tests
        try {
            Locale locale = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale = LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale = LocaleUtils.toLocale("en_GB_xx");
            fail("Expected IllegalArgumentException for invalid variant");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale = LocaleUtils.toLocale("es_ES_xx_XX");
            fail("Expected IllegalArgumentException for invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale = LocaleUtils.toLocale("fr_FR_fr_FR");
            fail("Expected IllegalArgumentException for duplicate separator");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale = LocaleUtils.toLocale("es_ES_es_ES_es_ES");
            fail("Expected IllegalArgumentException for too many components");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale = LocaleUtils.toLocale("es_ES_es_ES_es_ES_es_ES");
            fail("Expected IllegalArgumentException for too many components");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = LocaleUtils.localeLookupList(locale);
        List<Locale> actualLocaleList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        Locale locale1 = null;
        List<Locale> localeList1 = LocaleUtils.localeLookupList(locale1);
        assertTrue(localeList1.isEmpty());

        Locale locale2 = new Locale("fr", "CA");
        List<Locale> localeList2 = LocaleUtils.localeLookupList(locale2, new Locale("en"));
        List<Locale> expectedLocaleList2 = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expectedLocaleList2, localeList2);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedLocaleSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedLocaleSet, localeSet);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        boolean expectedResult = true;
        boolean actualResult = LocaleUtils.isAvailableLocale(locale);
        assertEquals(expectedResult, actualResult);

        // Edge cases and exception handling tests
        Locale locale1 = null;
        boolean result1 = LocaleUtils.isAvailableLocale(locale1);
        assertFalse(result1);

        Locale locale2 = new Locale("fr", "CA");
        boolean result2 = LocaleUtils.isAvailableLocale(locale2);
        assertFalse(result2);

        List<Locale> locales = new ArrayList<>();
        locales.add(locale2);
        boolean result3 = LocaleUtils.isAvailableLocale(locale1, locales.toArray(new Locale[0]));
        assertFalse(result3);
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"), new Locale("es", "CA"));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        String countryCode1 = null;
        List<Locale> localeList1 = LocaleUtils.languagesByCountry(countryCode1);
        assertTrue(localeList1.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "CA"), new Locale("en", "US"), new Locale("en", "GB"));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        String languageCode1 = null;
        List<Locale> localeList1 = LocaleUtils.countriesByLanguage(languageCode1);
        assertTrue(localeList1.isEmpty());
    }
}