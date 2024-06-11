import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("en", LocaleUtils.toLocale("en").toString());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("aa_ZZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AA");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX_YYY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en-GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB-XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(Locale.US, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        assertEquals(Locale.ENGLISH, localeList.get(2));

        List<Locale> localeList2 = LocaleUtils.localeLookupList(Locale.FRANCE, Locale.ENGLISH);
        assertEquals(Locale.FRANCE, localeList2.get(0));
        assertEquals(Locale.FRANCE, localeList2.get(1));
        assertEquals(Locale.FRENCH, localeList2.get(2));
        assertEquals(Locale.ENGLISH, localeList2.get(3));

        // Edge cases and exception handling tests
        List<Locale> localeList3 = LocaleUtils.localeLookupList(null);
        assertTrue(localeList3.isEmpty());

        List<Locale> localeList4 = LocaleUtils.localeLookupList(Locale.US, null);
        assertTrue(localeList4.isEmpty());

        List<Locale> localeList5 = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertTrue(localeList5.isEmpty());

        List<Locale> localeList6 = LocaleUtils.localeLookupList(Locale.FRANCE, null);
        assertTrue(localeList6.isEmpty());

        List<Locale> localeList7 = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRANCE);
        assertTrue(localeList7.isEmpty());

        List<Locale> localeList8 = LocaleUtils.localeLookupList(Locale.ENGLISH, null);
        assertTrue(localeList8.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        int size = localeList.size();
        for (int i = 0; i < size; i++) {
            Locale locale = (Locale) localeList.get(i);
            // Check if at least one known locale: en_US, fr_FR, zh_CN, de_DE, es_ES
            assertTrue(locale.toString().matches("(en|fr|zh|de|es)[_-]?[A-Z]{2}"));
        }

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleList();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        int size = localeSet.size();
        for (Locale locale : localeSet) {
            // Check if at least one known locale: en_US, fr_FR, zh_CN, de_DE, es_ES
            assertTrue(locale.toString().matches("(en|fr|zh|de|es)[_-]?[A-Z]{2}"));
        }

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleSet();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPAIN));

        // Edge cases and exception handling tests
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("aa", "AA")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_XX", "US")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertEquals(4, languages.size());
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.ENGLISH_US));
        assertTrue(languages.contains(Locale.FRENCH));
        assertTrue(languages.contains(Locale.SPANISH));

        List<Locale> languages2 = LocaleUtils.languagesByCountry("CA");
        assertEquals(3, languages2.size());
        assertTrue(languages2.contains(Locale.ENGLISH));
        assertTrue(languages2.contains(Locale.FRENCH));
        assertTrue(languages2.contains(Locale.ITALIAN));

        // Edge cases and exception handling tests
        List<Locale> languages3 = LocaleUtils.languagesByCountry(null);
        assertTrue(languages3.isEmpty());

        List<Locale> languages4 = LocaleUtils.languagesByCountry("XX");
        assertTrue(languages4.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(4, countries.size());
        assertTrue(countries.contains(Locale.US));
        assertTrue(countries.contains(Locale.GB));
        assertTrue(countries.contains(Locale.CA));
        assertTrue(countries.contains(Locale.AU));

        List<Locale> countries2 = LocaleUtils.countriesByLanguage("fr");
        assertEquals(3, countries2.size());
        assertTrue(countries2.contains(Locale.FR));
        assertTrue(countries2.contains(Locale.BE));
        assertTrue(countries2.contains(Locale.CH));

        // Edge cases and exception handling tests
        List<Locale> countries3 = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries3.isEmpty());

        List<Locale> countries4 = LocaleUtils.countriesByLanguage("XX");
        assertTrue(countries4.isEmpty());
    }
}