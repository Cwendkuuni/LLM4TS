import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA"};
        for (String testCase : testCases) {
            Locale expected = new Locale(testCase.substring(0, 2), testCase.substring(3, testCase.length()));
            Locale actual = LocaleUtils.toLocale(testCase);
            assertEquals(expected.getDisplayName(), actual.getDisplayName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_xxx");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "CA");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "CA");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        Locale invalidLocale = new Locale("fr", "CA", "CA");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedList = Arrays.asList(new Locale("en", countryCode), new Locale("fr", countryCode));
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expectedList = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "US"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);
    }
}