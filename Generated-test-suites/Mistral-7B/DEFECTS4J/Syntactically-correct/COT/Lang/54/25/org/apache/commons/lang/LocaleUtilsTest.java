import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("en");
        actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(expected, actual);

        String invalidLocale = "InvalidLocaleFormat";
        try {
            actual = LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(baseLocale, null);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Locale.getAvailableLocales();

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        Locale knownLocale = new Locale("en", "US");
        Locale unknownLocale = new Locale("Unknown", "Unknown");

        assertTrue(LocaleUtils.isAvailableLocale(knownLocale));
        assertFalse(LocaleUtils.isAvailableLocale(unknownLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = LocaleUtils.languagesByCountry("CA");
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getCountry(), actual.get(i).getCountry());
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = LocaleUtils.countriesByLanguage("en");
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getLanguage(), actual.get(i).getLanguage());
        }
    }
}