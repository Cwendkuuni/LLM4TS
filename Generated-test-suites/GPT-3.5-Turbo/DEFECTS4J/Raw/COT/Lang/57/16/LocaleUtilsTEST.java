import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid input
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("EN_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;

        List lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));

        lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));
        assertEquals(defaultLocale, lookupList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(LocaleUtils.availableLocaleList(), LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(LocaleUtils.availableLocaleSet(), LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPAN));
    }

    @Test
    public void testLanguagesByCountry() {
        List langs = LocaleUtils.languagesByCountry("GB");
        assertEquals(2, langs.size());
        assertEquals(Locale.ENGLISH, langs.get(0));
        assertEquals(new Locale("gd", "GB"), langs.get(1));

        langs = LocaleUtils.languagesByCountry("US");
        assertEquals(1, langs.size());
        assertEquals(Locale.ENGLISH, langs.get(0));

        langs = LocaleUtils.languagesByCountry("XX");
        assertTrue(langs.isEmpty());

        langs = LocaleUtils.languagesByCountry(null);
        assertTrue(langs.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(4, countries.size());
        assertEquals(Locale.UK, countries.get(0));
        assertEquals(Locale.US, countries.get(1));
        assertEquals(Locale.CANADA, countries.get(2));
        assertEquals(Locale.ENGLISH, countries.get(3));

        countries = LocaleUtils.countriesByLanguage("gd");
        assertEquals(1, countries.size());
        assertEquals(new Locale("gd", "GB"), countries.get(0));

        countries = LocaleUtils.countriesByLanguage("xx");
        assertTrue(countries.isEmpty());

        countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
}