import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_EmptyString() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }
    
    @Test
    public void testToLocale_NullString() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHash() {
        LocaleUtils.toLocale("en#US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatShortString() {
        LocaleUtils.toLocale("e");
    }
    
    @Test
    public void testToLocale_ValidFormat_LanguageOnly() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
    }
    
    @Test
    public void testToLocale_ValidFormat_LanguageAndCountry() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
    }
    
    @Test
    public void testToLocale_ValidFormat_LanguageCountryAndVariant() {
        assertEquals(new Locale("en", "US", "variant"), LocaleUtils.toLocale("en_US_variant"));
    }
    
    @Test
    public void testLocaleLookupList_NullLocale() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }
    
    @Test
    public void testLocaleLookupList_LanguageOnly() {
        Locale locale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("", "")); // should also contain default locale
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    }
    
    @Test
    public void testLocaleLookupList_LanguageAndCountry() {
        Locale locale = new Locale("en", "US");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("en", ""));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    }
    
    @Test
    public void testLocaleLookupList_LanguageCountryAndVariant() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    }
    
    @Test
    public void testLocaleLookupList_NullLocales() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null, null));
    }
    
    @Test
    public void testLocaleLookupList_DefaultLocaleNotInList() {
        Locale locale = new Locale("en");
        Locale defaultLocale = new Locale("fr"); // not in the list
        List<Locale> expectedList = Arrays.asList(locale, new Locale("", ""), defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList()); // check if list is not null
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet()); // check if set is not null
    }
    
    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }
    
    @Test
    public void testIsAvailableLocale_NotAvailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr"))); // assuming "fr" is not available
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XYZ")); // assuming "XYZ" is not a valid country code
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        assertEquals(Arrays.asList(new Locale("en")), LocaleUtils.languagesByCountry("US")); // assuming "US" is a valid country code
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("XYZ")); // assuming "XYZ" is not a valid language code
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        assertEquals(Arrays.asList(new Locale("US")), LocaleUtils.countriesByLanguage("en")); // assuming "en" is a valid language code
    }
    
}