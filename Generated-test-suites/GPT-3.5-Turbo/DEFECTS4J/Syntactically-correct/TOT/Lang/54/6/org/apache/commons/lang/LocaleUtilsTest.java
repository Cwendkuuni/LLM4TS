import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case - valid locale string
        assertEquals(LocaleUtils.toLocale("en"), new Locale("en", ""));
        
        // Typical use case - valid locale string with country
        assertEquals(LocaleUtils.toLocale("en_GB"), new Locale("en", "GB"));
        
        // Typical use case - valid locale string with country and variant
        assertEquals(LocaleUtils.toLocale("en_GB_xxx"), new Locale("en", "GB", "xxx"));
        
        // Error scenario - null input
        assertNull(LocaleUtils.toLocale(null));
        
        // Error scenario - invalid locale format (length is less than 2)
        try {
            LocaleUtils.toLocale("e");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        // Error scenario - invalid locale format (invalid characters in language code)
        try {
            LocaleUtils.toLocale("ab_GB");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        // Error scenario - invalid locale format (invalid characters in country code)
        try {
            LocaleUtils.toLocale("en_gb");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        // Error scenario - invalid locale format (invalid separator)
        try {
            LocaleUtils.toLocale("en-GB");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        // Error scenario - invalid locale format (length is not 2, 5, or 7)
        try {
            LocaleUtils.toLocale("en_GB_x");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case - locale with country and variant
        List<Locale> expected1 = Arrays.asList(
            new Locale("en", "GB", "xxx"),
            new Locale("en", "GB"),
            new Locale("en")
        );
        assertEquals(expected1, LocaleUtils.localeLookupList(new Locale("en", "GB", "xxx")));
        
        // Typical use case - locale with country
        List<Locale> expected2 = Arrays.asList(
            new Locale("fr", "CA", ""),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        assertEquals(expected2, LocaleUtils.localeLookupList(new Locale("fr", "CA")));
        
        // Typical use case - locale without country or variant
        List<Locale> expected3 = Arrays.asList(
            new Locale("fr", "", ""),
            new Locale("fr"),
            new Locale("en")
        );
        assertEquals(expected3, LocaleUtils.localeLookupList(new Locale("fr")));
        
        // Error scenario - null locale
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case - available locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        
        // Typical use case - unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz")));
        
        // Error scenario - null input
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case - valid country code
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales()).stream()
                                   .filter(l -> l.getCountry().equals("GB") && l.getVariant().length() == 0)
                                   .collect(Collectors.toList());
        assertEquals(expected, LocaleUtils.languagesByCountry("GB"));
        
        // Typical use case - null country code
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case - valid language code
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales()).stream()
                                   .filter(l -> l.getLanguage().equals("en") &&
                                                l.getCountry().length() != 0 &&
                                                l.getVariant().length() == 0)
                                   .collect(Collectors.toList());
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        
        // Typical use case - null language code
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}