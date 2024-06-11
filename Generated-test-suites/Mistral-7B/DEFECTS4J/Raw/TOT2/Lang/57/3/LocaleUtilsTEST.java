import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for method toLocale
 @Test
 public void testToLocale() {
 String testString1 = "en";
 Locale expectedLocale1 = new Locale("en", "");
 Locale actualLocale1 = LocaleUtils.toLocale(testString1);
 assertEquals(expectedLocale1, actualLocale1);

 String testString2 = "en_GB";
 Locale expectedLocale2 = new Locale("en", "GB");
 Locale actualLocale2 = LocaleUtils.toLocale(testString2);
 assertEquals(expectedLocale2, actualLocale2);

 String testString3 = "en_GB_xxx";
 Locale expectedLocale3 = new Locale("en", "GB", "xxx");
 Locale actualLocale3 = LocaleUtils.toLocale(testString3);
 assertEquals(expectedLocale3, actualLocale3);

 String testString4 = "fr";
 assertNull(LocaleUtils.toLocale(testString4));

 String testString5 = "FR";
 Locale expectedLocale5 = new Locale("fr", "");
 Locale actualLocale5 = LocaleUtils.toLocale(testString5);
 assertEquals(expectedLocale5, actualLocale5);

 String testString6 = "fr_CA";
 Locale expectedLocale6 = new Locale("fr", "CA");
 Locale actualLocale6 = LocaleUtils.toLocale(testString6);
 assertEquals(expectedLocale6, actualLocale6);

 String testString7 = "fr_CA_xxx";
 Locale expectedLocale7 = new Locale("fr", "CA", "xxx");
 Locale actualLocale7 = LocaleUtils.toLocale(testString7);
 assertEquals(expectedLocale7, actualLocale7);

 String testString8 = "fr_CA_XXX";
 Locale expectedLocale8 = new Locale("fr", "CA", "XXX");
 Locale actualLocale8 = LocaleUtils.toLocale(testString8);
 assertEquals(expectedLocale8, actualLocale8);
 }

 // Test case for method localeLookupList
 @Test
 public void testLocaleLookupList() {
 String testLocaleString1 = "en";
 Locale testLocale1 = new Locale(testLocaleString1, "");

 String testLocaleString2 = "en_CA";
 Locale testLocale2 = new Locale(testLocaleString2, "CA");

 String testLocaleString3 = "fr";
 Locale testLocale3 = new Locale(testLocaleString3, "");

 String defaultLocaleString = "es";
 Locale defaultLocale = new Locale(defaultLocaleString, "");

 List<Locale> expectedList1 = Arrays.asList(testLocale1, testLocale2, testLocale3, defaultLocale);
 List<Locale> actualList1 = LocaleUtils.localeLookupList(testLocale1);
 assertEquals(expectedList1, actualList1);

 List<Locale> expectedList2 = Arrays.asList(testLocale1, testLocale2, testLocale3, defaultLocale);
 List<Locale> actualList2 = LocaleUtils.localeLookupList(testLocale1, defaultLocale);
 assertEquals(expectedList2, actualList2);
 }

 // Test case for method isAvailableLocale
 @Test
 public void testIsAvailableLocale() {
 String testLocaleString1 = "en";
 Locale testLocale1 = new Locale(testLocaleString1, "");
 assertTrue(LocaleUtils.isAvailableLocale(testLocale1));

 String testLocaleString2 = "fr_CA_XXX";
 Locale testLocale2 = new Locale(testLocaleString2, "CA", "XXX");
 assertFalse(LocaleUtils.isAvailableLocale(testLocale2));
 }

 // Test case for methods languagesByCountry and countriesByLanguage
 @Test
 public void testLanguagesAndCountries() {
 String testCountry = "CA";
 List<Locale> expectedLanguagesList = Arrays.asList(new Locale("en", testCountry), new Locale("fr", testCountry));
 List<Locale> actualLanguagesList = LocaleUtils.languagesByCountry(testCountry);
 assertEquals(expectedLanguagesList, actualLanguagesList);

 String testLanguage = "en";
 List<Locale> expectedCountriesList = Arrays.asList(new Locale("en", "CA"), new Locale("en", "US"), new Locale("en", "GB"));
 List<Locale> actualCountriesList = LocaleUtils.countriesByLanguage(testLanguage);
 assertEquals(expectedCountriesList, actualCountriesList);
 }
}