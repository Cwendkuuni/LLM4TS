import org.junit.*;
import java.util.concurrent.*;
import java.util.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("#en_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThan2Characters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithNonUpperCaseCharactersAfterUnderscore_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_en");
    }
    
    @Test
    public void testToLocale_ValidFormatWithLanguageAndCountry_ReturnLocale() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidCountry_ReturnIllegalArgumentException() {
        LocaleUtils.toLocale("en_USA");
    }
    
    @Test
    public void testToLocale_ValidFormatWithLanguageAndEmptyCountry_ReturnLocaleWithEmptyCountry() {
        Locale locale = LocaleUtils.toLocale("en_");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithLanguageCountryAndVariant_ReturnLocale() {
        Locale locale = LocaleUtils.toLocale("en_US_VARIANT");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("VARIANT", locale.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidSeparatorAfterLanguage_ReturnIllegalArgumentException() {
        LocaleUtils.toLocale("en-");
    }
    
    @Test
    public void testLocaleLookupList_LocaleNotNull_ReturnListOfLocales() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale(locale.getLanguage(), locale.getCountry()));
        expected.add(new Locale(locale.getLanguage(), ""));
        expected.add(locale);
        assertEquals(expected, result);
    }
    
    @Test
    public void testLocaleLookupList_LocaleNull_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_LocaleNotNullAndDefaultLocaleNotNull_ReturnListOfLocalesWithDefaultLocaleAppended() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale(locale.getLanguage(), locale.getCountry()));
        expected.add(new Locale(locale.getLanguage(), ""));
        expected.add(defaultLocale);
        assertEquals(expected, result);
    }
    
    @Test
    public void testLocaleLookupList_DefaultLocaleNotInList_ReturnListOfLocalesWithDefaultLocaleAppended() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertTrue(result.contains(defaultLocale));
    }
    
    @Test
    public void testLocaleLookupList_DefaultLocaleInList_ReturnListOfLocalesWithoutDefaultLocaleAppended() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertFalse(result.contains(defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
    }
    
    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocale_NotAvailableLocale_ReturnFalse() {
        Locale locale = new Locale("zz", "ZZ");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry_CountryCodeNull_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_CountryCodeNotNullAndInMap_ReturnListFromMap() {
        LocaleUtils.cLanguagesByCountry.put("US", Arrays.asList(new Locale("en", "US")));
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"));
        assertEquals(expected, result);
    }
    
    @Test
    public void testLanguagesByCountry_CountryCodeNotNullAndNotInMap_ReturnListFromLocales() {
        List<Locale> locales = Arrays.asList(
            new Locale("en", "US"),
            new Locale("fr", "FR")
        );
        List<Locale> expected = Arrays.asList(new Locale("en", "US"));
        LocaleUtils.cLanguagesByCountry.clear();
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, result);
    }
    
    @Test
    public void testCountriesByLanguage_LanguageCodeNull_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_LanguageCodeNotNullAndInMap_ReturnListFromMap() {
        LocaleUtils.cCountriesByLanguage.put("en", Arrays.asList(new Locale("en", "US")));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"));
        assertEquals(expected, result);
    }
    
    @Test
    public void testCountriesByLanguage_LanguageCodeNotNullAndNotInMap_ReturnListFromLocales() {
        List<Locale> locales = Arrays.asList(
            new Locale("en", ""),
            new Locale("fr", "FR")
        );
        List<Locale> expected = Arrays.asList(new Locale("en", ""));
        LocaleUtils.cCountriesByLanguage.clear();
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);
    }
}