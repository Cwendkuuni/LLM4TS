import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale_basic() {
    Locale expected = new Locale("en", "GB");
    Locale result = LocaleUtils.toLocale("en_GB");
    Assert.assertEquals(expected, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_invalidFormat() {
    LocaleUtils.toLocale("invalid_locale_format");
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
    List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale("en"));
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> locales = LocaleUtils.availableLocaleList();
    Assert.assertNotNull(locales);
    Assert.assertTrue(locales.size() > 0);
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> locales = LocaleUtils.availableLocaleSet();
    Assert.assertNotNull(locales);
    Assert.assertTrue(locales.size() > 0);
  }
  
  @Test
  public void testIsAvailableLocale_true() {
    Locale locale = new Locale("en", "US");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsAvailableLocale_false() {
    Locale locale = new Locale("xx", "XX");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testLanguagesByCountry() {
    List<Locale> languages = LocaleUtils.languagesByCountry("US");
    Assert.assertNotNull(languages);
    Assert.assertTrue(languages.size() > 0);
  }
  
  @Test
  public void testCountriesByLanguage() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    Assert.assertNotNull(countries);
    Assert.assertTrue(countries.size() > 0);
  }
}