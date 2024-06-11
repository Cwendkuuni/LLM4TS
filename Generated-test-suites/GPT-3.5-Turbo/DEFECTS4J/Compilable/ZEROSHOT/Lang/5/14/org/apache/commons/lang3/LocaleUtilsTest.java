package org.apache.commons.lang3;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    // Test null input
    assertNull(LocaleUtils.toLocale(null));

    // Test valid locale strings
    assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

    // Test invalid locale strings
    try {
      LocaleUtils.toLocale("EN");
      fail("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // Expected exception thrown
    }

    try {
      LocaleUtils.toLocale("en_gb");
      fail("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // Expected exception thrown
    }
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale = new Locale("fr", "CA", "xxx");
    List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));

    assertEquals(expected, LocaleUtils.localeLookupList(locale));
  }

  @Test
  public void testLocaleLookupListWithDefault() {
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));

    assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
  }

  @Test
  public void testAvailableLocaleList() {
    // Test that the list is unmodifiable
    List<Locale> locales = LocaleUtils.availableLocaleList();
    try {
      locales.add(new Locale("test"));
      fail("UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception thrown
    }
  }

  @Test
  public void testAvailableLocaleSet() {
    // Test that the set is unmodifiable
    Set<Locale> locales = LocaleUtils.availableLocaleSet();
    try {
      locales.add(new Locale("test"));
      fail("UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception thrown
    }
  }

  @Test
  public void testIsAvailableLocale() {
    assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    assertFalse(LocaleUtils.isAvailableLocale(new Locale("test")));
  }

  @Test
  public void testLanguagesByCountry() {
    List<Locale> expected = Arrays.asList(Locale.CANADA_FRENCH);

    assertEquals(expected, LocaleUtils.languagesByCountry("CA"));
  }

  @Test
  public void testCountriesByLanguage() {
    List<Locale> expected = Arrays.asList(Locale.CANADA_FRENCH);

    assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));
  }

}