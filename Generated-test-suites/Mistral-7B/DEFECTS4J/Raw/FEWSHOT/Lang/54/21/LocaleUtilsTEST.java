package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expected;
 Locale actual;

 // Test empty string
 expected = null;
 actual = LocaleUtils.toLocale("");
 assertEquals(expected, actual);

 // Test valid locales
 expected = new Locale("en", "");
 actual = LocaleUtils.toLocale("en");
 assertEquals(expected, actual);
 expected = new Locale("en", "GB");
 actual = LocaleUtils.toLocale("en_GB");
 assertEquals(expected, actual);
 expected = new Locale("en", "GB", "xxx");
 actual = LocaleUtils.toLocale("en_GB_xxx");
 assertEquals(expected, actual);

 // Test invalid locales
 String[] invalidLocales = new String[] { "xx", "en_", "en_GB_", "en_GB_xx_" };
 for (String localeStr : invalidLocales) {
 throw new IllegalArgumentException("Invalid locale format: " + localeStr);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidLanguageCode() {
 LocaleUtils.toLocale("XX");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidCountryCode() {
 LocaleUtils.toLocale("en_xx");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidSeparator() {
 LocaleUtils.toLocale("en_GB-xx");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidLength() {
 LocaleUtils.toLocale("en_GB_xx_xx");
 }

 @Test
 public void testLocaleLookupList() {
 Locale defaultLocale = new Locale("en");
 Locale locale1 = new Locale("fr", "CA", "xxx");
 List localeLookupList = LocaleUtils.localeLookupList(locale1);
 List expected = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
 List actual = LocaleUtils.localeLookupList(locale1, defaultLocale);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListWithEmptyList() {
 List localeLookupList = LocaleUtils.localeLookupList(null);
 assertEquals(Collections.EMPTY_LIST, localeLookupList);
 }

 @Test
 public void testLocaleLookupListWithDefaultLocaleOnly() {
 Locale defaultLocale = new Locale("en");
 List localeLookupList = LocaleUtils.localeLookupList(defaultLocale);
 assertEquals(Collections.singletonList(defaultLocale), localeLookupList);
 }

 @Test
 public void testLocaleLookupListWithLocaleAndDefaultLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 Locale defaultLocale = new Locale("en");
 List localeLookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
 List expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
 List actual = localeLookupList;
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleUtilsIsAvailableLocale() {
 Locale locale1 = new Locale("en", "");
 Locale locale2 = new Locale("fr", "FR");
 assertTrue(LocaleUtils.isAvailableLocale(locale1));
 assertTrue(LocaleUtils.isAvailableLocale(locale2));
 }

 @Test
 public void testLocaleUtilsIsAvailableLocaleWithNullLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List expected = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
 List actual = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountryWithNullCountryCode() {
 List actual = LocaleUtils.languagesByCountry(null);
 assertEquals(Collections.EMPTY_LIST, actual);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List expected = Arrays.asList(new Locale(languageCode, "US"), new Locale(languageCode, "GB"));
 List actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguageWithNullLanguageCode() {
 List actual = LocaleUtils.countriesByLanguage(null);
 assertEquals(Collections.EMPTY_LIST, actual);
 }

 @Test
 public void testCountriesByLanguageWithLanguageCodeThatHasNoCountries() {
 String languageCode = "xx";
 List actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(Collections.EMPTY_LIST, actual);
 }
}