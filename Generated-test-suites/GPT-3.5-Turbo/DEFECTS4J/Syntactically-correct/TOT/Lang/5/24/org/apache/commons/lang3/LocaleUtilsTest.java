import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(Locale.ENGLISH, locale1);

        // Edge case: Minimum possible length
        Locale locale2 = LocaleUtils.toLocale("a_");
        Assert.assertEquals(new Locale("a", ""), locale2);

        // Edge case: JDK variant constructor behavior change
        Locale locale3 = LocaleUtils.toLocale("en_US_xxx");
        Assert.assertEquals(new Locale("en", "US", "xxx"), locale3);

        // Error scenario: Invalid format
        try {
            LocaleUtils.toLocale("abcd");
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(4, lookupList.size());
        Assert.assertEquals(locale, lookupList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        Assert.assertEquals(new Locale("fr", ""), lookupList.get(2));
        Assert.assertEquals(Locale.getDefault(), lookupList.get(3));

        // Edge case: Null input
        List<Locale> lookupList2 = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, lookupList2.size());
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocaleList);
        Assert.assertFalse(availableLocaleList.isEmpty());

        // Ensure list is unmodifiable
        try {
            availableLocaleList.add(Locale.CANADA);
            Assert.fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Exception expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocaleSet);
        Assert.assertFalse(availableLocaleSet.isEmpty());

        // Ensure set is unmodifiable
        try {
            availableLocaleSet.add(Locale.CANADA);
            Assert.fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Exception expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case: Locale is available
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Typical use case: Locale is not available
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz")));

        // Edge case: Null input
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(1, languagesByCountry.size());
        Assert.assertEquals(Locale.CANADA, languagesByCountry.get(0));

        // Edge case: Null input
        List<Locale> languagesByCountry2 = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, languagesByCountry2.size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(37, countriesByLanguage.size());
        Assert.assertTrue(countriesByLanguage.contains(Locale.CANADA));

        // Edge case: Null input
        List<Locale> countriesByLanguage2 = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(0, countriesByLanguage2.size());
    }
}