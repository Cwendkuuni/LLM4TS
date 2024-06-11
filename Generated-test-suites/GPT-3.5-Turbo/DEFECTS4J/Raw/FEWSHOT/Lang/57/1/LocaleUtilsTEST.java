package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    Locale locale = LocaleUtils.toLocale("en");
    assertEquals(new Locale("en", ""), locale);
    
    locale = LocaleUtils.toLocale("en_GB");
    assertEquals(new Locale("en", "GB"), locale);
    
    locale = LocaleUtils.toLocale("en_GB_xxx");
    assertEquals(new Locale("en", "GB", "xxx"), locale);
    
    locale = LocaleUtils.toLocale("fr");
    assertEquals(new Locale("fr", ""), locale);
    
    locale = LocaleUtils.toLocale("fr_CA");
    assertEquals(new Locale("fr", "CA"), locale);
    
    locale = LocaleUtils.toLocale("fr_CA_xxx");
    assertEquals(new Locale("fr", "CA", "xxx"), locale);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_invalidFormat() {
    LocaleUtils.toLocale("invalid");
  }
    
  @Test
  public void testLocaleLookupList() {
    List<Locale> locales = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
    assertEquals(4, locales.size());
    assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
    assertEquals(new Locale("fr", "CA"), locales.get(1));
    assertEquals(new Locale("fr", ""), locales.get(2));
    assertEquals(new Locale("en", ""), locales.get(3));
  }
  
  @Test
  public void testLocaleLookupList_withDefaultLocale() {
    List<Locale> locales = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
    assertEquals(4, locales.size());
    assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
    assertEquals(new Locale("fr", "CA"), locales.get(1));
    assertEquals(new Locale("fr", ""), locales.get(2));
    assertEquals(new Locale("en", ""), locales.get(3));
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> locales = LocaleUtils.availableLocaleList();
    assertFalse(locales.isEmpty());
  }
  
  @Test
  public void testAvailableLocaleSet() {
    assertTrue(LocaleUtils.availableLocaleSet().contains(new Locale("en")));
  }
  
  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
  }
  
  @Test
  public void testLanguagesByCountry() {
    List<Locale> languages = LocaleUtils.languagesByCountry("CA");
    assertFalse(languages.isEmpty());
  }
  
  @Test
  public void testLanguagesByCountry_invalidCode() {
    List<Locale> languages = LocaleUtils.languagesByCountry("XX");
    assertTrue(languages.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguage() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
    assertFalse(countries.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguage_invalidCode() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("XX");
    assertTrue(countries.isEmpty());
  }
}