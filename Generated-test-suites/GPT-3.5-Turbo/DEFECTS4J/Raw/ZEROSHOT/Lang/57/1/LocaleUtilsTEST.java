import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    // Test valid string formats
    assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

    // Test invalid string formats
    try {
      LocaleUtils.toLocale("e"); // Less than 2 characters
      fail("Exception not thrown for invalid locale format");
    } catch (IllegalArgumentException e) {}

    try {
      LocaleUtils.toLocale("EN"); // Upper case characters
      fail("Exception not thrown for invalid locale format");
    } catch (IllegalArgumentException e) {}

    // Test null input
    assertNull(LocaleUtils.toLocale(null));
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> expectedList = new ArrayList<Locale>(Arrays.asList(
        new Locale("fr", "CA", "xxx"),
        new Locale("fr", "CA"),
        new Locale("fr"),
        new Locale("en")
    ));
    assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));

    // Test null input
    assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null, defaultLocale));

    // Test defaultLocale = null
    assertEquals(expectedList, LocaleUtils.localeLookupList(locale, null));
  }

  @Test
  public void testAvailableLocaleList() {
    List<Locale> availableLocales = LocaleUtils.availableLocaleList();

    // Test non-null and non-empty
    assertNotNull(availableLocales);
    assertFalse(availableLocales.isEmpty());

    // Test immutability
    try {
      availableLocales.add(new Locale("es"));
      fail("List is mutable");
    } catch (UnsupportedOperationException e) {}
  }

  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();

    // Test non-null and non-empty
    assertNotNull(availableLocaleSet);
    assertFalse(availableLocaleSet.isEmpty());

    // Test immutability
    try {
      availableLocaleSet.add(new Locale("es"));
      fail("Set is mutable");
    } catch (UnsupportedOperationException e) {}
  }

  @Test
  public void testIsAvailableLocale() {
    // Test available locale
    assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

    // Test non available locale
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("es")));

    // Test null input
    assertFalse(LocaleUtils.isAvailableLocale(null));
  }

  @Test
  public void testLanguagesByCountry() {
    String countryCode = "US";
    List<Locale> expectedLanguages = new ArrayList<Locale>(Arrays.asList(
        new Locale("en", "US")
    ));
    assertEquals(expectedLanguages, LocaleUtils.languagesByCountry(countryCode));

    // Test null input
    assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
  }

  @Test
  public void testCountriesByLanguage() {
    String languageCode = "en";
    List<Locale> expectedCountries = new ArrayList<Locale>(Arrays.asList(
        new Locale("en", "US"),
        new Locale("en", "GB")
    ));
    assertEquals(expectedCountries, LocaleUtils.countriesByLanguage(languageCode));

    // Test null input
    assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
  }
}