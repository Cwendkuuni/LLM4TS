
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

public class LocaleUtilsTest {

  @Test
  public void testToLocale() {
    // Test valid locale string
    Locale locale1 = LocaleUtils.toLocale("en");
    assertEquals(new Locale("en", ""), locale1);

    Locale locale2 = LocaleUtils.toLocale("en_GB");
    assertEquals(new Locale("en", "GB"), locale2);

    Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
    assertEquals(new Locale("en", "GB", "xxx"), locale3);

    // Test invalid locale strings
    try {
      LocaleUtils.toLocale("EN");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid locale format: EN", e.getMessage());
    }

    try {
      LocaleUtils.toLocale("en_GB_");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid locale format: en_GB_", e.getMessage());
    }
  }

  @Test
  public void testLocaleLookupList() {
    Locale locale1 = LocaleUtils.toLocale("fr_CA_xxx");
    Locale defaultLocale = LocaleUtils.toLocale("en");

    List<Locale> result = LocaleUtils.localeLookupList(locale1);
    List<Locale> expected = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
    assertEquals(expected, result);

    List<Locale> result2 = LocaleUtils.localeLookupList(locale1, defaultLocale);
    assertEquals(expected, result2);
  }

  @Test
  public void testAvailableLocaleList() {
    List<Locale> result = LocaleUtils.availableLocaleList();
    List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
    assertEquals(expected, result);
  }

  @Test
  public void testAvailableLocaleSet() {
    Set<Locale> result = LocaleUtils.availableLocaleSet();
    Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
    assertEquals(expected, result);
  }

  @Test
  public void testIsAvailableLocale() {
    Locale locale = LocaleUtils.toLocale("fr_CA");

    boolean result = LocaleUtils.isAvailableLocale(locale);
    assertEquals(true, result);

    boolean result2 = LocaleUtils.isAvailableLocale(new Locale("xx"));
    assertEquals(false, result2);
  }

  @Test
  public void testLanguagesByCountry() {
    List<Locale> result = LocaleUtils.languagesByCountry("CA");
    List<Locale> expected = new ArrayList<>();
    expected.add(LocaleUtils.toLocale("en_CA"));
    expected.add(LocaleUtils.toLocale("fr_CA"));
    assertEquals(expected, result);
  }

  @Test
  public void testCountriesByLanguage() {
    List<Locale> result = LocaleUtils.countriesByLanguage("fr");
    List<Locale> expected = new ArrayList<>();
    expected.add(LocaleUtils.toLocale("fr_CA"));
    expected.add(LocaleUtils.toLocale("fr_FR"));
    expected.add(LocaleUtils.toLocale("fr_CH"));
    assertEquals(expected, result);
  }
}
