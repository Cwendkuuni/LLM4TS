import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals(locale.getLanguage(), "en");
        Assert.assertEquals(locale.getCountry(), "");

        locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(locale.getLanguage(), "en");
        Assert.assertEquals(locale.getCountry(), "GB");

        locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(locale.getLanguage(), "en");
        Assert.assertEquals(locale.getCountry(), "GB");
        Assert.assertEquals(locale.getVariant(), "xxx");

        try {
            locale = LocaleUtils.toLocale("eN");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            locale = LocaleUtils.toLocale("en_G");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(localeList.size(), 3);
        Assert.assertEquals(localeList.get(0).getLanguage(), "fr");
        Assert.assertEquals(localeList.get(0).getCountry(), "CA");
        Assert.assertEquals(localeList.get(0).getVariant(), "xxx");
        Assert.assertEquals(localeList.get(1).getLanguage(), "fr");
        Assert.assertEquals(localeList.get(1).getCountry(), "CA");
        Assert.assertEquals(localeList.get(1).getVariant(), "");
        Assert.assertEquals(localeList.get(2).getLanguage(), "fr");
        Assert.assertEquals(localeList.get(2).getCountry(), "");
        Assert.assertEquals(localeList.get(2).getVariant(), "");

        localeList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(localeList.size(), 4);
        Assert.assertEquals(localeList.get(0).getLanguage(), "fr");
        Assert.assertEquals(localeList.get(0).getCountry(), "CA");
        Assert.assertEquals(localeList.get(0).getVariant(), "xxx");
        Assert.assertEquals(localeList.get(1).getLanguage(), "fr");
        Assert.assertEquals(localeList.get(1).getCountry(), "CA");
        Assert.assertEquals(localeList.get(1).getVariant(), "");
        Assert.assertEquals(localeList.get(2).getLanguage(), "fr");
        Assert.assertEquals(localeList.get(2).getCountry(), "");
        Assert.assertEquals(localeList.get(2).getVariant(), "");
        Assert.assertEquals(localeList.get(3).getLanguage(), "en");
        Assert.assertEquals(localeList.get(3).getCountry(), "");
        Assert.assertEquals(localeList.get(3).getVariant(), "");
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(localeList);
        Assert.assertTrue(localeList.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(localeSet);
        Assert.assertTrue(localeSet.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        boolean result = LocaleUtils.isAvailableLocale(Locale.US);
        Assert.assertTrue(result);

        result = LocaleUtils.isAvailableLocale(new Locale("xx"));
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> localeList = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(localeList);
        Assert.assertTrue(localeList.size() > 0);
        Assert.assertEquals(localeList.get(0).getCountry(), "CA");
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage("fr");
        Assert.assertNotNull(localeList);
        Assert.assertTrue(localeList.size() > 0);
        Assert.assertEquals(localeList.get(0).getLanguage(), "fr");
    }
}