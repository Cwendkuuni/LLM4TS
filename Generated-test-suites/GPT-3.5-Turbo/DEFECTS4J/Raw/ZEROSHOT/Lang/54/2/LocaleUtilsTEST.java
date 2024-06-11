import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalid() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expectedList, list);
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
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(expectedList, list);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Collections.singletonList(new Locale("en", "US"));
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expectedList, list);
    }
}