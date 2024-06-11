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
    public void testToLocale_EmptyString_ReturnDefaultLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("invalid#format");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LengthLessThanTwo_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_FirstCharacterNotUnderscore_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("xyz");
    }
    
    @Test
    public void testToLocale_ValidFormat_OneOccurrenceLowerCase_ReturnLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_OneOccurrenceLowerCase_InvalidLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("enx");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_OneOccurrenceLowerCase_NotAllLowerCase_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("En");
    }
    
    @Test
    public void testToLocale_ValidFormat_TwoOccurrencesLowerCase_ReturnLocale() {
        assertEquals(new Locale("de", "DE"), LocaleUtils.toLocale("de_DE"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_TwoOccurrencesLowerCase_InvalidLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("de_DEx");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_TwoOccurrencesLowerCase_NotAllLowerCase_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("de_DEx");
    }
    
    @Test
    public void testToLocale_ValidFormat_ThreeOccurrencesLowerCase_ReturnLocale() {
        assertEquals(new Locale("en", "US", "variant"), LocaleUtils.toLocale("en_US_variant"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThreeOccurrencesLowerCase_FirstNotAllLowerCase_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("En_US_variant");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThreeOccurrencesLowerCase_FirstInvalidLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("enu_US_variant");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThreeOccurrencesLowerCase_SecondNotAllUpperCase_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_us_variant");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThreeOccurrencesLowerCase_SecondInvalidLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_USx_variant");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThreeOccurrencesLowerCase_ThirdEmpty_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThreeOccurrencesLowerCase_ThirdEmptyWithValidLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_x");
    }
    
    @Test
    public void testToLocale_ValidFormat_FirstThreeCharactersUpperCase_ReturnLocale() {
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_FirstThreeCharactersUpperCase_InvalidLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_USA");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_FirstThreeCharactersUpperCase_SecondNotUpperCase_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_us");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_FirstThreeCharactersUpperCase_ThirdNotUpperCase_Empty_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_US_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_FirstThreeCharactersUpperCase_InvalidLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_US_xxx");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_FirstThreeCharactersUpperCase_InvalidFormat_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_US123");
    }
    
    @Test
    public void testLocaleLookupList_SingleLocale_ReturnListWithSingleLocale() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en"));
        
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("en"));
        
        assertEquals(expected, result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithVariant_ReturnListWithTwoLocales() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en"));
        expected.add(new Locale("en", "US"));
        
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("en", "US"));
        
        assertEquals(expected, result);
        assertEquals(2, result.size());
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithCountry_ReturnListWithTwoLocales() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en"));
        expected.add(new Locale("en", ""));
        
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("en", "US"));
        
        assertEquals(expected, result);
        assertEquals(2, result.size());
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithCountryAndVariant_ReturnListWithThreeLocales() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en"));
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "US", "variant"));
        
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("en", "US", "variant"));
        
        assertEquals(expected, result);
        assertEquals(3, result.size());
    }
    
    @Test
    public void testAvailableLocaleList_ReturnListWithAllAvailableLocales() {
        List<Locale> expected = SyncAvoid.AVAILABLE_LOCALE_LIST;
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        assertEquals(expected.size(), result.size());
        assertTrue(result.containsAll(expected));
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnSetWithAllAvailableLocales() {
        Set<Locale> expected = SyncAvoid.AVAILABLE_LOCALE_SET;
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        assertEquals(expected.size(), result.size());
        assertTrue(result.containsAll(expected));
    }
    
    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_NonAvailableLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        
        assertEquals(expected, result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListOfLanguages() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en"));
        
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        
        assertEquals(expected, result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        
        assertEquals(expected, result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListOfCountries() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("", "US"));
        
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        
        assertEquals(expected, result);
        assertEquals(1, result.size());
    }
    
}