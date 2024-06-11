import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.Locale;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = {"en", "en_GB", "en_GB_XXX", "fr_CA", "fr_CA_XXX"};
        for (String localeString : testCases) {
            Locale expectedLocale = new Locale(localeString.substring(0, 2), localeString.substring(3, localeString.length()));
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            assertEquals("ToLocale test failed for input: " + localeString, expectedLocale, actualLocale);
        }

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("toLocale should have thrown an exception for null input");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidLocaleString = "xx_xx";
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("toLocale should have thrown an exception for invalid input: " + invalidLocaleString);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test with valid locales
        Locale baseLocale = new Locale("en_GB", "GB");
        List<Locale> expectedLocaleList = Arrays.asList(baseLocale, new Locale("en", "GB"), new Locale("en"));
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale);
        Assert.assertEquals("localeLookupList test failed for baseLocale: en_GB", expectedLocaleList, actualLocaleList);

        Locale defaultLocale = new Locale("fr", "CA");
        List<Locale> expectedLocaleList2 = Arrays.asList(baseLocale, new Locale("en", "GB"), new Locale("en"), defaultLocale);
        List<Locale> actualLocaleList2 = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        Assert.assertEquals("localeLookupList test failed for baseLocale: en_GB and defaultLocale: fr_CA", expectedLocaleList2, actualLocaleList2);

        // Test with null input
        Locale nullLocale = null;
        List<Locale> expectedLocaleList3 = Collections.EMPTY_LIST;
        List<Locale> actualLocaleList3 = LocaleUtils.localeLookupList(nullLocale);
        Assert.assertEquals("localeLookupList test failed for null input", expectedLocaleList3, actualLocaleList3);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals("availableLocaleList test failed", expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        Assert.assertEquals("availableLocaleSet test failed", expectedLocaleSet, actualLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> testLocales = new ArrayList<>();
        testLocales.add(new Locale("en", "US"));
        testLocales.add(new Locale("fr", "FR"));
        testLocales.add(new Locale("es", "ES"));

        for (Locale locale : testLocales) {
            assertTrue("isAvailableLocale test failed for locale: " + locale, LocaleUtils.isAvailableLocale(locale));
        }

        Locale notAvailableLocale = new Locale("xx", "xx");
        assertFalse("isAvailableLocale test failed for locale: " + notAvailableLocale, LocaleUtils.isAvailableLocale(notAvailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals("languagesByCountry test failed for countryCode: CA", expectedLocaleList, actualLocaleList);

        String countryCode2 = "US";
        List<Locale> expectedLocaleList2 = Collections.EMPTY_LIST;
        List<Locale> actualLocaleList2 = LocaleUtils.languagesByCountry(countryCode2);
        Assert.assertEquals("languagesByCountry test failed for countryCode: US", expectedLocaleList2, actualLocaleList2);

        String countryCode3 = null;
        List<Locale> expectedLocaleList3 = Collections.EMPTY_LIST;
        List<Locale> actualLocaleList3 = LocaleUtils.languagesByCountry(countryCode3);
        Assert.assertEquals("languagesByCountry test failed for null countryCode", expectedLocaleList3, actualLocaleList3);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals("countriesByLanguage test failed for languageCode: fr", expectedLocaleList, actualLocaleList);

        String languageCode2 = "es";
        List<Locale> expectedLocaleList2 = Collections.EMPTY_LIST;
        List<Locale> actualLocaleList2 = LocaleUtils.countriesByLanguage(languageCode2);
        Assert.assertEquals("countriesByLanguage test failed for languageCode: es", expectedLocaleList2, actualLocaleList2);

        String languageCode3 = null;
        List<Locale> expectedLocaleList3 = Collections.EMPTY_LIST;
        List<Locale> actualLocaleList3 = LocaleUtils.countriesByLanguage(languageCode3);
        Assert.assertEquals("countriesByLanguage test failed for null languageCode", expectedLocaleList3, actualLocaleList3);
    }
}