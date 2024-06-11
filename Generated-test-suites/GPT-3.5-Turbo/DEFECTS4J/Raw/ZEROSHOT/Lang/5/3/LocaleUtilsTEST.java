import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    private Locale testLocale;

    @Before
    public void setUp() {
        testLocale = new Locale("en", "US");
    }

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(locale, new Locale("en", ""));

        locale = LocaleUtils.toLocale("en_US");
        assertEquals(locale, new Locale("en", "US"));

        locale = LocaleUtils.toLocale("en_US_xxx");
        assertEquals(locale, new Locale("en", "US", "xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        Locale defaultLocale = new Locale("fr");

        // Test with locale
        assertEquals(LocaleUtils.localeLookupList(testLocale), Arrays.asList(testLocale, new Locale("en"), defaultLocale));

        // Test with locale and defaultLocale
        assertEquals(LocaleUtils.localeLookupList(testLocale, defaultLocale), Arrays.asList(testLocale, new Locale("en"), defaultLocale));

        // Test with null locale
        assertEquals(LocaleUtils.localeLookupList(null), Collections.emptyList());

        // Test with null locale and defaultLocale
        assertEquals(LocaleUtils.localeLookupList(null, defaultLocale), Collections.singletonList(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(testLocale));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(LocaleUtils.languagesByCountry("US"), Arrays.asList(new Locale("en", "US")));
        assertEquals(LocaleUtils.languagesByCountry("invalid"), Collections.emptyList());
        assertEquals(LocaleUtils.languagesByCountry(null), Collections.emptyList());
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(LocaleUtils.countriesByLanguage("en"), Arrays.asList(new Locale("en", "US")));
        assertEquals(LocaleUtils.countriesByLanguage("invalid"), Collections.emptyList());
        assertEquals(LocaleUtils.countriesByLanguage(null), Collections.emptyList());
    }
}