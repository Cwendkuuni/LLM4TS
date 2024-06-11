import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en", ""), locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = LocaleUtils.toLocale("fr_CA_xxx");

        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_withDefaultLocale() {
        Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
        Locale defaultLocale = LocaleUtils.toLocale("en");

        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(expectedList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(expectedSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailable);
    }

    @Test
    public void testIsAvailableLocale_notAvailable() {
        Locale locale = new Locale("xx", "XX");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(expectedList);
    }

    @Test
    public void testLanguagesByCountry_nullInput() {
        List<Locale> expectedList = LocaleUtils.languagesByCountry(null);
        Assert.assertNotNull(expectedList);
        Assert.assertTrue(expectedList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = LocaleUtils.countriesByLanguage("fr");
        Assert.assertNotNull(expectedList);
    }

    @Test
    public void testCountriesByLanguage_nullInput() {
        List<Locale> expectedList = LocaleUtils.countriesByLanguage(null);
        Assert.assertNotNull(expectedList);
        Assert.assertTrue(expectedList.isEmpty());
    }
}