import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    // Test cases for toLocale() method
    @Test
    public void testToLocale_NullString() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("Invalid#Format");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LengthLessThan2() {
        LocaleUtils.toLocale("I");
    }
    
    @Test
    public void testToLocale_Lowercase_2Characters() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
    }
    
    @Test
    public void testToLocale_Lowercase_3Characters() {
        assertEquals(new Locale("eng"), LocaleUtils.toLocale("eng"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_Lowercase_InvalidLength() {
        LocaleUtils.toLocale("engi");
    }
    
    @Test
    public void testToLocale_CountryCode_2Characters() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
    }
    
    @Test
    public void testToLocale_CountryCode_3Characters() {
        assertEquals(new Locale("eng", "USA"), LocaleUtils.toLocale("eng_USA"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_CountryCode_InvalidLength() {
        LocaleUtils.toLocale("eng_USAA");
    }
    
    @Test
    public void testToLocale_LanguageCountryVariant() {
        assertEquals(new Locale("eng", "", "variant"), LocaleUtils.toLocale("eng__variant"));
    }

    // Test cases for localeLookupList() method
    @Test
    public void testLocaleLookupList_DefaultLocaleNull() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.localeLookupList(locale, null).contains(locale));
    }
    
    @Test
    public void testLocaleLookupList_EmptyLocale() {
        Locale locale = new Locale("", "");
        assertTrue(LocaleUtils.localeLookupList(locale).isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_VariantNotNull() {
        Locale locale = new Locale("en", "US", "variant");
        assertTrue(LocaleUtils.localeLookupList(locale).contains(locale));
    }
    
    @Test
    public void testLocaleLookupList_CountryNotEmpty() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.localeLookupList(locale).contains(new Locale(locale.getLanguage(), "")));
    }

    // Test cases for availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    // Test cases for availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    // Test cases for isAvailableLocale() method
    @Test
    public void testIsAvailableLocale_True() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }
    
    @Test
    public void testIsAvailableLocale_False() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }

    // Test cases for languagesByCountry() method
    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        assertFalse(LocaleUtils.languagesByCountry("US").isEmpty());
    }

    // Test cases for countriesByLanguage() method
    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        assertFalse(LocaleUtils.countriesByLanguage("en").isEmpty());
    }
}