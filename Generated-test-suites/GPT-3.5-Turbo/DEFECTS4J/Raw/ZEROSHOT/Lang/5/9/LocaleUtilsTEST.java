import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    Locale locale = LocaleUtils.toLocale("en");
    assertEquals(new Locale("en", ""), locale);
    
    locale = LocaleUtils.toLocale("en_GB");
    assertEquals(new Locale("en", "GB"), locale);
    
    locale = LocaleUtils.toLocale("en_GB_xxx");
    assertEquals(new Locale("en", "GB", "xxx"), locale);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testToLocale_invalid() {
    LocaleUtils.toLocale("e");
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
    List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
    List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> availableLocales = LocaleUtils.availableLocaleList();
    assertNotNull(availableLocales);
    assertFalse(availableLocales.isEmpty());
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
    assertNotNull(availableLocales);
    assertFalse(availableLocales.isEmpty());
  }
  
  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
  }
  
  @Test
  public void testLanguagesByCountry() {
    List<Locale> languages = LocaleUtils.languagesByCountry("US");
    assertNotNull(languages);
    assertFalse(languages.isEmpty());
  }
  
  @Test
  public void testCountriesByLanguage() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    assertNotNull(countries);
    assertFalse(countries.isEmpty());
  }
}