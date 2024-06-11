import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test toLocale method
 @Test
 public void testToLocale() {
 String localeString = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 String invalidLocaleString = "en_us";
 LocaleUtils.toLocale(invalidLocaleString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleEmptyString() {
 LocaleUtils.toLocale("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidCharacter() {
 String invalidLocaleString = "en_US%";
 LocaleUtils.toLocale(invalidLocaleString);
 }

 // Test localeLookupList method
 @Test
 public void testLocaleLookupList() {
 String baseLocale = "fr_CA";
 String fallbackLocale = "en";
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale(baseLocale),
 new Locale(baseLocale, ""),
 new Locale(baseLocale.substring(0, 2), ""),
 new Locale(fallbackLocale)
 );
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(Locale.parse(baseLocale), Locale.parse(fallbackLocale));
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 @Test
 public void testLocaleLookupListEmptyInput() {
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null);
 assertTrue(actualLocaleList.isEmpty());
 }

 @Test
 public void testLocaleLookupListEmptyFallback() {
 String baseLocale = "fr_CA";
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(Locale.parse(baseLocale), null);
 assertTrue(actualLocaleList.isEmpty());
 }

 // Test availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> actualLocaleList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH);
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 // Test isAvailableLocale method
 @Test
 public void testIsAvailableLocaleTrue() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleFalse() {
 Locale locale = new Locale("en", "US_ variant");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 // Test languagesByCountry method
 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale("en", "US"),
 new Locale("en", "CA"),
 new Locale("en", "GB")
 );
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 @Test
 public void testLanguagesByCountryEmptyInput() {
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(null);
 assertTrue(actualLocaleList.isEmpty());
 }

 // Test countriesByLanguage method
 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale("US"),
 new Locale("CA"),
 new Locale("GB")
 );
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 @Test
 public void testCountriesByLanguageEmptyInput() {
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(null);
 assertTrue(actualLocaleList.isEmpty());
 }

}