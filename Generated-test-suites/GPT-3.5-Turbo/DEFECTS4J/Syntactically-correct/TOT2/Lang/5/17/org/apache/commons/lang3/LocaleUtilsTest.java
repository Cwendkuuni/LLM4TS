import org.junit.Test;
import org.junit.Assert;

import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale1.getLanguage());
        Assert.assertEquals("", locale1.getCountry());

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale2.getLanguage());
        Assert.assertEquals("GB", locale2.getCountry());

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals("en", locale3.getLanguage());
        Assert.assertEquals("GB", locale3.getCountry());
        Assert.assertEquals("xxx", locale3.getVariant());
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, lookupList.size());
        Assert.assertEquals(locale, lookupList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        Assert.assertEquals(new Locale("fr"), lookupList.get(2));

        List<Locale> lookupListWithDefault = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(4, lookupListWithDefault.size());
        Assert.assertEquals(new Locale("en"), lookupListWithDefault.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertTrue(availableLocales.contains(Locale.US));
        Assert.assertTrue(availableLocales.contains(Locale.CANADA));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(availableLocales.contains(Locale.US));
        Assert.assertTrue(availableLocales.contains(Locale.CANADA));
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesUS = LocaleUtils.languagesByCountry("US");
        Assert.assertTrue(languagesUS.contains(Locale.ENGLISH));
        Assert.assertTrue(languagesUS.contains(Locale.SPANISH));

        List<Locale> languagesCA = LocaleUtils.languagesByCountry("CA");
        Assert.assertTrue(languagesCA.contains(Locale.ENGLISH));
        Assert.assertTrue(languagesCA.contains(Locale.FRENCH));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesEN = LocaleUtils.countriesByLanguage("en");
        Assert.assertTrue(countriesEN.contains(Locale.US));
        Assert.assertTrue(countriesEN.contains(Locale.CANADA));

        List<Locale> countriesFR = LocaleUtils.countriesByLanguage("fr");
        Assert.assertTrue(countriesFR.contains(Locale.CANADA));
        Assert.assertTrue(countriesFR.contains(Locale.FRANCE));
    }
}