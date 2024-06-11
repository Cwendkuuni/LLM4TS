import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locales
        Locale expectedLocale1 = new Locale("en", "");
        Locale actualLocale1 = LocaleUtils.toLocale("en");
        assertEquals(expectedLocale1, actualLocale1);

        Locale expectedLocale2 = new Locale("en", "GB");
        Locale actualLocale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(expectedLocale2, actualLocale2);

        Locale expectedLocale3 = new Locale("en", "GB", "xxx");
        Locale actualLocale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(expectedLocale3, actualLocale3);

        // Test empty string
        Locale actualLocale4 = LocaleUtils.toLocale("");
        assertNull(actualLocale4);

        // Test invalid format
        String invalidLocale1 = "en_CA_xxx";
        try {
            Locale actualLocale5 = LocaleUtils.toLocale(invalidLocale1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidLocale2 = "en_CA_aa";
        try {
            Locale actualLocale6 = LocaleUtils.toLocale(invalidLocale2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        Locale defaultLocale = Locale.getDefault();
        List expectedLocaleList1 = Collections.singletonList(defaultLocale);
        List actualLocaleList1 = LocaleUtils.localeLookupList(null);
        assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test non-empty list
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List expectedLocaleList2 = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"));
        List actualLocaleList2 = LocaleUtils.localeLookupList(locale1, defaultLocale);
        assertEquals(expectedLocaleList2, actualLocaleList2);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Locale locale1 = Locale.FRENCH;
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        // Test unavailable locale
        String unavailableLocale = "en_XX";
        assertFalse(LocaleUtils.isAvailableLocale(new Locale(unavailableLocale)));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test languages for a valid country
        String countryCode = "CA";
        List expectedLanguages = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        List actualLanguages = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLanguages, actualLanguages);

        // Test languages for an invalid country
        String invalidCountryCode = "XX";
        List actualLanguages2 = LocaleUtils.languagesByCountry(invalidCountryCode);
        assertTrue(actualLanguages2.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test countries for a valid language
        String languageCode = "fr";
        List expectedCountries = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "FR"));
        List actualCountries = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedCountries, actualCountries);

        // Test countries for an invalid language
        String invalidLanguageCode = "XX";
        List actualCountries2 = LocaleUtils.countriesByLanguage(invalidLanguageCode);
        assertTrue(actualCountries2.isEmpty());
    }
}