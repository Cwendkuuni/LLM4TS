
import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    assertNull(LocaleUtils.toLocale(null));
    assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> expected = Arrays.asList(
        new Locale("fr", "CA", "xxx"),
        new Locale("fr", "CA"),
        new Locale("fr"),
        new Locale("en")
    );
    assertEquals(expected, LocaleUtils.localeLookupList(locale));
    assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> availableLocales = LocaleUtils.availableLocaleList();
    assertTrue(availableLocales.contains(new Locale("en")));
    assertTrue(availableLocales.contains(new Locale("fr")));
    // Add more assertions for other available locales
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
    assertTrue(availableLocales.contains(new Locale("en")));
    assertTrue(availableLocales.contains(new Locale("fr")));
    // Add more assertions for other available locales
  }
  
  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
    assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr")));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("de")));
    // Add more assertions for other available locales
  }
  
  @Test
  public void testLanguagesByCountry() {
    List<Locale> languages = LocaleUtils.languagesByCountry("CA");
    assertTrue(languages.contains(new Locale("en", "CA")));
    assertTrue(languages.contains(new Locale("fr", "CA")));
    // Add more assertions for other languages in Canada
  }
  
  @Test
  public void testCountriesByLanguage() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
    assertTrue(countries.contains(new Locale("fr", "CA")));
    // Add more assertions for other countries with the French language
  }
}
