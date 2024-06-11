import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        String[] validInputs = {"en", "en_GB", "en_GB_US", "fr_CA"};
        Locale[] expectedOutputs = {new Locale("en"), new Locale("en", "GB"), new Locale("en", "GB", "US"), new Locale("fr", "CA")};
        for (int i = 0; i < validInputs.length; i++) {
            Locale actualOutput = LocaleUtils.toLocale(validInputs[i]);
            assertEquals("Unexpected output for input: " + validInputs[i], expectedOutputs[i], actualOutput);
        }

        // Test invalid inputs
        String[] invalidInputs = {"en_GB_xx", "en_GB_", "en_GB__", "en_GB_xx_"};
        for (String invalidInput : invalidInputs) {
            try {
                LocaleUtils.toLocale(invalidInput);
                fail("Expected IllegalArgumentException for input: " + invalidInput);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }

        // Test null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Test with a valid locale
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedOutput = LocaleUtils.localeLookupList(locale);
        List<Locale> actualOutput = new ArrayList<>();
        actualOutput.add(locale);
        actualOutput.add(new Locale("fr", "CA"));
        actualOutput.add(new Locale("fr"));
        assertEquals("Unexpected output for locale: " + locale, actualOutput, expectedOutput);

        // Test with a null locale
        expectedOutput = Collections.emptyList();
        actualOutput = LocaleUtils.localeLookupList(null);
        assertEquals("Unexpected output for null input", expectedOutput, actualOutput);

        // Test with a default locale
        Locale defaultLocale = new Locale("en");
        expectedOutput = LocaleUtils.localeLookupList(locale, defaultLocale);
        actualOutput = new ArrayList<>();
        actualOutput.add(locale);
        actualOutput.add(new Locale("fr", "CA"));
        actualOutput.add(new Locale("fr"));
        actualOutput.add(defaultLocale);
        assertEquals("Unexpected output for locale and defaultLocale: " + locale + ", " + defaultLocale, actualOutput, expectedOutput);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test getting available locales
        List<Locale> actualOutput = LocaleUtils.availableLocaleList();
        List<Locale> expectedOutput = Locale.getAvailableLocales();
        assertEquals("Unexpected output for availableLocaleList()", expectedOutput, actualOutput);

        // Test empty input
        assertEmpty(LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test getting available locales as a set
        Set<Locale> actualOutput = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedOutput = new HashSet<>(Locale.getAvailableLocales());
        assertEquals("Unexpected output for availableLocaleSet()", expectedOutput, actualOutput);

        // Test empty input
        assertEmpty(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testLanguagesByCountry() {
        // Test valid inputs
        String[] validCountryCodes = {"CA", "US", "GB"};
        for (String countryCode : validCountryCodes) {
            List<Locale> actualOutput = LocaleUtils.languagesByCountry(countryCode);
            List<Locale> expectedOutput = new ArrayList<>();
            List<Locale> availableLocales = LocaleUtils.availableLocaleList();
            for (Locale locale : availableLocales) {
                if (locale.getCountry().equals(countryCode)) {
                    expectedOutput.add(locale);
                }
            }
            assertEquals("Unexpected output for countryCode: " + countryCode, expectedOutput, actualOutput);
        }

        // Test null input
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test valid inputs
        String[] validLanguageCodes = {"fr", "en"};
        for (String languageCode : validLanguageCodes) {
            List<Locale> actualOutput = LocaleUtils.countriesByLanguage(languageCode);
            List<Locale> expectedOutput = new ArrayList<>();
            List<Locale> availableLocales = LocaleUtils.availableLocaleList();
            for (Locale locale : availableLocales) {
                if (locale.getLanguage().equals(languageCode) && locale.getCountry().length() > 0) {
                    expectedOutput.add(locale);
                }
            }
            assertEquals("Unexpected output for languageCode: " + languageCode, expectedOutput, actualOutput);
        }

        // Test null input
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }
}