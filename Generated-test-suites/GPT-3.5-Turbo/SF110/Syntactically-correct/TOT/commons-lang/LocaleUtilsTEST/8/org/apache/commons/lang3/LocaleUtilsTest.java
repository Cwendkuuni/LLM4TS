import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validLanguageCode_returnLocale() {
        String str = "en";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals(new Locale("en"), result);
    }
    
    @Test
    public void testToLocale_validLanguageAndCountryCode_returnLocale() {
        String str = "en_US";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals(new Locale("en", "US"), result);
    }
    
    @Test
    public void testToLocale_validLanguageCountryAndVariantCode_returnLocale() {
        String str = "en_US_variant";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals(new Locale("en", "US", "variant"), result);
    }
    
    @Test
    public void testToLocale_validLanguageCodeLowerCase_returnLocale() {
        String str = "fr";
        LocaleUtils.toLocale(str);
        assertEquals(new Locale("fr"), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat_throwIllegalArgumentException() {
        String str = "abc";
        LocaleUtils.toLocale(str);
    }
    
    @Test
    public void testToLocale_nullInput_returnNull() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        assertNull(result);
    }
    
    @Test
    public void testToLocale_emptyInput_returnLocaleWithEmptyLanguageAndCountry() {
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals(new Locale("", ""), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeFormatContainsHash_throwIllegalArgumentException() {
        String str = "en#US";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeFormatLessThan2Characters_throwIllegalArgumentException() {
        String str = "e";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeStartsWithUnderscore_throwIllegalArgumentException() {
        String str = "_en";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeLessThan3CharactersStartingInUnderscore_throwIllegalArgumentException() {
        String str = "_e";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeHasUpperCaseAfterUnderscore_throwIllegalArgumentException() {
        String str = "_En";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeFormatLessThan5CharactersStartingInUnderscore_throwIllegalArgumentException() {
        String str = "_fr_FR";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeDoesNotStartWithUnderscore_throwIllegalArgumentException() {
        String str = "fr_FR";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeCountryAndVariantIsEmpty_throwIllegalArgumentException() {
        String str = "en__";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormatVariantIsEmpty_throwIllegalArgumentException() {
        String str = "en_variant";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeStartsWithUnderscoreAndVariantIsEmpty_throwIllegalArgumentException() {
        String str = "_en_variant";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeVariantIsEmpty_throwIllegalArgumentException() {
        String str = "en_variant";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeCountryIsEmpty_throwIllegalArgumentException() {
        String str = "en__variant";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_localeHasUpperCaseAfterCountry_throwIllegalArgumentException() {
        String str = "en_UK_variant";
        LocaleUtils.toLocale(str);
    }
    
    @Test
    public void testLocaleLookupList_localeNotNull_returnLocaleList() {
        Locale locale = Locale.US;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale(locale.getLanguage(), locale.getCountry())));
        assertTrue(result.contains(new Locale(locale.getLanguage(), "")));
        assertTrue(result.contains(Locale.getDefault()));
    }
    
    @Test
    public void testLocaleLookupList_localeIsNull_returnEmptyList() {
        Locale locale = null;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_variantNotEmpty_returnLocaleListWithVariantAndCountry() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale(locale.getLanguage(), locale.getCountry())));
        assertTrue(result.contains(new Locale(locale.getLanguage(), "")));
        assertTrue(result.contains(Locale.getDefault()));
    }
    
    @Test
    public void testLocaleLookupList_countryNotEmpty_returnLocaleListWithCountryOnly() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale(locale.getLanguage(), locale.getCountry())));
        assertTrue(result.contains(new Locale(locale.getLanguage(), "")));
        assertTrue(result.contains(Locale.getDefault()));
    }
    
    @Test
    public void testAvailableLocaleList_returnListOfAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet_returnSetOfAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale_availableLocale_returnTrue() {
        Locale locale = Locale.US;
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_unavailableLocale_returnFalse() {
        Locale locale = new Locale("xx", "XX");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_validCountryCode_returnListOfLanguages() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_nullCountryCode_returnEmptyList() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_validLanguageCode_returnListOfCountries() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_nullLanguageCode_returnEmptyList() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}