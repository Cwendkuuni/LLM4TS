
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical case
        Locale locale1 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale1.getLanguage());
        assertEquals("GB", locale1.getCountry());

        // Edge case with variant
        Locale locale2 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());
        assertEquals("xxx", locale2.getVariant());

        // Error scenarios
        try {
            LocaleUtils.toLocale(null);
            fail("Should throw IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("e");
            fail("Should throw IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("ENgb");
            fail("Should throw IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical cases
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(3, list1.size());
        assertEquals(locale1, list1.get(0));
        assertEquals(new Locale("fr", "CA"), list1.get(1));
        assertEquals(new Locale("fr"), list1.get(2));

        Locale locale2 = new Locale("en");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2);
        assertEquals(1, list2.size());
        assertEquals(locale2, list2.get(0));

        // Edge case with default locale
        Locale locale3 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list3 = LocaleUtils.localeLookupList(locale3, defaultLocale);
        assertEquals(4, list3.size());
        assertEquals(locale3, list3.get(0));
        assertEquals(new Locale("fr", "CA"), list3.get(1));
        assertEquals(new Locale("fr"), list3.get(2));
        assertEquals(defaultLocale, list3.get(3));

        // Error scenarios
        List<Locale> list4 = LocaleUtils.localeLookupList(null);
        assertNull(list4);

        List<Locale> list5 = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(1, list5.size());
        assertEquals(defaultLocale, list5.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
        assertTrue(locales.contains(Locale.GERMAN));
        assertTrue(locales.contains(Locale.CANADA));
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.UK));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
        assertTrue(locales.contains(Locale.GERMAN));
        assertTrue(locales.contains(Locale.CANADA));
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.UK));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINA));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPAN));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical case
        List<Locale> langs1 = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, langs1.size());
        assertEquals(new Locale("fr", "CA"), langs1.get(0));
        assertEquals(new Locale("en", "CA"), langs1.get(1));

        // Edge case with invalid input
        List<Locale> langs2 = LocaleUtils.languagesByCountry(null);
        assertNotNull(langs2);
        assertEquals(0, langs2.size());

        // Error scenario
        List<Locale> langs3 = LocaleUtils.languagesByCountry("XX");
        assertNotNull(langs3);
        assertEquals(0, langs3.size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical case
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, countries1.size());
        assertEquals(new Locale("fr", "CA"), countries1.get(0));

        // Edge case with invalid input
        List<Locale> countries2 = LocaleUtils.countriesByLanguage(null);
        assertNotNull(countries2);
        assertEquals(0, countries2.size());

        // Error scenario
        List<Locale> countries3 = LocaleUtils.countriesByLanguage("xx");
        assertNotNull(countries3);
        assertEquals(0, countries3.size());
    }
}
