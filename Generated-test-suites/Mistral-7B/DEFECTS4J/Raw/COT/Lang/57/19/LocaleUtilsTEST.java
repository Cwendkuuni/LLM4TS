import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] validInputs = new String[]{"en", "en_GB", "en_GB_US"};
        for (String input : validInputs) {
            Locale expected = new Locale(input.substring(0, 2), input.substring(3));
            Locale actual = LocaleUtils.toLocale(input);
            assertEquals(expected.getLanguage(), actual.getLanguage());
            assertEquals(expected.getCountry(), actual.getCountry());
        }

        // Test invalid input
        String[] invalidInputs = new String[]{"en_GB_xxx", "en_invalid", "en_US_invalid"};
        for (String input : invalidInputs) {
            try {
                LocaleUtils.toLocale(input);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
        assertEquals(expectedList, actualList);

        List<Locale> expectedListEmpty = Collections.emptyList();
        List<Locale> actualListEmpty = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(expectedListEmpty, actualListEmpty);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale invalidLocale = new Locale("invalid", "invalid");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", countryCode),
                new Locale("en", countryCode),
                new Locale("es", countryCode)
        );
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);

        String emptyCountryCode = null;
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> actualListEmpty = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertEquals(emptyList, actualListEmpty);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedList = Arrays.asList(
                new Locale(languageCode, "CA"),
                new Locale(languageCode, "BE")
        );
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);

        String emptyLanguageCode = null;
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> actualListEmpty = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertEquals(emptyList, actualListEmpty);
    }
}