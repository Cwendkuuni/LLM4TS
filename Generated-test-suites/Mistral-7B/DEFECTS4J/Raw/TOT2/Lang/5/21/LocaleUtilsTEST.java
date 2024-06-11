import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for toLocale method
 @Test
 public void testToLocale() {
 String input = "en_US";
 Locale expected = new Locale("en", "US");
 Locale actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = "fr_CA";
 expected = new Locale("fr", "CA");
 actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = "es_ES";
 expected = new Locale("es", "ES");
 actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = "";
 actual = LocaleUtils.toLocale(input);
 assertNull(actual);

 input = "xy_XY";
 expected = new Locale("xy", "XY");
 actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = "en_GB_London";
 expected = new Locale("en", "GB", "London");
 actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = "fr_FR_Paris";
 expected = new Locale("fr", "FR", "Paris");
 actual = LocLocaleUtils.toLocale(input);
 assertEquals(expected, actual);
 }

 // Test case for localeLookupList method
 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> expected = Arrays.asList(
 new Locale("fr", "CA", "xxx"),
 new Locale("fr", "CA"),
 new Locale("fr"),
 new Locale("en")
 );
 List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
 assertEquals(expected, actual);

 locale = null;
 expected = Collections.emptyList();
 actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
 assertEquals(expected, actual);
 }

 // Test case for availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
 List<Locale> actual = LocaleUtils.availableLocaleList();
 assertEquals(expected, actual);
 }

 // Test case for availableLocaleSet method
 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expected = new HashSet<>(Locale.getAvailableLocales());
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 assertEquals(expected, actual);
 }

 // Test case for isAvailableLocale method
 @Test
 public void testIsAvailableLocale() {
 Locale locale1 = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale1));

 Locale locale2 = new Locale("xy", "XY");
 assertFalse(LocaleUtils.isAvailableLocale(locale2));
 }

 // Test case for languagesByCountry method
 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expected = Arrays.asList(
 new Locale("en", "US"),
 new Locale("en", "CA"),
 new Locale("en", "GB"),
 new Locale("en", "IE"),
 new Locale("en", "AU"),
 new Locale("en", "NZ")
 );
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected, actual);

 countryCode = null;
 expected = Collections.emptyList();
 actual = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected, actual);
 }

 // Test case for countriesByLanguage method
 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expected = Arrays.asList(
 new Locale("en", "US"),
 new Locale("en", "CA"),
 new Locale("en", "GB"),
 new Locale("en", "IE"),
 new Locale("en", "AU"),
 new Locale("en", "NZ")
 );
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, actual);

 languageCode = null;
 expected = Collections.emptyList();
 actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, actual);
 }
}