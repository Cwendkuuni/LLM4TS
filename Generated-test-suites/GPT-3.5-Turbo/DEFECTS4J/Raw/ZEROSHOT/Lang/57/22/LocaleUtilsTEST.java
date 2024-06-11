import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // test valid locale strings
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // test invalid locale strings
        try {
            LocaleUtils.toLocale("en_GB_xx");
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_GB_");
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_");
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_XX");
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("");
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("en");

        // test localeLookupList with single parameter
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        Assert.assertEquals(3, list1.size());
        Assert.assertEquals(locale1, list1.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list1.get(1));
        Assert.assertEquals(new Locale("fr"), list1.get(2));

        List<Locale> list2 = LocaleUtils.localeLookupList(locale2);
        Assert.assertEquals(2, list2.size());
        Assert.assertEquals(locale2, list2.get(0));
        Assert.assertEquals(new Locale("en"), list2.get(1));

        // test localeLookupList with two parameters
        List<Locale> list3 = LocaleUtils.localeLookupList(locale1, locale2);
        Assert.assertEquals(4, list3.size());
        Assert.assertEquals(locale1, list3.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list3.get(1));
        Assert.assertEquals(new Locale("fr"), list3.get(2));
        Assert.assertEquals(locale2, list3.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocales);
        Assert.assertTrue(availableLocales.size() > 0);
        Assert.assertTrue(availableLocales.contains(Locale.ENGLISH));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocales);
        Assert.assertTrue(availableLocales.size() > 0);
        Assert.assertTrue(availableLocales.contains(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(languages);
        Assert.assertTrue(languages.size() > 0);
        Assert.assertTrue(languages.get(0).getCountry().equals("CA"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        Assert.assertNotNull(countries);
        Assert.assertTrue(countries.size() > 0);
        Assert.assertTrue(countries.get(0).getLanguage().equals("fr"));
    }
}