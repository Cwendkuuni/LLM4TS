import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        Assert.assertNull(result);
    }

    @Test
    public void testToLocale_ValidLanguageCode_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en");
        Assert.assertEquals(Locale.ENGLISH, result);
    }

    @Test
    public void testToLocale_ValidLanguageAndCountryCode_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(Locale.UK, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("invalid_format");
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsCorrectList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));
        Assert.assertEquals(Locale.getDefault(), result.get(3));
    }

    @Test
    public void testLocaleLookupList_ValidLocaleWithDefaultLocale_ReturnsCorrectList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));
        Assert.assertEquals(defaultLocale, result.get(3));
    }

    @Test
    public void testAvailableLocaleList_ReturnsNonEmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnsNonEmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsLanguageList() {
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(Locale.FRENCH));
        Assert.assertTrue(result.contains(Locale.ENGLISH));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsCountryList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(6, result.size());
        Assert.assertTrue(result.contains(Locale.CANADA_FRENCH));
        Assert.assertTrue(result.contains(Locale.FRANCE));
        // Add more assertions for other expected locales
    }

}