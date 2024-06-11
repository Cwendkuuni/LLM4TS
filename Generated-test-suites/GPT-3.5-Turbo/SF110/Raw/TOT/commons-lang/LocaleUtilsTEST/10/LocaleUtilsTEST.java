import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_typicalCase() {
        String str = "en_US";
        Locale result = LocaleUtils.toLocale(str);
        Locale expected = new Locale("en", "US");
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_emptyString() {
        String str = "";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        String str = "en_US_#";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLanguageCode() {
        String str = "ENG_US";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidCountryCode() {
        String str = "en_USA";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidVariant() {
        String str = "en_US_variant";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testLocaleLookupList_typicalCase() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = Arrays.asList(locale, new Locale("en"), new Locale("en", ""));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_nullLocale() {
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        List<Locale> expected = Arrays.asList(defaultLocale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_typicalCase() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet_typicalCase() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_available() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_unavailable() {
        Locale locale = new Locale("xx", "XX");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_typicalCase() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testLanguagesByCountry_nullCountryCode() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Collections.emptyList();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_typicalCase() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testCountriesByLanguage_nullLanguageCode() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Collections.emptyList();
        Assert.assertEquals(expected, result);
    }

}