import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class LocaleUtilsTest {

    // Test toLocale method

    @Test
    public void testToLocaleWithValidLocaleString() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidLocaleString() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testToLocaleWithNullLocaleString() {
        Locale locale = LocaleUtils.toLocale(null);
        Assert.assertNull(locale);
    }

    // Test localeLookupList method

    @Test
    public void testLocaleLookupListWithValidAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> resultList = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, resultList.size());
        Assert.assertEquals(locale, resultList.get(0));
        Assert.assertEquals(new Locale("fr","CA"), resultList.get(1));
        Assert.assertEquals(new Locale("fr"), resultList.get(2));
        Assert.assertEquals(defaultLocale, resultList.get(3));
    }

    @Test
    public void testLocaleLookupListWithNullLocaleAndDefaultLocale() {
        Locale defaultLocale = new Locale("en");
        List<Locale> resultList = LocaleUtils.localeLookupList(null, defaultLocale);
        Assert.assertEquals(1, resultList.size());
        Assert.assertEquals(defaultLocale, resultList.get(0));
    }

    // Test availableLocaleList method

    @Test
    public void testAvailableLocaleList() {
        List<Locale> resultList = LocaleUtils.availableLocaleList();
        Assert.assertTrue(resultList.size() > 0);
        Assert.assertTrue(resultList.contains(Locale.getDefault()));
    }

    // Test availableLocaleSet method

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> resultSet = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(resultSet.size() > 0);
        Assert.assertTrue(resultSet.contains(Locale.getDefault()));
    }

    // Test isAvailableLocale method

    @Test
    public void testIsAvailableLocaleWithAvailableLocale() {
        Locale locale = Locale.getDefault();
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocaleWithUnavailableLocale() {
        Locale locale = new Locale("xyz");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    // Test languagesByCountry method

    @Test
    public void testLanguagesByCountryWithValidCountryCode() {
        List<Locale> resultList = LocaleUtils.languagesByCountry("US");
        Assert.assertTrue(resultList.size() > 0);
        for (Locale locale : resultList) {
            Assert.assertEquals("US", locale.getCountry());
            Assert.assertEquals("", locale.getVariant());
        }
    }

    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        List<Locale> resultList = LocaleUtils.languagesByCountry("invalid");
        Assert.assertEquals(0, resultList.size());
    }

    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        List<Locale> resultList = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, resultList.size());
    }

    // Test countriesByLanguage method

    @Test
    public void testCountriesByLanguageWithValidLanguageCode() {
        List<Locale> resultList = LocaleUtils.countriesByLanguage("en");
        Assert.assertTrue(resultList.size() > 0);
        for (Locale locale : resultList) {
            Assert.assertEquals("en", locale.getLanguage());
            Assert.assertEquals("", locale.getVariant());
        }
    }
    
    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        List<Locale> resultList = LocaleUtils.countriesByLanguage("invalid");
        Assert.assertEquals(0, resultList.size());
    }

    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        List<Locale> resultList = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(0, resultList.size());
    }
}