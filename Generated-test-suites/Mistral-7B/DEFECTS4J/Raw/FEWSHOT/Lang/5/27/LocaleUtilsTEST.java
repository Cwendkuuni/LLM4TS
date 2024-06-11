package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 String[] invalidLocales = new String[]{"en", "en_GB_xxx", "en_GB_en", "en_GB.xyz", "en_GB", "_en_GB", "_en_GB_"};
 String[] validLocales = new String[]{"en", "en_GB", "en_GB_en"};

 for (String localeString : validLocales) {
 Locale expectedLocale = new Locale(localeString.split("_")[0], localeString.split("_")[1]);
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
 assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
 }

 for (String invalidLocaleString : invalidLocales) {
 assertNull(LocaleUtils.toLocale(invalidLocaleString));
 }
 }

 @Test
 public void testToLocaleNullInput() {
 assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
 List<Locale> expectedLocaleLookupList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
 assertEquals(expectedLocaleLookupList, localeLookupList);
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 Locale defaultLocale = new Locale("en");
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
 List<Locale> expectedLocaleLookupList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
 assertEquals(expectedLocaleLookupList, localeLookupList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.getAvailableLocales());
 assertTrue(availableLocaleList.size() > 0);
 assertEquals(expectedAvailableLocaleList, availableLocaleList);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "fr";
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expectedCountriesByLanguage = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
 assertEquals(expectedCountriesByLanguage, countriesByLanguage);
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expectedLanguagesByCountry = Collections.singletonList(new Locale("en", "CA"));
 assertEquals(expectedLanguagesByCountry, languagesByCountry);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }
}