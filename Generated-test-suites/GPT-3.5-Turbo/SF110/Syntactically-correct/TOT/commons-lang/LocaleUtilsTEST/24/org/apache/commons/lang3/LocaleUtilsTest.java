import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

  @Test
  public void testToLocale_NullInput_ReturnNull() {
    assertNull(LocaleUtils.toLocale(null));
  }

  @Test
  public void testToLocale_EmptyInput_ReturnEmptyLocale() {
    Locale result = LocaleUtils.toLocale("");
    assertEquals("", result.getLanguage());
    assertEquals("", result.getCountry());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("#en_UK");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_LengthLessThan2_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("a");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_LanguageLowerCaseLengthNot2Or3_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("en_UK");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_LanguageUpperCaseLengthNot2Or3_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("EN_UK");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_LanguageLength2_CountryUpperCaseLengthNot2_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("enUK");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_LanguageLength2_CountryUpperCaseLength0_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("en_");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_LanguageLength2_CountryUpperCaseLength2_InvalidThirdComponent_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("en_UK_invalid");
  }

  @Test
  public void testToLocale_ValidFormat_LanguageLength2_CountryUpperCaseLength2_ReturnLocale() {
    Locale result = LocaleUtils.toLocale("en_UK");
    assertEquals("en", result.getLanguage());
    assertEquals("UK", result.getCountry());
  }

  @Test
  public void testToLocale_ValidFormat_LanguageLength2_CountryUpperCaseLength0_ReturnLocale() {
    Locale result = LocaleUtils.toLocale("en_");
    assertEquals("en", result.getLanguage());
    assertEquals("", result.getCountry());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_StartsWithUnderscoreLengthLessThan3_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("_U");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_SecondCharacterNotUpperCase_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("_uU");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_ThirdCharacterNotUpperCase_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("_Uu");
  }

  @Test
  public void testToLocale_ValidFormat_Length3WithCountry_ReturnLocale() {
    Locale result = LocaleUtils.toLocale("_UK");
    assertEquals("", result.getLanguage());
    assertEquals("UK", result.getCountry());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_LengthLessThan5_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("_UKinvalid");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_FourthCharacterNotUnderscore_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("_UKinvalid");
  }

  @Test
  public void testToLocale_ValidFormat_LengthGreaterThan4_ReturnLocale() {
    Locale result = LocaleUtils.toLocale("_UK_invalid");
    assertEquals("", result.getLanguage());
    assertEquals("UK", result.getCountry());
    assertEquals("invalid", result.getVariant());
  }

  @Test
  public void testLocaleLookupList_WithDefaultLocale_ReturnListContainingLocaleAndDefaultLocale() {
    Locale locale = new Locale("en", "US", "variant");
    Locale defaultLocale = new Locale("fr", "FR");
    List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
    
    assertEquals(4, result.size());
    assertTrue(result.contains(locale));
    assertTrue(result.contains(defaultLocale));
  }

  @Test
  public void testLocaleLookupList_WithoutDefaultLocale_ReturnListContainingLocale() {
    Locale locale = new Locale("en", "US", "variant");
    List<Locale> result = LocaleUtils.localeLookupList(locale);
    
    assertEquals(1, result.size());
    assertTrue(result.contains(locale));
  }

  @Test
  public void testAvailableLocaleList_ReturnListOfAvailableLocales() {
    List<Locale> result = LocaleUtils.availableLocaleList();

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  public void testAvailableLocaleSet_ReturnSetOfAvailableLocales() {
    Set<Locale> result = LocaleUtils.availableLocaleSet();
    
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
    Locale locale = Locale.getDefault();
    assertTrue(LocaleUtils.isAvailableLocale(locale));
  }

  @Test
  public void testIsAvailableLocale_NonAvailableLocale_ReturnFalse() {
    Locale locale = new Locale("invalid", "invalid");
    assertFalse(LocaleUtils.isAvailableLocale(locale));
  }

  @Test
  public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
    List<Locale> result = LocaleUtils.languagesByCountry(null);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testLanguagesByCountry_ValidCountryCode_ReturnListContainingLocalesWithMatchingCountryCode() {
    List<Locale> result = LocaleUtils.languagesByCountry("US");
    
    assertNotNull(result);
    assertFalse(result.isEmpty());
    for (Locale locale : result) {
      assertEquals("US", locale.getCountry());
      assertTrue(locale.getVariant().isEmpty());
    }
  }

  @Test
  public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
    List<Locale> result = LocaleUtils.countriesByLanguage(null);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testCountriesByLanguage_ValidLanguageCode_ReturnListContainingLocalesWithMatchingLanguageCode() {
    List<Locale> result = LocaleUtils.countriesByLanguage("en");
    
    assertNotNull(result);
    assertFalse(result.isEmpty());
    for (Locale locale : result) {
      assertEquals("en", locale.getLanguage());
      assertFalse(locale.getCountry().isEmpty());
      assertTrue(locale.getVariant().isEmpty());
    }
  }
}