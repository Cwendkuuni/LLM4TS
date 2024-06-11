import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test for toLocale method
 @Test
 public void testToLocale() {
 String localeString = "en_GB";
 Locale expectedLocale = new Locale("en", "GB");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);

 localeString = "fr_CA_xxx";
 expectedLocale = new Locale("fr", "CA", "xxx");
 actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);

 localeString = null;
 actualLocale = LocaleUtils.toLocale(localeString);
 assertNull(actualLocale);
 }

 // Test for localeLookupList method with valid locale
 @Test
 public void testLocaleLookupListWithValidLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> expectedLocaleList = LocaleUtils.localeLookupList(locale);
 List<Locale> actualLocaleList = new ArrayList<>();
 actualLocaleList.add(locale);
 actualLocaleList.add(new Locale("fr", "CA"));
 actualLocaleList.add(new Locale("fr", ""));
 actualLocaleList.add(new Locale("en"));
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 // Test for localeLookupList method with null locale
 @Test
 public void testLocaleLookupListWithNullLocale() {
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null);
 assertTrue(actualLocaleList.isEmpty());
 }

 // Test for availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 // Test for isAvailableLocale method
 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("fr", "CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("fr");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("en");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("es_ES_xxx");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 // Test for languagesByCountry method
 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> expectedLocaleList = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> filteredLocaleList = new ArrayList<>();
 for (Locale locale : actualLocaleList) {
 if (locale.getCountry().equals(countryCode) && locale.getVariant().length() == 0) {
 filteredLocaleList.add(locale);
 }
 }
 assertEquals(expectedLocaleList, filteredLocaleList);
 }

 // Test for countriesByLanguage method
 @Test
 public void testCountriesByLanguage() {
 String languageCode = "fr";
 List<Locale> expectedLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> filteredLocaleList = new ArrayList<>();
 for (Locale locale : actualLocaleList) {
 if (locale.getLanguage().equals(languageCode) && locale.getCountry().length() != 0 && locale.getVariant().length() == 0) {
 filteredLocaleList.add(locale);
 }
 }
 assertEquals(expectedLocaleList, filteredLocaleList);
 }
}