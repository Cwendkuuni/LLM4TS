import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {
    
    // Test cases for toLocale(String str)
    @Test
    public void testToLocale_ValidInput_TwoCharacterLanguageCode() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), result);
    }
    
    @Test
    public void testToLocale_ValidInput_TwoCharacterLanguageCode_CountryCode() {
        Locale result = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), result);
    }
    
    @Test
    public void testToLocale_ValidInput_TwoCharacterLanguageCode_CountryCode_Variant() {
        Locale result = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_LengthLessThanTwo() {
        LocaleUtils.toLocale("e");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_InvalidLanguageCode() {
        LocaleUtils.toLocale("12_GB");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_InvalidCountryCode() {
        LocaleUtils.toLocale("en_gb");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_InvalidVariant() {
        LocaleUtils.toLocale("en_GB_123");
    }
    
    // Test cases for localeLookupList(Locale locale)
    @Test
    public void testLocaleLookupList_SingleLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr")
        );
        assertEquals(expected, result);
    }
    
    // Test cases for localeLookupList(Locale locale, Locale defaultLocale)
    @Test
    public void testLocaleLookupList_MatchDefaultLocale() {
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
    public void testLocaleLookupList_NoMatchDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("es");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("es")
        );
        assertEquals(expected, result);
    }
    
    // Test cases for availableLocaleList()
    @Test
    public void testAvailableLocaleList_NotEmpty() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertFalse(result.isEmpty());
    }
    
    // Test cases for availableLocaleSet()
    @Test
    public void testAvailableLocaleSet_NotEmpty() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertFalse(result.isEmpty());
    }
    
    // Test cases for isAvailableLocale(Locale locale)
    @Test
    public void testIsAvailableLocale_ValidLocale_True() {
        Locale locale = Locale.US;
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_InvalidLocale_False() {
        Locale locale = new Locale("xyz");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }
    
    // Test cases for languagesByCountry(String countryCode)
    @Test
    public void testLanguagesByCountry_ExistingCountryCode() {
        String countryCode = "CA";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(
            new Locale("en", "CA"),
            new Locale("fr", "CA")
        );
        assertEquals(expected, result);
    }
    
    @Test
    public void testLanguagesByCountry_NonExistingCountryCode() {
        String countryCode = "XY";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }
    
    // Test cases for countriesByLanguage(String languageCode)
    @Test
    public void testCountriesByLanguage_ExistingLanguageCode() {
        String languageCode = "fr";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA")
        );
        assertEquals(expected, result);
    }
    
    @Test
    public void testCountriesByLanguage_NonExistingLanguageCode() {
        String languageCode = "xy";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }

}