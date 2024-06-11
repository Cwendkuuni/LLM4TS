import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validLanguageCode() {
        // Typical use case
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_validLanguageAndCountryCode() {
        // Typical use case
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
    }

    @Test
    public void testToLocale_validLanguageCountryAndVariantCode() {
        // Typical use case
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
        Assert.assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        // Error scenario - Invalid format
        LocaleUtils.toLocale("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLanguageCode() {
        // Error scenario - Invalid language code
        LocaleUtils.toLocale("99_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidCountryCode() {
        // Error scenario - Invalid country code
        LocaleUtils.toLocale("en_99");
    }

    @Test
    public void testLocaleLookupList_localeNotNull() {
        // Typical use case
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));
    }

    @Test
    public void testLocaleLookupList_localeAndDefaultLocaleNotNull() {
        // Typical use case
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));
        Assert.assertEquals(defaultLocale, result.get(3));
    }

    @Test
    public void testLocaleLookupList_localeIsNull() {
        // Error scenario - null input
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testLocaleLookupList_localeAndDefaultLocaleAreNull() {
        // Error scenario - both inputs are null
        List<Locale> result = LocaleUtils.localeLookupList(null, null);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case - check if the list is not empty
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertTrue(locales.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case - check if the set is not empty
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(locales.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_availableLocale() {
        // Typical use case - locale is available
        boolean result = LocaleUtils.isAvailableLocale(Locale.US);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_unavailableLocale() {
        // Typical use case - locale is not available
        boolean result = LocaleUtils.isAvailableLocale(new Locale("xx", "YY"));
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        // Typical use case - country exists
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testLanguagesByCountry_invalidCountryCode() {
        // Typical use case - country does not exist
        List<Locale> result = LocaleUtils.languagesByCountry("XX");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testLanguagesByCountry_nullCountryCode() {
        // Error scenario - null input
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        // Typical use case - language exists
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testCountriesByLanguage_invalidLanguageCode() {
        // Typical use case - language does not exist
        List<Locale> result = LocaleUtils.countriesByLanguage("xx");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testCountriesByLanguage_nullLanguageCode() {
        // Error scenario - null input
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(0, result.size());
    }
}