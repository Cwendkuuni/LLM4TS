
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    // Test valid locales
    assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    
    // Test invalid locales
    assertNull(LocaleUtils.toLocale("en_"));
    assertNull(LocaleUtils.toLocale("EN_GB"));
    assertNull(LocaleUtils.toLocale("en_gb_xxx"));
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> expectedList = new ArrayList<Locale>();
    expectedList.add(new Locale("fr", "CA", "xxx"));
    expectedList.add(new Locale("fr", "CA"));
    expectedList.add(new Locale("fr"));
    expectedList.add(new Locale("en"));
    
    List<Locale> actualList = LocaleUtils.localeLookupList(locale);
    assertEquals(expectedList, actualList);
    
    actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(expectedList, actualList);
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> availableLocales = LocaleUtils.availableLocaleList();
    assertFalse(availableLocales.isEmpty());
    assertTrue(availableLocales.contains(Locale.US));
    assertTrue(availableLocales.contains(Locale.UK));
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
    assertFalse(availableLocales.isEmpty());
    assertTrue(availableLocales.contains(Locale.US));
    assertTrue(availableLocales.contains(Locale.UK));
  }
  
  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
  }
  
  @Test
  public void testLanguagesByCountry() {
    // Test valid country code
    List<Locale> languages = LocaleUtils.languagesByCountry("CA");
    assertFalse(languages.isEmpty());
    assertTrue(languages.contains(new Locale("fr", "CA")));
    
    // Test invalid country code
    List<Locale> emptyLanguages = LocaleUtils.languagesByCountry("XX");
    assertTrue(emptyLanguages.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguage() {
    // Test valid language code
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    assertFalse(countries.isEmpty());
    assertTrue(countries.contains(new Locale("en", "GB")));
    
    // Test invalid language code
    List<Locale> emptycountries = LocaleUtils.countriesByLanguage("xx");
    assertTrue(emptycountries.isEmpty());
  }
  
}
