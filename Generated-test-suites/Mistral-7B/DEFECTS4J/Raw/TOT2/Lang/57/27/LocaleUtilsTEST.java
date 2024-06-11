import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test for toLocale method
 @Test
 public void testToLocale() {
 String localeString = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);

 localeString = "fr_CA";
 expectedLocale = new Locale("fr", "CA");
 actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);

 localeString = "es_ES";
 expectedLocale = new Locale("es", "ES");
 actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);

 localeString = null;
 assertNull(LocaleUtils.toLocale(localeString));
 }

 // Test for localeLookupList method
 @Test
 public void testLocaleLookupList() {
 String baseLocaleString = "fr_CA";
 Locale baseLocale = LocaleUtils.toLocale(baseLocaleString);

 String defaultLocaleString = "en";
 Locale defaultLocale = LocaleUtils.toLocale(defaultLocaleString);

 List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale("fr", "CA"),
 new Locale("fr", ""),
 new Locale("fr"),
 new Locale("en")
 );
 assertEquals(expectedLocaleList, localeList);
 }

 // Test for isAvailableLocale method
 @Test
 public void testIsAvailableLocale() {
 Locale locale1 = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale1));

 Locale locale2 = new Locale("es", "ES");
 assertTrue(LocaleUtils.isAvailableLocale(locale2));

 Locale locale3 = new Locale("fr", "CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale3));

 Locale locale4 = new Locale("zh_CN");
 assertTrue(LocaleUtils.isAvailableLocale(locale4));

 Locale locale5 = new Locale("xx", "YY");
 assertFalse(LocaleUtils.isAvailableLocale(locale5));
 }

 // Test for languagesByCountry method
 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale("en", "US"),
 new Locale("en", "GB"),
 new Locale("en", "CA"),
 new Locale("en", "IE"),
 new Locale("en", "AU"),
 new Locale("en", "NZ")
 );
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLocaleList, actualLocaleList);

 countryCode = "CA";
 expectedLocaleList = Arrays.asList(
 new Locale("fr", "CA"),
 new Locale("en", "CA")
 );
 actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLocaleList, actualLocaleList);

 countryCode = null;
 assertEquals(Collections.EMPTY_LIST, LocaleUtils.languagesByCountry(countryCode));
 }

 // Test for countriesByLanguage method
 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale("US"),
 new Locale("GB"),
 new Locale("CA"),
 new Locale("IE"),
 new Locale("AU"),
 new Locale("NZ")
 );
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedLocaleList, actualLocaleList);

 languageCode = "fr";
 expectedLocaleList = Arrays.asList(
 new Locale("CA")
 );
 actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedLocaleList, actualLocaleList);

 languageCode = null;
 assertEquals(Collections.EMPTY_LIST, LocaleUtils.countriesByLanguage(languageCode));
 }
}