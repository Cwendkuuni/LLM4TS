
import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

	@Test
	public void testToLocale() {
		Locale locale = LocaleUtils.toLocale("en");
		assertEquals(new Locale("en", ""), locale);
		
		locale = LocaleUtils.toLocale("en_GB");
		assertEquals(new Locale("en", "GB"), locale);
		
		locale = LocaleUtils.toLocale("en_GB_xxx");
		assertEquals(new Locale("en", "GB", "xxx"), locale);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testToLocaleWithInvalidFormat() {
		LocaleUtils.toLocale("en-GB");
	}
	
	@Test
	public void testLocaleLookupList() {
		Locale locale = new Locale("fr", "CA", "xxx");
		List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
		List<Locale> resultList = LocaleUtils.localeLookupList(locale);
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testLocaleLookupListWithDefaultLocale() {
		Locale locale = new Locale("fr", "CA", "xxx");
		Locale defaultLocale = new Locale("en");
		List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
		List<Locale> resultList = LocaleUtils.localeLookupList(locale, defaultLocale);
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void testAvailableLocaleList() {
		List<Locale> availableLocales = LocaleUtils.availableLocaleList();
		assertTrue(availableLocales.contains(Locale.ENGLISH));
		assertTrue(availableLocales.contains(Locale.FRENCH));
		assertTrue(availableLocales.size() > 0);
	}
	
	@Test
	public void testAvailableLocaleSet() {
		Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
		assertTrue(availableLocales.contains(Locale.ENGLISH));
		assertTrue(availableLocales.contains(Locale.FRENCH));
		assertTrue(availableLocales.size() > 0);
	}
	
	@Test
	public void testIsAvailableLocale() {
		assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
		assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINESE));
	}
	
	@Test
	public void testLanguagesByCountry() {
		List<Locale> languages = LocaleUtils.languagesByCountry("CA");
		assertTrue(languages.contains(new Locale("fr", "CA")));
		assertTrue(languages.size() > 1);
	}
	
	@Test
	public void testCountriesByLanguage() {
		List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
		assertTrue(countries.contains(new Locale("fr", "CA")));
		assertTrue(countries.size() > 1);
	}
	
}
