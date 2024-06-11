import org.junit.*;
import java.util.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullString_ReturnNull() {
        Locale result = LocaleUtils.toLocale(null);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnDefaultLocale() {
        Locale result = LocaleUtils.toLocale("");
        
        Assert.assertEquals("", result.getLanguage());
        Assert.assertEquals("", result.getCountry());
        Assert.assertEquals("", result.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("##");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LengthLessThanTwo_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LengthGreaterThanThree_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("abcd");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_Ch0NotUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("a_bc");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_OneOccurrence_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_ABC");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_TwoOccurrences_Ch0NotUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_AA_BC");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_TwoOccurrences_Ch1NotUpperCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_ab_BC");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThreeOccurrences_Ch0NotUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en_AA_BC_DE");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_Ch2NotUpperCase_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_ab_cde");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LengthLessThanFive_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_ABC_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_Ch3NotUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_ABC_123");
    }
    
    @Test
    public void testToLocale_ValidFormat_LanguageAndCountry_ReturnLocale() {
        Locale result = LocaleUtils.toLocale("en_US");
        
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("US", result.getCountry());
        Assert.assertEquals("", result.getVariant());
    }
    
    @Test
    public void testToLocale_ValidFormat_LanguageCountryAndVariant_ReturnLocale() {
        Locale result = LocaleUtils.toLocale("en_US_variant");
        
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("US", result.getCountry());
        Assert.assertEquals("variant", result.getVariant());
    }
    
    @Test
    public void testToLocale_ValidFormat_LanguageAndVariant_ReturnLocaleWithEmptyCountry() {
        Locale result = LocaleUtils.toLocale("en__variant");
        
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("", result.getCountry());
        Assert.assertEquals("variant", result.getVariant());
    }
    
    @Test
    public void testLocaleLookupList_LocaleNotNull_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US", "variant");
        Locale defaultLocale = new Locale("fr", "FR");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.contains(locale));
        Assert.assertTrue(result.contains(defaultLocale));
    }
    
    @Test
    public void testLocaleLookupList_LocaleVariantNotEmpty_ReturnListWithLocaleAndLocaleWithEmptyVariant() {
        Locale locale = new Locale("en", "US", "variant");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(locale));
        Assert.assertTrue(result.contains(new Locale("en", "US")));
    }
    
    @Test
    public void testLocaleLookupList_LocaleCountryNotEmpty_ReturnListWithLocaleAndLocaleWithEmptyCountry() {
        Locale locale = new Locale("en", "US");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(locale));
        Assert.assertTrue(result.contains(new Locale("en", "")));
    }
    
    @Test
    public void testLocaleLookupList_LocaleNull_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleList_ReturnAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        Assert.assertEquals(Locale.getAvailableLocales().length, result.size());
        for (Locale locale : Locale.getAvailableLocales()) {
            Assert.assertTrue(result.contains(locale));
        }
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        Assert.assertEquals(Locale.getAvailableLocales().length, result.size());
        for (Locale locale : Locale.getAvailableLocales()) {
            Assert.assertTrue(result.contains(locale));
        }
    }
    
    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        Locale locale = Locale.getDefault();
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_NotAvailableLocale_ReturnFalse() {
        Locale locale = new Locale("xyz", "XYZ");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_CountryCodeNotAvailable_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("XYZ");
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test(timeout=1000)
    public void testLanguagesByCountry_CountryCodeValid_ReturnListWithLocales() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        
        Assert.assertFalse(result.isEmpty());
        for (Locale locale : result) {
            Assert.assertEquals("US", locale.getCountry());
            Assert.assertFalse(locale.getVariant().isEmpty());
        }
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_LanguageCodeNotAvailable_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("xxx");
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test(timeout=1000)
    public void testCountriesByLanguage_LanguageCodeValid_ReturnListWithLocales() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        
        Assert.assertFalse(result.isEmpty());
        for (Locale locale : result) {
            Assert.assertEquals("en", locale.getLanguage());
            Assert.assertFalse(locale.getCountry().isEmpty());
            Assert.assertFalse(locale.getVariant().isEmpty());
        }
    }
}