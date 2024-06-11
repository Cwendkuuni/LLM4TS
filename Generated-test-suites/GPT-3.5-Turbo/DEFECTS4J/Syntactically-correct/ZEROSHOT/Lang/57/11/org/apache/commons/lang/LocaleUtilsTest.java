import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale string
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale);

        // Test null input
        locale = LocaleUtils.toLocale(null);
        Assert.assertNull(locale);

        // Test invalid locale format
        try {
            LocaleUtils.toLocale("abcde");
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: abcde", e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test with locale and default locale
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(locale, list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
        Assert.assertEquals(defaultLocale, list.get(3));

        // Test with null locale and default locale
        list = LocaleUtils.localeLookupList(null, defaultLocale);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(defaultLocale, list.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(set);
        Assert.assertFalse(set.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with available locale
        Locale locale = new Locale("en", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailable);

        // Test with unavailable locale
        locale = new Locale("xx", "XX");
        isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(langs);
        Assert.assertFalse(langs.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(countries);
        Assert.assertFalse(countries.isEmpty());
    }

}