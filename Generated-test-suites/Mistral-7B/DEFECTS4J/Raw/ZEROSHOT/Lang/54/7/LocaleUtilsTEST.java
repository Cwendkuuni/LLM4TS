import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expectedLocale = new Locale("en", "GB");
 Locale actualLocale = LocaleUtils.toLocale("en_GB");
 assertEquals(expectedLocale, actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 LocaleUtils.toLocale("en_GB_xxx"); // invalid variant
 }

 @Test
 public void testLocaleLookupList() {
 Locale baseLocale = new Locale("fr", "CA", "xxx");
 List<Locale> expectedLocaleList = Arrays.asList(
 new Locale("fr", "CA", "xxx"),
 new Locale("fr", "CA"),
 new Locale("fr"),
 new Locale("en")
 );
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLocaleLookupListNullLocale() {
 LocaleUtils.localeLookupList(null, new Locale("en"));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "GB");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("en"), new Locale("fr"));
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry("CA");
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("CA"), new Locale("GB"));
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage("en");
 assertEquals(expectedLocaleList, actualLocaleList);
 }

}