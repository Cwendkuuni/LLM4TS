import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnsEmptyLocale() {
        Locale expected = new Locale("", "");
        assertEquals(expected, LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ShortString_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("x");
    }

    @Test
    public void testToLocale_ValidLanguage_ReturnsLocale() {
        Locale expected = new Locale("en");
        assertEquals(expected, LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_ValidLanguageAndCountry_ReturnsLocale() {
        Locale expected = new Locale("en", "US");
        assertEquals(expected, LocaleUtils.toLocale("en_US"));
    }

    @Test
    public void testToLocale_ValidLanguageCountryAndVariant_ReturnsLocale() {
        Locale expected = new Locale("en", "US", "variant");
        assertEquals(expected, LocaleUtils.toLocale("en_US_variant"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguage_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("EN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageAndCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("EN_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCountryAndVariant_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("EN_US_VARIANT");
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsList() {
        Locale locale = new Locale("en", "US", "variant");
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
    }

    @Test
    public void testAvailableLocaleList_ReturnsList() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnsSet() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsList() {
        assertFalse(LocaleUtils.languagesByCountry("US").isEmpty());
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsList() {
        assertFalse(LocaleUtils.countriesByLanguage("en").isEmpty());
    }
}

