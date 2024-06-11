import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    private Locale englishLocale;
    private Locale germanLocale;
    private Locale frenchLocale;

    @Before
    public void setUp() throws Exception {
        englishLocale = new Locale("en");
        germanLocale = new Locale("de");
        frenchLocale = new Locale("fr");
    }

    @Test
    public void testToLocaleWithValidInput() {
        // typical use case
        assertEquals(englishLocale, LocaleUtils.toLocale("en"));
        assertEquals(germanLocale, LocaleUtils.toLocale("de"));
        assertEquals(frenchLocale, LocaleUtils.toLocale("fr"));

        // edge case
        assertEquals(englishLocale, LocaleUtils.toLocale("en_US"));

        // error scenario
        try {
            LocaleUtils.toLocale("ens");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testLocaleLookupListWithValidInput() {
        // typical use case
        List<Locale> expected1 = Arrays.asList(englishLocale);
        List<Locale> actual1 = LocaleUtils.localeLookupList(englishLocale);
        assertEquals(expected1, actual1);

        List<Locale> expected2 = Arrays.asList(germanLocale, englishLocale);
        List<Locale> actual2 = LocaleUtils.localeLookupList(germanLocale, englishLocale);
        assertEquals(expected2, actual2);

        // edge case
        List<Locale> expected3 = Arrays.asList(englishLocale, germanLocale);
        List<Locale> actual3 = LocaleUtils.localeLookupList(englishLocale, germanLocale);
        assertEquals(expected3, actual3);

        // error scenario
        List<Locale> expected4 = Arrays.asList(englishLocale, frenchLocale, germanLocale);
        List<Locale> actual4 = LocaleUtils.localeLookupList(englishLocale, germanLocale);
        assertNotEquals(expected4, actual4);
    }

    @Test
    public void testAvailableLocaleList() {
        // typical use case
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());

        // edge case
        assertTrue(availableLocales.contains(englishLocale));
        
        // error scenario
        assertFalse(availableLocales.contains(new Locale("abcd")));
    }

    @Test
    public void testAvailableLocaleSet() {
        // typical use case
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());

        // edge case
        assertTrue(availableLocales.contains(germanLocale));
        
        // error scenario
        assertFalse(availableLocales.contains(new Locale("abcd")));
    }

    @Test
    public void testIsAvailableLocale() {
        // typical use case
        assertTrue(LocaleUtils.isAvailableLocale(englishLocale));
        assertTrue(LocaleUtils.isAvailableLocale(frenchLocale));

        // edge case
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abcd")));

        // error scenario
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountryWithValidInput() {
        // typical use case
        List<Locale> expected1 = Arrays.asList(englishLocale, frenchLocale);
        List<Locale> actual1 = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected1, actual1);

        List<Locale> expected2 = Arrays.asList(germanLocale);
        List<Locale> actual2 = LocaleUtils.languagesByCountry("DE");
        assertEquals(expected2, actual2);

        // edge case
        List<Locale> expected3 = Collections.emptyList();
        List<Locale> actual3 = LocaleUtils.languagesByCountry("US");
        assertEquals(expected3, actual3);

        // error scenario
        List<Locale> expected4 = Arrays.asList(englishLocale, frenchLocale);
        List<Locale> actual4 = LocaleUtils.languagesByCountry("US");
        assertNotEquals(expected4, actual4);
    }

    @Test
    public void testCountriesByLanguageWithValidInput() {
        // typical use case
        List<Locale> expected1 = Arrays.asList(englishLocale);
        List<Locale> actual1 = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected1, actual1);

        List<Locale> expected2 = Arrays.asList(frenchLocale);
        List<Locale> actual2 = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expected2, actual2);

        // edge case
        List<Locale> expected3 = Collections.emptyList();
        List<Locale> actual3 = LocaleUtils.countriesByLanguage("de");
        assertEquals(expected3, actual3);

        // error scenario
        List<Locale> expected4 = Arrays.asList(englishLocale);
        List<Locale> actual4 = LocaleUtils.countriesByLanguage("de");
        assertNotEquals(expected4, actual4);
    }

}