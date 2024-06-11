import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    // test toLocale method
    @Test
    public void testToLocale() {
        // typical use case: language code
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en", ""), locale1);

        // typical use case: language code and country code
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale2);

        // typical use case: language code, country code, and variant
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale3);

        // edge case: empty string
        Locale locale4 = LocaleUtils.toLocale("");
        Assert.assertNull(locale4);

        // error scenario: invalid format
        try {
            LocaleUtils.toLocale("en_gb");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    // test localeLookupList method
    @Test
    public void testLocaleLookupList() {
        // typical use case: locale with variant
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        Assert.assertEquals(4, list1.size());
        Assert.assertEquals(new Locale("fr", "CA", "xxx"), list1.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list1.get(1));
        Assert.assertEquals(new Locale("fr"), list1.get(2));
        Assert.assertEquals(Locale.getDefault(), list1.get(3));

        // typical use case: locale without variant
        Locale locale2 = new Locale("en");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2);
        Assert.assertEquals(2, list2.size());
        Assert.assertEquals(new Locale("en"), list2.get(0));
        Assert.assertEquals(Locale.getDefault(), list2.get(1));

        // edge case: null locale
        List<Locale> list3 = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, list3.size());

        // error scenario: null defaultLocale
        Locale locale4 = new Locale("en");
        try {
            LocaleUtils.localeLookupList(locale4, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }

    // test availableLocaleList method
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertTrue(list.contains(Locale.getDefault()));
    }

    // test availableLocaleSet method
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(set.contains(Locale.getDefault()));
    }

    // test isAvailableLocale method
    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        boolean available1 = LocaleUtils.isAvailableLocale(locale1);
        Assert.assertTrue(available1);

        Locale locale2 = new Locale("zz", "ZZ");
        boolean available2 = LocaleUtils.isAvailableLocale(locale2);
        Assert.assertFalse(available2);
    }

    // test languagesByCountry method
    @Test
    public void testLanguagesByCountry() {
        // typical use case: supported country
        List<Locale> langs1 = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(2, langs1.size());
        Assert.assertEquals(new Locale("en", "CA"), langs1.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), langs1.get(1));

        // typical use case: unsupported country
        List<Locale> langs2 = LocaleUtils.languagesByCountry("ZZ");
        Assert.assertEquals(0, langs2.size());

        // edge case: null country
        List<Locale> langs3 = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, langs3.size());
    }

    // test countriesByLanguage method
    @Test
    public void testCountriesByLanguage() {
        // typical use case: supported language
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(31, countries1.size());
        Assert.assertEquals(new Locale("fr", "BE"), countries1.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), countries1.get(1));

        // typical use case: unsupported language
        List<Locale> countries2 = LocaleUtils.countriesByLanguage("zz");
        Assert.assertEquals(0, countries2.size());

        // edge case: null language
        List<Locale> countries3 = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(0, countries3.size());
    }
}