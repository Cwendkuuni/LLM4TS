import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));

        // Edge cases and exceptions

        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test empty string input
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid format (language code not lowercase)
        try {
            LocaleUtils.toLocale("EN_US");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format (country code not uppercase)
        try {
            LocaleUtils.toLocale("en_us");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format (separator not underscore)
        try {
            LocaleUtils.toLocale("en_US_");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format (length < 2)
        try {
            LocaleUtils.toLocale("en");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format (language code and country code are the same)
        try {
            LocaleUtils.toLocale("en_en");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format (country code is empty)
        try {
            LocaleUtils.toLocale("en_");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format (variant is not empty)
        try {
            LocaleUtils.toLocale("en_US_variant");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format (language code and country code are empty)
        try {
            LocaleUtils.toLocale("_");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "XXX"));
        assertEquals(3, localeList.size());
        assertEquals(new Locale("fr", "CA", "XXX"), localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));

        // Test null input
        List<Locale> localeListNull = LocaleUtils.localeLookupList(null);
        assertTrue(localeListNull.isEmpty());

        // Test empty list
        List<Locale> localeListEmpty = LocaleUtils.localeLookupList(new Locale(""));
        assertTrue(localeListEmpty.isEmpty());

        // Test empty country code
        List<Locale> localeListEmptyCountry = LocaleUtils.localeLookupList(new Locale("en", ""));
        assertTrue(localeListEmptyCountry.isEmpty());

        // Test default locale
        List<Locale> localeListDefault = LocaleUtils.localeLookupList(new Locale("fr", "CA"), new Locale("en"));
        assertEquals(4, localeListDefault.size());
        assertEquals(new Locale("fr", "CA", "XXX"), localeListDefault.get(0));
        assertEquals(new Locale("fr", "CA"), localeListDefault.get(1));
        assertEquals(new Locale("fr"), localeListDefault.get(2));
        assertEquals(new Locale("en"), localeListDefault.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertFalse(localeList.isEmpty());

        // Test null input
        List<Locale> localeListNull = LocaleUtils.availableLocaleList(null);
        assertTrue(localeListNull.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertFalse(localeSet.isEmpty());

        // Test null input
        Set<Locale> localeSetNull = LocaleUtils.availableLocaleSet(null);
        assertTrue(localeSetNull.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Test null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertFalse(languages.isEmpty());

        // Test null input
        List<Locale> languagesNull = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesNull.isEmpty());

        // Test empty country code
        List<Locale> languagesEmpty = LocaleUtils.languagesByCountry("");
        assertTrue(languagesEmpty.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertFalse(countries.isEmpty());

        // Test null input
        List<Locale> countriesNull = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesNull.isEmpty());

        // Test empty language code
        List<Locale> countriesEmpty = LocaleUtils.countriesByLanguage("");
        assertTrue(countriesEmpty.isEmpty());
    }

}