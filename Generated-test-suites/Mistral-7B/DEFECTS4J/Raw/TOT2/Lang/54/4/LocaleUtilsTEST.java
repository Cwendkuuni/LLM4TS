import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // basic usage
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // null input
        assertNull(LocaleUtils.toLocale(null));

        // invalid format
        try {
            LocaleUtils.toLocale("abc");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // edge cases: empty input
        Locale emptyLocale = LocaleUtils.toLocale("");
        assertTrue(emptyLocale.isEmpty());
        assertEquals("", emptyLocale.getDisplayCountry());
        assertEquals("", emptyLocale.getDisplayLanguage());
    }

    @Test
    public void testLocaleLookupList() {
        // basic usage
        Locale targetLocale = new Locale("fr", "CA", "XX");
        List<Locale> expectedList = LocaleUtils.localeLookupList(targetLocale);
        List<Locale> actualList = new ArrayList<>();
        actualList.add(targetLocale);
        actualList.add(new Locale("fr", "CA"));
        actualList.add(new Locale("fr"));
        assertEquals(expectedList, actualList);

        // null input
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // edge cases: empty list
        Locale defaultLocale = new Locale("en");
        List<Locale> emptyList = LocaleUtils.localeLookupList(defaultLocale);
        assertEquals(Collections.singletonList(defaultLocale), emptyList);
    }

    @Test
    public void testAvailableLocaleList() {
        // basic usage
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, actualList);

        // null input
        assertNotNull(LocaleUtils.availableLocaleList());
        assertEquals(Collections.emptyList(), LocaleUtils.availableLocaleList());
    }

    @Test
    public void testIsAvailableLocale() {
        // basic usage
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // edge cases: unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xy", "xy")));

        // null input
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // basic usage
        List<Locale> expectedList = LocaleUtils.languagesByCountry("CA");
        List<Locale> actualList = LocaleUtils.languagesByCountry(new Locale("", "CA"));
        assertEquals(expectedList, actualList);

        // null input
        assertNotNull(LocaleUtils.languagesByCountry(null));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));

        // edge cases: empty list
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testCountriesByLanguage() {
        // basic usage
        List<Locale> expectedList = LocaleUtils.countriesByLanguage("en");
        List<Locale> actualList = LocaleUtils.countriesByLanguage(new Locale("en", ""));
        assertEquals(expectedList, actualList);

        // null input
        assertNotNull(LocaleUtils.countriesByLanguage(null));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));

        // edge cases: empty list
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));
    }
}