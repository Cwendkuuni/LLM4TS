import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    /*
     * Test case for toLocale() method
     * 
     * Test if the method successfully converts a valid locale string to a Locale object
     */
    @Test
    public void testToLocaleValidLocaleString() {
        String localeString = "en_GB";
        Locale expectedLocale = new Locale("en", "GB");
        
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        
        assertEquals(expectedLocale, actualLocale);
    }
    
    /*
     * Test case for toLocale() method
     * 
     * Test if the method throws an IllegalArgumentException when given an invalid locale string
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLocaleString() {
        String localeString = "invalid_locale";
        
        LocaleUtils.toLocale(localeString);
    }

    /*
     * Test case for localeLookupList(Locale) method
     * 
     * Test if the method correctly generates the expected list of locales to search through
     */
    @Test
    public void testLocaleLookupListValidLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(defaultLocale);
        
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(expectedList, actualList);
    }
    
    /*
     * Test case for localeLookupList(Locale) method
     * 
     * Test if the method returns a list containing only the given locale when the given locale is null
     */
    @Test
    public void testLocaleLookupListNullLocale() {
        Locale locale = null;
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(defaultLocale);
        
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(expectedList, actualList);
    }
    
    /*
     * Test case for availableLocaleList() method
     * 
     * Test if the method returns a non-null list of available locales
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        
        assertNotNull(availableLocales);
    }
    
    /*
     * Test case for availableLocaleSet() method
     * 
     * Test if the method returns a non-null set of available locales
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        
        assertNotNull(availableLocales);
    }
    
    /*
     * Test case for isAvailableLocale(Locale) method
     * 
     * Test if the method correctly returns true when the given locale is available
     */
    @Test
    public void testIsAvailableLocaleValidLocale() {
        Locale locale = new Locale("fr", "CA");
        
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        
        assertTrue(isAvailable);
    }
    
    /*
     * Test case for isAvailableLocale(Locale) method
     * 
     * Test if the method correctly returns false when the given locale is not available
     */
    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        Locale locale = new Locale("xx", "XX");
        
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        
        assertFalse(isAvailable);
    }
    
    /*
     * Test case for languagesByCountry(String) method
     * 
     * Test if the method correctly returns a list of languages supported for a given country
     */
    @Test
    public void testLanguagesByCountryValidCountryCode() {
        String countryCode = "CA";
        List<Locale> expectedLanguages = new ArrayList<>();
        expectedLanguages.add(new Locale("en", "CA"));
        expectedLanguages.add(new Locale("fr", "CA"));
        
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry(countryCode);
        
        assertEquals(expectedLanguages, actualLanguages);
    }
    
    /*
     * Test case for languagesByCountry(String) method
     * 
     * Test if the method returns an empty list when given an invalid country code
     */
    @Test
    public void testLanguagesByCountryInvalidCountryCode() {
        String countryCode = "XX";
        List<Locale> expectedLanguages = new ArrayList<>();
        
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry(countryCode);
        
        assertEquals(expectedLanguages, actualLanguages);
    }
    
    /*
     * Test case for countriesByLanguage(String) method
     * 
     * Test if the method correctly returns a list of countries supported for a given language
     */
    @Test
    public void testCountriesByLanguageValidLanguageCode() {
        String languageCode = "fr";
        List<Locale> expectedCountries = new ArrayList<>();
        expectedCountries.add(new Locale("fr", "CA"));
        expectedCountries.add(new Locale("fr", "FR"));
        
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage(languageCode);
        
        assertEquals(expectedCountries, actualCountries);
    }
    
    /*
     * Test case for countriesByLanguage(String) method
     * 
     * Test if the method returns an empty list when given an invalid language code
     */
    @Test
    public void testCountriesByLanguageInvalidLanguageCode() {
        String languageCode = "xx";
        List<Locale> expectedCountries = new ArrayList<>();
        
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage(languageCode);
        
        assertEquals(expectedCountries, actualCountries);
    }
}