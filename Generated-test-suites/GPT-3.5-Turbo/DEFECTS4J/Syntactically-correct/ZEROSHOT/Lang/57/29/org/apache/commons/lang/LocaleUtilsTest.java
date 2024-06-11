import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

	@Test
	public void testToLocale() {
		assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
		assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
		assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

		try {
			LocaleUtils.toLocale("EN_GB");
			fail("Expected IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testLocaleLookupList() {
	    Locale locale1 = new Locale("fr", "CA", "xxx");
	    Locale locale2 = new Locale("en");
	    List<Locale> result = LocaleUtils.localeLookupList(locale1);
	    assertEquals(3, result.size());
	    assertEquals(locale1, result.get(0));
	    assertEquals(new Locale("fr", "CA"), result.get(1));
	    assertEquals(new Locale("fr"), result.get(2));
	    
	    result = LocaleUtils.localeLookupList(locale1, locale2);
	    assertEquals(4, result.size());
        assertEquals(locale1, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(locale2, result.get(3));
	}

	@Test
	public void testAvailableLocaleList() {
		List<Locale> result = LocaleUtils.availableLocaleList();
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testAvailableLocaleSet() {
		Set<Locale> result = LocaleUtils.availableLocaleSet();
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testIsAvailableLocale() {
		assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
		assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
	}

	@Test
	public void testLanguagesByCountry() {
		List<Locale> result = LocaleUtils.languagesByCountry("CA");
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testCountriesByLanguage() {
		List<Locale> result = LocaleUtils.countriesByLanguage("fr");
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}
}