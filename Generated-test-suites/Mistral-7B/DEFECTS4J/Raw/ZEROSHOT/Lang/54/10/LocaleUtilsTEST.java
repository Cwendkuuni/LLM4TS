import org.apache.commons.lang.LocaleUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        List<String> validInputs = Arrays.asList(
                "en",
                "en_GB",
                "en_GB_xxx",
                "fr",
                "fr_CA",
                "fr_CA_xxx"
        );
        for (String str : validInputs) {
            Locale expectedLocale = new Locale(str.substring(0, 2), str.substring(3, str.length()));
            Locale actualLocale = LocaleUtils.toLocale(str);
            Assertions.assertEquals(expectedLocale, actualLocale);
        }

        // Test invalid input
        List<String> invalidInputs = Arrays.asList(
                null,
                "",
                "en_GB_aaa",
                "en_aB",
                "en__",
                "en_GB_",
                "en_GB__",
                "en_GB_xx",
                "en_GB_xx_aaa",
                "en_GB_aaa_xx"
        );
        for (String str : invalidInputs) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(str));
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedLocaleList = Collections.singletonList(defaultLocale);
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null, defaultLocale);
        Assertions.assertEquals(expectedLocaleList, actualLocaleList);

        // Test valid input
        Locale locale = new Locale("fr", "CA", "xxx");
        expectedLocaleList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        actualLocaleList = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assertions.assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        Assertions.assertFalse(availableLocaleList.isEmpty());
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty input
        List<Locale> expectedLocaleList = Collections.emptyList();
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(null);
        Assertions.assertEquals(expectedLocaleList, actualLocaleList);

        // Test valid input
        String countryCode = "CA";
        expectedLocaleList = Arrays.asList(new Locale("en", countryCode), new Locale("fr", countryCode));
        actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assertions.assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty input
        List<Locale> expectedLocaleList = Collections.emptyList();
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(null);
        Assertions.assertEquals(expectedLocaleList, actualLocaleList);

        // Test valid input
        String languageCode = "en";
        expectedLocaleList = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "US"));
        actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assertions.assertEquals(expectedLocaleList, actualLocaleList);
    }

}