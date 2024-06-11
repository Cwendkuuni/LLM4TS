package org.apache.commons.lang;
import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Set;

public class LocaleUtilsTest {

  @Test
  public void testToLocaleNullInput() {
    Locale locale = LocaleUtils.toLocale(null);
    Assert.assertNull(locale);
  }
  
  @Test
  public void testToLocaleInvalidFormat() {
    try {
      Locale locale = LocaleUtils.toLocale("en_gb");
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      // Exception expected
    }
  }

  @Test
  public void testToLocaleLanguageOnly() {
    Locale locale = LocaleUtils.toLocale("en");
    Assert.assertEquals("en", locale.getLanguage());
    Assert.assertEquals("", locale.getCountry());
  }
  
  @Test
  public void testToLocaleLanguageAndCountry() {
    Locale locale = LocaleUtils.toLocale("en_GB");
    Assert.assertEquals("en", locale.getLanguage());
    Assert.assertEquals("GB", locale.getCountry());
  }
  
  @Test
  public void testToLocaleLanguageCountryAndVariant() {
    Locale locale = LocaleUtils.toLocale("en_GB_xxx");
    Assert.assertEquals("en", locale.getLanguage());
    Assert.assertEquals("GB", locale.getCountry());
    Assert.assertEquals("xxx", locale.getVariant());
  }
  
  @Test
  public void testLocaleLookupListWithLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> result = LocaleUtils.localeLookupList(locale);
    Assert.assertEquals(3, result.size());
    Assert.assertEquals(locale, result.get(0));
    Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
    Assert.assertEquals(new Locale("fr"), result.get(2));
  }
  
  @Test
  public void testLocaleLookupListWithLocaleAndDefaultLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
    Assert.assertEquals(4, result.size());
    Assert.assertEquals(locale, result.get(0));
    Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
    Assert.assertEquals(new Locale("fr"), result.get(2));
    Assert.assertEquals(defaultLocale, result.get(3));
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> result = LocaleUtils.availableLocaleList();
    Assert.assertEquals(Locale.getAvailableLocales().length, result.size());
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> result = LocaleUtils.availableLocaleSet();
    Assert.assertEquals(Locale.getAvailableLocales().length, result.size());
  }
  
  @Test
  public void testIsAvailableLocaleWhenAvailable() {
    Locale locale = new Locale("en", "US");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsAvailableLocaleWhenNotAvailable() {
    Locale locale = new Locale("xx", "XX");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testLanguagesByCountryWithCountryCodeNull() {
    List<Locale> result = LocaleUtils.languagesByCountry(null);
    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }
  
  @Test
  public void testLanguagesByCountryWithCountryCodeValid() {
    List<Locale> result = LocaleUtils.languagesByCountry("CA");
    Assert.assertNotNull(result);
    Assert.assertTrue(result.size() > 0);
  }
  
  @Test
  public void testCountriesByLanguageWithLanguageCodeNull() {
    List<Locale> result = LocaleUtils.countriesByLanguage(null);
    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }
  
  @Test
  public void testCountriesByLanguageWithLanguageCodeValid() {
    List<Locale> result = LocaleUtils.countriesByLanguage("en");
    Assert.assertNotNull(result);
    Assert.assertTrue(result.size() > 0);
  }
}