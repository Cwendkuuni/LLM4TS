import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for toLocale method
 @Test
 public void testToLocale() {
 String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_CA", "fr_CA_FR"};
 for (String testCase : testCases) {
 Locale expectedLocale = new Locale(testCase.substring(0, 2), testCase.substring(3, testCase.length()));
 Locale actualLocale = LocaleUtils.toLocale(testCase);
 assertEquals(expectedLocale, actualLocale);
 }
 }

 // Test case for localeLookupList method with valid locale
 @Test
 public void testLocaleLookupListWithValidLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
 assertEquals(locale, localeLookupList.get(0));
 }

 // Test case for localeLookupList method with null locale
 @Test
 public void testLocaleLookupListWithNullLocale() {
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(null);
 assertTrue(localeLookupList.isEmpty());
 }

 // Test case for localeLookupList method with defaultLocale
 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale, new Locale("fr", "CA"));
 assertEquals(locale, localeLookupList.get(0));
 assertEquals(new Locale("fr", "CA"), localeLookupList.get(1));
 }

 // Test case for availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 assertEquals(LocaleUtils.availableLocaleList().size(), 214);
 }

 // Test case for isAvailableLocale method with available locale
 @Test
 public void testIsAvailableLocaleWithAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 // Test case for isAvailableLocale method with invalid locale
 @Test(expected = IllegalArgumentException.class)
 public void testIsAvailableLocaleWithInvalidLocale() {
 Locale locale = new Locale("en", "US", "vn");
 LocaleUtils.isAvailableLocale(locale);
 }

 // Test case for languagesByCountry method with valid country code
 @Test
 public void testLanguagesByCountryWithValidCountryCode() {
 List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
 assertEquals(languagesByCountry.get(0).getLanguage(), "en");
 }

 // Test case for languagesByCountry method with null country code
 @Test
 public void testLanguagesByCountryWithNullCountryCode() {
 List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(null);
 assertTrue(languagesByCountry.isEmpty());
 }

}