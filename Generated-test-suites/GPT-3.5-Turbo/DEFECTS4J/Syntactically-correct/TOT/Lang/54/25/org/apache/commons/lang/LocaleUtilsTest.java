import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    // Typical use case - converting a valid locale String
    Locale locale1 = LocaleUtils.toLocale("en_GB");
    Assert.assertEquals(new Locale("en", "GB"), locale1);
    
    // Edge case - converting a locale String with variant
    Locale locale2 = LocaleUtils.toLocale("en_US_xxx");
    Assert.assertEquals(new Locale("en", "US", "xxx"), locale2);
    
    // Error scenario - converting an invalid locale String
    try {
      LocaleUtils.toLocale("invalid_locale");
      Assert.fail("Expected IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      // Exception should be thrown
    }
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    
    // Typical use case - getting the locale lookup list
    List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
    Assert.assertEquals(locale, locales.get(0));
    Assert.assertEquals(new Locale("fr", "CA"), locales.get(1));
    Assert.assertEquals(new Locale("fr"), locales.get(2));
    Assert.assertEquals(defaultLocale, locales.get(3));
  }
  
  @Test
  public void testAvailableLocaleList() {
    // Typical use case - getting the list of available locales
    List<Locale> locales = LocaleUtils.availableLocaleList();
    Assert.assertNotNull(locales);
    Assert.assertFalse(locales.isEmpty());
  }
  
  @Test
  public void testAvailableLocaleSet() {
    // Typical use case - getting the set of available locales
    Set<Locale> locales = LocaleUtils.availableLocaleSet();
    Assert.assertNotNull(locales);
    Assert.assertFalse(locales.isEmpty());
  }
  
  @Test
  public void testIsAvailableLocale() {
    Locale locale1 = new Locale("en", "US");
    Locale locale2 = new Locale("invalid", "locale");
    
    // Typical use case - checking if a known locale is available
    Assert.assertTrue(LocaleUtils.isAvailableLocale(locale1));
    
    // Typical use case - checking if an unknown locale is available
    Assert.assertFalse(LocaleUtils.isAvailableLocale(locale2));
  }
  
  @Test
  public void testLanguagesByCountry() {
    // Typical use case - getting the list of languages for a country
    List<Locale> languages = LocaleUtils.languagesByCountry("US");
    Assert.assertNotNull(languages);
    Assert.assertFalse(languages.isEmpty());
    
    // Error scenario - getting the list of languages for an invalid country code
    List<Locale> languagesInvalid = LocaleUtils.languagesByCountry("invalid");
    Assert.assertNotNull(languagesInvalid);
    Assert.assertTrue(languagesInvalid.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguage() {
    // Typical use case - getting the list of countries for a language
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    Assert.assertNotNull(countries);
    Assert.assertFalse(countries.isEmpty());
    
    // Error scenario - getting the list of countries for an invalid language code
    List<Locale> countriesInvalid = LocaleUtils.countriesByLanguage("invalid");
    Assert.assertNotNull(countriesInvalid);
    Assert.assertTrue(countriesInvalid.isEmpty());
  }
}