import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleValidInput() {
        // Typical use case: language code and country code provided
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));

        // Typical use case: language code provided, no country code
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));

        // Edge case: variant code provided
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Edge case: language code, country code, and variant code provided
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Error scenario: invalid input
        try {
            LocaleUtils.toLocale("enGB");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("en");

        // Typical use case: locale with variant
        Assert.assertEquals(Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), locale2),
                LocaleUtils.localeLookupList(locale1, locale2));

        // Typical use case: locale without variant
        Assert.assertEquals(Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), locale2),
                LocaleUtils.localeLookupList(new Locale("fr", "CA"), locale2));

        // Error scenario: null input
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Error scenario: default locale is null
        Assert.assertEquals(Arrays.asList(locale1), LocaleUtils.localeLookupList(locale1, null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test that availableLocaleList returns a non-empty list
        Assert.assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test that availableLocaleSet returns a non-empty set
        Assert.assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale availableLocale = new Locale("en", "US");
        Locale unavailableLocale = new Locale("xx", "XX");

        // Typical use case: available locale
        Assert.assertTrue(LocaleUtils.isAvailableLocale(availableLocale));

        // Typical use case: unavailable locale
        Assert.assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case: country with available languages
        Assert.assertEquals(Arrays.asList(new Locale("en"), new Locale("fr")), LocaleUtils.languagesByCountry("CA"));

        // Typical use case: country with no available languages
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XX"));

        // Error scenario: null input
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case: language with available countries
        Assert.assertEquals(Arrays.asList(new Locale("CA"), new Locale("US")), LocaleUtils.countriesByLanguage("en"));

        // Typical use case: language with no available countries
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("xx"));

        // Error scenario: null input
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}