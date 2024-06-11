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
 LocaleUtils.toLocale("en_GB_xxx"); // invalid variant
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
 public void testIsAvailableLocaleFalse() {
 Locale locale = new Locale("fr", "CA", "xxx");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> actual = LocaleUtils.languagesByCountry("CA");
 List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountryEmpty() {
 List<Locale> actual = LocaleUtils.languagesByCountry(null);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> actual = LocaleUtils.countriesByLanguage("en");
 List<Locale> expected = Arrays.asList(new Locale("CA", "en"), new Locale("US", "en"), new Locale("GB", "en"));
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguageEmpty() {
 List<Locale> actual = LocaleUtils.countriesByLanguage(null);
 assertTrue(actual.isEmpty());
 }
}