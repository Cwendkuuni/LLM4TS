import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale locale1 = LocaleUtils.toLocale("en_US");
        Assert.assertEquals("en", locale1.getLanguage());
        Assert.assertEquals("US", locale1.getCountry());

        // Edge case: empty string input
        Locale locale2 = LocaleUtils.toLocale("");
        Assert.assertEquals("", locale2.getLanguage());
        Assert.assertEquals("", locale2.getCountry());

        // Error scenario: Invalid locale format
        try {
            LocaleUtils.toLocale("#Invalid");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case: Default locale is same as input locale
        Locale locale1 = new Locale("en", "US");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1, locale1);
        Assert.assertEquals(4, list1.size());
        Assert.assertEquals(locale1, list1.get(0));
        Assert.assertEquals(new Locale("en", ""), list1.get(1));
        Assert.assertEquals(new Locale("", "US"), list1.get(2));
        Assert.assertEquals(locale1, list1.get(3));

        // Typical use case: Default locale is different from input locale
        Locale locale2 = new Locale("es", "ES");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2, locale1);
        Assert.assertEquals(4, list2.size());
        Assert.assertEquals(locale2, list2.get(0));
        Assert.assertEquals(new Locale("es", ""), list2.get(1));
        Assert.assertEquals(new Locale("", "ES"), list2.get(2));
        Assert.assertEquals(locale1, list2.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertEquals(Locale.getAvailableLocales().length, list.size());
        for (Locale locale : list) {
            Assert.assertNotNull(locale);
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(Locale.getAvailableLocales().length, set.size());
        for (Locale locale : set) {
            Assert.assertNotNull(locale);
        }
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        boolean isAvailable1 = LocaleUtils.isAvailableLocale(locale1);
        Assert.assertTrue(isAvailable1);

        Locale locale2 = new Locale("xyz", "XYZ");
        boolean isAvailable2 = LocaleUtils.isAvailableLocale(locale2);
        Assert.assertFalse(isAvailable2);
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case: valid country code with matching locales
        List<Locale> list1 = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(1, list1.size());
        Assert.assertEquals("en", list1.get(0).getLanguage());
        Assert.assertEquals("US", list1.get(0).getCountry());

        // Edge case: Empty country code
        List<Locale> list2 = LocaleUtils.languagesByCountry("");
        Assert.assertEquals(0, list2.size());

        // Error scenario: Invalid country code
        List<Locale> list3 = LocaleUtils.languagesByCountry("XYZ");
        Assert.assertEquals(0, list3.size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case: valid language code with matching locales
        List<Locale> list1 = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(1, list1.size());
        Assert.assertEquals("en", list1.get(0).getLanguage());
        Assert.assertEquals("US", list1.get(0).getCountry());

        // Typical use case: valid language code with no matching locales
        List<Locale> list2 = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(0, list2.size());

        // Edge case: Empty language code
        List<Locale> list3 = LocaleUtils.countriesByLanguage("");
        Assert.assertEquals(0, list3.size());

        // Error scenario: Invalid language code
        List<Locale> list4 = LocaleUtils.countriesByLanguage("XYZ");
        Assert.assertEquals(0, list4.size());
    }
}