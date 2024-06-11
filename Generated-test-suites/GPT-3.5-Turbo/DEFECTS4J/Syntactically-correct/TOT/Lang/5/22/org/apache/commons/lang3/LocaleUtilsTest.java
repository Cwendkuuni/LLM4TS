import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

  @Test
  public void testToLocale_ValidLocaleString() {
    // Typical use case
    Locale locale1 = LocaleUtils.toLocale("en_GB");
    Assert.assertEquals("en", locale1.getLanguage());
    Assert.assertEquals("GB", locale1.getCountry());
    
    // Edge case - empty country
    Locale locale2 = LocaleUtils.toLocale("fr__");
    Assert.assertEquals("fr", locale2.getLanguage());
    Assert.assertEquals("", locale2.getCountry());
    
    // Edge case - empty variant
    Locale locale3 = LocaleUtils.toLocale("de_DE_");
    Assert.assertEquals("de", locale3.getLanguage());
    Assert.assertEquals("DE", locale3.getCountry());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocale_InvalidLocaleString() {
    // Error scenario - invalid format
    LocaleUtils.toLocale("invalid");
  }
  
  @Test
  public void testLocaleLookupList() {
    // Typical use case - locale with variant
    Locale locale1 = new Locale("fr","CA","xxx");
    List<Locale> lookupList1 = LocaleUtils.localeLookupList(locale1);
    Assert.assertEquals(locale1, lookupList1.get(0));
    Assert.assertEquals(new Locale("fr","CA"), lookupList1.get(1));
    Assert.assertEquals(new Locale("fr"), lookupList1.get(2));
    Assert.assertEquals(Locale.getDefault(), lookupList1.get(3));
    
    // Typical use case - locale without variant
    Locale locale2 = new Locale("en");
    List<Locale> lookupList2 = LocaleUtils.localeLookupList(locale2);
    Assert.assertEquals(locale2, lookupList2.get(0));
    Assert.assertEquals(Locale.getDefault(), lookupList2.get(1));
  }
  
  @Test
  public void testAvailableLocaleList() {
    // Typical use case
    List<Locale> availableLocales = LocaleUtils.availableLocaleList();
    Assert.assertNotNull(availableLocales);
    Assert.assertFalse(availableLocales.isEmpty());
  }
  
  @Test
  public void testAvailableLocaleSet() {
    // Typical use case
    Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
    Assert.assertNotNull(availableLocales);
    Assert.assertFalse(availableLocales.isEmpty());
  }
  
  @Test
  public void testIsAvailableLocale() {
    // Typical use case
    Locale locale1 = LocaleUtils.availableLocaleList().get(0);
    Assert.assertTrue(LocaleUtils.isAvailableLocale(locale1));
    
    // Edge case - invalid locale
    Locale locale2 = new Locale("xx");
    Assert.assertFalse(LocaleUtils.isAvailableLocale(locale2));
  }
  
  @Test
  public void testLanguagesByCountry() {
    // Typical use case
    List<Locale> languages = LocaleUtils.languagesByCountry("US");
    Assert.assertNotNull(languages);
    Assert.assertFalse(languages.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguage() {
    // Typical use case
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    Assert.assertNotNull(countries);
    Assert.assertFalse(countries.isEmpty());
  }
}