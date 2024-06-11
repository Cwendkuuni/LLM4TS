import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test
    public void testToLocale_EmptyString_ReturnsDefaultLocale() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals(new Locale("", ""), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat1_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("#abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat2_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat3_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_");
    }

    @Test
    public void testToLocale_ValidFormat1_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en"), result);
    }

    @Test
    public void testToLocale_ValidFormat2_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testToLocale_ValidFormat3_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en_US_variant");
        assertEquals(new Locale("en", "US", "variant"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat4_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_AA");
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsDefaultList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        List<Locale> expected = Collections.singletonList(null);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = Arrays.asList(locale, new Locale("en", ""));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_ValidLocaleWithDefault_ReturnsLookupListWithDefault() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = Arrays.asList(locale, new Locale("en", ""), defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_ReturnsNonEmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnsNonEmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xx", "XX");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_NullCountry_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountry_ReturnsLanguageList() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_NullLanguage_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguage_ReturnsCountryList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}