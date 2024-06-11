package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocaleWithValidString() {
    Locale locale = LocaleUtils.toLocale("en_GB");
    assertNotNull(locale);
    assertEquals("en", locale.getLanguage());
    assertEquals("GB", locale.getCountry());
  }
  
  @Test
  public void testToLocaleWithInvalidString() {
    try {
      Locale locale = LocaleUtils.toLocale("abc");
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testLocaleLookupListWithLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> expectedList = Arrays.asList(
        new Locale("fr", "CA", "xxx"),
        new Locale("fr", "CA"),
        new Locale("fr")
    );
    List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
    assertEquals(expectedList, lookupList);
  }
  
  @Test
  public void testLocaleLookupListWithLocaleAndDefaultLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> expectedList = Arrays.asList(
        new Locale("fr", "CA", "xxx"),
        new Locale("fr", "CA"),
        new Locale("fr"),
        new Locale("en")
    );
    List<Locale> lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(expectedList, lookupList);
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> availableLocales = LocaleUtils.availableLocaleList();
    assertNotNull(availableLocales);
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
    assertNotNull(availableLocales);
  }
  
  @Test
  public void testIsAvailableLocaleWithAvailableLocale() {
    boolean result = LocaleUtils.isAvailableLocale(Locale.US);
    assertTrue(result);
  }
  
  @Test
  public void testIsAvailableLocaleWithUnavailableLocale() {
    boolean result = LocaleUtils.isAvailableLocale(new Locale("abc"));
    assertFalse(result);
  }
  
  @Test
  public void testLanguagesByCountryWithValidCountryCode() {
    List<Locale> languages = LocaleUtils.languagesByCountry("GB");
    assertNotNull(languages);
  }
  
  @Test
  public void testLanguagesByCountryWithInvalidCountryCode() {
    List<Locale> languages = LocaleUtils.languagesByCountry("abc");
    assertNotNull(languages);
    assertTrue(languages.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguageWithValidLanguageCode() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    assertNotNull(countries);
  }
  
  @Test
  public void testCountriesByLanguageWithInvalidLanguageCode() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("abc");
    assertNotNull(countries);
    assertTrue(countries.isEmpty());
  }
}