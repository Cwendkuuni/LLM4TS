import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test typical use case
        Locale locale1 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale1.getLanguage());
        Assert.assertEquals("GB", locale1.getCountry());

        // Test edge case with variant
        Locale locale2 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals("en", locale2.getLanguage());
        Assert.assertEquals("GB", locale2.getCountry());
        Assert.assertEquals("xxx", locale2.getVariant());

        // Test null input
        Locale locale3 = LocaleUtils.toLocale(null);
        Assert.assertNull(locale3);

        // Test invalid format
        try {
            LocaleUtils.toLocale("invalid");
            Assert.fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test typical use case
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1);
        Assert.assertEquals(locale1, list1.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list1.get(1));
        Assert.assertEquals(new Locale("fr"), list1.get(2));

        // Test edge case with default locale
        Locale locale2 = new Locale("en");
        Locale defaultLocale2 = new Locale("fr");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2, defaultLocale2);
        Assert.assertEquals(locale2, list2.get(0));
        Assert.assertEquals(defaultLocale2, list2.get(3));

        // Test null input
        List<Locale> list3 = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, list3.size());
    }

    @Test
    public void testAvailableLocaleList() {
        // Test that the available locale list is not null
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(list);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test that the available locale set is not null
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(set);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with available locale
        Locale locale1 = new Locale("en");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale1));

        // Test with unavailable locale
        Locale locale2 = new Locale("zz");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with existing country code
        List<Locale> langs1 = LocaleUtils.languagesByCountry("CA");
        Assert.assertTrue(langs1.contains(new Locale("fr", "CA")));
        Assert.assertTrue(langs1.contains(new Locale("en", "CA")));

        // Test with non-existing country code
        List<Locale> langs2 = LocaleUtils.languagesByCountry("ZZ");
        Assert.assertEquals(0, langs2.size());

        // Test null input
        List<Locale> langs3 = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, langs3.size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with existing language code
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("fr");
        Assert.assertTrue(countries1.contains(new Locale("fr", "CA")));
        Assert.assertTrue(countries1.contains(new Locale("fr", "FR")));

        // Test with non-existing language code
        List<Locale> countries2 = LocaleUtils.countriesByLanguage("zz");
        Assert.assertEquals(0, countries2.size());

        // Test null input
        List<Locale> countries3 = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(0, countries3.size());
    }

}