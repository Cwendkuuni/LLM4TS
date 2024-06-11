import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("es", "ES", "Castilian");
        actual = LocaleUtils.toLocale("es_ES_Castilian");
        assertEquals(expected, actual);

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
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
            LocaleUtils.toLocale("es_ES_Castilian_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("es_ES_Castilian_Castilian");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test with valid input
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
        assertEquals(expected, actual);

        // Test with null input
        List<Locale> actualNull = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertTrue(actualNull.isEmpty());

        // Test with empty list
        List<Locale> actualEmpty = LocaleUtils.localeLookupList(new Locale("en"), null);
        assertTrue(actualEmpty.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Test with valid input
        List<Locale> expected = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);

        // Test with empty list
        List<Locale> actualEmpty = LocaleUtils.availableLocaleList();
        assertTrue(actualEmpty.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with valid inputs
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("fr", "FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale2));

        // Test with invalid inputs
        Locale locale3 = new Locale("es", "ES", "Castilian");
        assertFalse(LocaleUtils.isAvailableLocale(locale3));

        Locale locale4 = new Locale("es_ES", "Castilian");
        assertFalse(LocaleUtils.isAvailableLocale(locale4));

        Locale locale5 = new Locale("es_ES_Castilian_Castilian");
        assertFalse(LocaleUtils.isAvailableLocale(locale5));

        Locale locale6 = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale6));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with valid inputs
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("es", "CA")
        );
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Test with null input
        List<Locale> actualNull = LocaleUtils.languagesByCountry(null);
        assertTrue(actualNull.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with valid inputs
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "BE"),
                new Locale("fr", "CH"),
                new Locale("fr", "FR")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Test with empty list
        List<Locale> actualEmpty = LocaleUtils.countriesByLanguage(null);
        assertTrue(actualEmpty.isEmpty());
    }
}