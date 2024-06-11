import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test method for toLocale
 @Test
 public void testToLocale() {
 String input = "en";
 Locale expected = new Locale("en", "");
 Locale actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = "en_GB";
 expected = new Locale("en", "GB");
 actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = "en_GB_xxx";
 expected = new Locale("en", "GB", "xxx");
 actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);
 }

 // Test method for localeLookupList
 @Test
 public void testLocaleLookupList() {
 String localeStr = "fr_CA";
 Locale locale = new Locale(localeStr);
 Locale defaultLocale = new Locale("en");
 List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expectedList, actualList);

 localeStr = null;
 expectedList = Collections.emptyList();
 actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expectedList, actualList);
 }

 // Test method for availableLocaleList
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.availableLocaleList();
 List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expectedList, actualList);
 }

 // Test method for availableLocaleSet
 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
 Set<Locale> actualSet = new HashSet<>(Locale.getAvailableLocales());
 assertEquals(expectedSet, actualSet);
 }

 // Test method for isAvailableLocale
 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr_CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("xyz");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 // Test method for languagesByCountry
 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedList, actualList);

 countryCode = null;
 expectedList = Collections.emptyList();
 actualList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedList, actualList);
 }

 // Test method for countriesByLanguage
 @Test
 public void testCountriesByLanguage() {
 String languageCode = "fr";
 List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedList, actualList);

 languageCode = null;
 expectedList = Collections.emptyList();
 actualList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedList, actualList);
 }

 // Test method for exception handling
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleException() {
 String input = "en_gb_XY";
 LocaleUtils.toLocale(input);
 }

 // Test method for null input
 @Test
 public void testToLocaleNullInput() {
 String input = null;
 Locale actual = LocaleUtils.toLocale(input);
 assertNull(actual);
 }
}