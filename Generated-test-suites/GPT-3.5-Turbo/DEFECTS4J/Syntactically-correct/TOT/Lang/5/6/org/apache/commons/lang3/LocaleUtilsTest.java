import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case: converting a valid locale string
        Locale locale = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(Locale.US, locale);

        // Edge case: converting a valid locale string with variant
        Locale localeWithVariant = LocaleUtils.toLocale("en_US_xxx");
        Locale expectedLocaleWithVariant = new Locale("en", "US", "xxx");
        Assert.assertEquals(expectedLocaleWithVariant, localeWithVariant);

        // Error scenario: converting an invalid locale string
        try {
            LocaleUtils.toLocale("invalid_locale");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }

        // Error scenario: converting a null string
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case: getting the list of locales to search through starting from a specific locale
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(4, localeLookupList.size());
        Assert.assertEquals(locale, localeLookupList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), localeLookupList.get(1));
        Assert.assertEquals(new Locale("fr"), localeLookupList.get(2));
        Assert.assertEquals(Locale.getDefault(), localeLookupList.get(3));

        // Error scenario: using a null locale
        List<Locale> nullLocaleLookupList = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, nullLocaleLookupList.size());
    }

    @Test
    public void testAvailableLocaleList() {
        // Getting the list of available locales
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocaleList);
        Assert.assertTrue(availableLocaleList.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Getting the set of available locales
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocaleSet);
        Assert.assertTrue(availableLocaleSet.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case: checking if an available locale is valid
        Locale locale = Locale.US;
        boolean isAvailableLocale = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailableLocale);

        // Typical use case: checking if an unavailable locale is valid
        Locale invalidLocale = new Locale("xx", "YY");
        boolean isInvalidLocale = LocaleUtils.isAvailableLocale(invalidLocale);
        Assert.assertFalse(isInvalidLocale);
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case: getting the list of languages supported for a country
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(languagesByCountry);
        Assert.assertTrue(languagesByCountry.size() > 0);

        // Error scenario: accessing languages for a null country code
        List<Locale> nullLanguagesByCountry = LocaleUtils.languagesByCountry(null);
        Assert.assertNotNull(nullLanguagesByCountry);
        Assert.assertEquals(0, nullLanguagesByCountry.size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case: getting the list of countries supported for a language
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(countriesByLanguage);
        Assert.assertTrue(countriesByLanguage.size() > 0);

        // Error scenario: accessing countries for a null language code
        List<Locale> nullCountriesByLanguage = LocaleUtils.countriesByLanguage(null);
        Assert.assertNotNull(nullCountriesByLanguage);
        Assert.assertEquals(0, nullCountriesByLanguage.size());
    }

}