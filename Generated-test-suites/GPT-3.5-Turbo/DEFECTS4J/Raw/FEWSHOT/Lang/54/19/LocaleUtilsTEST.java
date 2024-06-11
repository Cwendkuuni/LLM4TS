import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid input
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Invalid input
        try {
            LocaleUtils.toLocale("ENG");
            fail("Should throw an IllegalArgumentException for invalid input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Should throw an IllegalArgumentException for invalid input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Locale exists in list, default locale provided
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale1 = new Locale("en");
        assertEquals(Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), defaultLocale1),
                LocaleUtils.localeLookupList(locale1, defaultLocale1));

        // Locale exists in list, default locale not provided
        assertEquals(Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr")),
                LocaleUtils.localeLookupList(locale1, null));

        // Locale does not exist in list, default locale provided
        Locale locale2 = new Locale("de", "AT");
        Locale defaultLocale2 = new Locale("en");
        assertEquals(Arrays.asList(locale2, new Locale("de")), LocaleUtils.localeLookupList(locale2, defaultLocale2));

        // Locale does not exist in list, default locale not provided
        assertEquals(Arrays.asList(locale2, new Locale("de")), LocaleUtils.localeLookupList(locale2, null));

        // Null locale, default locale provided
        Locale defaultLocale3 = new Locale("en");
        assertEquals(Collections.singletonList(defaultLocale3), LocaleUtils.localeLookupList(null, defaultLocale3));

        // Null locale, default locale not provided
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null, null));
    }

    @Test
    public void testAvailableLocaleList() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Available locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xxx")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Valid country code
        assertEquals(Collections.singletonList(new Locale("en", "GB")), LocaleUtils.languagesByCountry("GB"));

        // Invalid country code
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XX"));

        // Null country code
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Valid language code
        assertEquals(Collections.singletonList(new Locale("en", "GB")), LocaleUtils.countriesByLanguage("en"));

        // Invalid language code
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("xx"));

        // Null language code
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}