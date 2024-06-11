import org.junit.Assert;
import org.junit.Test;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    // Test valid input
    Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

    // Test invalid input
    try {
      LocaleUtils.toLocale(null);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }

    try {
      LocaleUtils.toLocale("e");
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }

    try {
      LocaleUtils.toLocale("en_");
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testLocaleLookupList() {
    // Test locale and default locale not null
    Locale locale = new Locale("fr", "CA", "xxx");
    Locale defaultLocale = new Locale("en");
    List<Locale> expected1 = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
    Assert.assertEquals(expected1, LocaleUtils.localeLookupList(locale, defaultLocale));

    // Test locale null, default locale not null
    locale = null;
    List<Locale> expected2 = Collections.singletonList(defaultLocale);
    Assert.assertEquals(expected2, LocaleUtils.localeLookupList(locale, defaultLocale));

    // Test locale and default locale null
    defaultLocale = null;
    List<Locale> expected3 = Collections.emptyList();
    Assert.assertEquals(expected3, LocaleUtils.localeLookupList(locale, defaultLocale));
  }

  @Test
  public void testAvailableLocaleList() {
    List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
    Assert.assertEquals(expected, LocaleUtils.availableLocaleList());
  }

  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
    Assert.assertEquals(expected, LocaleUtils.availableLocaleSet());
  }

  @Test
  public void testIsAvailableLocale() {
    // Test available locale
    Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

    // Test unavailable locale
    Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
  }

  @Test
  public void testLanguagesByCountry() {
    // Test valid input
    List<Locale> expected1 = Arrays.asList(
        new Locale("en", "GB"),
        new Locale("fr", "GB")
    );
    Assert.assertEquals(expected1, LocaleUtils.languagesByCountry("GB"));

    // Test null input
    List<Locale> expected2 = Collections.emptyList();
    Assert.assertEquals(expected2, LocaleUtils.languagesByCountry(null));
  }

  @Test
  public void testCountriesByLanguage() {
    // Test valid input
    List<Locale> expected1 = Arrays.asList(
        new Locale("en", "GB"),
        new Locale("en", "US")
    );
    Assert.assertEquals(expected1, LocaleUtils.countriesByLanguage("en"));

    // Test null input
    List<Locale> expected2 = Collections.emptyList();
    Assert.assertEquals(expected2, LocaleUtils.countriesByLanguage(null));
  }
}