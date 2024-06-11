import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;
import java.util.Set;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    Locale locale = LocaleUtils.toLocale("en");
    Assert.assertEquals(new Locale("en", ""), locale);
    
    locale = LocaleUtils.toLocale("en_GB");
    Assert.assertEquals(new Locale("en", "GB"), locale);
    
    locale = LocaleUtils.toLocale("en_GB_xxx");
    Assert.assertEquals(new Locale("en", "GB", "xxx"), locale);
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> result = LocaleUtils.localeLookupList(locale);
    List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
    
    Assert.assertEquals(expected, result);
    
    result = LocaleUtils.localeLookupList(locale, new Locale("en"));
    expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> result = LocaleUtils.availableLocaleList();
    Assert.assertNotNull(result);
    
    // Check if the list is unmodifiable
    try {
      result.add(new Locale("de"));
      Assert.fail("List should be unmodifiable");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> result = LocaleUtils.availableLocaleSet();
    Assert.assertNotNull(result);
    
    // Check if the set is unmodifiable
    try {
      result.add(new Locale("de"));
      Assert.fail("Set should be unmodifiable");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testIsAvailableLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    Assert.assertTrue(result);
    
    locale = new Locale("de", "DE");
    result = LocaleUtils.isAvailableLocale(locale);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testLanguagesByCountry() {
    List<Locale> result = LocaleUtils.languagesByCountry("GB");
    List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("gd", "GB"));
    
    Assert.assertEquals(expected, result);
    
    result = LocaleUtils.languagesByCountry("ZZ");
    Assert.assertEquals(Collections.emptyList(), result);
  }
  
  @Test
  public void testCountriesByLanguage() {
    List<Locale> result = LocaleUtils.countriesByLanguage("en");
    List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en", "ZZ"));
    
    Assert.assertEquals(expected, result);
    
    result = LocaleUtils.countriesByLanguage("zz");
    Assert.assertEquals(Collections.emptyList(), result);
  }

}