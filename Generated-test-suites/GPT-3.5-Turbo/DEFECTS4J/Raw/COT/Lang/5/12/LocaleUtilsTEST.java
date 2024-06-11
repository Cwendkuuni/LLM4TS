import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    // Test valid inputs
    assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
    assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
    assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

    // Test invalid inputs
    try {
      LocaleUtils.toLocale(""); // Empty string
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      LocaleUtils.toLocale("e"); // Invalid format
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      LocaleUtils.toLocale("en_GB_"); // Invalid format
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");

    // Test with defaultLocale
    assertEquals(4, LocaleUtils.localeLookupList(locale).size());
    assertEquals(locale, LocaleUtils.localeLookupList(locale).get(0));
    assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale).get(1));
    assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale).get(2));
    assertEquals(Locale.getDefault(), LocaleUtils.localeLookupList(locale).get(3));

    // Test without defaultLocale
    assertEquals(3, LocaleUtils.localeLookupList(locale, null).size());
    assertEquals(locale, LocaleUtils.localeLookupList(locale, null).get(0));
    assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale, null).get(1));
    assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale, null).get(2));
  }

  @Test
  public void testAvailableLocaleList() {
    assertEquals(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST, LocaleUtils.availableLocaleList());
  }

  @Test
  public void testAvailableLocaleSet() {
    assertEquals(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET, LocaleUtils.availableLocaleSet());
  }

  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
  }

  @Test
  public void testLanguagesByCountry() {
    // Test with valid inputs
    assertEquals(3, LocaleUtils.languagesByCountry("CA").size());
    assertTrue(LocaleUtils.languagesByCountry("CA").contains(new Locale("fr", "CA")));
    assertTrue(LocaleUtils.languagesByCountry("CA").contains(new Locale("en", "CA")));
    assertTrue(LocaleUtils.languagesByCountry("CA").contains(new Locale("fr")));

    // Test with invalid inputs
    assertEquals(0, LocaleUtils.languagesByCountry("").size());
    assertEquals(0, LocaleUtils.languagesByCountry("XY").size());
  }

  @Test
  public void testCountriesByLanguage() {
    // Test with valid inputs
    assertEquals(1, LocaleUtils.countriesByLanguage("fr").size());
    assertTrue(LocaleUtils.countriesByLanguage("fr").contains(new Locale("fr", "CA")));

    // Test with invalid inputs
    assertEquals(0, LocaleUtils.countriesByLanguage("").size());
    assertEquals(0, LocaleUtils.countriesByLanguage("XY").size());
  }

}