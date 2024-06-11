package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expected = new Locale("en", "GB");
 Locale actual = LocaleUtils.toLocale("en_GB");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 LocaleUtils.toLocale("enGB"); // lowercase G
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat2() {
 LocaleUtils.toLocale("en_GB_"); // missing country code
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat3() {
 LocaleUtils.toLocale("en_GB_us"); // invalid country code
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = LocaleUtils.toLocale("fr_CA_QC");
 List<Locale> expected = Arrays.asList(
 LocaleUtils.toLocale("fr_CA_QC"),
 LocaleUtils.toLocale("fr_CA"),
 LocaleUtils.toLocale("fr"),
 Locale.getDefault());
 List<Locale> actual = LocaleUtils.localeLookupList(locale, Locale.getDefault());
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListDefaultIsNull() {
 Locale locale = LocaleUtils.toLocale("fr_CA_QC");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
 assertNull(actual);
 }

 @Test
 public void testLocaleLookupListDefaultIsLocale() {
 Locale locale = LocaleUtils.toLocale("fr_CA_QC");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, LocaleUtils.toLocale("en_US"));
 List<Locale> expected = Arrays.asList(
 LocaleUtils.toLocale("fr_CA_QC"),
 LocaleUtils.toLocale("fr_CA"),
 LocaleUtils.toLocale("fr"),
 LocaleUtils.toLocale("en_US"));
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = Collections.newHashSet(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale() {
 assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
 assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> actual = LocaleUtils.languagesByCountry("US");
 List<Locale> expected = Arrays.asList(
 Locale.ENGLISH,
 Locale.FRENCH,
 Locale.SPANISH,
 Locale.GERMAN,
 Locale.ITALIAN,
 Locale.PORTUGUESE,
 Locale.DUTCH,
 Locale.SWEDISH,
 Locale.FAROESE,
 Locale.Danish,
 Locale.FINNISH,
 Locale.NORwegian,
 Locale.IRISH,
 Locale.POLISH,
 Locale.Romanian,
 Locale.Russian,
 Locale.Serbian,
 Locale.Slovak,
 Locale.Slovenian,
 Locale.Czech,
 Locale.Hungarian,
 Locale.Polish,
 Locale.Bulgarian,
 Locale.Greek,
 Locale.Turkish);
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountryNull() {
 List<Locale> actual = LocaleUtils.languagesByCountry(null);
 assertEquals(Collections.EMPTY_LIST, actual);
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> actual = LocaleUtils.countriesByLanguage("en");
 List<Locale> expected = Arrays.asList(
 Locale.US,
 Locale.CA,
 Locale.GB,
 Locale.IE,
 Locale.AU,
 Locale.NZ,
 Locale.JM,
 Locale.TT,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE,
 Locale.IE);
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguageNull() {
 List<Locale> actual = LocaleUtils.countriesByLanguage(null);
 assertEquals(Collections.EMPTY_LIST, actual);
 }

}