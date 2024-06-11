import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals(Locale.US, locale);

        // Edge case - null input
        assertNull(LocaleUtils.toLocale(null));

        // Exception handling - invalid format
        try {
            LocaleUtils.toLocale("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        List<Locale> expectedList = Arrays.asList(Locale.US, new Locale("en"), Locale.getDefault());
        List<Locale> result = LocaleUtils.localeLookupList(Locale.US, Locale.getDefault());
        assertEquals(expectedList, result);

        // Edge case - null starting locale
        List<Locale> emptyList = Collections.emptyList();
        result = LocaleUtils.localeLookupList(null, Locale.getDefault());
        assertEquals(emptyList, result);
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.contains(Locale.US));

        // Edge case - check if list is unmodifiable
        try {
            availableLocales.add(Locale.JAPAN);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> availableLocalesSet = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocalesSet);
        assertTrue(availableLocalesSet.contains(Locale.US));

        // Edge case - check if set is unmodifiable
        try {
            availableLocalesSet.add(Locale.JAPAN);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

        // Edge case - non-existing locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> languagesForUS = LocaleUtils.languagesByCountry("US");
        assertTrue(languagesForUS.contains(Locale.US));

        // Edge case - null input
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> languagesForNull = LocaleUtils.languagesByCountry(null);
        assertEquals(emptyList, languagesForNull);
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> countriesForEN = LocaleUtils.countriesByLanguage("en");
        assertTrue(countriesForEN.contains(Locale.US));

        // Edge case - non-existing language
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> countriesForXX = LocaleUtils.countriesByLanguage("xx");
        assertEquals(emptyList, countriesForXX);
    }
}