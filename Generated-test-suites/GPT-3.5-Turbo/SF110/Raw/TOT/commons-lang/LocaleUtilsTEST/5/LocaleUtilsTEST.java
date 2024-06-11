import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyStringInput_ReturnEmptyLocale() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatInput_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testToLocale_ValidLanguageCodeInput_ReturnLocaleWithLanguage() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals("en", result.getLanguage());
    }

    @Test
    public void testToLocale_ValidLanguageAndCountryCodeInput_ReturnLocaleWithLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testToLocale_ValidLanguageCountryAndVariantCodeInput_ReturnLocaleWithLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("en_US_variant");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("variant", result.getVariant());
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(Arrays.asList(locale, new Locale("en", ""), defaultLocale), result);
    }

    @Test
    public void testAvailableLocaleList_ReturnListWithAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), result);
    }

    @Test
    public void testAvailableLocaleSet_ReturnSetWithAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(new HashSet<>(Arrays.asList(Locale.getAvailableLocales())), result);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("ab", "CD")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListWithLanguages() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(Arrays.asList(Locale.US, Locale.ENGLISH), result);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListWithCountries() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(Arrays.asList(Locale.US, Locale.UK), result);
    }
}