import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
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
            LocaleUtils.toLocale("en_US_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_yy");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_yy_zz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_.zz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_zz.");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_zz_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_zz_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_zz_xx_yy");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_zz_xx_yy_zz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_zz_xx_yy_zz_zz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList, localeList);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList("");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("fr", "CA", ""));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("fr", "", ""));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("fr", "CA", ""));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("", "CA", ""));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("en"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedAvailableLocaleList, availableLocaleList);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleList(null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.availableLocaleList("");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedAvailableLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expectedAvailableLocaleSet, availableLocaleSet);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleSet(null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.availableLocaleSet("");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.isAvailableLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale(new Locale("en", "US_"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale(new Locale("en", "US_xx"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale(new Locale("en", "US_xx_yy"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale(new Locale("en", "US_xx_yy_zz"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale(new Locale("en", "US_xx_yy_zz_zz"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedLanguagesByCountry = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "US"));
        assertEquals(expectedLanguagesByCountry, languagesByCountry);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("xx");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguage = Arrays.asList(new Locale("US", "en"), new Locale("CA", "en"), new Locale("GB", "en"));
        assertEquals(expectedCountriesByLanguage, countriesByLanguage);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("xx");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}