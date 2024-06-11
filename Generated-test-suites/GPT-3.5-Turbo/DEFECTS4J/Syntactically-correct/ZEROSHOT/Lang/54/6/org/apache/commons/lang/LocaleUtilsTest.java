import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test with valid input
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test with invalid input
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("e"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("EN"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_"));

        // Test with different JDK versions
        // (The variant constructor behavior changed between JDK1.3 and JDK1.4)
        if (System.getProperty("java.version").startsWith("1.4") || System.getProperty("java.version").startsWith("1.5") || System.getProperty("java.version").startsWith("1.6")) {
            assertEquals(new Locale("en", "GB", "XXX"), LocaleUtils.toLocale("en_GB_XXX"));
        } else {
            assertEquals(new Locale("en", "GB", "XXX"), LocaleUtils.toLocale("en_GB_XXX".toUpperCase()));
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);

        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test that the list is not null and contains some locales
        assertNotNull(LocaleUtils.availableLocaleList());
        assertTrue(LocaleUtils.availableLocaleList().size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test that the set is not null and contains some locales
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertTrue(LocaleUtils.availableLocaleSet().size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with available and unavailable locales
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("qwerty")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with valid country code
        assertEquals(Arrays.asList(Locale.FRENCH, Locale.CANADA_FRENCH), LocaleUtils.languagesByCountry("CA"));

        // Test with invalid country code
        assertEquals(Collections.EMPTY_LIST, LocaleUtils.languagesByCountry("ZZ"));
        assertEquals(Collections.EMPTY_LIST, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with valid language code
        assertEquals(Arrays.asList(Locale.CANADA, Locale.CANADA_FRENCH), LocaleUtils.countriesByLanguage("fr"));

        // Test with invalid language code
        assertEquals(Collections.EMPTY_LIST, LocaleUtils.countriesByLanguage("zz"));
        assertEquals(Collections.EMPTY_LIST, LocaleUtils.countriesByLanguage(null));
    }
}