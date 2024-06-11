import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test method for toLocale(String)
 @Test
 public void testToLocale() {
 String validLocale = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(validLocale);
 assertEquals(expectedLocale, actualLocale);

 String invalidLocale = null;
 Locale result = LocaleUtils.toLocale(invalidLocale);
 assertNull(result);

 invalidLocale = "fr_CA_invalid";
 try {
 Locale locale = LocaleUtils.toLocale(invalidLocale);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 // Test method for localeLookupList(Locale)
 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> expectedList = Arrays.asList(
 new Locale("fr", "CA", "xxx"),
 new Locale("fr", "CA"),
 new Locale("fr"),
 new Locale("en")
 );
 List<Locale> resultList = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedList, resultList);
 }

 // Test method for localeLookupList(Locale, Locale)
 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 Locale defaultLocale = new Locale("en");
 List<Locale> expectedList = Arrays.asList(
 new Locale("fr", "CA", "xxx"),
 new Locale("fr", "CA"),
 new Locale("fr"),
 new Locale("en")
 );
 List<Locale> resultList = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expectedList, resultList);
 }

 // Test method for availableLocaleList()
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.availableLocaleList();
 // Perform some checks on the list to ensure it's not empty
 assertTrue(expectedList.size() > 0);
 assertFalse(expectedList.isEmpty());
 }

 // Test method for availableLocaleSet()
 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
 // Perform some checks on the set to ensure it's not empty
 assertTrue(expectedSet.size() > 0);
 assertFalse(expectedSet.isEmpty());
 }

 // Test method for isAvailableLocale(Locale)
 @Test
 public void testIsAvailableLocale() {
 Locale availableLocale = new Locale("en", "US");
 Locale invalidLocale = new Locale("fr", "XX");
 assertTrue(LocaleUtils.isAvailableLocale(availableLocale));
 assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
 }

 // Test method for languagesByCountry(String)
 @Test
 public void testLanguagesByCountry() {
 String validCountryCode = "US";
 List<Locale> expectedList = Arrays.asList(
 new Locale("en", "US"),
 new Locale("en", "CA"),
 new Locale("en", "GB")
 );
 List<Locale> resultList = LocaleUtils.languagesByCountry(validCountryCode);
 assertEquals(expectedList, resultList);

 String invalidCountryCode = null;
 List<Locale> emptyList = Collections.emptyList();
 List<Locale> resultEmptyList = LocaleUtils.languagesByCountry(invalidCountryCode);
 assertEquals(emptyList, resultEmptyList);
 }

 // Test method for countriesByLanguage(String)
 @Test
 public void testCountriesByLanguage() {
 String validLanguageCode = "en";
 List<Locale> expectedList = Arrays.asList(
 new Locale("en", "US"),
 new Locale("en", "CA"),
 new Locale("en", "GB")
 );
 List<Locale> resultList = LocaleUtils.countriesByLanguage(validLanguageCode);
 assertEquals(expectedList, resultList);

 String invalidLanguageCode = null;
 List<Locale> emptyList = Collections.emptyList();
 List<Locale> resultEmptyList = LocaleUtils.countriesByLanguage(invalidLanguageCode);
 assertEquals(emptyList, resultEmptyList);
 }

}