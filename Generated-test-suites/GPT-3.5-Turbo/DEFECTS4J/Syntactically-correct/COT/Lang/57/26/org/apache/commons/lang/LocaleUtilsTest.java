import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test cases
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Test for variant in JDK1.3
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_gb"));
        
        // Test invalid input
        assertNull(LocaleUtils.toLocale(null));
        assertIllegalArgumentException(() -> LocaleUtils.toLocale(""));
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("e"));
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("en-GB"));
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("en_GB_"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        assertEquals(3, locales.size());
        assertEquals(locale, locales.get(0));
        assertEquals(new Locale("fr", "CA"), locales.get(1));
        assertEquals(new Locale("fr"), locales.get(2));

        locales = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, locales.size());
        assertEquals(locale, locales.get(0));
        assertEquals(new Locale("fr", "CA"), locales.get(1));
        assertEquals(new Locale("fr"), locales.get(2));
        assertEquals(defaultLocale, locales.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.UK));
        assertTrue(locales.contains(Locale.FRANCE));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.contains(Locale.US));
        assertTrue(set.contains(Locale.UK));
        assertTrue(set.contains(Locale.FRANCE));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINA));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, langs.size());
        assertTrue(langs.contains(new Locale("fr", "CA")));
        assertTrue(langs.contains(new Locale("en", "CA")));
        
        langs = LocaleUtils.languagesByCountry("ZZ");
        assertEquals(0, langs.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, countries.size());
        assertTrue(countries.contains(new Locale("fr", "CA")));
        
        countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(3, countries.size());
        assertTrue(countries.contains(new Locale("en", "US")));
        assertTrue(countries.contains(new Locale("en", "GB")));
        assertTrue(countries.contains(new Locale("en", "CA")));
        
        countries = LocaleUtils.countriesByLanguage("zz");
        assertEquals(0, countries.size());
    }

    private void assertIllegalArgumentException(Runnable runnable) {
        try {
            runnable.run();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}