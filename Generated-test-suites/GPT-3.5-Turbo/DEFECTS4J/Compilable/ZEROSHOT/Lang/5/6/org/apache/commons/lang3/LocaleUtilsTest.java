package org.apache.commons.lang3;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.Test;

public class LocaleUtilsTest {

	@Test
	public void testToLocale() {
		Locale locale1 = LocaleUtils.toLocale("en");
		assertEquals(Locale.ENGLISH, locale1);
		
		Locale locale2 = LocaleUtils.toLocale("en_GB");
		assertEquals(Locale.UK, locale2);
		
		Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
		assertEquals(new Locale("en", "GB", "xxx"), locale3);
		
		Locale locale4 = LocaleUtils.toLocale("fr");
		assertEquals(Locale.FRENCH, locale4);
		
		Locale locale5 = LocaleUtils.toLocale("fr_XY");
		assertEquals(new Locale("fr", "XY"), locale5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocale_invalidFormat() {
		LocaleUtils.toLocale("invalidLocale");
	}

	@Test
	public void testLocaleLookupList() {
		List<Locale> locales1 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
		assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")),
				locales1);

		List<Locale> locales2 = LocaleUtils.localeLookupList(new Locale("en"));
		assertEquals(Arrays.asList(new Locale("en")), locales2);
	}

	@Test
	public void testLocaleLookupList_withDefaultLocale() {
		List<Locale> locales = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
		assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en")),
				locales);
	}

	@Test
	public void testAvailableLocaleList() {
		List<Locale> locales = LocaleUtils.availableLocaleList();
		assertNotNull(locales);
	}

	@Test
	public void testAvailableLocaleSet() {
		Set<Locale> locales = LocaleUtils.availableLocaleSet();
		assertNotNull(locales);
	}

	@Test
	public void testIsAvailableLocale() {
		assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
		assertFalse(LocaleUtils.isAvailableLocale(new Locale("xy")));
	}

	@Test
	public void testLanguagesByCountry() {
		List<Locale> languages = LocaleUtils.languagesByCountry("CA");
		assertEquals(Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA")), languages);
	}

	@Test
	public void testLanguagesByCountry_withInvalidCountryCode() {
		List<Locale> languages = LocaleUtils.languagesByCountry("ABC");
		assertEquals(Collections.emptyList(), languages);
	}

	@Test
	public void testCountriesByLanguage() {
		List<Locale> countries = LocaleUtils.countriesByLanguage("en");
		assertEquals(Arrays.asList(new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "US")), countries);
	}

	@Test
	public void testCountriesByLanguage_withInvalidLanguageCode() {
		List<Locale> countries = LocaleUtils.countriesByLanguage("XYZ");
		assertEquals(Collections.emptyList(), countries);
	}
}