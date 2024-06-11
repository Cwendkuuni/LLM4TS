import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        String localeStr = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeStr);
        assertEquals(expectedLocale, actualLocale);

        // Edge case testing
        String invalidLocaleStr1 = "en";
        assertNull(LocaleUtils.toLocale(invalidLocaleStr1));

        String invalidLocaleStr2 = "en_US_";
        assertNull(LocaleUtils.toLocale(invalidLocaleStr2));

        String invalidLocaleStr3 = "en_US_xxx";
        assertNull(LocaleUtils.toLocale(invalidLocaleStr3));

        String invalidLocaleStr4 = "en_US_xx";
        assertNull(LocaleUtils.toLocale(invalidLocaleStr4));

        String invalidLocaleStr5 = "en_US_x";
        assertNull(LocaleUtils.toLocale(invalidLocaleStr5));

        // Exception handling testing
        String nullInputStr = null;
        assertNull(LocaleUtils.toLocale(nullInputStr));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case testing
        Locale emptyLocale = new Locale("");
        List<Locale> emptyLocaleList = Arrays.asList(new Locale(""));
        List<Locale> actualEmptyLocaleList = LocaleUtils.localeLookupList(emptyLocale, new Locale("en"));
        assertEquals(expectedLocaleList.subList(0, 1), actualEmptyLocaleList);

        Locale noCountryLocale = new Locale("en");
        List<Locale> noCountryLocaleList = Arrays.asList(new Locale("en"));
        List<Locale> actualNoCountryLocaleList = LocaleUtils.localeLookupList(noCountryLocale, new Locale("en"));
        assertEquals(expectedLocaleList, actualNoCountryLocaleList);

        Locale noLanguageLocale = new Locale("");
        List<Locale> noLanguageLocaleList = Arrays.asList(new Locale(""));
        List<Locale> actualNoLanguageLocaleList = LocaleUtils.localeLookupList(new Locale(""), new Locale("en"));
        assertEquals(expectedLocaleList, actualNoLanguageLocaleList);

        // Exception handling testing
        Locale nullInputLocale = null;
        assertNull(LocaleUtils.localeLookupList(nullInputLocale, new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case testing
        List<Locale> emptyLocaleList = Collections.emptyList();
        assertTrue(expectedLocaleList.isEmpty());
        assertTrue(emptyLocaleList.isEmpty());

        // Exception handling testing
        List<Locale> nullInputLocaleList = null;
        assertNull(LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> expectedLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expectedLocaleSet, actualLocaleSet);

        // Edge case testing
        Set<Locale> emptyLocaleSet = Collections.emptySet();
        assertTrue(expectedLocaleSet.isEmpty());
        assertTrue(emptyLocaleSet.isEmpty());

        // Exception handling testing
        Set<Locale> nullInputLocaleSet = null;
        assertNull(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge case testing
        Locale invalidLocale = new Locale("en", "US_xxx");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));

        // Exception handling testing
        Locale nullInputLocale = null;
        assertFalse(LocaleUtils.isAvailableLocale(nullInputLocale));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountryWithInvalidInput() {
        String invalidCountryCode = "invalidCountryCode";
        LocaleUtils.languagesByCountry(invalidCountryCode);
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "US";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "CA"),
                new Locale("en", "GB"),
                new Locale("en", "IE"),
                new Locale("en", "AU"),
                new Locale("en", "NZ")
        );
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case testing
        String emptyCountryCode = "";
        List<Locale> emptyCountryCodeLocaleList = Collections.emptyList();
        List<Locale> actualEmptyCountryCodeLocaleList = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertEquals(Collections.emptyList(), actualEmptyCountryCodeLocaleList);

        // Exception handling testing
        String nullInputCountryCode = null;
        assertNull(LocaleUtils.languagesByCountry(nullInputCountryCode));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguageWithInvalidInput() {
        String invalidLanguageCode = "invalidLanguageCode";
        LocaleUtils.countriesByLanguage(invalidLanguageCode);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "CA"),
                new Locale("en", "GB"),
                new Locale("en", "IE"),
                new Locale("en", "AU"),
                new Locale("en", "NZ"),
                new Locale("en", "ZA"),
                new Locale("en", "JM"),
                new Locale("en", "TT"),
                new Locale("en", "BM"),
                new Locale("en", "CG"),
                new Locale("en", "VC"),
                new Locale("en", "VG"),
                new Locale("en", "SH"),
                new Locale("en", "IE"),
                new Locale("en", "IM"),
                new Locale("en", "GG"),
                new Locale("en", "GI"),
                new Locale("en", "JE"),
                new Locale("en", "GG"),
                new Locale("en", "GG"),
                new Locale("en", "GG")
        );
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case testing
        String emptyLanguageCode = "";
        List<Locale> emptyLanguageCodeLocaleList = Collections.emptyList();
        List<Locale> actualEmptyLanguageCodeLocaleList = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertEquals(Collections.emptyList(), actualEmptyLanguageCodeLocaleList);

        // Exception handling testing
        String nullInputLanguageCode = null;
        assertNull(LocaleUtils.countriesByLanguage(nullInputLanguageCode));
    }

}