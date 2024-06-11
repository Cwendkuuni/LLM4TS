import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for toLocale method
 @Test
 public void testToLocale() {
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale("en_US");
 assertEquals("Expected locale should match the actual locale", expectedLocale, actualLocale);
 }

 // Test case for localeLookupList method with valid input
 @Test
 public void testLocaleLookupListWithValidInput() {
 Locale locale = new Locale("fr", "CA", "XXX");
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale("fr", "CA", "XXX"),
 new Locale("fr", "CA"),
 new Locale("fr"),
 new Locale("en")
 );
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(locale);
 assertEquals("Expected locale list should match the actual locale list", expectedLocaleList, actualLocaleList);
 }

 // Test case for localeLookupList method with null input
 @Test
 public void testLocaleLookupListWithNullInput() {
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null);
 assertTrue("Expected empty list when input is null", actualLocaleList.isEmpty());
 }

 // Test case for availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals("Expected available locale list should match the actual available locale list", expectedLocaleList, actualLocaleList);
 }

 // Test case for isAvailableLocale method
 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("es", "ES");
 assertTrue("Expected true when locale is available", LocaleUtils.isAvailableLocale(locale));
 Locale invalidLocale = new Locale("es", "ESX");
 assertFalse("Expected false when locale is not available", LocaleUtils.isAvailableLocale(invalidLocale));
 }

 // Test case for languagesByCountry method with valid input
 @Test
 public void testLanguagesByCountryWithValidInput() {
 String countryCode = "US";
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale("en", "US"),
 new Locale("en", "CA"),
 new Locale("en", "GB")
 );
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals("Expected locale list should match the actual locale list", expectedLocaleList, actualLocaleList);
 }

 // Test case for languagesByCountry method with null input
 @Test
 public void testLanguagesByCountryWithNullInput() {
 String countryCode = null;
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
 assertTrue("Expected empty list when country code is null", actualLocaleList.isEmpty());
 }

 // Test case for countriesByLanguage method with valid input
 @Test
 public void testCountriesByLanguageWithValidInput() {
 String languageCode = "en";
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale("US"),
 new Locale("CA"),
 new Locale("GB"),
 new Locale("AU")
 );
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals("Expected locale list should match the actual locale list", expectedLocaleList, actualLocaleList);
 }

 // Test case for countriesByLanguage method with null input
 @Test
 public void testCountriesByLanguageWithNullInput() {
 String languageCode = null;
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 assertTrue("Expected empty list when language code is null", actualLocaleList.isEmpty());
 }

 // Test case for getAvailableLocales method with valid input
 @Test
 public void testGetAvailableLocalesWithValidInput() {
 List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());
 List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
 assertEquals("Expected available locale list should match the actual available locale list", expectedLocaleList, actualLocaleList);
 }

 // Test case for getAvailableLocales method with empty input
 @Test (expected = IllegalArgumentException.class)
 public void testGetAvailableLocalesWithEmptyInput() {
 LocaleUtils.availableLocaleList();
 }

}