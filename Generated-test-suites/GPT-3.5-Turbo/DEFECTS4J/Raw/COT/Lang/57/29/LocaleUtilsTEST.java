
import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    Locale locale1 = LocaleUtils.toLocale("en");
    Assert.assertEquals(Locale.ENGLISH, locale1);
    
    Locale locale2 = LocaleUtils.toLocale("en_GB");
    Assert.assertEquals(Locale.UK, locale2);
    
    Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
    Assert.assertEquals("xxx", locale3.getVariant());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidFormat() {
    LocaleUtils.toLocale("invalid");
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> locales = LocaleUtils.localeLookupList(locale);
    Assert.assertEquals(3, locales.size());
    Assert.assertEquals(Locale.CANADA_FRENCH, locales.get(0));
    Assert.assertEquals(Locale.FRENCH, locales.get(2));
  }
  
  @Test
  public void testLocaleLookupListWithDefaultLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = Locale.ENGLISH;
    List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
    Assert.assertEquals(4, locales.size());
    Assert.assertEquals(Locale.FRANCE, locales.get(2));
    Assert.assertEquals(defaultLocale, locales.get(3));
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> locales = LocaleUtils.availableLocaleList();
    Assert.assertEquals(Locale.getAvailableLocales().length, locales.size());
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
    Assert.assertEquals(Locale.getAvailableLocales().length, localeSet.size());
  }
  
  @Test
  public void testIsAvailableLocale() {
    Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
  }
  
  @Test
  public void testLanguagesByCountry() {
    List<Locale> languages = LocaleUtils.languagesByCountry("CA");
    Assert.assertEquals(2, languages.size());
    Assert.assertEquals(Locale.ENGLISH, languages.get(0));
    Assert.assertEquals(Locale.FRENCH, languages.get(1));
  }
  
  @Test
  public void testCountriesByLanguage() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    Assert.assertEquals(Locale.CANADA, countries.get(0));
  }
  
}

