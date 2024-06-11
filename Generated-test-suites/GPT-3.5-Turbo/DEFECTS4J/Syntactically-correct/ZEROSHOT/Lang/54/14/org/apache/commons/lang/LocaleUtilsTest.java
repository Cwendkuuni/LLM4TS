import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    assertNull(LocaleUtils.toLocale(null));
    assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
    assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
    assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidFormat() {
    LocaleUtils.toLocale("invalid");
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> list = LocaleUtils.localeLookupList(locale);
    assertEquals(locale, list.get(0));
    assertEquals(new Locale("fr", "CA"), list.get(1));
    assertEquals(new Locale("fr"), list.get(2));
  }

  @Test
  public void testLocaleLookupListWithDefaultLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = Locale.ENGLISH;
    List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(locale, list.get(0));
    assertEquals(new Locale("fr", "CA"), list.get(1));
    assertEquals(new Locale("fr"), list.get(2));
    assertEquals(defaultLocale, list.get(3));
  }

  @Test
  public void testAvailableLocaleList() {
    assertNotNull(LocaleUtils.availableLocaleList());
    assertEquals(Locale.getAvailableLocales().length, LocaleUtils.availableLocaleList().size());
  }

  @Test
  public void testAvailableLocaleSet() {
    assertNotNull(LocaleUtils.availableLocaleSet());
    assertEquals(Locale.getAvailableLocales().length, LocaleUtils.availableLocaleSet().size());
  }

  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid")));
  }

  @Test
  public void testLanguagesByCountry() {
    assertNull(LocaleUtils.languagesByCountry(null));
    assertEquals(1, LocaleUtils.languagesByCountry("GB").size());
    assertEquals(0, LocaleUtils.languagesByCountry("invalid").size());
  }

  @Test
  public void testCountriesByLanguage() {
    assertNull(LocaleUtils.countriesByLanguage(null));
    assertEquals(1, LocaleUtils.countriesByLanguage("en").size());
    assertEquals(0, LocaleUtils.countriesByLanguage("invalid").size());
  }
}