import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLocaleString_ReturnsLocaleObject() {
        // typical use case
        Assert.assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        
        // edge case
        Assert.assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));

        // error scenario
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleString_ThrowsIllegalArgumentException() {
        // error scenario
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testLocaleLookupList_SingleLocale_ReturnsListWithSingleLocale() {
        // typical use case
        Locale locale = new Locale("en");
        Assert.assertEquals(1, LocaleUtils.localeLookupList(locale).size());

        // edge case
        locale = new Locale("fr", "CA");
        Assert.assertEquals(1, LocaleUtils.localeLookupList(locale).size());

        // error scenario
        Assert.assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testLocaleLookupList_MultipleLocales_ReturnsListWithExpectedOrder() {
        // typical use case
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        Assert.assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
        Assert.assertEquals(locale, LocaleUtils.localeLookupList(locale, defaultLocale).get(0));
        Assert.assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale, defaultLocale).get(1));
        Assert.assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale, defaultLocale).get(2));
        Assert.assertEquals(defaultLocale, LocaleUtils.localeLookupList(locale, defaultLocale).get(3));

        // edge case
        locale = new Locale("fr", "CA");
        defaultLocale = new Locale("en");
        Assert.assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
        Assert.assertEquals(locale, LocaleUtils.localeLookupList(locale, defaultLocale).get(0));
        Assert.assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale, defaultLocale).get(1));
        Assert.assertEquals(defaultLocale, LocaleUtils.localeLookupList(locale, defaultLocale).get(2));

        // error scenario
        Assert.assertEquals(0, LocaleUtils.localeLookupList(null, new Locale("en")).size());
    }

    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        // typical use case
        Assert.assertEquals(181, LocaleUtils.availableLocaleList().size());

        // edge case - verify list is unmodifiable
        try {
            LocaleUtils.availableLocaleList().add(Locale.CANADA);
            Assert.fail("Expected UnsupportedOperationException, but no exception was thrown.");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        // typical use case
        Assert.assertEquals(181, LocaleUtils.availableLocaleSet().size());

        // edge case - verify set is unmodifiable
        try {
            LocaleUtils.availableLocaleSet().add(Locale.CANADA);
            Assert.fail("Expected UnsupportedOperationException, but no exception was thrown.");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    @Test
    public void testIsAvailableLocale_ExistingLocale_ReturnsTrue() {
        // typical use case
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        
        // edge case
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
        
        // error scenario
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testIsAvailableLocale_NonExistingLocale_ReturnsFalse() {
        // typical use case
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
        
        // edge case
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ", "xxx")));
        
        // error scenario
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry_ExistingCountryCode_ReturnsListOfLanguages() {
        // typical use case
        Assert.assertEquals(14, LocaleUtils.languagesByCountry("GB").size());

        // edge case
        Assert.assertEquals(5, LocaleUtils.languagesByCountry("CA").size());

        // error scenario
        Assert.assertEquals(0, LocaleUtils.languagesByCountry("XXX").size());
    }

    @Test
    public void testLanguagesByCountry_NonExistingCountryCode_ReturnsEmptyList() {
        // typical use case
        Assert.assertEquals(0, LocaleUtils.languagesByCountry("ZZ").size());

        // edge case
        Assert.assertEquals(0, LocaleUtils.languagesByCountry("YY").size());

        // error scenario
        Assert.assertEquals(0, LocaleUtils.languagesByCountry(null).size());
    }

    @Test
    public void testCountriesByLanguage_ExistingLanguageCode_ReturnsListOfCountries() {
        // typical use case
        Assert.assertEquals(5, LocaleUtils.countriesByLanguage("en").size());

        // edge case
        Assert.assertEquals(18, LocaleUtils.countriesByLanguage("fr").size());

        // error scenario
        Assert.assertEquals(0, LocaleUtils.countriesByLanguage("xxx").size());
    }

    @Test
    public void testCountriesByLanguage_NonExistingLanguageCode_ReturnsEmptyList() {
        // typical use case
        Assert.assertEquals(0, LocaleUtils.countriesByLanguage("xx").size());

        // edge case
        Assert.assertEquals(0, LocaleUtils.countriesByLanguage("zz").size());

        // error scenario
        Assert.assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
    }

}