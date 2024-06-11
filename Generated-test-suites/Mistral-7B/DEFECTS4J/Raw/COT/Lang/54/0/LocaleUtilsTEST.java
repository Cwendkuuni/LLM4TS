import org.junit.Test;
import org.junit.Assert.*;
import java.util.Locale;
import static org.apache.commons.lang.LocaleUtils.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "es_ES", "ja_JP", null};
        Locale[] expectedLocales = new Locale[] {new Locale("en", ""), new Locale("en", "GB"), new Locale("en", "GB", "US"), new Locale("fr", "FR"), new Locale("es", "ES"), new Locale("ja", "JP"), null};

        for (int i = 0; i < testCases.length; i++) {
            Locale actualLocale = toLocale(testCases[i]);
            Assert.assertEquals("Invalid locale conversion for test case: " + testCases[i], expectedLocales[i], actualLocale);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        toLocale("xx_XX_XXX");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> testCases = new ArrayList<Locale>() {{
            add(new Locale("fr", "CA", "xxx"));
            add(new Locale("fr", "CA"));
            add(new Locale("fr"));
            add(new Locale("en"));
        }};

        List<Locale> expectedLocalesList = new ArrayList<Locale>() {{
            add(new Locale("fr", "CA", "xxx"));
            add(new Locale("fr", "CA"));
            add(new Locale("fr"));
            add(new Locale("en"));
        }};

        List<Locale> actualLocalesList = localeLookupList(testCases.get(0));
        Assert.assertEquals("Invalid locale lookup list for test case: " + testCases.get(0), expectedLocalesList, actualLocalesList);

        expectedLocalesList = new ArrayList<Locale>() {{
            add(new Locale("fr", "CA", "xxx"));
            add(new Locale("fr", "CA"));
            add(new Locale("fr"));
            add(new Locale("en"));
            add(new Locale("fr")); //defaultLocale
        }};
        actualLocalesList = localeLookupList(testCases.get(0), new Locale("en"));
        Assert.assertEquals("Invalid locale lookup list for test case: " + testCases.get(0) + " with defaultLocale: " + new Locale("en"), expectedLocalesList, actualLocalesList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actualLocaleList = availableLocaleList();
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals("Invalid available locales list", expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actualLocaleSet = availableLocaleSet();
        Set<Locale> expectedLocaleSet = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
        Assert.assertEquals("Invalid available locales set", expectedLocaleSet, actualLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("es", "ES");
        assertTrue("Locale " + locale1 + " should be available", isAvailableLocale(locale1));
        assertTrue("Locale " + locale2 + " should be available", isAvailableLocale(locale2));
        assertFalse("Locale null should not be available", isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedLocales = new ArrayList<Locale>() {{
            add(new Locale("fr", countryCode));
            add(new Locale("en", countryCode));
        }};
        List<Locale> actualLocales = languagesByCountry(countryCode);
        Assert.assertEquals("Invalid languages by country for country code: " + countryCode, expectedLocales, actualLocales);

        String countryCode2 = null;
        List<Locale> emptyLocaleList = new ArrayList<Locale>();
        List<Locale> actualLocales2 = languagesByCountry(countryCode2);
        Assert.assertEquals("Invalid languages by country for null country code", emptyLocaleList, actualLocales2);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedLocales = new ArrayList<Locale>() {{
            add(new Locale(languageCode, "CA"));
            add(new Locale("fr", "BE"));
        }};
        List<Locale> actualLocales = countriesByLanguage(languageCode);
        Assert.assertEquals("Invalid countries by language for language code: " + languageCode, expectedLocales, actualLocales);

        String languageCode2 = null;
        List<Locale> emptyLocaleList = new ArrayList<Locale>();
        List<Locale> actualLocales2 = countriesByLanguage(languageCode2);
        Assert.assertEquals("Invalid countries by language for null language code", emptyLocaleList, actualLocales2);
    }

}