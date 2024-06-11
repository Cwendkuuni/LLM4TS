import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr", "fr_CA", "fr_CA_FR"};
        for (String localeString : testCases) {
            Locale expectedLocale = new Locale(localeString.substring(0, 2), localeString.substring(3));
            if (localeString.length() > 5) {
                expectedLocale = new Locale(localeString.substring(0, 2), localeString.substring(3), localeString.substring(6));
            }
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            if (localeString.length() > 5) {
                assertEquals(expectedLocale.getVariant(), actualLocale.getVariant());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_abc");
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("en", "US");
        List availableLocales = LocaleUtils.availableLocaleList();
        List expectedLocaleList = LocaleUtils.localeLookupList(baseLocale, new Locale("fr"));
        List actualLocaleList = LocaleUtils.localeLookupList(baseLocale, new Locale("fr"));
        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            Locale expectedLocale = (Locale) expectedLocaleList.get(i);
            Locale actualLocale = (Locale) actualLocaleList.get(i);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            if (expectedLocale.getVariant().length() > 0) {
                assertEquals(expectedLocale.getVariant(), actualLocale.getVariant());
            }
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocales = LocaleUtils.availableLocaleList();
        assertTrue(availableLocales.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        Locale invalidLocale = new Locale("en", "CA");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List expectedLocales = LocaleUtils.languagesByCountry("US");
        List actualLocales = LocaleUtils.languagesByCountry("US");
        assertEquals(expectedLocales.size(), actualLocales.size());
        for (int i = 0; i < expectedLocales.size(); i++) {
            Locale expectedLocale = (Locale) expectedLocales.get(i);
            Locale actualLocale = (Locale) actualLocales.get(i);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
        }

        List emptyLocales = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.EMPTY_LIST, emptyLocales);
    }

    @Test
    public void testCountriesByLanguage() {
        List expectedLocales = LocaleUtils.countriesByLanguage("en");
        List actualLocales = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedLocales.size(), actualLocales.size());
        for (int i = 0; i < expectedLocales.size(); i++) {
            Locale expectedLocale = (Locale) expectedLocales.get(i);
            Locale actualLocale = (Locale) actualLocales.get(i);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
        }

        List emptyLocales = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.EMPTY_LIST, emptyLocales);
    }

}