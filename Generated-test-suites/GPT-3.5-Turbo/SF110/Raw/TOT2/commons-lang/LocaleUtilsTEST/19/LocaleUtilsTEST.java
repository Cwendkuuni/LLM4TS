import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("invalid_locale");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(new Locale("en", ""));
        expectedList.add(defaultLocale);

        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
        assertEquals(Collections.singletonList(new Locale("en", "US")), LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
        assertEquals(Collections.singletonList(new Locale("en", "US")), LocaleUtils.countriesByLanguage("en"));
    }

}