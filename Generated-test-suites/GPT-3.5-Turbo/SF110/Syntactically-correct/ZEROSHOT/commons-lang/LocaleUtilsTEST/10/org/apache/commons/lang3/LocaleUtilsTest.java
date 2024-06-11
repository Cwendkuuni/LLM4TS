import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnsNull() {
        Locale locale = LocaleUtils.toLocale(null);
        assertNull(locale);
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnsEmptyLocale() {
        Locale locale = LocaleUtils.toLocale("");
        assertNotNull(locale);
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsException() {
        LocaleUtils.toLocale("#");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLength_ThrowsException() {
        LocaleUtils.toLocale("a");
    }
    
    @Test
    public void testToLocale_ValidLanguageCode_ReturnsLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test
    public void testToLocale_ValidLanguageAndCountryCode_ReturnsLocale() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryAndVariant_ReturnsLocale() {
        Locale locale = LocaleUtils.toLocale("en_US_variant");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("variant", locale.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCountryCode_ThrowsException() {
        LocaleUtils.toLocale("en_");
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsLookupList() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        
        assertNotNull(list);
        assertEquals(4, list.size());
        assertTrue(list.contains(locale));
        assertTrue(list.contains(new Locale("en", "US")));
        assertTrue(list.contains(new Locale("en", "")));
        assertTrue(list.contains(locale.getDefault()));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertTrue(set.size() > 0);
    }
    
    @Test
    public void testIsAvailableLocale_True() {
        Locale locale = new Locale("en");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocale_False() {
        Locale locale = new Locale("xy");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsLanguagesList() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsCountriesList() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
}