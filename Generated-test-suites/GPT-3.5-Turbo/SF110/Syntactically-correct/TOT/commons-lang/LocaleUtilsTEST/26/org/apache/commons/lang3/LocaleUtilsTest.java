import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
  
  // Test for toLocale method
  
  // Test for null input
  @Test
  public void testToLocale_NullInput() {
    assertNull(LocaleUtils.toLocale(null));
  }
  
  // Test for empty input
  @Test
  public void testToLocale_EmptyInput() {
    Locale expectedLocale = new Locale("", "");
    assertEquals(expectedLocale, LocaleUtils.toLocale(""));
  }
  
  // Test for invalid format with #
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithHash() {
    LocaleUtils.toLocale("en#US");
  }
  
  // Test for invalid format with less than 2 characters
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithLessThan2Chars() {
    LocaleUtils.toLocale("e");
  }
  
  // Test for invalid format with invalid characters as first character
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithInvalidFirstChar() {
    LocaleUtils.toLocale("#en");
  }
  
  // Test for valid format with language code only
  @Test
  public void testToLocale_ValidFormatWithLanguageCodeOnly() {
    Locale expectedLocale = new Locale("en");
    assertEquals(expectedLocale, LocaleUtils.toLocale("en"));
  }
  
  // Test for invalid format with lowercase language code and invalid length
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithLowerCaseLanguageCodeAndInvalidLength() {
    LocaleUtils.toLocale("ena");
  }
  
  // Test for invalid format with uppercase country code
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithUpperCaseCountryCode() {
    LocaleUtils.toLocale("en_US");
  }
  
  // Test for valid format with language code and uppercase country code
  @Test
  public void testToLocale_ValidFormatWithLanguageCodeAndUpperCaseCountryCode() {
    Locale expectedLocale = new Locale("en", "US");
    assertEquals(expectedLocale, LocaleUtils.toLocale("en_US"));
  }
  
  // Test for invalid format with invalid second part length
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithInvalidSecondPartLength() {
    LocaleUtils.toLocale("en__US");
  }
  
  // Test for valid format with language code, country code and variant
  @Test
  public void testToLocale_ValidFormatWithLanguageCodeCountryCodeAndVariant() {
    Locale expectedLocale = new Locale("en", "US", "variant");
    assertEquals(expectedLocale, LocaleUtils.toLocale("en_US_variant"));
  }
  
  
  // Test for localeLookupList method
  
  // Test for null input
  @Test
  public void testLocaleLookupList_NullInput() {
    Locale defaultLocale = new Locale("en", "US");
    assertNotNull(LocaleUtils.localeLookupList(null, defaultLocale));
  }
  
  // Test for valid locale and defaultLocale
  @Test
  public void testLocaleLookupList_ValidLocaleAndDefaultLocale() {
    Locale locale = new Locale("en", "US");
    Locale defaultLocale = new Locale("en", "GB");
    assertNotNull(LocaleUtils.localeLookupList(locale, defaultLocale));
  }
  
  // Test for empty variant in locale
  @Test
  public void testLocaleLookupList_EmptyVariantInLocale() {
    Locale locale = new Locale("en", "US", "");
    Locale defaultLocale = new Locale("en", "GB");
    assertNotNull(LocaleUtils.localeLookupList(locale, defaultLocale));
  }
  
  // Test for empty country in locale
  @Test
  public void testLocaleLookupList_EmptyCountryInLocale() {
    Locale locale = new Locale("en", "", "");
    Locale defaultLocale = new Locale("en", "GB");
    assertNotNull(LocaleUtils.localeLookupList(locale, defaultLocale));
  }
  
  // Test for locale not equal to defaultLocale
  @Test
  public void testLocaleLookupList_LocaleNotEqualToDefaultLocale() {
    Locale locale = new Locale("en", "US", "");
    Locale defaultLocale = new Locale("en", "GB");
    assertTrue(LocaleUtils.localeLookupList(locale, defaultLocale).contains(defaultLocale));
  }
  
  
  
  // Test for availableLocaleList method
  
  // Test for non-null return value
  @Test
  public void testAvailableLocaleList_NonNullReturnValue() {
    assertNotNull(LocaleUtils.availableLocaleList());
  }
  
  
  
  // Test for availableLocaleSet method
  
  // Test for non-null return value
  @Test
  public void testAvailableLocaleSet_NonNullReturnValue() {
    assertNotNull(LocaleUtils.availableLocaleSet());
  }
  
  
  
  // Test for isAvailableLocale method
  
  // Test for null input
  @Test
  public void testIsAvailableLocale_NullInput() {
    assertFalse(LocaleUtils.isAvailableLocale(null));
  }
  
  // Test for available locale
  @Test
  public void testIsAvailableLocale_AvailableLocale() {
    Locale locale = new Locale("en", "US");
    assertTrue(LocaleUtils.isAvailableLocale(locale));
  }
  
  // Test for unavailable locale
  @Test
  public void testIsAvailableLocale_UnavailableLocale() {
    Locale locale = new Locale("xx", "YY");
    assertFalse(LocaleUtils.isAvailableLocale(locale));
  }
  
  
  
  // Test for languagesByCountry method
  
  // Test for null country code
  @Test
  public void testLanguagesByCountry_NullCountryCode() {
    assertNotNull(LocaleUtils.languagesByCountry(null));
  }
  
  // Test for empty country code
  @Test
  public void testLanguagesByCountry_EmptyCountryCode() {
    assertNotNull(LocaleUtils.languagesByCountry(""));
  }
  
  // Test for non-existent country code
  @Test
  public void testLanguagesByCountry_NonExistentCountryCode() {
    assertEquals(0, LocaleUtils.languagesByCountry("XX").size());
  }
  
  // Test for existing country code with no variant
  @Test
  public void testLanguagesByCountry_ExistingCountryCodeNoVariant() {
    assertEquals(1, LocaleUtils.languagesByCountry("US").size());
  }
  
  // Test for existing country code with variant
  @Test
  public void testLanguagesByCountry_ExistingCountryCodeWithVariant() {
    assertEquals(0, LocaleUtils.languagesByCountry("US_variant").size());
  }
  
  
  
  // Test for countriesByLanguage method
  
  // Test for null language code
  @Test
  public void testCountriesByLanguage_NullLanguageCode() {
    assertNotNull(LocaleUtils.countriesByLanguage(null));
  }
  
  // Test for empty language code
  @Test
  public void testCountriesByLanguage_EmptyLanguageCode() {
    assertNotNull(LocaleUtils.countriesByLanguage(""));
  }
  
  // Test for non-existent language code
  @Test
  public void testCountriesByLanguage_NonExistentLanguageCode() {
    assertEquals(0, LocaleUtils.countriesByLanguage("xx").size());
  }
  
  // Test for existing language code with empty country and variant
  @Test
  public void testCountriesByLanguage_ExistingLanguageCodeEmptyCountryAndVariant() {
    assertEquals(0, LocaleUtils.countriesByLanguage("en").size());
  }
  
  // Test for existing language code with country and variant
  @Test
  public void testCountriesByLanguage_ExistingLanguageCodeCountryAndVariant() {
    assertEquals(0, LocaleUtils.countriesByLanguage("en_US_variant").size());
  }
}