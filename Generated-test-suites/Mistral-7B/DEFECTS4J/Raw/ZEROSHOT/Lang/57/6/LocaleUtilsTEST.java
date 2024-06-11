import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expectedLocale1 = new Locale("en");
 Locale expectedLocale2 = new Locale("en_GB");
 Locale expectedLocale3 = new Locale("en_GB_xxx");
 Locale locale1 = LocaleUtils.toLocale("en");
 Locale locale2 = LocaleUtils.toLocale("en_GB");
 Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
 assertEquals(expectedLocale1, locale1);
 assertEquals(expectedLocale2, locale2);
 assertEquals(expectedLocale3, locale3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 String invalidLocaleString = "InvalidFormat";
 Locale locale = LocaleUtils.toLocale(invalidLocaleString);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testLocaleLookupList() {
 Locale baseLocale = new Locale("fr", "CA", "xxx");
 List localeList1 = LocaleUtils.localeLookupList(baseLocale);
 List localeList2 = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
 assertEquals(3, localeList1.size());
 assertEquals(5, localeList2.size());
 // Check order of elements
 assertEquals(baseLocale, localeList1.get(0));
 // Check normalized list
 List normalizedLocaleList1 = new ArrayList(localeList1);
 for (int i = 1; i < normalizedLocaleList1.size(); i++) {
 Locale locale = (Locale) normalizedLocaleList1.get(i);
 assertTrue(locale.getLanguage().equals(baseLocale.getLanguage()) || locale.getCountry().length() > 0);
 }
 assertEquals(baseLocale, normalizedLocaleList1.get(0));
 assertEquals(new Locale(baseLocale.getLanguage(), baseLocale.getCountry()), normalizedLocaleList1.get(1));
 assertEquals(new Locale(baseLocale.getLanguage(), ""), normalizedLocaleList1.get(2));
 assertEquals(new Locale("en"), normalizedLocaleList1.get(3));
 assertEquals(baseLocale, localeList2.get(0));
 assertEquals(new Locale(baseLocale.getLanguage(), baseLocale.getCountry()), localeList2.get(1));
 assertEquals(new Locale(baseLocale.getLanguage(), ""), localeList2.get(2));
 assertEquals(new Locale("en"), localeList2.get(3));
 assertEquals(new Locale("en"), localeList2.get(4));
 }

 @Test
 public void testAvailableLocaleList() {
 List availableLocaleList = LocaleUtils.availableLocaleList();
 // Check size
 assertEquals(174, availableLocaleList.size());
 // Check first element
 assertEquals(new Locale("af", "ZA"), availableLocaleList.get(0));
 // Check last element
 assertEquals(new Locale("yi", "IL"), availableLocaleList.get(173));
 }

 @Test
 public void testAvailableLocaleSet() {
 Set availableLocaleSet = LocaleUtils.availableLocaleSet();
 // Check size
 assertEquals(174, availableLocaleSet.size());
 // Check first element
 assertTrue(availableLocaleSet.contains(new Locale("af", "ZA")));
 // Check last element
 assertTrue(availableLocaleSet.contains(new Locale("yi", "IL")));
 }

 @Test
 public void testIsAvailableLocale() {
 Locale knownLocale = new Locale("fr", "FR");
 Locale unknownLocale1 = new Locale("InvalidLocale", "FR");
 Locale unknownLocale2 = new Locale("fr", "InvalidCountry");
 assertTrue(LocaleUtils.isAvailableLocale(knownLocale));
 assertFalse(LocaleUtils.isAvailableLocale(unknownLocale1));
 assertFalse(LocaleUtils.isAvailableLocale(unknownLocale2));
 }

 @Test
 public void testLanguagesByCountry() {
 List languagesByCountryFr = LocaleUtils.languagesByCountry("FR");
 List languagesByCountryUS = LocaleUtils.languagesByCountry("US");
 // Check size
 assertEquals(11, languagesByCountryFr.size());
 assertEquals(34, languagesByCountryUS.size());
 // Check first element
 assertEquals(new Locale("fr", "FR"), languagesByCountryFr.get(0));
 // Check last element
 assertEquals(new Locale("ha", "NG"), languagesByCountryFr.get(10));
 // Check first element
 assertEquals(new Locale("en", "US"), languagesByCountryUS.get(0));
 // Check last element
 assertEquals(new Locale("as", "AS"), languagesByCountryUS.get(33));
 }

 @Test
 public void testCountriesByLanguage() {
 List countriesByLanguageEn = LocaleUtils.countriesByLanguage("en");
 List countriesByLanguageFr = LocaleUtils.countriesByLanguage("fr");
 // Check size
 assertEquals(26, countriesByLanguageEn.size());
 assertEquals(50, countriesByLanguageFr.size());
 // Check first element
 assertEquals(new Locale("GB", "GB"), countriesByLanguageEn.get(0));
 // Check last element
 assertEquals(new Locale("JE", "JE"), countriesByLanguageEn.get(25));
 // Check first element
 assertEquals(new Locale("FR", "FR"), countriesByLanguageFr.get(0));
 // Check last element
 assertEquals(new Locale("MC", "MC"), countriesByLanguageFr.get(49));
 }

}