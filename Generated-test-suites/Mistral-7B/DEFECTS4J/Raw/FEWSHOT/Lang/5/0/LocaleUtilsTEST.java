package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
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

 locale = LocaleUtils.toLocale("en_GB_xxx_yyy");
 assertEquals("en", locale.getLanguage());
 assertEquals("GB", locale.getCountry());
 assertEquals("xxx", locale.getVariant());
 assertFalse(locale.getDisplayLanguage().equals("yyy"));
 assertFalse(locale.getDisplayCountry().equals("yyy"));
 }

 @Test
 public void testLocaleLookupList() {
 Locale baseLocale = LocaleUtils.toLocale("en_CA");
 List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale);

 assertEquals(4, localeList.size());
 assertSame(baseLocale, localeList.get(0));
 assertNotNull(localeList.get(1));
 assertEquals("en", localeList.get(1).getLanguage());
 assertEquals("CA", localeList.get(1).getCountry());
 assertNull(localeList.get(1).getVariant());

 assertNotNull(localeList.get(2));
 assertEquals("en", localeList.get(2).getLanguage());
 assertEquals("", localeList.get(2).getCountry());
 assertNull(localeList.get(2).getVariant());

 assertNotNull(localeList.get(3));
 assertEquals("en", localeList.get(3).getLanguage());
 assertEquals("US", localeList.get(3).getCountry());
 assertNull(localeList.get(3).getVariant());
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale baseLocale = LocaleUtils.toLocale("fr");
 Locale defaultLocale = LocaleUtils.toLocale("en");
 List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);

 assertEquals(5, localeList.size());
 assertSame(baseLocale, localeList.get(0));
 assertSame(defaultLocale, localeList.get(4));

 assertNotNull(localeList.get(1));
 assertEquals("fr", localeList.get(1).getLanguage());
 assertEquals("CA", localeList.get(1).getCountry());
 assertNull(localeList.get(1).getVariant());

 assertNotNull(localeList.get(2));
 assertEquals("fr", localeList.get(2).getLanguage());
 assertEquals("", localeList.get(2).getCountry());
 assertNull(localeList.get(2).getVariant());

 assertNotNull(localeList.get(3));
 assertEquals("en", localeList.get(3).getLanguage());
 assertEquals("US", localeList.get(3).getCountry());
 assertNull(localeList.get(3).getVariant());
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> localeList = LocaleUtils.availableLocaleList();
 assertFalse(localeList.isEmpty());

 String firstLocaleLanguage = localeList.get(0).getLanguage();
 assertNotNull(firstLocaleLanguage);
 String secondLocaleLanguage = localeList.get(1).getLanguage();
 assertNotSame(firstLocaleLanguage, secondLocaleLanguage);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
 assertFalse(localeSet.isEmpty());

 Locale locale1 = localeSet.iterator().next();
 Locale locale2 = localeSet.iterator().next();
 assertFalse(locale1.equals(locale2));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> languageList = LocaleUtils.languagesByCountry(countryCode);
 assertFalse(languageList.isEmpty());

 String firstLanguage = languageList.get(0).getLanguage();
 String secondLanguage = languageList.get(1).getLanguage();
 assertNotSame(firstLanguage, secondLanguage);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";List<Locale> countryList = LocaleUtils.countriesByLanguage(languageCode);
 assertFalse(countryList.isEmpty());

 String firstCountry = countryList.get(0).getCountry();
 String secondCountry = countryList.get(1).getCountry();
 assertNotSame(firstCountry, secondCountry);
 }

}