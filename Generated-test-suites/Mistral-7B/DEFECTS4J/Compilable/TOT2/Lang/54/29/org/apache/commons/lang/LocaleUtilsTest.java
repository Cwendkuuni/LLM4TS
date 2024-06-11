package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

 // Test case for toLocale method
 @Test
 public void testToLocale() {
 String[] testCases = { "en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA" };
 for (String testCase : testCases) {
 Locale expectedLocale = new Locale(testCase.split("_")[0], testCase.split("_")[1]);
 Locale actualLocale = LocaleUtils.toLocale(testCase);
 assertEquals(expectedLocale, actualLocale);
 }
 }

 // Test case for localeLookupList method with valid locales
 @Test
 public void testLocaleLookupListValidLocales() {
 Locale locale = new Locale("fr", "CA", "XX");
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA", "XX"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(locale, new Locale("en"));
 assertEquals(expectedLocaleList.size(), actualLocaleList.size());
 for (int i = 0; i < expectedLocaleList.size(); i++) {
 assertEquals(expectedLocaleList.get(i), actualLocaleList.get(i));
 }
 }

 // Test case for localeLookupList method with null locale
 @Test
 public void testLocaleLookupListNullLocale() {
 List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null, new Locale("en"));
 assertTrue(actualLocaleList.isEmpty());
 }

 // Test case for availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expectedLocaleList.size(), actualLocaleList.size());
 for (int i = 0; i < expectedLocaleList.size(); i++) {
 assertEquals(expectedLocaleList.get(i), actualLocaleList.get(i));
 }
 }

 // Test case for isAvailableLocale method
 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr", "CA");
 Locale localeNotAvailable = new Locale("InvalidLocale");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 assertFalse(LocaleUtils.isAvailableLocale(localeNotAvailable));
 }

 // Test case for languagesByCountry method
 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLocaleList.size(), actualLocaleList.size());
 for (int i = 0; i < expectedLocaleList.size(); i++) {
 assertEquals(expectedLocaleList.get(i), actualLocaleList.get(i));
 }
 }

 // Test case for countriesByLanguage method
 @Test
 public void testCountriesByLanguage() {
 String languageCode = "fr";
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"));
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedLocaleList.size(), actualLocaleList.size());
 for (int i = 0; i < expectedLocaleList.size(); i++) {
 assertEquals(expectedLocaleList.get(i), actualLocaleList.get(i));
 }
 }
}