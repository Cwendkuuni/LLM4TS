import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    // Testing toLocale() method
    
    @Test
    public void testToLocaleWithValidLocaleString() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);
    }
    
    @Test
    public void testToLocaleWithValidLocaleStringAndVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
    }
    
    @Test
    public void testToLocaleWithValidLocaleStringAndSingleLetterLanguageCode() {
        Locale locale = LocaleUtils.toLocale("a_GB");
        assertEquals(new Locale("a", "GB"), locale);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidLocaleString() {
        LocaleUtils.toLocale("en_GB_xx");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidLocaleStringAndLowercaseCountryCode() {
        LocaleUtils.toLocale("en_gb");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidLocaleStringAndUppercaseLanguageCode() {
        LocaleUtils.toLocale("EN_GB");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidLocaleStringAndInvalidSeparator() {
        LocaleUtils.toLocale("en-GB");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidLocaleFormat() {
        LocaleUtils.toLocale("en");
    }
    
    @Test
    public void testToLocaleWithNullInput() {
        Locale locale = LocaleUtils.toLocale(null);
        assertNull(locale);
    }
    
    // Testing localeLookupList() method
    
    @Test
    public void testLocaleLookupListWithValidLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        
        List<Locale> resultList = LocaleUtils.localeLookupList(locale);
        
        assertEquals(expectedList, resultList);
    }
    
    @Test
    public void testLocaleLookupListWithValidLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(new Locale("en"));
        
        List<Locale> resultList = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(expectedList, resultList);
    }
    
    @Test
    public void testLocaleLookupListWithNullLocale() {
        List<Locale> resultList = LocaleUtils.localeLookupList(null);
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }
    
    // Testing availableLocaleList() method
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        
        assertNotNull(availableLocales);
        assertTrue(availableLocales.size() > 0);
    }
    
    // Testing availableLocaleSet() method
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        
        assertNotNull(availableLocales);
        assertTrue(availableLocales.size() > 0);
    }
    
    // Testing isAvailableLocale() method
    
    @Test
    public void testIsAvailableLocaleWithAvailableLocale() {
        Locale locale = new Locale("en", "GB");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocaleWithUnavailableLocale() {
        Locale locale = new Locale("xx", "YY");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    // Testing languagesByCountry() method
    
    @Test
    public void testLanguagesByCountryWithValidCountryCode() {
        List<Locale> expectedLanguages = new ArrayList<Locale>();
        expectedLanguages.add(new Locale("en_GB"));
        expectedLanguages.add(new Locale("de_GB"));
        expectedLanguages.add(new Locale("fr_GB"));
        
        List<Locale> languages = LocaleUtils.languagesByCountry("GB");
        
        assertEquals(expectedLanguages, languages);
    }
    
    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("XX");
        
        assertNotNull(languages);
        assertEquals(0, languages.size());
    }
    
    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        
        assertNotNull(languages);
        assertEquals(0, languages.size());
    }
    
    // Testing countriesByLanguage() method
    
    @Test
    public void testCountriesByLanguageWithValidLanguageCode() {
        List<Locale> expectedCountries = new ArrayList<Locale>();
        expectedCountries.add(new Locale("en_GB"));
        expectedCountries.add(new Locale("en_US"));
        expectedCountries.add(new Locale("en_CA"));
        
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        
        assertEquals(expectedCountries, countries);
    }
    
    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("XX");
        
        assertNotNull(countries);
        assertEquals(0, countries.size());
    }
    
    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        
        assertNotNull(countries);
        assertEquals(0, countries.size());
    }
}