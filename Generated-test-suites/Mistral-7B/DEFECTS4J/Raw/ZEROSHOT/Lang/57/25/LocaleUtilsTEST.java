import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expectedLocale1 = new Locale("en");
 Locale expectedLocale2 = new Locale("en", "GB");
 Locale expectedLocale3 = new Locale("en", "GB", "xxx");

 Locale actualLocale1 = LocaleUtils.toLocale("en");
 Locale actualLocale2 = LocaleUtils.toLocale("en_GB");
 Locale actualLocale3 = LocaleUtils.toLocale("en_GB_xxx");

 assertEquals(expectedLocale1, actualLocale1);
 assertEquals(expectedLocale2, actualLocale2);
 assertEquals(expectedLocale3, actualLocale3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 LocaleUtils.toLocale("en_gb"); // lowercase 'g'
 LocaleUtils.toLocale("en_GB_xxx_yyy"); // more than 3 separators
 LocaleUtils.toLocale("es_ES_XXX_YYY_ZZZ"); // more than 3 separators and invalid characters
 }

 @Test
 public void testLocaleLookupList() {
 Locale baseLocale = new Locale("fr", "CA", "xxx");
 Locale defaultLocale = new Locale("en");

 List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA", "xxx"),
 new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);

 assertEquals(expectedLocaleList, actualLocaleList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 assertEquals(152, availableLocaleList.size());
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale1 = new Locale("en_US");
 Locale locale2 = new Locale("es_ES");
 Locale locale3 = new Locale("fr_FR");
 Locale locale4 = new Locale("es_MX");
 Locale locale5 = new Locale("fr_CH");

 assertTrue(LocaleUtils.isAvailableLocale(locale1));
 assertTrue(LocaleUtils.isAvailableLocale(locale2));
 assertTrue(LocaleUtils.isAvailableLocale(locale3));
 assertTrue(LocaleUtils.isAvailableLocale(locale4));
 assertTrue(LocaleUtils.isAvailableLocale(locale5));

 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testLanguagesByCountry() {
 Locale localeFrance = new Locale("fr", "FR");
 List<Locale> expectedLocaleList = Arrays.asList(localeFrance, new Locale("fr", "BE"), new Locale("fr", "CH"));
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry("FR");

 assertEquals(expectedLocaleList, actualLocaleList);
 }

 @Test
 public void testCountriesByLanguage() {
 Locale localeFrench = new Locale("fr", "FR");
 List<Locale> expectedLocaleList = Arrays.asList(localeFrench, new Locale("fr", "BE"), new Locale("fr", "CH"));
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage("fr");

 assertEquals(expectedLocaleList, actualLocaleList);
 }

}