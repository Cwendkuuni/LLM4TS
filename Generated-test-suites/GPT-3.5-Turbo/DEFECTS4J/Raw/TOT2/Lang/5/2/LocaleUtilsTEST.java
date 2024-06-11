import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        Locale expected = new Locale("en", "GB", "xxx");
        Locale result = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocale_nullInput() {
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("ENG_ABC");
    }

    @Test
    public void testLocaleLookupList_singleLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = Collections.singletonList(locale);
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_multipleLocales() {
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_notEmpty() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_notEmpty() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_true() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testLanguagesByCountry_validInput() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_validInput() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("fr", "FR"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expected, result);
    }
}