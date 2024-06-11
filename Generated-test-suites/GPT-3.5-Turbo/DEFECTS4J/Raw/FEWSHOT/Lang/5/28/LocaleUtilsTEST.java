package org.apache.commons.lang3;
import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
		
	@Test
	public void testToLocale() {
		// Testing valid inputs
		assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
		assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
		assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
		
		// Testing invalid inputs
		try {
			LocaleUtils.toLocale("");
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			LocaleUtils.toLocale("e");
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			LocaleUtils.toLocale("_GB");
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			LocaleUtils.toLocale("en_");
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			LocaleUtils.toLocale("en_GB_");
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testLocaleLookupList() {
		// Test null input
		assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
		
		// Test normal input
		Locale locale = new Locale("fr", "CA", "xxx");
		Locale defaultLocale = new Locale("en");
		assertEquals(4, LocaleUtils.localeLookupList(locale).size());
		assertEquals(locale, LocaleUtils.localeLookupList(locale).get(0));
		assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale).get(1));
		assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale).get(2));
		assertEquals(defaultLocale, LocaleUtils.localeLookupList(locale).get(3));
		
		assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
		assertEquals(locale, LocaleUtils.localeLookupList(locale, defaultLocale).get(0));
		assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale, defaultLocale).get(1));
		assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale, defaultLocale).get(2));
		assertEquals(defaultLocale, LocaleUtils.localeLookupList(locale, defaultLocale).get(3));
	}
	
	@Test
	public void testAvailableLocaleList() {
		// Test size and content of available locale list
		assertEquals(Arrays.asList(Locale.getAvailableLocales()), LocaleUtils.availableLocaleList());
	}
	
	@Test
	public void testAvailableLocaleSet() {
		// Test size and content of available locale set
		assertEquals(new HashSet<>(Arrays.asList(Locale.getAvailableLocales())), LocaleUtils.availableLocaleSet());
	}
	
	@Test
	public void testIsAvailableLocale() {
		// Testing available locale
		assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
		assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "GB", "xxx")));
		
		// Testing unavailable locale
		assertFalse(LocaleUtils.isAvailableLocale(new Locale("ar")));
		assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "US")));
	}
	
	@Test
	public void testLanguagesByCountry() {
		// Test when input is null
		assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
		
		// Test when input is invalid
		assertTrue(LocaleUtils.languagesByCountry("XX").isEmpty());
		
		// Test when input is valid
		assertEquals(3, LocaleUtils.languagesByCountry("CA").size());
		assertTrue(LocaleUtils.languagesByCountry("CA").contains(new Locale("en", "CA")));
		assertTrue(LocaleUtils.languagesByCountry("CA").contains(new Locale("fr", "CA")));
		assertTrue(LocaleUtils.languagesByCountry("CA").contains(new Locale("iu", "CA")));
	}
	
	@Test
	public void testCountriesByLanguage() {
		// Test when input is null
		assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
		
		// Test when input is invalid
		assertTrue(LocaleUtils.countriesByLanguage("xx").isEmpty());
		
		// Test when input is valid
		assertEquals(3, LocaleUtils.countriesByLanguage("fr").size());
		assertTrue(LocaleUtils.countriesByLanguage("fr").contains(new Locale("fr", "CA")));
		assertTrue(LocaleUtils.countriesByLanguage("fr").contains(new Locale("fr", "BL")));
		assertTrue(LocaleUtils.countriesByLanguage("fr").contains(new Locale("fr", "MQ")));
	}
}