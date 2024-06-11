import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

  @Test
  public void test_toLocale_null() {
    Locale result = LocaleUtils.toLocale(null);
    assertNull(result);
  }

  @Test
  public void test_toLocale_validInput() {
    Locale result = LocaleUtils.toLocale("en_GB");
    assertEquals("en", result.getLanguage());
    assertEquals("GB", result.getCountry());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_toLocale_invalidFormat() {
    LocaleUtils.toLocale("english");
  }

  @Test
  public void test_localeLookupList_null() {
    List<Locale> result = LocaleUtils.localeLookupList(null);
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void test_localeLookupList_validInput() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> result = LocaleUtils.localeLookupList(locale);
    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals("fr", result.get(0).getLanguage());
    assertEquals("CA", result.get(0).getCountry());
    assertEquals("fr", result.get(1).getLanguage());
    assertEquals("", result.get(1).getCountry());
    assertEquals("fr", result.get(2).getLanguage());
    assertEquals("", result.get(2).getCountry());
  }

  @Test
  public void test_localeLookupList_validInputWithDefaultLocale() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
    assertNotNull(result);
    assertEquals(4, result.size());
    assertEquals("fr", result.get(0).getLanguage());
    assertEquals("CA", result.get(0).getCountry());
    assertEquals("fr", result.get(1).getLanguage());
    assertEquals("", result.get(1).getCountry());
    assertEquals("fr", result.get(2).getLanguage());
    assertEquals("", result.get(2).getCountry());
    assertEquals("en", result.get(3).getLanguage());
    assertEquals("", result.get(3).getCountry());
  }

  @Test
  public void test_availableLocaleList() {
    List<Locale> result = LocaleUtils.availableLocaleList();
    assertNotNull(result);
    assertTrue(result.size() > 0);
  }

  @Test
  public void test_availableLocaleSet() {
    Set<Locale> result = LocaleUtils.availableLocaleSet();
    assertNotNull(result);
    assertTrue(result.size() > 0);
  }

  @Test
  public void test_isAvailableLocale_true() {
    Locale locale = new Locale("en", "US");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    assertTrue(result);
  }

  @Test
  public void test_isAvailableLocale_false() {
    Locale locale = new Locale("en", "XX");
    boolean result = LocaleUtils.isAvailableLocale(locale);
    assertFalse(result);
  }

  @Test
  public void test_languagesByCountry_null() {
    List<Locale> result = LocaleUtils.languagesByCountry(null);
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void test_languagesByCountry_validInput() {
    List<Locale> result = LocaleUtils.languagesByCountry("US");
    assertNotNull(result);
    assertTrue(result.size() > 0);
  }

  @Test
  public void test_countriesByLanguage_null() {
    List<Locale> result = LocaleUtils.countriesByLanguage(null);
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void test_countriesByLanguage_validInput() {
    List<Locale> result = LocaleUtils.countriesByLanguage("en");
    assertNotNull(result);
    assertTrue(result.size() > 0);
  }

}