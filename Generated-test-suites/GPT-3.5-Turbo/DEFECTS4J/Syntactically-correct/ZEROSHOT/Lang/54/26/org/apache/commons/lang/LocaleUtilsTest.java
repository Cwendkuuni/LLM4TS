import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    // Test for valid locale
    Locale actual1 = LocaleUtils.toLocale("en");
    assertEquals(Locale.ENGLISH, actual1);
    
    // Test for locale with language and country
    Locale actual2 = LocaleUtils.toLocale("en_GB");
    assertEquals(Locale.UK, actual2);
    
    // Test for locale with language, country, and variant
    Locale actual3 = LocaleUtils.toLocale("en_GB_xxx");
    assertEquals("en", actual3.getLanguage());
    assertEquals("GB", actual3.getCountry());
    assertEquals("xxx", actual3.getVariant());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidFormat() {
    LocaleUtils.toLocale("invalid");
  }
  
  @Test
  public void testLocaleLookupList_NullLocale() {
    List<Locale> result = LocaleUtils.localeLookupList(null);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testLocaleLookupList_SingleLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> result = LocaleUtils.localeLookupList(locale);
    assertEquals(3, result.size());
    assertEquals(locale, result.get(0));
    assertEquals(new Locale("fr", "CA"), result.get(1));
    assertEquals(new Locale("fr"), result.get(2));
  }
  
  @Test
  public void testLocaleLookupList_DefaultLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = Locale.ENGLISH;
    List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(4, result.size());
    assertEquals(locale, result.get(0));
    assertEquals(new Locale("fr", "CA"), result.get(1));
    assertEquals(new Locale("fr"), result.get(2));
    assertEquals(defaultLocale, result.get(3));
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> result = LocaleUtils.availableLocaleList();
    assertFalse(result.isEmpty());
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> result = LocaleUtils.availableLocaleSet();
    assertFalse(result.isEmpty());
  }
  
  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid")));
  }
  
  @Test
  public void testLanguagesByCountry() {
    List<Locale> result = LocaleUtils.languagesByCountry("CA");
    assertFalse(result.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguage() {
    List<Locale> result = LocaleUtils.countriesByLanguage("fr");
    assertFalse(result.isEmpty());
  }
}