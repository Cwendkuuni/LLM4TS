package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expected = new Locale("en", "GB");
 Locale actual = LocaleUtils.toLocale("en_GB");
 assertEquals(expected, actual);

 expected = new Locale("en", "");
 actual = LocaleUtils.toLocale("en");
 assertEquals(expected, actual);

 expected = new Locale("en", "GB", "US");
 actual = LocaleUtils.toLocale("en_GB_US");
 assertEquals(expected, actual);

 expected = null;
 actual = LocaleUtils.toLocale(null);
 assertNull(actual);

 expected = new Locale("fr", "CA", "XXX");
 String input = "fr_CA_XXX";
 actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = "fr_CA_XXX_YYY";
 actual = LocaleUtils.toLocale(input);
 assertNull(actual);

 input = "fr_CA";
 actual = LocaleUtils.toLocale(input);
 assertEquals(new Locale("fr", "CA"), actual);

 input = "fr";
 actual = LocaleUtils.toLocale(input);
 assertEquals(new Locale("fr", ""), actual);
 }

 @Test
 public void testLocaleLookupList() {
 Locale base = new Locale("fr", "CA", "XXX");
 Locale defaultLocale = new Locale("en");

 List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 List<Locale> actual = LocaleUtils.localeLookupList(base, defaultLocale);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListDefaultLocaleIsNull() {
 Locale base = new Locale("fr", "CA", "XXX");

 List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"), Collections.EMPTY_LIST);
 List<Locale> actual = LocaleUtils.localeLookupList(base, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListDefaultLocaleIsSameAsBase() {
 Locale base = new Locale("fr", "CA", "XXX");
 Locale defaultLocale = new Locale("fr", "CA", "XXX");

 List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"), new Locale("fr", "CA", "XXX"));
 List<Locale> actual = LocaleUtils.localeLookupList(base, defaultLocale);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListDefaultLocaleIsMoreGeneral() {
 Locale base = new Locale("fr", "CA", "XXX");
 Locale defaultLocale = new Locale("fr");

 List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"), new Locale("fr"));
 List<Locale> actual = LocaleUtils.localeLookupList(base, defaultLocale);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListDefaultLocaleIsLessGeneral() {
 Locale base = new Locale("fr", "CA", "XXX");
 Locale defaultLocale = new Locale("fr", "CA", "YYY");

 List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr", "CA", "YYY"), new Locale("fr"));
 List<Locale> actual = LocaleUtils.localeLookupList(base, defaultLocale);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr", "CA", "XXX");

 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("fr", "CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("fr");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("en");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("de");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expected = LocaleUtils.availableLocaleList();
 List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expected = LocaleUtils.availableLocaleSet();
 Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> expected = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> actual = Arrays.asList(new Locale("fr"), new Locale("en"));
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountryNull() {
 List<Locale> expected = Collections.EMPTY_LIST;
 List<Locale> actual = LocaleUtils.languagesByCountry(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "fr";
 List<Locale> expected = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> actual = Arrays.asList(new Locale("CA"), new Locale("CH"));
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguageNull() {
 List<Locale> expected = Collections.EMPTY_LIST;
 List<Locale> actual = LocaleUtils.countriesByLanguage(null);
 assertEquals(expected, actual);
 }

}