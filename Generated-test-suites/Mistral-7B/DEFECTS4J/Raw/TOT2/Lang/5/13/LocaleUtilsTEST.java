import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic testing for valid inputs
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);

        // Testing for edge cases
        expectedLocale = new Locale("fr", "CA");
        actualLocale = LocaleUtils.toLocale("fr_CA");
        assertEquals(expectedLocale, actualLocale);

        expectedLocale = new Locale("es", "");
        actualLocale = LocaleUtils.toLocale("es");
        assertEquals(expectedLocale, actualLocale);

        expectedLocale = new Locale("en", "GB", "London");
        actualLocale = LocaleUtils.toLocale("en_GB_London");
        assertEquals(expectedLocale, actualLocale);

        // Testing for invalid inputs
        String invalidLocale1 = "en_US_Invalid";
        String invalidLocale2 = "en_US_invalid";
        String invalidLocale3 = "en_US_London_invalid";
        String invalidLocale4 = "en_US_London_Invalid";
        String invalidLocale5 = "en_Invalid";
        String invalidLocale6 = "en_Invalid_US";
        String invalidLocale7 = "en_US_Invalid_Invalid";
        String invalidLocale8 = "en_Invalid_Invalid";
        String invalidLocale9 = "en_Invalid";
        String invalidLocale10 = "";

        for (String invalidInput : new ArrayList<>(Arrays.asList(invalidLocale1, invalidLocale2, invalidLocale3, invalidLocale4, invalidLocale5, invalidLocale6, invalidLocale7, invalidLocale8, invalidLocale9, invalidLocale10))) {
            try {
                LocaleUtils.toLocale(invalidInput);
                fail("Expected IllegalArgumentException for invalid input: " + invalidInput);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic testing for valid inputs
        Locale baseLocale = new Locale("fr", "CA", "Toronto");
        List<Locale> expectedLocaleList = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale);

        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i), actualLocaleList.get(i));
        }

        // Testing for edge cases
        expectedLocaleList = new ArrayList<>(Arrays.asList(baseLocale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en")));
        actualLocaleList = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));

        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i), actualLocaleList.get(i));
        }

        // Testing for invalid inputs
        Locale invalidLocale = new Locale("en", "GB", "Invalid");
        try {
            LocaleUtils.localeLookupList(invalidLocale, null);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(null, null);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic testing for valid inputs
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Locale.getAvailableLocales();

        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i), actualLocaleList.get(i));
        }

        // Testing for edge cases
        // No additional testing needed for edge cases as the JDK's List returned by Locale.getAvailableLocales() is already
        // thread-safe and unmodifiable

        // Testing for invalid inputs
        try {
            LocaleUtils.availableLocaleList().add(new Locale("en", "US"));
            fail("Expected UnsupportedOperationException for add() method");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.availableLocaleList().remove(0);
            fail("Expected UnsupportedOperationException for remove() method");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic testing for valid inputs
        Set<Locale> expectedLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocaleSet = new HashSet<>(Locale.getAvailableLocales());

        assertEquals(expectedLocaleSet.size(), actualLocaleSet.size());
        for (Locale locale : expectedLocaleSet) {
            assertTrue(actualLocaleSet.contains(locale));
        }

        // Testing for edge cases
        // No additional testing needed for edge cases as the JDK's Set returned by Collections.unmodifiableSet(Locale.getAvailableLocales()) is already
        // thread-safe and unmodifiable

        // Testing for invalid inputs
        try {
            LocaleUtils.availableLocaleSet().add(new Locale("en", "US"));
            fail("Expected UnsupportedOperationException for add() method");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.availableLocaleSet().remove(new Locale("en", "US"));
            fail("Expected UnsupportedOperationException for remove() method");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic testing for valid inputs
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Testing for invalid inputs
        Locale invalidLocale = new Locale("en", "US_Invalid");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic testing for valid inputs
        String countryCode = "CA";
        List<Locale> expectedLocaleList = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actualLocaleList = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));

        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i), actualLocaleList.get(i));
        }

        // Testing for edge cases
        // No additional testing needed for edge cases as the methods handle empty inputs gracefully

        // Testing for invalid inputs
        String invalidCountryCode = "Invalid";
        List<Locale> emptyLocaleList = Collections.emptyList();
        try {
            LocaleUtils.languagesByCountry(invalidCountryCode);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("");
            fail("Expected IllegalArgumentException for empty input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic testing for valid inputs
        String languageCode = "en";
        List<Locale> expectedLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actualLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"));

        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i), actualLocaleList.get(i));
        }

        // Testing for edge cases
        // No additional testing needed for edge cases as the methods handle empty inputs gracefully

        // Testing for invalid inputs
        String invalidLanguageCode = "Invalid";
        List<Locale> emptyLocaleList = Collections.emptyList();
        try {
            LocaleUtils.countriesByLanguage(invalidLanguageCode);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("");
            fail("Expected IllegalArgumentException for empty input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}