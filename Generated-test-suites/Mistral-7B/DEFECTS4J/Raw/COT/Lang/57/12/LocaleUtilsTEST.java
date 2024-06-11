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

        // Edge cases
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_yy");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List localeList = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(3, localeList.size());
        assertEquals(Locale.US, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        assertEquals(Locale.ROOT, localeList.get(2));

        // Edge cases
        List localeListNull = LocaleUtils.localeLookupList(null);
        assertEquals(0, localeListNull.size());

        List localeListDefaultLocale = LocaleUtils.localeLookupList(Locale.US, Locale.FRENCH);
        assertEquals(4, localeListDefaultLocale.size());
        assertEquals(Locale.US, localeListDefaultLocale.get(0));
        assertEquals(Locale.ENGLISH, localeListDefaultLocale.get(1));
        assertEquals(Locale.ROOT, localeListDefaultLocale.get(2));
        assertEquals(Locale.FRENCH, localeListDefaultLocale.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List localeList = LocaleUtils.availableLocaleList();
        assertEquals(true, localeList.isEmpty() == false);

        // Edge cases
        List localeListEmpty = LocaleUtils.availableLocaleList();
        assertEquals(true, localeListEmpty.isEmpty() == true);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ROOT));

        // Edge cases
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List languages = LocaleUtils.languagesByCountry("US");
        assertEquals(true, languages.isEmpty() == false);
        assertEquals("en", languages.get(0).getLanguage());

        // Edge cases
        List languagesNull = LocaleUtils.languagesByCountry(null);
        assertEquals(true, languagesNull.isEmpty() == true);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(true, countries.isEmpty() == false);
        assertEquals("US", countries.get(0).getCountry());

        // Edge cases
        List countriesNull = LocaleUtils.countriesByLanguage(null);
        assertEquals(true, countriesNull.isEmpty() == true);
    }

}