import org.junit.Test;
import java.util.concurrent.*;
import java.util.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnEmptyLocale() {
        Locale emptyLocale = new Locale("", "");
        assertEquals(emptyLocale, LocaleUtils.toLocale(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowException() {
        LocaleUtils.toLocale("en_US#");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan2_ThrowException() {
        LocaleUtils.toLocale("a");
    }
    
    @Test
    public void testToLocale_ValidFormatWithOnlyLanguage_ReturnLocaleWithLanguage() {
        Locale expectedLocale = new Locale("en");
        assertEquals(expectedLocale, LocaleUtils.toLocale("en"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidLanguage_ThrowException() {
        LocaleUtils.toLocale("aBC");
    }
    
    @Test
    public void testToLocale_ValidFormatWithLanguageAndCountry_ReturnLocaleWithLanguageAndCountry() {
        Locale expectedLocale = new Locale("en", "US");
        assertEquals(expectedLocale, LocaleUtils.toLocale("en_US"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidLanguageOrCountry_ThrowException() {
        LocaleUtils.toLocale("En_us");
    }
    
    @Test
    public void testToLocale_ValidFormatWithLanguageCountryAndVariant_ReturnLocaleWithLanguageCountryAndVariant() {
        Locale expectedLocale = new Locale("en", "US", "POSIX");
        assertEquals(expectedLocale, LocaleUtils.toLocale("en_US_POSIX"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithMissingVariant_ThrowException() {
        LocaleUtils.toLocale("en_US_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidVariant_ThrowException() {
        LocaleUtils.toLocale("en_US_POSIX_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidLocale_ThrowException() {
        LocaleUtils.toLocale("en_US_POSIX_XYZ");
    }
    
    @Test
    public void testLocaleLookupList_WithLocale_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(locale);
        expectedList.add(new Locale("en"));
        expectedList.add(new Locale("", "US"));
        expectedList.add(defaultLocale);
        
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testLocaleLookupList_WithNullLocale_ReturnEmptyList() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.localeLookupList(null));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        assertEquals(expectedList, actualList);
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnAvailableLocaleSet() {
        Set<Locale> expectedSet = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedSet, actualSet);
    }
    
    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnFalse() {
        Locale locale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnListOfLanguages() {
        String countryCode = "US";
        List<Locale> expectedLanguages = new ArrayList<>();
        expectedLanguages.add(new Locale("en"));
        expectedLanguages.add(new Locale("es"));
        
        assertEquals(expectedLanguages, LocaleUtils.languagesByCountry(countryCode));
    }
    
    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ReturnEmptyList() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnListOfCountries() {
        String languageCode = "en";
        List<Locale> expectedCountries = new ArrayList<>();
        expectedCountries.add(new Locale("US"));
        expectedCountries.add(new Locale("GB"));
        
        assertEquals(expectedCountries, LocaleUtils.countriesByLanguage(languageCode));
    }
    
    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ReturnEmptyList() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(null));
    }
}