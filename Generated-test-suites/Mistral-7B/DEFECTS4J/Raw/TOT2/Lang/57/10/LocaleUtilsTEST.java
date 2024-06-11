import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for toLocale() method
 @Test
 public void testToLocale() {
 String localeString = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);
 }

 // Test case for toLocale() method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidInput() {
 String invalidLocaleString = "xx_yy";
 LocaleUtils.toLocale(invalidLocaleString);
 }

 // Test case for localeLookupList() method with valid input
 @Test
 public void testLocaleLookupList() {
 Locale baseLocale = new Locale("fr", "CA", "XX");
 List expectedLocaleList = LocaleUtils.localeLookupList(baseLocale);
 List actualLocaleList = new ArrayList();
 actualLocaleList.add(baseLocale);
 actualLocaleList.add(new Locale("fr", "CA"));
 actualLocaleList.add(new Locale("fr", ""));
 actualLocaleList.add(new Locale("en"));
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 // Test case for localeLookupList() method with null input
 @Test
 public void testLocaleLookupListNullInput() {
 List localeList = LocaleUtils.localeLookupList(null);
 assertTrue(localeList.isEmpty());
 }

 // Test case for availableLocaleList() method
 @Test
 public void testAvailableLocaleList() {
 List availableLocales = LocaleUtils.availableLocaleList();
 List expectedAvailableLocales = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expectedAvailableLocales, availableLocales);
 }

 // Test case for isAvailableLocale() method
 @Test
 public void testIsAvailableLocale() {
 Locale availableLocale = new Locale("en", "US");
 Locale notAvailableLocale = new Locale("xx", "yy");
 assertTrue(LocaleUtils.isAvailableLocale(availableLocale));
 assertFalse(LocaleUtils.isAvailableLocale(notAvailableLocale));
 }

 // Test case for languagesByCountry() method with valid input
 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List expectedLanguages = Arrays.asList(new Locale("en", "US"), new Locale("en", ""));
 List actualLanguages = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLanguages, actualLanguages);
 }

 // Test case for languagesByCountry() method with null input
 @Test
 public void testLanguagesByCountryNullInput() {
 String nullCountryCode = null;
 List languages = LocaleUtils.languagesByCountry(nullCountryCode);
 assertTrue(languages.isEmpty());
 }

}