import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;

public class LocaleUtilsTest {

	@Test
	public void testToLocale() {
		Locale locale = LocaleUtils.toLocale("en");
		Assert.assertEquals("en", locale.getLanguage());
		Assert.assertEquals("", locale.getCountry());
	}

	@Test
	public void testToLocaleWithCountry() {
		Locale locale = LocaleUtils.toLocale("en_GB");
		Assert.assertEquals("en", locale.getLanguage());
		Assert.assertEquals("GB", locale.getCountry());
	}

	@Test
	public void testToLocaleWithVariant() {
		Locale locale = LocaleUtils.toLocale("en_GB_xxx");
		Assert.assertEquals("en", locale.getLanguage());
		Assert.assertEquals("GB", locale.getCountry());
		Assert.assertEquals("xxx", locale.getVariant());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocaleWithInvalidFormat() {
		LocaleUtils.toLocale("invalid");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocaleWithInvalidLanguageCode() {
		LocaleUtils.toLocale("AA_GB");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocaleWithInvalidCountryCode() {
		LocaleUtils.toLocale("en_gb");
	}

	@Test
	public void testLocaleLookupList() {
		Locale locale = new Locale("fr", "CA", "xxx");
		List<Locale> expectedResult = Arrays.asList(
			new Locale("fr", "CA", "xxx"),
			new Locale("fr", "CA"),
			new Locale("fr")
		);
		List<Locale> result = LocaleUtils.localeLookupList(locale);
		Assert.assertEquals(expectedResult, result);
	}

	@Test
	public void testLocaleLookupListWithDefaultLocale() {
		Locale locale = new Locale("fr", "CA", "xxx");
		Locale defaultLocale = new Locale("en");
		List<Locale> expectedResult = Arrays.asList(
			new Locale("fr", "CA", "xxx"),
			new Locale("fr", "CA"),
			new Locale("fr"),
			new Locale("en")
		);
		List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
		Assert.assertEquals(expectedResult, result);
	}

	@Test
	public void testAvailableLocaleList() {
		List<Locale> resultList = LocaleUtils.availableLocaleList();
		List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
		Assert.assertEquals(expectedList, resultList);
	}

	@Test
	public void testAvailableLocaleSet() {
		Set<Locale> resultSet = LocaleUtils.availableLocaleSet();
		Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
		Assert.assertEquals(expectedSet, resultSet);
	}

	@Test
	public void testIsAvailableLocaleWithAvailableLocale() {
		boolean result = LocaleUtils.isAvailableLocale(Locale.CANADA);
		Assert.assertTrue(result);
	}

	@Test
	public void testIsAvailableLocaleWithUnavailableLocale() {
		boolean result = LocaleUtils.isAvailableLocale(new Locale("aa", "BB"));
		Assert.assertFalse(result);
	}

	@Test
	public void testLanguagesByCountry() {
		List<Locale> expectedResult = Arrays.asList(
			new Locale("fr", "CA"),
			new Locale("en", "CA")
		);
		List<Locale> result = LocaleUtils.languagesByCountry("CA");
		Assert.assertEquals(expectedResult, result);
	}

	@Test
	public void testLanguagesByCountryWithInvalidCountryCode() {
		List<Locale> result = LocaleUtils.languagesByCountry("ZZ");
		Assert.assertEquals(Collections.emptyList(), result);
	}

	@Test
	public void testCountriesByLanguage() {
		List<Locale> expectedResult = Arrays.asList(
			new Locale("en", "US"),
			new Locale("en", "GB"),
			new Locale("en", "IN")
		);
		List<Locale> result = LocaleUtils.countriesByLanguage("en");
		Assert.assertEquals(expectedResult, result);
	}

	@Test
	public void testCountriesByLanguageWithInvalidLanguageCode() {
		List<Locale> result = LocaleUtils.countriesByLanguage("ZZ");
		Assert.assertEquals(Collections.emptyList(), result);
	}

}