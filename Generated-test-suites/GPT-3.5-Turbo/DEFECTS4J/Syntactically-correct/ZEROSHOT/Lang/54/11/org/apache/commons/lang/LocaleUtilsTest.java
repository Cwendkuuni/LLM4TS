import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_xx");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test that the list is not null and contains at least one element
        assertNotNull(LocaleUtils.availableLocaleList());
        assertTrue(LocaleUtils.availableLocaleList().size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test that the set is not null and contains at least one element
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertTrue(LocaleUtils.availableLocaleSet().size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("fr", "FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        assertTrue(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        Locale locale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "FR";
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "RE"), new Locale("fr", "MF"), new Locale("fr", "PM"), new Locale("fr", "WF"));
        assertEquals(expectedList, LocaleUtils.languagesByCountry(countryCode));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CH"), new Locale("fr", "CA"), new Locale("fr", "LU"), new Locale("fr", "MC"));
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(languageCode));
    }

}