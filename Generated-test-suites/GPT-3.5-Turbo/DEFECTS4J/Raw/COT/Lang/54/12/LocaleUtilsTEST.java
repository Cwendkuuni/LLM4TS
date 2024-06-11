import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals(Locale.ENGLISH, locale);

        locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(Locale.UK, locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, locales.size());
        Assert.assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), locales.get(1));
        Assert.assertEquals(new Locale("fr"), locales.get(2));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, locales.size());
        Assert.assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), locales.get(1));
        Assert.assertEquals(new Locale("fr"), locales.get(2));
        Assert.assertEquals(new Locale("en"), locales.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(locales);
        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(set);
        Assert.assertFalse(set.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        boolean result = LocaleUtils.isAvailableLocale(Locale.ENGLISH);
        Assert.assertTrue(result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> locales = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(2, locales.size());
        Assert.assertEquals(new Locale("fr", "CA"), locales.get(0));
        Assert.assertEquals(new Locale("en", "CA"), locales.get(1));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> locales = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(30, locales.size());
        Assert.assertEquals(new Locale("fr", "CD"), locales.get(0));
        Assert.assertEquals(new Locale("fr", "ML"), locales.get(1));
        Assert.assertEquals(new Locale("fr", "BE"), locales.get(2));
        // ... more assertions
    }
}