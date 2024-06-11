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
        // Test valid cases
        String[] validInputs = new String[]{"en", "en_US", "fr_FR", "en_GB_en_GB"};
        for (String input : validInputs) {
            Locale expected = new Locale(input.substring(0, 2), input.substring(3));
            Locale actual = LocaleUtils.toLocale(input);
            assertEquals(expected.toString(), actual.toString());
        }

        // Test invalid cases
        String[] invalidInputs = new String[]{"en_GB_en", "en_US_en_GB_en", "en_GB_en_us", "en_GB_en_GB_en_GB"};
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
        // Test valid cases
        Locale baseLocale = new Locale("en", "US");
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "US", ""),
                new Locale("en", "US", "EN"),
                new Locale("en", "", ""),
                new Locale("en", "", "EN"),
                new Locale("", "", ""),
                new Locale("", "", "EN")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(expectedList.toString(), actualList.toString());

        // Test empty list
        List<Locale> emptyList = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.EMPTY_LIST.toString(), emptyList.toString());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList.toString(), actualList.toString());
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = Arrays.asList(new Locale("en", "US"), new Locale("fr", "FR"));
        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale notAvailableLocale = new Locale("es", "ES");
        assertFalse(LocaleUtils.isAvailableLocale(notAvailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedList.toString(), actualList.toString());

        List<Locale> emptyList = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.EMPTY_LIST.toString(), emptyList.toString());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(new Locale("CA", "en"), new Locale("CA", "fr"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedList.toString(), actualList.toString());

        List<Locale> emptyList = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.EMPTY_LIST.toString(), emptyList.toString());
    }
}