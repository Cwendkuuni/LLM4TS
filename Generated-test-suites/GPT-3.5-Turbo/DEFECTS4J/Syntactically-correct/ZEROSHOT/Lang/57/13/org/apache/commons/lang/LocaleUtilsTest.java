import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        try {
            LocaleUtils.toLocale("en ");
            Assert.fail("Invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("en_GB_");
            Assert.fail("Invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX");
            Assert.fail("Invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List localeList1 = LocaleUtils.localeLookupList(locale1);
        Assert.assertEquals(3, localeList1.size());
        Assert.assertEquals(locale1, localeList1.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), localeList1.get(1));
        Assert.assertEquals(new Locale("fr"), localeList1.get(2));

        Locale locale2 = new Locale("en");
        Locale defaultLocale = new Locale("fr");
        List localeList2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        Assert.assertEquals(4, localeList2.size());
        Assert.assertEquals(locale2, localeList2.get(0));
        Assert.assertEquals(new Locale("fr", "CA", "xxx"), localeList2.get(1));
        Assert.assertEquals(new Locale("fr", "CA"), localeList2.get(2));
        Assert.assertEquals(defaultLocale, localeList2.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocaleList);
        Assert.assertEquals(Locale.getAvailableLocales().length, availableLocaleList.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocaleSet);
        Assert.assertEquals(Locale.getAvailableLocales().length, availableLocaleSet.size());
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry() {
        List languagesByCountry = LocaleUtils.languagesByCountry("US");
        Assert.assertNotNull(languagesByCountry);
        Assert.assertEquals(1, languagesByCountry.size());
        Assert.assertEquals(Locale.US, languagesByCountry.get(0));

        List languagesByInvalidCountry = LocaleUtils.languagesByCountry("XX");
        Assert.assertNotNull(languagesByInvalidCountry);
        Assert.assertEquals(0, languagesByInvalidCountry.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(countriesByLanguage);
        Assert.assertEquals(1, countriesByLanguage.size());
        Assert.assertEquals(Locale.US, countriesByLanguage.get(0));

        List countriesByInvalidLanguage = LocaleUtils.countriesByLanguage("xx");
        Assert.assertNotNull(countriesByInvalidLanguage);
        Assert.assertEquals(0, countriesByInvalidLanguage.size());
    }

}