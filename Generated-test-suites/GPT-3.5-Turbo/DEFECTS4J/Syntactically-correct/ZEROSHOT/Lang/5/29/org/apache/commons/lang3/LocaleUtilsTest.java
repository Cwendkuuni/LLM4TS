import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    Locale locale1 = LocaleUtils.toLocale("en");
    assertEquals(new Locale("en", ""), locale1);

    Locale locale2 = LocaleUtils.toLocale("en_GB");
    assertEquals(new Locale("en", "GB"), locale2);

    Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
    assertEquals(new Locale("en", "GB", "xxx"), locale3);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testToLocaleInvalidFormat() {
    LocaleUtils.toLocale("invalid");
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
    Locale defaultLocale = LocaleUtils.toLocale("en");

    List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
    assertEquals(4, lookupList.size());
    assertEquals(locale, lookupList.get(0));
    assertEquals(new Locale("fr", "CA"), lookupList.get(1));
    assertEquals(new Locale("fr", ""), lookupList.get(2));
    assertEquals(defaultLocale, lookupList.get(3));

    List<Locale> lookupList2 = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertEquals(4, lookupList2.size());
    assertEquals(locale, lookupList2.get(0));
    assertEquals(new Locale("fr", "CA"), lookupList2.get(1));
    assertEquals(new Locale("fr", ""), lookupList2.get(2));
    assertEquals(defaultLocale, lookupList2.get(3));
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
    assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
  }

  @Test
  public void testLanguagesByCountry() {
    List<Locale> languages = LocaleUtils.languagesByCountry("CA");
    assertNotNull(languages);
  }

  @Test
  public void testCountriesByLanguage() {
    List<Locale> countries = LocaleUtils.countriesByLanguage("en");
    assertNotNull(countries);
  }
}