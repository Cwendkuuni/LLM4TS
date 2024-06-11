import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid input
        try {
            LocaleUtils.toLocale("en_US_xx");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_US_xx", e.getMessage());
        }
        
        // Test valid input
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals("en", locale1.getLanguage());
        assertEquals("", locale1.getCountry());
        assertEquals("", locale1.getVariant());

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());
        assertEquals("", locale2.getVariant());

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());
    }

    @Test
    public void testLocaleLookupList() {
        // Test null input
        assertEquals(0, LocaleUtils.localeLookupList(null).size());

        // Test valid input
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List localeList1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(3, localeList1.size());
        assertEquals(locale1, localeList1.get(0));
        assertEquals(new Locale("fr", "CA"), localeList1.get(1));
        assertEquals(new Locale("fr"), localeList1.get(2));

        Locale locale2 = new Locale("en");
        List localeList2 = LocaleUtils.localeLookupList(locale1, locale2);
        assertEquals(4, localeList2.size());
        assertEquals(locale1, localeList2.get(0));
        assertEquals(new Locale("fr", "CA"), localeList2.get(1));
        assertEquals(new Locale("fr"), localeList2.get(2));
        assertEquals(locale2, localeList2.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test size
        assertEquals(542, LocaleUtils.availableLocaleList().size());

        // Test elements
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.CANADA));
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.US));
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.ENGLISH));
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test size
        assertEquals(542, LocaleUtils.availableLocaleSet().size());

        // Test elements
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.CANADA));
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.US));
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale() {
        // Test known locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Test unknown locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test null input
        assertEquals(0, LocaleUtils.languagesByCountry(null).size());

        // Test valid country code
        List languages = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, languages.size());
        assertTrue(languages.contains(new Locale("fr", "CA")));
        assertTrue(languages.contains(new Locale("en", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test null input
        assertEquals(0, LocaleUtils.countriesByLanguage(null).size());

        // Test valid language code
        List countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(77, countries.size());
        assertTrue(countries.contains(new Locale("en", "US")));
        assertTrue(countries.contains(new Locale("en", "GB")));
    }
}