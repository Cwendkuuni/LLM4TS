import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(LocaleUtils.toLocale("en"), new Locale("en", ""));
        assertEquals(LocaleUtils.toLocale("en_GB"), new Locale("en", "GB"));
        assertEquals(LocaleUtils.toLocale("en_GB_xxx"), new Locale("en", "GB", "xxx"));
        
        // Test edge cases
        assertNull(LocaleUtils.toLocale(null));
        // Test exception handling
        try {
            LocaleUtils.toLocale("x_y"); // Invalid format
            // If the test reaches this point, it means the exception wasn't thrown
            // Fail the test
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        
        // Test edge cases
        assertNull(LocaleUtils.localeLookupList(null));
        // Test with default locale
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en")),
                LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        
        // Test if the list is unmodifiable
        try {
            availableLocales.add(new Locale("test")); // Try to modify the list
            // If the test reaches this point, it means the list is modifiable
            // Fail the test
            fail("Available locale list should be unmodifiable");
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        
        // Test if the set is unmodifiable
        try {
            availableLocales.add(new Locale("test")); // Try to modify the set
            // If the test reaches this point, it means the set is modifiable
            // Fail the test
            fail("Available locale set should be unmodifiable");
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("test")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        // Test if the list is unmodifiable
        try {
            languagesByCountry.add(new Locale("test")); // Try to modify the list
            // If the test reaches this point, it means the list is modifiable
            // Fail the test
            fail("Languages by country list should be unmodifiable");
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("fr");
        // Test if the list is unmodifiable
        try {
            countriesByLanguage.add(new Locale("test")); // Try to modify the list
            // If the test reaches this point, it means the list is modifiable
            // Fail the test
            fail("Countries by language list should be unmodifiable");
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
    }

}