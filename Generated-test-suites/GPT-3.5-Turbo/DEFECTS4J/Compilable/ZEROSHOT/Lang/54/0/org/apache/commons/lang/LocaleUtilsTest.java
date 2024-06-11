package org.apache.commons.lang;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;
import java.util.Set;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    Locale locale1 = LocaleUtils.toLocale("en");
    assertEquals(Locale.ENGLISH, locale1);
    
    Locale locale2 = LocaleUtils.toLocale("en_GB");
    assertEquals(Locale.UK, locale2);
    
    Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
    assertEquals("en", locale3.getLanguage());
    assertEquals("GB", locale3.getCountry());
    assertEquals("xxx", locale3.getVariant());
    
    Locale locale4 = LocaleUtils.toLocale("fr_CA_xxx");
    assertEquals("fr", locale4.getLanguage());
    assertEquals("CA", locale4.getCountry());
    assertEquals("xxx", locale4.getVariant());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_invalidFormat1() {
    LocaleUtils.toLocale("e");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_invalidFormat2() {
    LocaleUtils.toLocale("en_GB_xxxx");
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
    List<Locale> list = LocaleUtils.localeLookupList(locale);
    assertEquals(4, list.size());
    assertEquals(locale, list.get(0));
    assertEquals(Locale.CANADA_FRENCH, list.get(1));
    assertEquals(Locale.FRENCH, list.get(2));
    assertEquals(Locale.getDefault(), list.get(3));
    
    Locale defaultLocale = LocaleUtils.toLocale("en");
    List<Locale> list2 = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(4, list2.size());
    assertEquals(locale, list2.get(0));
    assertEquals(Locale.CANADA_FRENCH, list2.get(1));
    assertEquals(Locale.FRENCH, list2.get(2));
    assertEquals(defaultLocale, list2.get(3));
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> list = LocaleUtils.availableLocaleList();
    assertNotNull(list);
    assertFalse(list.isEmpty());
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> set = LocaleUtils.availableLocaleSet();
    assertNotNull(set);
    assertFalse(set.isEmpty());
  }
  
  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
  }
  
  @Test
  public void testLanguagesByCountry() {
    List<Locale> list = LocaleUtils.languagesByCountry("CA");
    assertNotNull(list);
    assertTrue(list.contains(Locale.CANADA_FRENCH));
  }
  
  @Test
  public void testCountriesByLanguage() {
    List<Locale> list = LocaleUtils.countriesByLanguage("fr");
    assertNotNull(list);
    assertTrue(list.contains(Locale.CANADA_FRENCH));
  }
  
}