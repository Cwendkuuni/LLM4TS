import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleWithInvalidFormat() {
    LocaleUtils.toLocale("enGB");
  }

  @Test
  public void testLocaleLookupListWithNullLocale() {
    List expected = Collections.emptyList();
    assertEquals(expected, LocaleUtils.localeLookupList(null));
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
    assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
  }

  @Test
  public void testAvailableLocaleList() {
    List<Locale> locales = LocaleUtils.availableLocaleList();
    assertNotNull(locales);
    assertFalse(locales.isEmpty());
  }

  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> locales = LocaleUtils.availableLocaleSet();
    assertNotNull(locales);
    assertFalse(locales.isEmpty());
  }

  @Test
  public void testIsAvailableLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    assertTrue(LocaleUtils.isAvailableLocale(locale));
  }

  @Test
  public void testIsAvailableLocaleWithInvalidLocale() {
    Locale locale = new Locale("xyz");
    assertFalse(LocaleUtils.isAvailableLocale(locale));
  }

  @Test
  public void testLanguagesByCountry() {
    List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("fr"));
    List<Locale> actual = LocaleUtils.languagesByCountry("CA");
    assertEquals(expected, actual);
  }

  @Test
  public void testLanguagesByCountryWithNullCountryCode() {
    List<Locale> expected = Collections.emptyList();
    assertEquals(expected, LocaleUtils.languagesByCountry(null));
  }

  @Test
  public void testCountriesByLanguage() {
    List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("GB"));
    List<Locale> actual = LocaleUtils.countriesByLanguage("en");
    assertEquals(expected, actual);
  }

  @Test
  public void testCountriesByLanguageWithNullLanguageCode() {
    List<Locale> expected = Collections.emptyList();
    assertEquals(expected, LocaleUtils.countriesByLanguage(null));
  }

}