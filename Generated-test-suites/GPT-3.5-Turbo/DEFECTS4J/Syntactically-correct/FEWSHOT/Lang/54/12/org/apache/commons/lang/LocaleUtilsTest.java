
package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    Locale locale = LocaleUtils.toLocale("en");
    assertEquals("en", locale.getLanguage());
    assertEquals("", locale.getCountry());
    assertEquals("", locale.getVariant());
    
    locale = LocaleUtils.toLocale("en_US");
    assertEquals("en", locale.getLanguage());
    assertEquals("US", locale.getCountry());
    assertEquals("", locale.getVariant());
    
    locale = LocaleUtils.toLocale("fr_CA_xxx");
    assertEquals("fr", locale.getLanguage());
    assertEquals("CA", locale.getCountry());
    assertEquals("xxx", locale.getVariant());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidFormat() {
    LocaleUtils.toLocale("invalid");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidLanguageCode() {
    LocaleUtils.toLocale("123");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidCountryCode() {
    LocaleUtils.toLocale("en_12");
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> locales = LocaleUtils.localeLookupList(locale);
    assertEquals(new ArrayList<>(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"))), locales);
    
    locales = LocaleUtils.localeLookupList(locale, new Locale("en"));
    assertEquals(new ArrayList<>(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"))), locales);
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> locales = LocaleUtils.availableLocaleList();
    assertTrue(locales.contains(Locale.ENGLISH));
    assertTrue(locales.contains(Locale.FRENCH));
  }
  
  @Test
  public void testAvailableLocaleSet() {
    List<Locale> locales = new ArrayList<>();
    locales.add(new Locale("en"));
    locales.add(new Locale("fr"));
    
    List<Locale> availableLocales = LocaleUtils.availableLocaleList();
    for (Locale locale : availableLocales) {
      assertTrue(locales.contains(locale));
    }
  }
  
  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("de")));
  }
  
  @Test
  public void testLanguagesByCountry() {
    List<Locale> langs = LocaleUtils.languagesByCountry("CA");
    assertTrue(langs.contains(new Locale("en", "CA")));
    assertTrue(langs.contains(new Locale("fr", "CA")));
    assertFalse(langs.contains(new Locale("de", "CA")));
  }
  
  @Test
  public void testCountriesByLanguage() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    assertTrue(countries.contains(new Locale("en", "CA")));
    assertTrue(countries.contains(new Locale("en", "US")));
    assertFalse(countries.contains(new Locale("en", "DE")));
  }
  
}
