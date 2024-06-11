import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_ValidInput_ReturnsLocaleObject() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("eng");
    }
    
    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testLocaleLookupList_SingleLocale_ReturnsListWithSingleLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(1, LocaleUtils.localeLookupList(locale).size());
        assertEquals(locale, LocaleUtils.localeLookupList(locale).get(0));
    }
    
    @Test
    public void testLocaleLookupList_ValidInput_ReturnsExpectedList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testLocaleLookupList_NullInput_ReturnsEmptyList() {
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        try {
            availableLocales.add(new Locale("es"));
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        try {
            availableLocales.add(new Locale("es"));
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsAvailableLocale_ValidLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
    }
    
    @Test
    public void testIsAvailableLocale_InvalidLocale_ReturnsFalse() {
        assertTrue(!LocaleUtils.isAvailableLocale(new Locale("es")));
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsCorrectList() {
        List<Locale> expectedList = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("es"));
        assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode_ReturnsEmptyList() {
        assertTrue(LocaleUtils.languagesByCountry("ZZ").isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsCorrectList() {
        List<Locale> expectedList = Arrays.asList(new Locale("CA"), new Locale("FR"));
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_ReturnsEmptyList() {
        assertTrue(LocaleUtils.countriesByLanguage("ZZ").isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }
}