package org.apache.commons.lang;
import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

 private List<Locale> localeList;

 @Test
 public void testToLocale() {
     // Test empty String
     assertNull(LocaleUtils.toLocale(null));
     // Test valid locale strings
     assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
     assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());
     assertEquals("es", LocaleUtils.toLocale("es").toString());
     assertEquals("de_DE_AT", LocaleUtils.toLocale("de_DE_AT").toString());
     // Test invalid locale strings
     IllegalArgumentException exception = null;
     try {
         LocaleUtils.toLocale("en_US_xx");
     } catch (IllegalArgumentException e) {
         exception = e;
     }
     assertNotNull(exception);
     assertTrue(exception.getMessage().startsWith("Invalid locale format:"));
     exception = null;
     try {
         LocaleUtils.toLocale("");
     } catch (IllegalArgumentException e) {
         exception = e;
     }
     assertNotNull(exception);
     assertTrue(exception.getMessage().startsWith("Invalid locale format:"));
     exception = null;
     try {
         LocaleUtils.toLocale("es_ES_xx");
     } catch (IllegalArgumentException e) {
         exception = e;
     }
     assertNotNull(exception);
     assertTrue(exception.getMessage().startsWith("Invalid locale format:"));
 }

 @Test
 public void testLocaleLookupList() {
     localeList = LocaleUtils.localeLookupList(Locale.US);
     assertEquals(3, localeList.size());
     assertEquals("en_US", localeList.get(0).toString());
     assertEquals("en", localeList.get(1).toString());
     assertEquals("en_US", localeList.get(2).toString());
     localeList = LocaleUtils.localeLookupList(Locale.FRANCE, Locale.US);
     assertEquals(5, localeList.size());
     assertEquals("fr_FR", localeList.get(0).toString());
     assertEquals("fr", localeList.get(1).toString());
     assertEquals("fr_FR", localeList.get(2).toString());
     assertEquals("en_US", localeList.get(3).toString());
     assertEquals("en", localeList.get(4).toString());
 }

 @Test
 public void testAvailableLocaleList() {
     List<Locale> availableLocales = LocaleUtils.availableLocaleList();
     assertEquals(232, availableLocales.size());
     List<String> countries = Arrays.asList("ZA", "AD", "AE", "AF", "AG", "AI", "AL", "AM", "AO", "AR", "AT", "AU", "BE", "BA", "BB", "BD", "BE", "BH", "BI", "BJ", "BM", "BN", "BO", "BR", "BS", "BT", "BW", "BY", "BZ", "CA", "CC", "CD", "CF", "CG", "CH", "CI", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CY", "CZ", "DE", "DK", "DO", "DZ", "EC", "EE", "EG", "EH", "ES", "FI", "FJ", "FO", "FR", "GA", "GB", "GD", "GE", "GF", "GG", "GI", "GL", "GM", "GN", "GP", "GR", "GT", "GU", "GY", "HK", "HN", "HR", "HT", "HU", "ID", "IE", "IL", "IM", "IN", "IQ", "IR", "IS", "IT", "JE", "JM", "JO", "JP", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY", "QA", "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SK", "SL", "SM", "SN", "SO", "SR", "SS", "ST", "SV", "SX", "SY", "TC", "TD", "TG", "TH", "TJ", "TK", "TL", "TM", "TN", "TO", "TR", "TT", "TV", "TW", "TZ", "UA", "UG", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "WF", "WS", "YE", "ZA", "ZM", "ZW");
     List<Locale> availableLocalesExpected = new ArrayList<>();
     for (String countryCode : countries) {
         Locale locale = new Locale("", countryCode);
         availableLocalesExpected.add(locale);
     }
     assertEquals(availableLocalesExpected, availableLocales);
 }

 @Test
 public void testIsAvailableLocale() {
     List<Locale> availableLocales = LocaleUtils.availableLocaleList();
     assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
     assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
     assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
     assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
     assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALY));
     assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testLanguagesByCountry() {
     // Test null countryCode
     assertNotNull(LocaleUtils.languagesByCountry(null));
     assertEquals(Collections.EMPTY_LIST, LocaleUtils.languagesByCountry(null));
     // Test valid countryCode
     List<Locale> expectedLocales = new ArrayList<>();
     expectedLocales.add(new Locale("fr", "CA"));
     expectedLocales.add(new Locale("en", "CA"));
     expectedLocales.add(new Locale("es", "ES"));
     expectedLocales.add(new Locale("fr", "FR"));
     List<Locale> actualLocales = LocaleUtils.languagesByCountry("CA");
     assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testCountriesByLanguage() {
     // Test null languageCode
     assertNotNull(LocaleUtils.countriesByLanguage(null));
     assertEquals(Collections.EMPTY_LIST, LocaleUtils.countriesByLanguage(null));
     // Test valid languageCode
     List<Locale> expectedLocales = new ArrayList<>();
     expectedLocales.add(new Locale("ca", "ES"));
     expectedLocales.add(new Locale("ca", "ES"));
     expectedLocales.add(new Locale("ca", "CA"));
     expectedLocales.add(new Locale("es", "ES"));
     List<Locale> actualLocales = LocaleUtils.countriesByLanguage("es");
     assertEquals(expectedLocales, actualLocales);
 }

}