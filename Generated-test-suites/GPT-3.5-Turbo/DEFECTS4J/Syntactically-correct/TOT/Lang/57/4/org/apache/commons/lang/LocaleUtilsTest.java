
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale_ValidTwoLetterCode_ReturnsLocale() {
    Locale locale = LocaleUtils.toLocale("en");
    assertEquals("en", locale.getLanguage());
    assertEquals("", locale.getCountry());
  }
  
  @Test
  public void testToLocale_ValidFiveLetterCode_ReturnsLocale() {
    Locale locale = LocaleUtils.toLocale("en_GB");
    assertEquals("en", locale.getLanguage());
    assertEquals("GB", locale.getCountry());
  }
  
  @Test
  public void testToLocale_ValidVariants_ReturnsLocale() {
    Locale locale = LocaleUtils.toLocale("en_GB_xxx");
    assertEquals("en", locale.getLanguage());
    assertEquals("GB", locale.getCountry());
    assertEquals("xxx", locale.getVariant());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("abc");
  }
  
  @Test
  public void testLocaleLookupList_LocaleNonNull_ReturnsCorrectLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> result = LocaleUtils.localeLookupList(locale);
    assertEquals(3, result.size());
    assertEquals(locale, result.get(0));
    assertEquals("fr", result.get(1).getLanguage());
    assertEquals("CA", result.get(1).getCountry());
    assertEquals("fr", result.get(2).getLanguage());
    assertEquals("", result.get(2).getCountry());
  }
  
  @Test
  public void testLocaleLookupList_DefaultLocaleNonNull_ReturnsCorrectLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(4, result.size());
    assertEquals(locale, result.get(0));
    assertEquals("fr", result.get(1).getLanguage());
    assertEquals("CA", result.get(1).getCountry());
    assertEquals("fr", result.get(2).getLanguage());
    assertEquals("", result.get(2).getCountry());
    assertEquals(defaultLocale, result.get(3));
  }
  
  @Test
  public void testAvailableLocaleList_ReturnsUnmodifiableList() {
    List<Locale> result = LocaleUtils.availableLocaleList();
    try {
      result.add(new Locale("en"));
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
  
  @Test
  public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
    Set<Locale> result = LocaleUtils.availableLocaleSet();
    try {
      result.add(new Locale("en"));
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
  
  @Test
  public void testIsAvailableLocale_LocaleInList_ReturnsTrue() {
    Locale locale = new Locale("en", "US");
    assertTrue(LocaleUtils.isAvailableLocale(locale));
  }
  
  @Test
  public void testIsAvailableLocale_LocaleNotInList_ReturnsFalse() {
    Locale locale = new Locale("zz", "ZZ");
    assertFalse(LocaleUtils.isAvailableLocale(locale));
  }
  
  @Test
  public void testLanguagesByCountry_CountryCodeValid_ReturnsListOfLanguages() {
    String countryCode = "CA";
    List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
    assertEquals(3, result.size());
    assertEquals("en", result.get(0).getLanguage());
    assertEquals("fr", result.get(1).getLanguage());
    assertEquals("fr_CA", result.get(2).toString());
  }
  
  @Test
  public void testLanguagesByCountry_CountryCodeInvalid_ReturnsEmptyList() {
    String countryCode = "ZZ";
    List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguage_LanguageCodeValid_ReturnsListOfCountries() {
    String languageCode = "en";
    List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
    assertEquals(6, result.size());
    assertEquals("US", result.get(0).getCountry());
    assertEquals("GB", result.get(1).getCountry());
    assertEquals("CA", result.get(2).getCountry());
    assertEquals("IE", result.get(3).getCountry());
    assertEquals("AU", result.get(4).getCountry());
    assertEquals("NZ", result.get(5).getCountry());
  }
  
  @Test
  public void testCountriesByLanguage_LanguageCodeInvalid_ReturnsEmptyList() {
    String languageCode = "zz";
    List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
    assertTrue(result.isEmpty());
  }
}
