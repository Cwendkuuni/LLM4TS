import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Locale expectedLocale1 = new Locale("en", "");
        Locale actualLocale1 = LocaleUtils.toLocale("en");
        assertEquals(expectedLocale1, actualLocale1);

        Locale expectedLocale2 = new Locale("en", "GB");
        Locale actualLocale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(expectedLocale2, actualLocale2);

        Locale expectedLocale3 = new Locale("en", "GB", "xxx");
        Locale actualLocale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(expectedLocale3, actualLocale3);

        // Test invalid locale strings
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_CA_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_CA_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List localeList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);

        List expectedList = new ArrayList<>();
        expectedList.add(baseLocale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr", ""));
        expectedList.add(defaultLocale);

        assertEquals(expectedList, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(LocaleUtils.availableLocaleList().size(), 212);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertEquals(availableLocaleSet.size(), 212);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale2));

        Locale locale3 = new Locale("fr", "CA", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale3));
    }

    @Test
    public void testLanguagesByCountry() {
        List languagesByCountryFrance = LocaleUtils.languagesByCountry("FR");
        assertEquals(languagesByCountryFrance.size(), 11);

        String expectedLanguage1 = "fr";
        assertEquals(expectedLanguage1, languagesByCountryFrance.get(0).getLanguage());

        String expectedLanguage2 = "fr_BE";
        assertEquals(expectedLanguage2, languagesByCountryFrance.get(1).toString());

        String expectedLanguage3 = "fr_CH";
        assertEquals(expectedLanguage3, languagesByCountryFrance.get(2).toString());
    }

    @Test
    public void testCountriesByLanguage() {
        List countriesByLanguageEnglish = LocaleUtils.countriesByLanguage("EN");
        assertEquals(countriesByLanguageEnglish.size(), 27);

        String expectedCountry1 = "CA";
        assertEquals(expectedCountry1, countriesByLanguageEnglish.get(0).getCountry());

        String expectedCountry2 = "GB";
        assertEquals(expectedCountry2, countriesByLanguageEnglish.get(1).getCountry());

        String expectedCountry3 = "IE";
        assertEquals(expectedCountry3, countriesByLanguageEnglish.get(2).getCountry());
    }
}