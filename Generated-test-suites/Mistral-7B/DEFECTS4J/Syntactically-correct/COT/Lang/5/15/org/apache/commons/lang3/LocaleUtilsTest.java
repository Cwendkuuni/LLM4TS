import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validLocale() {
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_emptyLocale() {
        String localeString = "";
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertNull(actualLocale);
    }

    @Test
    public void testToLocale_invalidLocale() {
        String localeString = "aaa_bbb";
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertNull(actualLocale);
    }

    @Test
    public void testLocaleLookupList_validLocale() {
        String baseLocaleString = "fr_CA";
        Locale baseLocale = new Locale("fr", "CA");
        Locale variantLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> expectedLocaleList = Arrays.asList(variantLocale, baseLocale, baseLocale, defaultLocale);
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testLocaleLookupList_emptyLocale() {
        Locale locale = null;
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(locale, null);
        Assert.assertEquals(Collections.emptyList(), actualLocaleList);
    }

    @Test
    public void testLocaleLookupList_defaultLocale() {
        String baseLocaleString = "fr_CA";
        Locale baseLocale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en");

        List<Locale> expectedLocaleList = Arrays.asList(baseLocale, defaultLocale);
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, null);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleList_empty() {
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(Collections.emptyList(), actualLocaleList);
    }

    @Test
    public void testAvailableLocaleList_nonEmpty() {
        List<Locale> expectedLocaleList = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testIsAvailableLocale_availableLocale() {
        String localeString = "en_US";
        Locale locale = new Locale("en", "US");
        boolean expectedResult = true;
        boolean actualResult = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsAvailableLocale_unavailableLocale() {
        String localeString = "aaa_bbb";
        Locale locale = new Locale("aaa", "bbb");
        boolean expectedResult = false;
        boolean actualResult = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        String countryCode = "US";
        List<Locale> expectedLocaleList = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_US, Locale.ENGLISH_GB);
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testLanguagesByCountry_emptyCountryCode() {
        String countryCode = "";
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(Collections.emptyList(), actualLocaleList);
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_US, Locale.ENGLISH_GB, Locale.ENGLISH_IE);
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testCountriesByLanguage_emptyLanguageCode() {
        String languageCode = "";
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(Collections.emptyList(), actualLocaleList);
    }

}