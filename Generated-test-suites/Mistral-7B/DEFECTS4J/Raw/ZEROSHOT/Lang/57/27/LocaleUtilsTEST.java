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
 LocaleUtils.toLocale("enGB");
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListEmpty() {
 List<Locale> actual = LocaleUtils.localeLookupList(null, new Locale("en"));
 assertNotNull(actual);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 assertFalse(actual.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 assertFalse(actual.isEmpty());
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr", "CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleInvalidLocale() {
 Locale locale = new Locale("fr", "CA", "XXX");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
 List<Locale> actual = LocaleUtils.languagesByCountry("CA");
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountryEmpty() {
 List<Locale> actual = LocaleUtils.languagesByCountry(null);
 assertNotNull(actual);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
 List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguageEmpty() {
 List<Locale> actual = LocaleUtils.countriesByLanguage(null);
 assertNotNull(actual);
 assertTrue(actual.isEmpty());
 }

}