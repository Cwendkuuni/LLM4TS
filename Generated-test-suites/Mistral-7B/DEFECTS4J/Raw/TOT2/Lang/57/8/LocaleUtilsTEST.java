import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for toLocale() method
 @Test
 public void testToLocale() {
 String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "fr_CA", "es_ES", "es_MX", "de_DE", "de_AT"};
 for (String testCase : testCases) {
 Locale expectedLocale = new Locale(testCase.substring(0, 2), testCase.substring(3, testCase.length()));
 Locale actualLocale = LocaleUtils.toLocale(testCase);
 assertEquals(expectedLocale.getDisplayName(), actualLocale.getDisplayName());
 }
 }

 // Test case for localeLookupList() method with valid locale
 @Test
 public void testLocaleLookupListWithValidLocale() {
 Locale locale = new Locale("fr", "CA");
 List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, null);
 assertEquals(expectedList, actualList);
 }

 // Test case for localeLookupList() method with null locale
 @Test
 public void testLocaleLookupListWithNullLocale() {
 List<Locale> actualList = LocaleUtils.localeLookupList(null, null);
 assertTrue(actualList.isEmpty());
 }

 // Test case for availableLocaleList() method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.availableLocaleList();
 List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expectedList, actualList);
 }

 // Test case for isAvailableLocale() method with available locale
 @Test
 public void testIsAvailableLocaleWithAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 // Test case for isAvailableLocale() method with invalid locale
 @Test(expected = IllegalArgumentException.class)
 public void testIsAvailableLocaleWithInvalidLocale() {
 Locale locale = new Locale(null, null);
 LocaleUtils.isAvailableLocale(locale);
 }

 // Test case for languagesByCountry() method with valid country code
 @Test
 public void testLanguagesByCountryWithValidCountryCode() {
 String countryCode = "CA";
 List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedList, actualList);
 }

 // Test case for languagesByCountry() method with null country code
 @Test
 public void testLanguagesByCountryWithNullCountryCode() {
 List<Locale> actualList = LocaleUtils.languagesByCountry(null);
 assertTrue(actualList.isEmpty());
 }

 // Test case for countriesByLanguage() method with valid language code
 @Test
 public void testCountriesByLanguageWithValidLanguageCode() {
 String languageCode = "fr";
 List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"));
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedList, actualList);
 }

 // Test case for countriesByLanguage() method with null language code
 @Test
 public void testCountriesByLanguageWithNullLanguageCode() {
 List<Locale> actualList = LocaleUtils.countriesByLanguage(null);
 assertTrue(actualList.isEmpty());
 }
}