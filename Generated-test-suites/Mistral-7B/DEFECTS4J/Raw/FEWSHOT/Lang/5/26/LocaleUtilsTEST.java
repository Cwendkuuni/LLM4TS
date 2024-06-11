package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 final String[] testCases = new String[]{"en", "en_US", "en_GB", "fr", "fr_CA", "fr_CA_xxx"};
 Locale[] expected = new Locale[]{new Locale("en"), new Locale("en", "US"), new Locale("en", "GB"), new Locale("fr"), new Locale("fr", "CA"), new Locale("fr", "CA", "xxx")};
 for (int i = 0; i < testCases.length; i++) {
 Locale locale = LocaleUtils.toLocale(testCases[i]);
 assertEquals(expected[i].getLanguage(), locale.getLanguage());
 assertEquals(expected[i].getCountry(), locale.getCountry());
 assertEquals(expected[i].getVariant(), locale.getVariant());
 }
 }

 @Test
 public void testToLocaleInvalidFormat() {
 String[] invalidTestCases = new String[]{"en_CA_GB", "en_GB_en", "en_GB_en_US", "en_GB.US"};
 for (String invalidTestCase : invalidTestCases) {
 try {
 LocaleUtils.toLocale(invalidTestCase);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("en", "CA", "en_CA");
 List<Locale> expected = Arrays.asList(locale, new Locale("en", "CA"), new Locale("en"));
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
 assertEquals(expected, localeLookupList);
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale locale = new Locale("en", "CA", "en_CA");
 Locale defaultLocale = new Locale("fr", "CA");
 List<Locale> expected = Arrays.asList(locale, new Locale("en", "CA"), new Locale("en"), defaultLocale);
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expected, localeLookupList);
 }

 @Test
 public void testLocaleLookupListWithNullLocale() {
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(null);
 assertTrue(localeLookupList.isEmpty());
 }

 @Test
 public void testLocaleLookupListWithNullDefaultLocale() {
 Locale locale = new Locale("en", "CA", "en_CA");
 List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale, null);
 List<Locale> expected = Arrays.asList(locale, new Locale("en", "CA"), new Locale("en"));
 assertEquals(expected, localeLookupList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expected, availableLocaleList);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = Collections.newHashSet(Locale.getAvailableLocales());
 assertEquals(expected, availableLocaleSet);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale1 = new Locale("en", "US");
 Locale locale2 = new Locale("es", "ES");
 List<Locale> availableLocales = LocaleUtils.availableLocaleList();
 assertTrue(LocaleUtils.isAvailableLocale(locale1));
 assertTrue(LocaleUtils.isAvailableLocale(locale2));
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("xy", "xy")));
 }

 @Test
 public void testLanguagesByCountry() {
 final String countryCode = "CA";
 List<Locale> expected = Arrays.asList(new Locale("en", countryCode), new Locale("fr", countryCode));
 List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected, languagesByCountry);
 }

 @Test
 public void testCountriesByLanguage() {
 final String languageCode = "en";
 List<Locale> expected = Arrays.asList(new Locale(languageCode, "US"), new Locale(languageCode, "CA"), new Locale(languageCode, "GB"));
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, countriesByLanguage);
 }

 @Test
 public void testCountriesByLanguageWithMultipleLocales() {
 final String languageCode = "fr";
 List<Locale> expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "CH"), new Locale(languageCode, "BE"));
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, countriesByLanguage);
 }

 @Test
 public void testCountriesByLanguageEmpty() {
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(null);
 assertTrue(countriesByLanguage.isEmpty());
 }

 @Test
 public void testCountriesByLanguageWithLanguageNotFound() {
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("xx");
 assertTrue(countriesByLanguage.isEmpty());
 }

 @Test
 public void testSameLocaleInstances() {
 Locale locale1 = new Locale("en", "US");
 Locale locale2 = new Locale("en", "US");
 assertEquals(locale1, locale2);
 }

 @Test
 public void testDifferentLocaleInstances() {
 Locale locale1 = new Locale("en", "US");
 Locale locale2 = new Locale("fr", "CA");
 assertNotSame(locale1, locale2);
 }

}