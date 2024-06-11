package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
	@Test
	public void testToLocale() {
		assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
		assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
		assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidLocaleFormat1() {
		LocaleUtils.toLocale("en_");
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidLocaleFormat2() {
		LocaleUtils.toLocale("en_Gb");
	}

	@Test
	public void testLocaleLookupList1() {
		List<Locale> expected = new ArrayList<>();
		expected.add(new Locale("fr", "CA", "xxx"));
		expected.add(new Locale("fr", "CA"));
		expected.add(new Locale("fr"));
		assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
	}

	@Test
	public void testLocaleLookupList2() {
		List<Locale> expected = new ArrayList<>();
		expected.add(new Locale("fr", "CA", "xxx"));
		expected.add(new Locale("fr", "CA"));
		expected.add(new Locale("fr"));
		expected.add(new Locale("en"));
		assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
	}

	@Test
	public void testAvailableLocaleList() {
		List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
		assertEquals(expected, LocaleUtils.availableLocaleList());
	}

	@Test
	public void testAvailableLocaleSet() {
		Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
		assertEquals(expected, LocaleUtils.availableLocaleSet());
	}

	@Test
	public void testIsAvailableLocale() {
		assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
		assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
	}

	@Test
	public void testLanguagesByCountry() {
		List<Locale> expected = new ArrayList<>();
		expected.add(new Locale("en", "US"));
		expected.add(new Locale("en", "GB"));
		expected.add(new Locale("en", "CA"));
		assertEquals(expected, LocaleUtils.languagesByCountry("US"));
	}

	@Test
	public void testLanguagesByCountryInvalidCode() {
		assertEquals(Collections.EMPTY_LIST, LocaleUtils.languagesByCountry("XX"));
	}

	@Test
	public void testCountriesByLanguage() {
		List<Locale> expected = new ArrayList<>();
		expected.add(new Locale("en", "US"));
		expected.add(new Locale("es", "US"));
		expected.add(new Locale("fr", "CA"));
		assertEquals(expected, LocaleUtils.countriesByLanguage("US"));
	}

	@Test
	public void testCountriesByLanguageInvalidCode() {
		assertEquals(Collections.EMPTY_LIST, LocaleUtils.countriesByLanguage("XX"));
	}
}