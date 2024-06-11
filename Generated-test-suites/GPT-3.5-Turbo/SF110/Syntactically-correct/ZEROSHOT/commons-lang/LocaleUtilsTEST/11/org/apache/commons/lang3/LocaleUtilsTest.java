import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_nullString_returnsNull() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        assertNull(result);
    }

    @Test
    public void testToLocale_emptyString_returnsLocaleWithEmptyLanguageAndCountry() {
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat_throwsIllegalArgumentException() {
        String str = "#en_US";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat_lengthLessThan2_throwsIllegalArgumentException() {
        String str = "e";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat_lowercaseLanguageAndInvalidLength_throwIllegalArgumentException() {
        String str = "en_USA";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat_lowercaseLanguageAndInvalidCountryFormat_throwIllegalArgumentException() {
        String str = "en_us";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testToLocale_validLocaleFormat_lowercaseLanguageAndValidCountry_returnsLocaleWithLanguageAndCountry() {
        String str = "en_US";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testToLocale_validLocaleFormat_lowercaseLanguageAndEmptyCountry_returnsLocaleWithLanguage() {
        String str = "en_";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat_lengthLessThan3_throwsIllegalArgumentException() {
        String str = "_US";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat_lowercaseLanguageWithInvalidFormat_throwsIllegalArgumentException() {
        String str = "_us";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testToLocale_validLocaleFormat_lengthEquals3_returnsLocaleWithEmptyLanguageAndCountry() {
        String str = "_US";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat_lengthLessThan5_throwsIllegalArgumentException() {
        String str = "_USa";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat_fourthCharacterNotUnderscore_throwsIllegalArgumentException() {
        String str = "_USa_EN";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testToLocale_validLocaleFormat_returnsLocaleWithEmptyLanguageAndCountry() {
        String str = "_US_EN";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testLocaleLookupList_nullLocale_returnsEmptyList() {
        Locale locale = null;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_validLocale_returnsListWithLocaleAndDifferentVariants() {
        Locale locale = new Locale("en", "US", "variant");
        Locale defaultLocale = Locale.FRENCH;
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("en", "")));
        assertTrue(result.contains(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_returnsListWithAllAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        for (Locale locale : Locale.getAvailableLocales()) {
            assertTrue(result.contains(locale));
        }
    }

    @Test
    public void testAvailableLocaleSet_returnsSetWithAllAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        for (Locale locale : Locale.getAvailableLocales()) {
            assertTrue(result.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale_availableLocale_returnsTrue() {
        Locale locale = Locale.US;
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_unavailableLocale_returnsFalse() {
        Locale locale = new Locale("zz");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_nullCountryCode_returnsEmptyList() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_validCountryCode_returnsListWithLanguages() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertFalse(result.isEmpty());
        for (Locale locale : result) {
            assertEquals(countryCode, locale.getCountry());
            assertTrue(locale.getVariant().isEmpty());
        }
    }

    @Test
    public void testCountriesByLanguage_nullLanguageCode_returnsEmptyList() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode_returnsListWithCountries() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertFalse(result.isEmpty());
        for (Locale locale : result) {
            assertEquals(languageCode, locale.getLanguage());
            assertFalse(locale.getCountry().isEmpty());
            assertTrue(locale.getVariant().isEmpty());
        }
    }
}