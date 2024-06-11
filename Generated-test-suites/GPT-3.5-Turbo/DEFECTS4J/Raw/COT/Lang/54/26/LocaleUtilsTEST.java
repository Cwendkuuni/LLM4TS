import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    Locale locale1 = LocaleUtils.toLocale("en");
    Locale locale2 = new Locale("en", "");
    Assert.assertEquals(locale2, locale1);

    Locale locale3 = LocaleUtils.toLocale("en_GB");
    Locale locale4 = new Locale("en", "GB");
    Assert.assertEquals(locale4, locale3);

    Locale locale5 = LocaleUtils.toLocale("en_GB_xxx");
    Locale locale6 = new Locale("en", "GB", "xxx");
    Assert.assertEquals(locale6, locale5);

    try {
      LocaleUtils.toLocale("en_GB_xxxx");
      Assert.fail("Invalid locale format should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale1 = new Locale("fr", "CA", "xxx");
    List list1 = LocaleUtils.localeLookupList(locale1);
    Assert.assertEquals(Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr")), list1);

    Locale locale2 = new Locale("en");
    List list2 = LocaleUtils.localeLookupList(locale1, locale2);
    Assert.assertEquals(Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), locale2), list2);
  }

  @Test
  public void testAvailableLocaleList() {
    List list = LocaleUtils.availableLocaleList();
    Assert.assertFalse(list.isEmpty());
    Assert.assertTrue(list.contains(new Locale("en")));
  }

  @Test
  public void testAvailableLocaleSet() {
    Set set = LocaleUtils.availableLocaleSet();
    Assert.assertFalse(set.isEmpty());
    Assert.assertTrue(set.contains(new Locale("en")));
  }

  @Test
  public void testIsAvailableLocale() {
    boolean isAvailable = LocaleUtils.isAvailableLocale(new Locale("en"));
    Assert.assertTrue(isAvailable);

    boolean isAvailable2 = LocaleUtils.isAvailableLocale(new Locale("xx"));
    Assert.assertFalse(isAvailable2);
  }

  @Test
  public void testLanguagesByCountry() {
    List langs = LocaleUtils.languagesByCountry("CA");
    Assert.assertEquals(Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA")), langs);

    List langs2 = LocaleUtils.languagesByCountry("XX");
    Assert.assertTrue(langs2.isEmpty());
  }

  @Test
  public void testCountriesByLanguage() {
    List countries = LocaleUtils.countriesByLanguage("fr");
    Assert.assertEquals(Arrays.asList(new Locale("fr", "CA")), countries);

    List countries2 = LocaleUtils.countriesByLanguage("xx");
    Assert.assertTrue(countries2.isEmpty());
  }

}