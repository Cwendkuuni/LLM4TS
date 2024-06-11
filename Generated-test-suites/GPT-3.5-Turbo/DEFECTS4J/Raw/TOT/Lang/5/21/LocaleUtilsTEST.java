import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    // Test a valid locale string with language and country
    String str1 = "en_GB";
    Locale expected1 = new Locale("en", "GB");
    Locale actual1 = LocaleUtils.toLocale(str1);
    Assert.assertEquals(expected1, actual1);

    // Test a valid locale string with language, country, and variant
    String str2 = "en_GB_xxx";
    Locale expected2 = new Locale("en", "GB", "xxx");
    Locale actual2 = LocaleUtils.toLocale(str2);
    Assert.assertEquals(expected2, actual2);

    // Test an invalid locale string with only one character
    String str3 = "e";
    try {
      LocaleUtils.toLocale(str3);
      Assert.fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      // Exception is expected
    }

    // Test an invalid locale string with invalid characters
    String str4 = "EN-gb";
    try {
      LocaleUtils.toLocale(str4);
      Assert.fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      // Exception is expected
    }
  }

  @Test
  public void testLocaleLookupList() {
    // Test with a valid locale and a default locale
    Locale locale1 = new Locale("fr", "CA", "xxx");
    Locale defaultLocale1 = new Locale("en");
    List<Locale> expected1 = List.of(
      new Locale("fr", "CA", "xxx"),
      new Locale("fr", "CA"),
      new Locale("fr"),
      new Locale("en")
    );
    List<Locale> actual1 = LocaleUtils.localeLookupList(locale1, defaultLocale1);
    Assert.assertEquals(expected1, actual1);

    // Test with a null locale and a default locale
    Locale locale2 = null;
    Locale defaultLocale2 = new Locale("en");
    List<Locale> expected2 = List.of(new Locale("en"));
    List<Locale> actual2 = LocaleUtils.localeLookupList(locale2, defaultLocale2);
    Assert.assertEquals(expected2, actual2);
  }

  @Test
  public void testAvailableLocaleList() {
    List<Locale> locales = LocaleUtils.availableLocaleList();
    for (Locale locale : locales) {
      Assert.assertNotNull(locale);
    }
  }

  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> locales = LocaleUtils.availableLocaleSet();
    for (Locale locale : locales) {
      Assert.assertNotNull(locale);
    }
  }

  @Test
  public void testIsAvailableLocale() {
    // Test with an available locale
    Locale locale1 = new Locale("en", "US");
    boolean expected1 = true;
    boolean actual1 = LocaleUtils.isAvailableLocale(locale1);
    Assert.assertEquals(expected1, actual1);

    // Test with an unavailable locale
    Locale locale2 = new Locale("zz", "ZZ");
    boolean expected2 = false;
    boolean actual2 = LocaleUtils.isAvailableLocale(locale2);
    Assert.assertEquals(expected2, actual2);
  }

  @Test
  public void testLanguagesByCountry() {
    // Test with a valid country code
    String countryCode1 = "US";
    List<Locale> expected1 = List.of(
      new Locale("en", "US"),
      new Locale("es", "US")
    );
    List<Locale> actual1 = LocaleUtils.languagesByCountry(countryCode1);
    Assert.assertEquals(expected1, actual1);

    // Test with a null country code
    String countryCode2 = null;
    List<Locale> expected2 = List.of();
    List<Locale> actual2 = LocaleUtils.languagesByCountry(countryCode2);
    Assert.assertEquals(expected2, actual2);
  }

  @Test
  public void testCountriesByLanguage() {
    // Test with a valid language code
    String languageCode1 = "en";
    List<Locale> expected1 = List.of(
      new Locale("en", "US"),
      new Locale("en", "GB")
    );
    List<Locale> actual1 = LocaleUtils.countriesByLanguage(languageCode1);
    Assert.assertEquals(expected1, actual1);

    // Test with a null language code
    String languageCode2 = null;
    List<Locale> expected2 = List.of();
    List<Locale> actual2 = LocaleUtils.countriesByLanguage(languageCode2);
    Assert.assertEquals(expected2, actual2);
  }

}