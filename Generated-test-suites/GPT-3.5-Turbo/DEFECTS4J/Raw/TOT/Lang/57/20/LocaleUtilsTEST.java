import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

  @Test
  public void testToLocaleValidLanguageCode() {
    Locale locale = LocaleUtils.toLocale("en");
    assertEquals("en", locale.getLanguage());
  }

  @Test
  public void testToLocaleValidLanguageAndCountryCode() {
    Locale locale = LocaleUtils.toLocale("en_GB");
    assertEquals("en", locale.getLanguage());
    assertEquals("GB", locale.getCountry());
  }

  @Test
  public void testToLocaleValidLanguageCountryAndVariantCode() {
    Locale locale = LocaleUtils.toLocale("en_GB_xxx");
    assertEquals("en", locale.getLanguage());
    assertEquals("GB", locale.getCountry());
    assertEquals("xxx", locale.getVariant());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidLength() {
    Locale locale = LocaleUtils.toLocale("en_GB_xxxx");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidLanguageCase() {
    Locale locale = LocaleUtils.toLocale("En");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidCountryCase() {
    Locale locale = LocaleUtils.toLocale("en_gb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidSeparator() {
    Locale locale = LocaleUtils.toLocale("en-GB");
  }

  @Test
  public void testToLocaleNullString() {
    Locale locale = LocaleUtils.toLocale(null);
    assertNull(locale);
  }

  @Test
  public void testLocaleLookupListWithLocale() {
    Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
    List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
    assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
  }

  @Test
  public void testLocaleLookupListWithLocaleAndDefaultLocale() {
    Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
    assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
  }

  @Test
  public void testLocaleLookupListWithNullLocale() {
    List<Locale> expectedList = Arrays.asList(Locale.getDefault());
    assertEquals(expectedList, LocaleUtils.localeLookupList(null));
  }

  @Test
  public void testLocaleLookupListWithNullLocaleAndDefaultLocale() {
    Locale defaultLocale = new Locale("en");
    List<Locale> expectedList = Arrays.asList(defaultLocale);
    assertEquals(expectedList, LocaleUtils.localeLookupList(null, defaultLocale));
  }

  @Test
  public void testAvailableLocaleList() {
    List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
    assertEquals(expectedList, LocaleUtils.availableLocaleList());
  }

  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
    assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
  }

  @Test
  public void testIsAvailableLocaleWithAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
  }

  @Test
  public void testIsAvailableLocaleWithUnavailableLocale() {
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
  }

  @Test
  public void testLanguagesByCountryWithValidCountryCode() {
    List<Locale> expectedList = Arrays.asList(Locale.CANADA, Locale.CANADA_FRENCH);
    assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));
  }

  @Test
  public void testLanguagesByCountryWithInvalidCountryCode() {
    List<Locale> expectedList = Collections.emptyList();
    assertEquals(expectedList, LocaleUtils.languagesByCountry("XX"));
  }

  @Test
  public void testLanguagesByCountryWithNullCountryCode() {
    List<Locale> expectedList = Collections.emptyList();
    assertEquals(expectedList, LocaleUtils.languagesByCountry(null));
  }

  @Test
  public void testCountriesByLanguageWithValidLanguageCode() {
    List<Locale> expectedList = Arrays.asList(Locale.CANADA, Locale.CANADA_FRENCH);
    assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
  }

  @Test
  public void testCountriesByLanguageWithInvalidLanguageCode() {
    List<Locale> expectedList = Collections.emptyList();
    assertEquals(expectedList, LocaleUtils.countriesByLanguage("xx"));
  }

  @Test
  public void testCountriesByLanguageWithNullLanguageCode() {
    List<Locale> expectedList = Collections.emptyList();
    assertEquals(expectedList, LocaleUtils.countriesByLanguage(null));
  }

}