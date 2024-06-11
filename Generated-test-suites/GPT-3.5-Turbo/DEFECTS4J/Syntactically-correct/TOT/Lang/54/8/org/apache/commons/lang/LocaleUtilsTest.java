import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

  @Test
  public void testToLocale_ValidString_ReturnsLocale() {
    Locale locale = LocaleUtils.toLocale("en_GB");
    assertEquals(Locale.UK, locale);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidLanguageCode_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("abc");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidCountryCode_ThrowsIllegalArgumentException() {
    LocaleUtils.toLocale("en_");
  }

  @Test
  public void testToLocale_NullString_ReturnsNull() {
    Locale locale = LocaleUtils.toLocale(null);
    assertNull(locale);
  }

  @Test
  public void testLocaleLookupList_SpecificLocale_ReturnsProperList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> locales = LocaleUtils.localeLookupList(locale);
    assertEquals(3, locales.size());
    assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
    assertEquals(new Locale("fr", "CA"), locales.get(1));
    assertEquals(new Locale("fr"), locales.get(2));
  }

  @Test
  public void testLocaleLookupList_SpecificLocaleWithDefaultLocale_ReturnsProperList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = Locale.ENGLISH;
    List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(4, locales.size());
    assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
    assertEquals(new Locale("fr", "CA"), locales.get(1));
    assertEquals(new Locale("fr"), locales.get(2));
    assertEquals(defaultLocale, locales.get(3));
  }

  @Test
  public void testAvailableLocaleList_ReturnsNonEmptyList() {
    List<Locale> locales = LocaleUtils.availableLocaleList();
    assertTrue(!locales.isEmpty());
  }

  @Test
  public void testAvailableLocaleSet_ReturnsNonEmptySet() {
    Set<Locale> locales = LocaleUtils.availableLocaleSet();
    assertTrue(!locales.isEmpty());
  }

  @Test
  public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
  }

  @Test
  public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
  }

  @Test
  public void testLanguagesByCountry_ValidCountryCode_ReturnsLanguageList() {
    List<Locale> languages = LocaleUtils.languagesByCountry("CA");
    assertTrue(!languages.isEmpty());
    assertTrue(languages.contains(Locale.ENGLISH));
  }

  @Test
  public void testLanguagesByCountry_InvalidCountryCode_ReturnsEmptyList() {
    List<Locale> languages = LocaleUtils.languagesByCountry("XYZ");
    assertTrue(languages.isEmpty());
  }

  @Test
  public void testCountriesByLanguage_ValidLanguageCode_ReturnsCountryList() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    assertTrue(!countries.isEmpty());
    assertTrue(countries.contains(Locale.CANADA));
  }

  @Test
  public void testCountriesByLanguage_InvalidLanguageCode_ReturnsEmptyList() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("xyz");
    assertTrue(countries.isEmpty());
  }

}