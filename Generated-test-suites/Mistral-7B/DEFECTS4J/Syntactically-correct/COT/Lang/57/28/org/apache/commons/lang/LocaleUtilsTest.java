import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] validLocaleStrings = { "en", "en_GB", "en_GB_US" };
        for (String str : validLocaleStrings) {
            Locale expectedLocale = new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6, str.length()));
            Locale actualLocale = LocaleUtils.toLocale(str);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            assertEquals(expectedLocale.getVariant(), actualLocale.getVariant());
        }

        // Test invalid locale strings
        String[] invalidLocaleStrings = { "", "en_", "en_GB_", "en_GB_xxx_US", "en_GB_US_xxx", "en_GB_US_xxx_", "en_GB_US_xxx_xxx" };
        for (String str : invalidLocaleStrings) {
            try {
                LocaleUtils.toLocale(str);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "XX");
        Locale defaultLocale = new Locale("en", "US");

        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA", "XX"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en", "US"));
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);

        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i).getLanguage(), actualLocaleList.get(i).getLanguage());
            assertEquals(expectedLocaleList.get(i).getCountry(), actualLocaleList.get(i).getCountry());
            assertEquals(expectedLocaleList.get(i).getVariant(), actualLocaleList.get(i).getVariant());
        }

        // Test empty list
        Locale emptyLocale = null;
        List<Locale> emptyLocaleList = LocaleUtils.localeLookupList(emptyLocale, defaultLocale);
        assertEquals(0, emptyLocaleList.size());

        // Test null defaultLocale
        List<Locale> nullDefaultLocaleList = LocaleUtils.localeLookupList(baseLocale, null);
        assertEquals(1, nullDefaultLocaleList.size());
        assertEquals(baseLocale.getLanguage(), nullDefaultLocaleList.get(0).getLanguage());
        assertEquals(baseLocale.getCountry(), nullDefaultLocaleList.get(0).getCountry());
        assertEquals(baseLocale.getVariant(), nullDefaultLocaleList.get(0).getVariant());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Locale.getAvailableLocales();

        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i).getLanguage(), actualLocaleList.get(i).getLanguage());
            assertEquals(expectedLocaleList.get(i).getCountry(), actualLocaleList.get(i).getCountry());
            assertEquals(expectedLocaleList.get(i).getVariant(), actualLocaleList.get(i).getVariant());
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocaleSet = new HashSet<>(Locale.getAvailableLocales());

        assertEquals(expectedLocaleSet.size(), actualLocaleSet.size());
        for (Locale locale : expectedLocaleSet) {
            assertTrue(actualLocaleSet.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> locales = Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), new Locale("es"), new Locale("es_ES"));
        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale invalidLocale = new Locale("fr", "CA", "XXX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);

        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i).getLanguage(), actualLocaleList.get(i).getLanguage());
            assertEquals(expectedLocaleList.get(i).getCountry(), actualLocaleList.get(i).getCountry());
            assertEquals(expectedLocaleList.get(i).getVariant(), actualLocaleList.get(i).getVariant());
        }

        // Test null countryCode
        List<Locale> nullCountryCodeList = LocaleUtils.languagesByCountry(null);
        assertEquals(0, nullCountryCodeList.size());
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);

        assertEquals(expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i).getLanguage(), actualLocaleList.get(i).getLanguage());
            assertEquals(expectedLocaleList.get(i).getCountry(), actualLocaleList.get(i).getCountry());
            assertEquals(expectedLocaleList.get(i).getVariant(), actualLocaleList.get(i).getVariant());
        }

        // Test null languageCode
        List<Locale> nullLanguageCodeList = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, nullLanguageCodeList.size());
    }
}