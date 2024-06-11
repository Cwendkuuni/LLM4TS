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
 LocaleUtils.toLocale("en_GB_xxx");
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> localeList = LocaleUtils.localeLookupList(locale);
 assertEquals(3, localeList.size());
 Locale expected = new Locale("fr", "CA", "xxx");
 assertEquals(expected, localeList.get(0));
 expected = new Locale("fr", "CA");
 assertEquals(expected, localeList.get(1));
 expected = new Locale("fr");
 assertEquals(expected, localeList.get(2));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 assertTrue(availableLocaleList.contains(new Locale("en", "US")));
 assertTrue(availableLocaleList.contains(new Locale("fr", "FR")));
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 locale = new Locale("es", "ES");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 locale = new Locale("es", "MX");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 locale = new Locale("es", "AR");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
 assertEquals(1, languagesByCountry.size());
 Locale expected = new Locale("en", "CA");
 assertEquals(expected, languagesByCountry.get(0));
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
 assertEquals(3, countriesByLanguage.size());
 Locale expected = new Locale("CA", "en");
 assertEquals(expected, countriesByLanguage.get(0));
 expected = new Locale("GB", "en");
 assertEquals(expected, countriesByLanguage.get(1));
 expected = new Locale("US", "en");
 assertEquals(expected, countriesByLanguage.get(2));
 }

}