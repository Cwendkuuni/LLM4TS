import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(locale1, new Locale("en", ""));

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(locale2, new Locale("en", "GB"));

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(locale3, new Locale("en", "GB", "xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid_format");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList1 = List.of(locale1, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList1 = LocaleUtils.localeLookupList(locale1);
        Assert.assertEquals(expectedList1, actualList1);

        Locale locale2 = new Locale("en");
        List<Locale> expectedList2 = List.of(locale1, new Locale("fr", "CA"), new Locale("fr"), locale2);
        List<Locale> actualList2 = LocaleUtils.localeLookupList(locale1, locale2);
        Assert.assertEquals(expectedList2, actualList2);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocales);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocalesSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocalesSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en");
        boolean isAvailable1 = LocaleUtils.isAvailableLocale(locale1);
        Assert.assertTrue(isAvailable1);

        Locale locale2 = new Locale("invalid");
        boolean isAvailable2 = LocaleUtils.isAvailableLocale(locale2);
        Assert.assertFalse(isAvailable2);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(languages);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(countries);
    }

}