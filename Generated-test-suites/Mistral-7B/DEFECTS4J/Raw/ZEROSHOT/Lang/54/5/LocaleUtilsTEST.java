import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expected = new Locale("en", "GB");
 Locale actual = LocaleUtils.toLocale("en_GB");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 LocaleUtils.toLocale("en_GB_invalid");
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"),
 new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListDefaultLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 Locale defaultLocale = new Locale("en");
 List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"),
 new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = new HashSet<>(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 Locale invalidLocale = new Locale("en", "XX");
 assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
 List<Locale> actual = LocaleUtils.languagesByCountry("CA");
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountryEmpty() {
 List<Locale> actual = LocaleUtils.languagesByCountry(null);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> expected = Arrays.asList(new Locale("CA", "en"), new Locale("CA", "fr"));
 List<Locale> actual = LocaleUtils.countriesByLanguage("en");
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguageEmpty() {
 List<Locale> actual = LocaleUtils.countriesByLanguage(null);
 assertTrue(actual.isEmpty());
 }
}