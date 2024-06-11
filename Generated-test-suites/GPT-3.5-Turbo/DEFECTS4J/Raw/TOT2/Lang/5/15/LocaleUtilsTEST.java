import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        Locale expected = new Locale("en");
        Locale result = LocaleUtils.toLocale("en");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocale_countryVariant() {
        Locale expected = new Locale("en", "GB");
        Locale result = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocale_variant() {
        Locale expected = new Locale("en", "GB", "xxx");
        Locale result = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("english");
    }

    @Test
    public void testLocaleLookupList_localeOnly() {
        Locale locale = new Locale("fr");
        List<Locale> expected = Collections.singletonList(locale);
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_countryVariant() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale_true() {
        Locale locale = new Locale("fr");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_false() {
        Locale locale = new Locale("xx");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_basic() {
        List<Locale> expected = Collections.singletonList(new Locale("en", "US"));
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_basic() {
        List<Locale> expected = Collections.singletonList(new Locale("US"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expected, result);
    }

}