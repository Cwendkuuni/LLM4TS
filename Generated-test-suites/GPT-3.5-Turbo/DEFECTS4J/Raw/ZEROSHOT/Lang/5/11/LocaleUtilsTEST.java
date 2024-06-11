import org.junit.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    Locale locale = LocaleUtils.toLocale("en");
    assertNotNull(locale);
    assertEquals("en", locale.getLanguage());

    locale = LocaleUtils.toLocale("en_GB");
    assertNotNull(locale);
    assertEquals("en", locale.getLanguage());
    assertEquals("GB", locale.getCountry());

    locale = LocaleUtils.toLocale("en_GB_xxx");
    assertNotNull(locale);
    assertEquals("en", locale.getLanguage());
    assertEquals("GB", locale.getCountry());
    assertEquals("xxx", locale.getVariant());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalid1() {
    LocaleUtils.toLocale("e");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalid2() {
    LocaleUtils.toLocale("eng");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalid3() {
    LocaleUtils.toLocale("en_GB_");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalid4() {
    LocaleUtils.toLocale("en_GB_xxxx");
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> list = LocaleUtils.localeLookupList(locale);
    assertNotNull(list);
    assertEquals(3, list.size());
    assertEquals(locale, list.get(0));
    assertTrue(list.contains(new Locale("fr", "CA")));
    assertTrue(list.contains(new Locale("fr")));
  }

  @Test
  public void testLocaleLookupListWithDefaultLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertNotNull(list);
    assertEquals(4, list.size());
    assertEquals(locale, list.get(0));
    assertTrue(list.contains(new Locale("fr", "CA")));
    assertTrue(list.contains(new Locale("fr")));
    assertTrue(list.contains(new Locale("en")));
  }

  @Test
  public void testAvailableLocaleList() {
    List<Locale> list = LocaleUtils.availableLocaleList();
    assertNotNull(list);
    assertFalse(list.isEmpty());
    assertTrue(list.contains(Locale.US));
    assertTrue(list.contains(Locale.UK));
    assertTrue(list.contains(Locale.FRANCE));
  }

  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> set = LocaleUtils.availableLocaleSet();
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(set.contains(Locale.US));
    assertTrue(set.contains(Locale.UK));
    assertTrue(set.contains(Locale.FRANCE));
  }

  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
    assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("fake")));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid")));
  }

  @Test
  public void testLanguagesByCountry() {
    List<Locale> list = LocaleUtils.languagesByCountry("CA");
    assertNotNull(list);
    assertFalse(list.isEmpty());
    assertTrue(list.contains(new Locale("fr", "CA")));
    assertTrue(list.contains(new Locale("en", "CA")));
  }

  @Test
  public void testLanguagesByInvalidCountry() {
    List<Locale> list = LocaleUtils.languagesByCountry("fake");
    assertNotNull(list);
    assertTrue(list.isEmpty());
  }

  @Test
  public void testCountriesByLanguage() {
    List<Locale> list = LocaleUtils.countriesByLanguage("fr");
    assertNotNull(list);
    assertFalse(list.isEmpty());
    assertTrue(list.contains(new Locale("fr", "CA")));
    assertTrue(list.contains(new Locale("fr", "BE")));
  }

  @Test
  public void testCountriesByInvalidLanguage() {
    List<Locale> list = LocaleUtils.countriesByLanguage("fake");
    assertNotNull(list);
    assertTrue(list.isEmpty());
  }
}