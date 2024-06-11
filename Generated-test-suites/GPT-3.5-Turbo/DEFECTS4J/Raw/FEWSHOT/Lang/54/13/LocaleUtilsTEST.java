import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        // Valid locale strings
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Invalid locale strings
        assertNull(LocaleUtils.toLocale(null));
        try {
            LocaleUtils.toLocale("");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("eng");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("eN");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("en_gb");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("en_GB_");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("en_GB_XXX");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("en_GB_XXX_");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("en_GB_XXX_XXX");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test with locale and default locale
        assertEquals(Collections.singletonList(new Locale("fr", "CA", "xxx")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));

        // Test with null locale and default locale
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null, new Locale("en")));
        assertEquals(Collections.singletonList(new Locale("en")), LocaleUtils.localeLookupList(null, new Locale("en")));

        // Test with locale and null default locale
        assertEquals(Collections.singletonList(new Locale("fr", "CA", "xxx")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null));

        // Test with null locale and null default locale
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null, null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test size of available locales
        assertEquals(0, LocaleUtils.availableLocaleList().size());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test size of available locales set
        assertEquals(0, LocaleUtils.availableLocaleSet().size());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with available and non-available locales
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "CA", "xxx")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de")));

        // Test with null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with valid country code
        assertEquals(Collections.singletonList(new Locale("fr", "CA")), LocaleUtils.languagesByCountry("CA"));
        assertEquals(Collections.singletonList(new Locale("en", "CA")), LocaleUtils.languagesByCountry("CA"));

        // Test with invalid country code
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XX"));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with valid language code
        assertEquals(Collections.singletonList(new Locale("fr", "CA")), LocaleUtils.countriesByLanguage("fr"));
        assertEquals(Collections.singletonList(new Locale("en", "CA")), LocaleUtils.countriesByLanguage("en"));

        // Test with invalid language code
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("XX"));
    }
}