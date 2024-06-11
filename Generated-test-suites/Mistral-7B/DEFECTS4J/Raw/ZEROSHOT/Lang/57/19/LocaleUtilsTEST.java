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
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> actual = LocaleUtils.languagesByCountry("CA");
 List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> actual = LocaleUtils.countriesByLanguage("en");
 List<Locale> expected = Arrays.asList(new Locale("CA", "en"), new Locale("GB", "en"), new Locale("US", "en"));
 assertEquals(expected, actual);
 }

}