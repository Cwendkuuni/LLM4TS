package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale locale = LocaleUtils.toLocale("en");
 assertEquals("en", locale.getLanguage());
 assertEquals("", locale.getCountry());

 locale = LocaleUtils.toLocale("en_GB");
 assertEquals("en", locale.getLanguage());
 assertEquals("GB", locale.getCountry());

 locale = LocaleUtils.toLocale("en_GB_xxx");
 assertEquals("en", locale.getLanguage());
 assertEquals("GB", locale.getCountry());
 assertEquals("xxx", locale.getVariant());
 }

 @Test
 public void testToLocaleInvalidFormat() {
 try {
 LocaleUtils.toLocale("xx_en");
 assertFalse(true);
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 LocaleUtils.toLocale("en_");
 assertFalse(true);
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 LocaleUtils.toLocale("en_GB_xx");
 assertFalse(true);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> list = LocaleUtils.localeLookupList(locale);
 assertEquals(3, list.size());
 assertEquals(locale, list.get(0));
 assertEquals(new Locale("fr", "CA"), list.get(1));
 assertEquals(new Locale("fr"), list.get(2));
 }

 @Test
 public void testLocaleLookupListNull() {
 List<Locale> list = LocaleUtils.localeLookupList(null);
 assertEquals(0, list.size());
 }

 @Test
 public void testLocaleLookupListDefaultLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> list = LocaleUtils.localeLookupList(locale, new Locale("en"));
 assertEquals(4, list.size());
 assertEquals(locale, list.get(0));
 assertEquals(new Locale("fr", "CA"), list.get(1));
 assertEquals(new Locale("fr"), list.get(2));
 assertEquals(new Locale("en"), list.get(3));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> list = LocaleUtils.availableLocaleList();
 assertEquals(LocaleUtils.availableLocaleSet().size(), list.size());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> set = LocaleUtils.availableLocaleSet();
 assertEquals(LocaleUtils.availableLocaleList().size(), set.size());
 }

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

 locale = new Locale("xx", "en");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> langs = LocaleUtils.languagesByCountry("CA");
 assertEquals(3, langs.size());
 assertTrue(langs.contains(new Locale("fr", "CA")));
 assertTrue(langs.contains(new Locale("en", "CA")));
 assertTrue(langs.contains(new Locale("es", "CA")));
 }

 @Test
 public void testLanguagesByCountryNull() {
 List<Locale> langs = LocaleUtils.languagesByCountry(null);
 assertEquals(0, langs.size());
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
 assertEquals(5, countries.size());
 assertTrue(countries.contains(new Locale("fr", "CA")));
 assertTrue(countries.contains(new Locale("fr", "FR")));
 assertTrue(countries.contains(new Locale("fr", "BE")));
 assertTrue(countries.contains(new Locale("fr", "CH")));
 assertTrue(countries.contains(new Locale("fr", "LU")));
 }

 @Test
 public void testCountriesByLanguageNull() {
 List<Locale> countries = LocaleUtils.countriesByLanguage(null);
 assertEquals(0, countries.size());
 }
}