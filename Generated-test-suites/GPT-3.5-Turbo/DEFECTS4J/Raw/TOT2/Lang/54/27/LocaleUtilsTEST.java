import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.lang.LocaleUtils;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_WithValidString() {
        String inputString = "en_GB";
        Locale expectedLocale = new Locale("en", "GB");
        Locale result = LocaleUtils.toLocale(inputString);
        Assert.assertEquals(expectedLocale, result);
    }

    @Test
    public void testToLocale_WithValidStringVariant() {
        String inputString = "en_GB_xxx";
        Locale expectedLocale = new Locale("en", "GB", "xxx");
        Locale result = LocaleUtils.toLocale(inputString);
        Assert.assertEquals(expectedLocale, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithInvalidString() {
        String inputString = "invalid_locale";
        LocaleUtils.toLocale(inputString);
    }

    @Test
    public void testLocaleLookupList_WithValidLocale() {
        Locale inputLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = List.of(new Locale("fr","CA","xxx"), new Locale("fr","CA"), new Locale("fr"));
        List<Locale> result = LocaleUtils.localeLookupList(inputLocale);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testLocaleLookupList_WithNullLocale() {
        List<Locale> expectedList = List.of(Locale.getDefault());
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = List.of(Locale.getAvailableLocales());
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(List.of(Locale.getAvailableLocales()));
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expectedSet, result);
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_WithUnavailableLocale() {
        Locale locale = new Locale("xx", "XX");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_WithValidCountryCode() {
        String countryCode = "CA";
        List<Locale> expectedList = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testLanguagesByCountry_WithNullCountryCode() {
        List<Locale> expectedList = Collections.EMPTY_LIST;
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testCountriesByLanguage_WithValidLanguageCode() {
        String languageCode = "fr";
        List<Locale> expectedList = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testCountriesByLanguage_WithNullLanguageCode() {
        List<Locale> expectedList = Collections.EMPTY_LIST;
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(expectedList, result);
    }
}