import org.junit.*;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {
	
	@Test
	public void testToLocale() {
		Locale locale1 = LocaleUtils.toLocale("en");
		assertEquals("en", locale1.getLanguage());
		assertEquals("", locale1.getCountry());
		
		Locale locale2 = LocaleUtils.toLocale("en_GB");
		assertEquals("en", locale2.getLanguage());
		assertEquals("GB", locale2.getCountry());
		
		Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
		assertEquals("en", locale3.getLanguage());
		assertEquals("GB", locale3.getCountry());
		assertEquals("xxx", locale3.getVariant());
		
		Locale locale4 = LocaleUtils.toLocale(null);
		assertNull(locale4);
		
		try {
			Locale locale5 = LocaleUtils.toLocale("EN_GB");
			fail("Expected IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid locale format: EN_GB", e.getMessage());
		}
		
		try {
			Locale locale6 = LocaleUtils.toLocale("en_gb_xxx");
			fail("Expected IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid locale format: en_gb_xxx", e.getMessage());
		}
	}

	@Test
	public void testLocaleLookupList() {
		Locale locale1 = new Locale("fr", "CA", "xxx");
		Locale locale2 = new Locale("fr");
		Locale locale3 = new Locale("en");
		Locale defaultLocale = new Locale("en", "US");
		
		List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
		assertEquals(locale1, list1.get(0));
		assertEquals(new Locale("fr", "CA"), list1.get(1));
		assertEquals(new Locale("fr"), list1.get(2));
		
		List<Locale> list2 = LocaleUtils.localeLookupList(locale1, defaultLocale);
		assertEquals(locale1, list2.get(0));
		assertEquals(new Locale("fr", "CA"), list2.get(1));
		assertEquals(new Locale("fr"), list2.get(2));
		assertEquals(defaultLocale, list2.get(3));
		
		List<Locale> list3 = LocaleUtils.localeLookupList(locale2, defaultLocale);
		assertEquals(locale2, list3.get(0));
		assertEquals(new Locale("fr"), list3.get(1));
		assertEquals(defaultLocale, list3.get(2));
		
		List<Locale> list4 = LocaleUtils.localeLookupList(locale3, defaultLocale);
		assertEquals(locale3, list4.get(0));
		assertEquals(defaultLocale, list4.get(1));
	}

	@Test
	public void testAvailableLocaleList() {
		List<Locale> list = LocaleUtils.availableLocaleList();
		assertTrue(list.contains(Locale.ENGLISH));
		assertTrue(list.contains(Locale.FRENCH));
		assertTrue(list.contains(Locale.GERMANY));
	}

	@Test
	public void testAvailableLocaleSet() {
		Set<Locale> set = LocaleUtils.availableLocaleSet();
		assertTrue(set.contains(Locale.ENGLISH));
		assertTrue(set.contains(Locale.FRENCH));
		assertTrue(set.contains(Locale.GERMANY));
	}

	@Test
	public void testIsAvailableLocale() {
		assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
		assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
		assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));
		
		Locale locale = new Locale("en", "GB");
		assertTrue(LocaleUtils.isAvailableLocale(locale));
		
		Locale locale2 = new Locale("en", "US");
		assertFalse(LocaleUtils.isAvailableLocale(locale2));
	}

	@Test
	public void testLanguagesByCountry() {
		List<Locale> list = LocaleUtils.languagesByCountry("US");
		assertTrue(list.contains(new Locale("en", "US")));
		assertTrue(list.contains(new Locale("es", "US")));
		
		List<Locale> list2 = LocaleUtils.languagesByCountry("CA");
		assertTrue(list2.contains(new Locale("en", "CA")));
		assertTrue(list2.contains(new Locale("fr", "CA")));
		
		List<Locale> list3 = LocaleUtils.languagesByCountry(null);
		assertTrue(list3.isEmpty());
	}

	@Test
	public void testCountriesByLanguage() {
		List<Locale> list = LocaleUtils.countriesByLanguage("en");
		assertTrue(list.contains(new Locale("en", "US")));
		assertTrue(list.contains(new Locale("en", "CA")));
		
		List<Locale> list2 = LocaleUtils.countriesByLanguage("fr");
		assertTrue(list2.contains(new Locale("fr", "CA")));
		assertTrue(list2.contains(new Locale("fr", "FR")));
		
		List<Locale> list3 = LocaleUtils.countriesByLanguage(null);
		assertTrue(list3.isEmpty());
	}
	
}