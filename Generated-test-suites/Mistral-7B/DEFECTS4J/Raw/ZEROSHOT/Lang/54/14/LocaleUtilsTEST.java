import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expectedLocale = new Locale("en", "GB");
 Locale actualLocale = LocaleUtils.toLocale("en_GB");
 assertEquals(expectedLocale, actualLocale);
 }

 @Test
 public void testToLocaleInvalidFormat() {
 try {
 LocaleUtils.toLocale("frCAxxx");
 assertTrue(false);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
 List<Locale> expectedLocaleLookupList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
 assertEquals(expectedLocaleLookupList, localeLookupList);
 }

 @Test
 public void testLocaleLookupListEmpty() {
 Locale locale = null;
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
 List<Locale> expectedLocaleLookupList = Collections.emptyList();
 assertEquals(expectedLocaleLookupList, localeLookupList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expectedAvailableLocaleList, availableLocaleList);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleFalse() {
 Locale locale = new Locale("en", "XX");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> expectedLanguagesByCountry = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("ca", "CA"));
 List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
 assertEquals(expectedLanguagesByCountry, languagesByCountry);
 }

 @Test
 public void testLanguagesByCountryEmpty() {
 List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(null);
 List<Locale> expectedLanguagesByCountry = Collections.emptyList();
 assertEquals(expectedLanguagesByCountry, languagesByCountry);
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> expectedCountriesByLanguage = Arrays.asList(new Locale("ca", "ES"), new Locale("fr", "BE"), new Locale("fr", "CH"));
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("fr");
 assertEquals(expectedCountriesByLanguage, countriesByLanguage);
 }

 @Test
 public void testCountriesByLanguageEmpty() {
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(null);
 List<Locale> expectedCountriesByLanguage = Collections.emptyList();
 assertEquals(expectedCountriesByLanguage, countriesByLanguage);
 }

}