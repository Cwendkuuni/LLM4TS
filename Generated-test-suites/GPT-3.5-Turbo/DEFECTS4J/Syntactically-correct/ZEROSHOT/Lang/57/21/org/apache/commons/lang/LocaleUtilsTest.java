import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidFormat() {
    LocaleUtils.toLocale("abc");
  }

  @Test
  public void testToLocaleNullInput() {
    assertNull(LocaleUtils.toLocale(null));
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"));
    assertEquals(expected, LocaleUtils.localeLookupList(locale));
  }

  @Test
  public void testLocaleLookupListWithDefaultLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
    assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
  }

  @Test
  public void testAvailableLocaleList() {
    List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
    assertEquals(expected, LocaleUtils.availableLocaleList());
  }

  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> expected = new HashSet<>(LocaleUtils.availableLocaleList());
    assertEquals(expected, LocaleUtils.availableLocaleSet());
  }

  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
  }

  @Test
  public void testLanguagesByCountry() {
    List<Locale> expected = List.of(new Locale("en", "GB"), new Locale("en", "US"));
    assertEquals(expected, LocaleUtils.languagesByCountry("GB"));
  }

  @Test
  public void testLanguagesByCountryNullInput() {
    List<Locale> expected = Collections.EMPTY_LIST;
    assertEquals(expected, LocaleUtils.languagesByCountry(null));
  }

  @Test
  public void testCountriesByLanguage() {
    List<Locale> expected = List.of(new Locale("en", "GB"), new Locale("en", "US"));
    assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
  }

  @Test
  public void testCountriesByLanguageNullInput() {
    List<Locale> expected = Collections.EMPTY_LIST;
    assertEquals(expected, LocaleUtils.countriesByLanguage(null));
  }

}