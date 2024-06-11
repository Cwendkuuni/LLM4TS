import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLowercaseLanguageCode_ReturnsLocaleObject() {
        Locale result = LocaleUtils.toLocale("en");
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testToLocale_ValidUppercaseLanguageCode_ReturnsLocaleObject() {
        Locale result = LocaleUtils.toLocale("EN");
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testToLocale_ValidLanguageAndCountryCode_ReturnsLocaleObject() {
        Locale result = LocaleUtils.toLocale("en_GB");
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("GB", result.getCountry());
    }

    @Test
    public void testToLocale_ValidLanguageCountryAndVariantCode_ReturnsLocaleObject() {
        Locale result = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("GB", result.getCountry());
        assertEquals("xxx", result.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLowerCaseLanguageCode_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("eng");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidUpperCaseLanguageCode_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("ENG");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCountryCode_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_GBx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_GB_xxx_");
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsListOfSupportedLocales() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;

        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }

    @Test
    public void testAvailableLocaleList_ReturnsListOfInstalledLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
        assertTrue(result.contains(Locale.GERMAN));
    }

    @Test
    public void testAvailableLocaleSet_ReturnsSetOfInstalledLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
        assertTrue(result.contains(Locale.GERMAN));
    }

    @Test
    public void testIsAvailableLocale_KnownLocale_ReturnsTrue() {
        boolean result = LocaleUtils.isAvailableLocale(Locale.ENGLISH);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_UnknownLocale_ReturnsFalse() {
        boolean result = LocaleUtils.isAvailableLocale(Locale.CHINESE);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfSupportedLanguages() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertNotNull(result);
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.SPANISH));
    }

    @Test
    public void testLanguagesByCountry_InvalidCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("XYZ");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfSupportedCountries() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertNotNull(result);
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.UK));
    }

    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("xyz");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}