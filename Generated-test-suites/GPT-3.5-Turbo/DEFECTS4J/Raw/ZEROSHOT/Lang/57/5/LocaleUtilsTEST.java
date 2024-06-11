import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    assert(LocaleUtils.toLocale(null) == null);
    assert(LocaleUtils.toLocale("en").equals(new Locale("en", "")));
    assert(LocaleUtils.toLocale("en_GB").equals(new Locale("en", "GB")));
    assert(LocaleUtils.toLocale("en_GB_xxx").equals(new Locale("en", "GB", "xxx")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLocaleInvalidFormat() {
    LocaleUtils.toLocale("abc");
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale1 = new Locale("fr", "CA", "xxx");
    Locale locale2 = new Locale("en");

    List list1 = LocaleUtils.localeLookupList(locale1);
    List list2 = LocaleUtils.localeLookupList(locale1, locale2);

    assert(list1.get(0).equals(locale1));
    assert(list1.get(1).equals(new Locale("fr", "CA")));
    assert(list1.get(2).equals(new Locale("fr")));
    assert(list2.get(0).equals(locale1));
    assert(list2.get(1).equals(new Locale("fr", "CA")));
    assert(list2.get(2).equals(new Locale("fr")));
    assert(list2.get(3).equals(locale2));
  }

  @Test
  public void testAvailableLocaleList() {
    List list = LocaleUtils.availableLocaleList();
    assert(list.size() > 0);
  }

  @Test
  public void testAvailableLocaleSet() {
    Set set = LocaleUtils.availableLocaleSet();
    assert(set.size() > 0);
  }

  @Test
  public void testIsAvailableLocale() {
    Locale locale = new Locale("en", "US");
    assert(LocaleUtils.isAvailableLocale(locale) == true);
  }

  @Test
  public void testLanguagesByCountry() {
    List list = LocaleUtils.languagesByCountry("CA");
    assert(list.size() > 0);
  }

  @Test
  public void testCountriesByLanguage() {
    List list = LocaleUtils.countriesByLanguage("en");
    assert(list.size() > 0);
  }

}