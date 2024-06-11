package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_ValidLocale() {
 Locale expected = new Locale("en", "US");
 Locale actual = LocaleUtils.toLocale("en_US");
 assertEquals("Expected and actual locales should be equal", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLanguageCode() {
 String invalidLanguageCode = "ABC";
 Locale actual = LocaleUtils.toLocale(invalidLanguageCode);
 assertNull("Locale should be null for invalid input", actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidCountryCode() {
 String invalidCountryCode = "ABC";
 Locale actual = LocaleUtils.toLocale("en_" + invalidCountryCode);
 assertNull("Locale should be null for invalid input", actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidSeparator() {
 String invalidLocaleString = "en:US";
 Locale actual = LocaleUtils.toLocale(invalidLocaleString);
 assertNull("Locale should be null for invalid input", actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLength() {
 String invalidLocaleString = "en_US_";
 Locale actual = LocaleUtils.toLocale(invalidLocaleString);
 assertNull("Locale should be null for invalid input", actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLanguageCodeLength() {
 String invalidLocaleString = "en_";
 Locale actual = LocaleUtils.toLocale(invalidLocaleString);
 assertNull("Locale should be null for invalid input", actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidCountryCodeLength() {
 String invalidLocaleString = "en_US_";
 Locale actual = LocaleUtils.toLocale(invalidLocaleString);
 assertNull("Locale should be null for invalid input", actual);
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "XXX");
 List<Locale> localeList = LocaleUtils.localeLookupList(locale);
 assertEquals("Size of locale list should be 3", 3, localeList.size());
 List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"));
 assertTrue("Locale lists should be equal", localeList.equals(expectedList));
 }

 @Test
 public void testLocaleLookupList_EmptyList() {
 List<Locale> localeList = LocaleUtils.localeLookupList(null);
 assertTrue("Locale lists should be empty", localeList.isEmpty());
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 Locale locale = new Locale("fr", "CA", "XXX");
 Locale defaultLocale = new Locale("en");
 List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals("Size of locale list should be 4", 4, localeList.size());
 List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 assertTrue("Locale lists should be equal", localeList.equals(expectedList));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> localeList = LocaleUtils.availableLocaleList();
 assertFalse("Locale list should not be empty", localeList.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
 assertFalse("Locale set should not be empty", localeSet.isEmpty());
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> localeList = LocaleUtils.languagesByCountry(countryCode);
 assertFalse("Locale list should not be empty", localeList.isEmpty());
 List<String> expectedLanguageCodes = Arrays.asList("fr", "en");
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
 assertTrue("Locale lists should be equal", localeList.equals(Collections.unmodifiableList(LocaleUtils.localeLookupList(new Locale("en", "CA"), new Locale("fr", "CA")))));
 assertTrue("Language codes should be contained in locale list", expectedLanguageCodes.containsAll(Arrays.asList(localeList.get(0).getLanguage(), localeList.get(1).getLanguage())));
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "fr";List<Locale> localeList = LocaleUtils.countriesByLanguage(languageCode);
 assertFalse("Locale list should not be empty", localeList.isEmpty());
 List<String> expectedCountryCodes = Arrays.asList("CA", "BE", "CH", "FR");
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"), new Locale("fr", "FR"));
 assertTrue("Locale lists should be equal", localeList.equals(Collections.unmodifiableList(LocaleUtils.localeLookupList(new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CH"), new Locale("fr", "CA")))));
 assertTrue("Country codes should be contained in locale list", expectedCountryCodes.containsAll(Arrays.asList(localeList.get(0).getCountry(), localeList.get(1).getCountry(), localeList.get(2).getCountry(), localeList.get(3).getCountry())));
 }

}