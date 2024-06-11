import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.Locale;
import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

public class LocaleUtilsTest {
  
  @Test
  public void testToLocale() {
    assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidFormat() {
    LocaleUtils.toLocale("invalidFormat");
  }
  
  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List expectedList = List.of(locale, new Locale("fr", "CA"), new Locale("fr"));
    assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
  }
  
  @Test
  public void testLocaleLookupListWithDefaultLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List expectedList = List.of(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
    assertEquals(expectedList, LocaleUtils.localeLookupList(locale, new Locale("en")));
  }
  
  @Test
  public void testAvailableLocaleList() {
    List availableLocales = LocaleUtils.availableLocaleList();
    // Check if the list contains at least some common locales
    assertEquals(true, availableLocales.contains(new Locale("en", "")));
    assertEquals(true, availableLocales.contains(new Locale("fr", "")));
    assertEquals(true, availableLocales.contains(new Locale("es", "")));
  }
  
  @Test
  public void testAvailableLocaleSet() {
    Set availableLocales = LocaleUtils.availableLocaleSet();
    // Check if the set contains at least some common locales
    assertEquals(true, availableLocales.contains(new Locale("en", "")));
    assertEquals(true, availableLocales.contains(new Locale("fr", "")));
    assertEquals(true, availableLocales.contains(new Locale("es", "")));
  }
  
  @Test
  public void testIsAvailableLocale() {
    assertEquals(true, LocaleUtils.isAvailableLocale(new Locale("en", "")));
    assertEquals(true, LocaleUtils.isAvailableLocale(new Locale("fr", "")));
    assertEquals(true, LocaleUtils.isAvailableLocale(new Locale("es", "")));
    assertEquals(false, LocaleUtils.isAvailableLocale(new Locale("invalid", "locale")));
  }
  
  @Test
  public void testLanguagesByCountry() {
    List expectedLanguages = List.of(new Locale("en", ""), new Locale("fr", ""), new Locale("es", ""));
    assertEquals(expectedLanguages, LocaleUtils.languagesByCountry("CA"));
  }
  
  @Test
  public void testLanguagesByCountryUnknownCountry() {
    List expectedLanguages = List.of();
    assertEquals(expectedLanguages, LocaleUtils.languagesByCountry("ZZ"));
  }
  
  @Test
  public void testCountriesByLanguage() {
    List expectedCountries = List.of(new Locale("en", "US"), new Locale("en", "GB"));
    assertEquals(expectedCountries, LocaleUtils.countriesByLanguage("en"));
  }
  
  @Test
  public void testCountriesByLanguageUnknownLanguage() {
    List expectedCountries = List.of();
    assertEquals(expectedCountries, LocaleUtils.countriesByLanguage("zz"));
  }
}