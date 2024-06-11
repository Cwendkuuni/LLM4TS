import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expected = new Locale("en", "US");
 Locale actual = LocaleUtils.toLocale("en_US");
 assertEquals(expected, actual);

 expected = new Locale("fr", "CA");
 actual = LocaleUtils.toLocale("fr_CA");
 assertEquals(expected, actual);

 expected = new Locale("es", "");
 actual = LocaleUtils.toLocale("es");
 assertEquals(expected, actual);

 expected = null;
 actual = LocaleUtils.toLocale(null);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 LocaleUtils.toLocale("en_US_");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleLowercaseLanguage() {
 LocaleUtils.toLocale("en_US_en");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidCountryCode() {
 LocaleUtils.toLocale("en_US_XX");
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List expected = LocaleUtils.localeLookupList(locale);
 List actual = LocaleUtils.localeLookupList(locale, Locale.getDefault());

 assertEquals(expected.size(), actual.size());
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List expected = LocaleUtils.availableLocaleList();
 List actual = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set expected = LocaleUtils.availableLocaleSet();
 Set actual = new HashSet(LocaleUtils.availableLocaleList());
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale1 = new Locale("en", "US");
 Locale locale2 = new Locale("fr", "FR");
 assertTrue(LocaleUtils.isAvailableLocale(locale1));
 assertFalse(LocaleUtils.isAvailableLocale(locale2));
 }

 @Test
 public void testLanguagesByCountry() {
 List expected = LocaleUtils.languagesByCountry("US");
 List actual = Arrays.asList(Locale.getAvailableLocales());
 List filtered = new ArrayList();
 for (Locale locale : actual) {
 if (locale.getCountry().equals("US")) {
 filtered.add(locale);
 }
 }
 assertEquals(expected, filtered);
 }

 @Test
 public void testCountriesByLanguage() {
 List expected = LocaleUtils.countriesByLanguage("en");
 List actual = Arrays.asList(Locale.getAvailableLocales());
 List filtered = new ArrayList();
 for (Locale locale : actual) {
 if (locale.getLanguage().equals("en")) {
 filtered.add(locale);
 }
 }
 List expectedCountries = new ArrayList();
 for (Locale locale : filtered) {
 expectedCountries.add(locale.getCountry());
 }
 assertEquals(expectedCountries, Collections.unmodifiableList(LocaleUtils.countriesByLanguage("en")));
 }
}