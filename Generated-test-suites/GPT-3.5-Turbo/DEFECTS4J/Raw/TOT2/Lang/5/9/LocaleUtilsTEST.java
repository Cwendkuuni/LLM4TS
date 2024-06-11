import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        String str = "en_US";
        Locale expected = new Locale("en", "US");
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocale_languageOnly() {
        String str = "en";
        Locale expected = new Locale("en", "");
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocale_invalidFormat() {
        String str = "en_US_xyz";
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList_basic() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", ""), new Locale("fr"), new Locale("en"));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_nullLocale() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
    }

    @Test
    public void testIsAvailableLocale_true() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_false() {
        Locale locale = new Locale("xx", "YY");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        Assert.assertNotNull(result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(result);
    }
}