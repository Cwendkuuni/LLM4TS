package org.apache.commons.lang3;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

  @Test
  public void testToLocaleValidInput() {
    Locale locale = LocaleUtils.toLocale("en_GB");
    assertEquals("en", locale.getLanguage());
    assertEquals("GB", locale.getCountry());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidFormat() {
    LocaleUtils.toLocale("en#GB");
  }

  @Test
  public void testToLocaleNullInput() {
    assertNull(LocaleUtils.toLocale(null));
  }

  @Test
  public void testLocaleLookupListWithNullLocale() {
    List<Locale> locales = LocaleUtils.localeLookupList(null);
    assertNotNull(locales);
    assertEquals(0, locales.size());
  }

  @Test
  public void testLocaleLookupListWithValidLocale() {
    Locale locale = new Locale("fr","CA", "xxx");
    List<Locale> locales = LocaleUtils.localeLookupList(locale);
    assertNotNull(locales);
    assertEquals(3, locales.size());
    assertEquals(locale, locales.get(0));
    assertEquals(new Locale("fr", "CA"), locales.get(1));
    assertEquals(new Locale("fr"), locales.get(2));
  }

  @Test
  public void testLocaleLookupListWithDefaultLocale() {
    Locale locale = new Locale("en");
    Locale defaultLocale = new Locale("es");
    List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertNotNull(locales);
    assertEquals(2, locales.size());
    assertEquals(locale, locales.get(0));
    assertEquals(defaultLocale, locales.get(1));
  }

  @Test
  public void testAvailableLocaleList() {
    List<Locale> locales = LocaleUtils.availableLocaleList();
    assertNotNull(locales);
    assertTrue(locales.size() > 0);
  }

  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> locales = LocaleUtils.availableLocaleSet();
    assertNotNull(locales);
    assertTrue(locales.size() > 0);
  }

  @Test
  public void testIsAvailableLocaleWithValidLocale() {
    Locale locale = Locale.ENGLISH;
    assertTrue(LocaleUtils.isAvailableLocale(locale));
  }

  @Test
  public void testIsAvailableLocaleWithInvalidLocale() {
    Locale locale = new Locale("zz");
    assertFalse(LocaleUtils.isAvailableLocale(locale));
  }

  @Test
  public void testLanguagesByCountryWithNullCountryCode() {
    List<Locale> locales = LocaleUtils.languagesByCountry(null);
    assertNotNull(locales);
    assertEquals(0, locales.size());
  }

  @Test
  public void testLanguagesByCountryWithValidCountryCode() {
    List<Locale> locales = LocaleUtils.languagesByCountry("CA");
    assertNotNull(locales);
    assertTrue(locales.size() > 0);
  }

  @Test
  public void testCountriesByLanguageWithNullLanguageCode() {
    List<Locale> locales = LocaleUtils.countriesByLanguage(null);
    assertNotNull(locales);
    assertEquals(0, locales.size());
  }

  @Test
  public void testCountriesByLanguageWithValidLanguageCode() {
    List<Locale> locales = LocaleUtils.countriesByLanguage("fr");
    assertNotNull(locales);
    assertTrue(locales.size() > 0);
  }

}