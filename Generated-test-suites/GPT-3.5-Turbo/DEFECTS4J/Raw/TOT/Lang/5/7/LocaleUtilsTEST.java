import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_WithValidLocaleString_ReturnsValidLocale() {
        Locale result = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), result);
    }

    @Test
    public void testToLocale_WithNullString_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithInvalidFormat_ThrowsException() {
        LocaleUtils.toLocale("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithLowerCaseCountryCode_ThrowsException() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithInvalidSeparator_ThrowsException() {
        LocaleUtils.toLocale("en-GB");
    }

    @Test
    public void testLocaleLookupList_WithValidLocale_ReturnsExpectedList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_WithNullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_WithValidLocaleAndDefault_ReturnsExpectedList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_ReturnsExpectedList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet_ReturnsExpectedSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        Locale locale = Locale.US;
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xx");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnsExpectedList() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales())
                .stream().filter(l -> countryCode.equals(l.getCountry()) && l.getVariant().isEmpty())
                .toList();
        assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnsExpectedList() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales())
                .stream().filter(l -> languageCode.equals(l.getLanguage()) && !l.getCountry().isEmpty() && l.getVariant().isEmpty())
                .toList();
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
}