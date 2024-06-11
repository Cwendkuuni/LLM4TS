import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid format (wrong length)
        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB____");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format (wrong character)
        try {
            LocaleUtils.toLocale("en_GB_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_gb");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_en-US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test valid format
        Locale actual2 = LocaleUtils.toLocale("fr_CA_xxx");
        assertNotNull(actual2);
        assertEquals("fr", actual2.getLanguage());
        assertEquals("CA", actual2.getCountry());
        assertEquals("xxx", actual2.getVariant());
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expected, actual);

        // Test null input
        assertNull(LocaleUtils.localeLookupList(null, null));
        assertNull(LocaleUtils.localeLookupList(null, Locale.ENGLISH));
        assertNull(LocaleUtils.localeLookupList(Locale.FRENCH, null));

        // Test empty list
        List<Locale> actualEmpty = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.FRENCH);
        assertEquals(Collections.emptyList(), actualEmpty);

        // Test single locale
        List<Locale> actualSingle = LocaleUtils.localeLookupList(new Locale("fr", "CA"));
        assertEquals(Collections.singletonList(new Locale("fr", "CA")), actualSingle);

        // Test default locale only
        List<Locale> actualDefault = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertEquals(Collections.singletonList(Locale.ENGLISH), actualDefault);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH);
        assertTrue(expected.containsAll(actual));
        assertEquals(actual.size(), expected.size());

        // Test empty list
        List<Locale> expectedEmpty = Collections.emptyList();
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
        assertTrue(expectedEmpty.equals(LocaleUtils.availableLocaleList().isEmpty()));
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic test
        Set<Locale> expected = new HashSet<>(LocaleUtils.availableLocaleList());
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertTrue(expected.containsAll(actual));
        assertEquals(actual.size(), expected.size());

        // Test empty set
        Set<Locale> expectedEmpty = Collections.emptySet();
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
        assertTrue(expectedEmpty.equals(LocaleUtils.availableLocaleSet().isEmpty()));
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Test null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));

        // Test available locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test
        List<Locale> expected = Arrays.asList(Locale.FRENCH, Locale.ENGLISH);
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);

        // Test null input
        assertNotNull(LocaleUtils.languagesByCountry(null));

        // Test empty list
        List<Locale> actualEmpty = LocaleUtils.languagesByCountry("");
        assertEquals(Collections.emptyList(), actualEmpty);

        // Test country not found
        List<Locale> actualNotFound = LocaleUtils.languagesByCountry("XX");
        assertEquals(Collections.emptyList(), actualNotFound);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test
        List<Locale> expected = Arrays.asList(
                new Locale("CA", "en"),
                new Locale("GB", "en"),
                new Locale("US", "en")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);

        // Test null input
        assertNotNull(LocaleUtils.countriesByLanguage(null));

        // Test empty list
        List<Locale> actualEmpty = LocaleUtils.countriesByLanguage("");
        assertEquals(Collections.emptyList(), actualEmpty);

        // Test language not found
        List<Locale> actualNotFound = LocaleUtils.countriesByLanguage("XX");
        assertEquals(Collections.emptyList(), actualNotFound);
    }
}