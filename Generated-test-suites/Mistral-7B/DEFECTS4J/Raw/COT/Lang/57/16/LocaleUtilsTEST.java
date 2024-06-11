import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertEquals(expectedLocale, actualLocale);

        // Test invalid input
        String invalidLocaleString1 = null;
        Assert.assertNull(LocaleUtils.toLocale(invalidLocaleString1));

        String invalidLocaleString2 = "ab_CD_EF";
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString2));

        String invalidLocaleString3 = "en_GB_xyz";
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString3));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test empty list
        Locale emptyLocale = null;
        List<Locale> actualEmptyLocaleList = LocaleUtils.localeLookupList(emptyLocale, new Locale("en"));
        Assert.assertEquals(Collections.emptyList(), actualEmptyLocaleList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Locale.getAvailableLocales();
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        Assert.assertEquals(emptyLocaleList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test
        Locale availableLocale = new Locale("en", "US");
        boolean expectedResult = true;
        boolean actualResult = LocaleUtils.isAvailableLocale(availableLocale);
        Assert.assertEquals(expectedResult, actualResult);

        // Test unavailable locale
        Locale unavailableLocale = new Locale("xy", "XY");
        expectedResult = false;
        actualResult = LocaleUtils.isAvailableLocale(unavailableLocale);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test
        String countryCode = "US";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "CA"),
                new Locale("en", "GB"),
                new Locale("es", "US"),
                new Locale("fr", "CA"),
                new Locale("fr", "BE"),
                new Locale("de", "DE")
        );
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test empty list
        String emptyCountryCode = null;
        List<Locale> actualEmptyLocaleList = LocaleUtils.languagesByCountry(emptyCountryCode);
        Assert.assertEquals(Collections.emptyList(), actualEmptyLocaleList);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "CA"),
                new Locale("en", "GB"),
                new Locale("en", "IE"),
                new Locale("en", "JM"),
                new Locale("en", "ZA"),
                new Locale("en", "SG"),
                new Locale("en", "TT"),
                new Locale("en", "GB-JE"),
                new Locale("en", "HK"),
                new Locale("en", "IE-GB")
        );
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test empty list
        String emptyLanguageCode = null;
        List<Locale> actualEmptyLocaleList = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        Assert.assertEquals(Collections.emptyList(), actualEmptyLocaleList);
    }
}