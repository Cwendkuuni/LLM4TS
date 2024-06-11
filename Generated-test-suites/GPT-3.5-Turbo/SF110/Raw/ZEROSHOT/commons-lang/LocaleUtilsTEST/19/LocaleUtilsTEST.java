import org.junit.Test;
import org.junit.Assert;
import java.util.concurrent.*;
import java.util.*;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale_NullInput_ReturnNull() {
    Locale result = LocaleUtils.toLocale(null);
    Assert.assertNull(result);
  }
  
  @Test
  public void testToLocale_EmptyInput_ReturnLocaleWithEmptyLanguageAndCountry() {
    Locale result = LocaleUtils.toLocale("");
    Assert.assertEquals("", result.getLanguage());
    Assert.assertEquals("", result.getCountry());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
    LocaleUtils.toLocale("en#US");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithLessThan2Length_ThrowIllegalArgumentException() {
    LocaleUtils.toLocale("e");
  }
  
  @Test
  public void testToLocale_ValidFormatWithAllLowerCase_ReturnLocaleWithSingleParameter() {
    Locale result = LocaleUtils.toLocale("en");
    Assert.assertEquals("en", result.getLanguage());
    Assert.assertEquals("", result.getCountry());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithLowerCaseLanguageAndUpperCaseCountry_ThrowIllegalArgumentException() {
    LocaleUtils.toLocale("en_US");
  }
  
  @Test
  public void testToLocale_ValidFormatWithLowerCaseLanguageAndUpperCaseCountry_ReturnLocaleWithTwoParameters() {
    Locale result = LocaleUtils.toLocale("en_US");
    Assert.assertEquals("en", result.getLanguage());
    Assert.assertEquals("US", result.getCountry());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithLowerCaseLanguageAndEmptyCountry_ThrowIllegalArgumentException() {
    LocaleUtils.toLocale("en_");
  }
  
  @Test
  public void testToLocale_ValidFormatWithLowerCaseLanguageEmptyCountryAndNonEmptyVariant_ReturnLocaleWithThreeParameters() {
    Locale result = LocaleUtils.toLocale("en__variant");
    Assert.assertEquals("en", result.getLanguage());
    Assert.assertEquals("", result.getCountry());
    Assert.assertEquals("variant", result.getVariant());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithUnderscoreAtSecondPosition_ThrowIllegalArgumentException() {
    LocaleUtils.toLocale("_EN_variant");
  }
  
  @Test
  public void testToLocale_ValidFormatWithUnderscoreAtSecondPosition_ReturnLocaleWithLanguageAndCountry() {
    Locale result = LocaleUtils.toLocale("_EN_variant");
    Assert.assertEquals("", result.getLanguage());
    Assert.assertEquals("EN", result.getCountry());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithLessThan5Length_ThrowIllegalArgumentException() {
    LocaleUtils.toLocale("_EN_");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormatWithUnderscoreAtFourthPosition_ThrowIllegalArgumentException() {
    LocaleUtils.toLocale("_EN");
  }
  
  @Test
  public void testToLocale_ValidFormatWithUnderscoreAtFourthPosition_ReturnLocaleWithLanguageAndVariant() {
    Locale result = LocaleUtils.toLocale("_EN_variant");
    Assert.assertEquals("", result.getLanguage());
    Assert.assertEquals("EN", result.getCountry());
    Assert.assertEquals("variant", result.getVariant());
  }
  
  @Test
  public void testLocaleLookupList_SingleLocale_ReturnListWithSingleLocale() {
    Locale locale = new Locale("en", "US");
    List<Locale> result = LocaleUtils.localeLookupList(locale);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals(locale, result.get(0));
  }
  
  @Test
  public void testLocaleLookupList_LocaleWithVariant_ReturnListWithLocaleAndLocaleWithSameLanguageAndCountry() {
    Locale locale = new Locale("en", "US", "variant");
    List<Locale> result = LocaleUtils.localeLookupList(locale);
    Assert.assertEquals(2, result.size());
    Assert.assertEquals(locale, result.get(0));
    Locale expectedLocale = new Locale("en", "US");
    Assert.assertEquals(expectedLocale, result.get(1));
  }
  
  @Test
  public void testLocaleLookupList_LocaleWithCountry_ReturnListWithLocaleAndLocaleWithSameLanguageAndEmptyCountry() {
    Locale locale = new Locale("en", "US");
    List<Locale> result = LocaleUtils.localeLookupList(locale);
    Assert.assertEquals(2, result.size());
    Assert.assertEquals(locale, result.get(0));
    Locale expectedLocale = new Locale("en", "");
    Assert.assertEquals(expectedLocale, result.get(1));
  }
  
  @Test
  public void testLocaleLookupList_LocaleWithCountry_ReturnListWithLocaleAndLocaleWithSameLanguageAndEmptyCountryAndDefaultLocale() {
    Locale locale = new Locale("en", "US");
    Locale defaultLocale = new Locale("fr", "FR");
    List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
    Assert.assertEquals(3, result.size());
    Assert.assertEquals(locale, result.get(0));
    Locale expectedLocale = new Locale("en", "");
    Assert.assertEquals(expectedLocale, result.get(1));
    Assert.assertEquals(defaultLocale, result.get(2));
  }
  
  @Test
  public void testAvailableLocaleList_ReturnListOfAvailableLocales() {
    List<Locale> result = LocaleUtils.availableLocaleList();
    Assert.assertNotNull(result);
    // Add assertions for expected Locale values
  }
  
  @Test
  public void testAvailableLocaleSet_ReturnSetOfAvailableLocales() {
    Set<Locale> result = LocaleUtils.availableLocaleSet();
    Assert.assertNotNull(result);
    // Add assertions for expected Locale values
  }
  
  @Test
  public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
    Locale locale = new Locale("en", "US");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsAvailableLocale_UnavailableLocale_ReturnFalse() {
    Locale locale = new Locale("xx", "XX");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
    List<Locale> result = LocaleUtils.languagesByCountry(null);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }
  
  @Test
  public void testLanguagesByCountry_ValidCountryCode_ReturnListOfLanguages() {
    List<Locale> result = LocaleUtils.languagesByCountry("US");
    Assert.assertNotNull(result);
    // Add assertions for expected Locale values
  }
  
  @Test
  public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
    List<Locale> result = LocaleUtils.countriesByLanguage(null);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguage_ValidLanguageCode_ReturnListOfCountries() {
    List<Locale> result = LocaleUtils.countriesByLanguage("en");
    Assert.assertNotNull(result);
    // Add assertions for expected Locale values
  }
  
}