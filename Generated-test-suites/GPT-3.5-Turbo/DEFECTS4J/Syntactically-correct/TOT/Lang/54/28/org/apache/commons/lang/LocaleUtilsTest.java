import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

  // Test cases for toLocale method
  @Test
  public void testValidLocaleString() {
    String localeString = "en_GB";
    Locale expectedLocale = new Locale("en", "GB");
    Locale actualLocale = LocaleUtils.toLocale(localeString);
    assertEquals(expectedLocale, actualLocale);
  }

  @Test
  public void testValidLocaleStringWithVariant() {
    String localeString = "en_GB_xxx";
    Locale expectedLocale = new Locale("en", "GB", "xxx");
    Locale actualLocale = LocaleUtils.toLocale(localeString);
    assertEquals(expectedLocale, actualLocale);
  }

  @Test
  public void testValidLocaleStringWithLowercase() {
    String localeString = "en";
    Locale expectedLocale = new Locale("en", "");
    Locale actualLocale = LocaleUtils.toLocale(localeString);
    assertEquals(expectedLocale, actualLocale);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidLocaleStringWithLowerCase() {
    String localeString = "EN_GB";
    LocaleUtils.toLocale(localeString);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidLocaleStringWithoutUnderscore() {
    String localeString = "enGB";
    LocaleUtils.toLocale(localeString);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidLocaleStringWithExtraCharacters() {
    String localeString = "en_GB_xxxxx";
    LocaleUtils.toLocale(localeString);
  }

  // Test cases for localeLookupList method
  
  @Test
  public void testValidLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> expectedList = Arrays.asList(
      new Locale("fr","CA","xxx"),
      new Locale("fr","CA"),
      new Locale("fr"),
      new Locale("en")
    );

    List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(expectedList, actualList);
  }

  @Test
  public void testValidLocaleLookupListWithNullLocale() {
    Locale locale = null;
    Locale defaultLocale = new Locale("en");
    List<Locale> expectedList = Arrays.asList(new Locale("en"));

    List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(expectedList, actualList);
  }

  // Test cases for availableLocaleList method
  
  @Test
  public void testAvailableLocaleList() {
    List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
    List<Locale> actualList = LocaleUtils.availableLocaleList();
    assertEquals(expectedList, actualList);
  }

  // Test cases for availableLocaleSet method
  
  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> expectedSet = new HashSet<>(Locale.getAvailableLocales());
    Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
    assertEquals(expectedSet, actualSet);
  }

  // Test cases for isAvailableLocale method
  
  @Test
  public void testIsAvailableLocaleWithAvailableLocale() {
    Locale locale = Locale.US;
    boolean result = LocaleUtils.isAvailableLocale(locale);
    assertTrue(result);
  }

  @Test
  public void testIsAvailableLocaleWithUnavailableLocale() {
    Locale locale = new Locale("xx");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    assertFalse(result);
  }

  // Test cases for languagesByCountry method
  
  @Test
  public void testValidLanguagesByCountry() {
    String countryCode = "FR";
    List<Locale> expectedList = Arrays.asList(
      new Locale("fr","FR")
    );

    List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
    assertEquals(expectedList, actualList);
  }

  @Test
  public void testInvalidLanguagesByCountry() {
    String countryCode = "XX";
    List<Locale> expectedList = Collections.EMPTY_LIST;

    List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
    assertEquals(expectedList, actualList);
  }

  // Test cases for countriesByLanguage method
  
  @Test
  public void testValidCountriesByLanguage() {
    String languageCode = "fr";
    List<Locale> expectedList = Arrays.asList(
      new Locale("fr","FR")
    );

    List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
    assertEquals(expectedList, actualList);
  }

  @Test
  public void testInvalidCountriesByLanguage() {
    String languageCode = "xx";
    List<Locale> expectedList = Collections.EMPTY_LIST;

    List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
    assertEquals(expectedList, actualList);
  }

}