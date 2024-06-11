import org.junit.*;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import java.util.*;
import org.apache.commons.lang3.StringUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ContainsHash() {
        LocaleUtils.toLocale("en#US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LengthLessThan2() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_FirstCharNotUnderscore() {
        LocaleUtils.toLocale("e_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_SplitOccurrences_0_Condition1() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_SplitOccurrences_0_Condition2() {
        LocaleUtils.toLocale("usa");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_SplitOccurrences_1_Condition1() {
        LocaleUtils.toLocale("en_USA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_SplitOccurrences_1_Condition2() {
        LocaleUtils.toLocale("usa_EN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_SplitOccurrences_2_Condition() {
        LocaleUtils.toLocale("e_n_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LengthLessThan3() {
        LocaleUtils.toLocale("_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_Chars2And3NotUpperCase() {
        LocaleUtils.toLocale("__us");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LengthLessThan5() {
        LocaleUtils.toLocale("__US_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_FourthCharNotUnderscore() {
        LocaleUtils.toLocale("__US_A");
    }

    @Test
    public void testLocaleLookupList_LocaleNotNull_DefaultLocaleNotNull() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = Arrays.asList(locale, new Locale("en"), new Locale("en", ""));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_LocaleNotNull_DefaultLocaleNull() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, null);
        List<Locale> expected = Arrays.asList(locale, new Locale("en"), new Locale("en", ""));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_LocaleNull_DefaultLocaleNotNull() {
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        List<Locale> expected = Arrays.asList(defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_BothLocalesNull() {
        List<Locale> result = LocaleUtils.localeLookupList(null, null);
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale() {
        Locale locale = new Locale("xyz", "ABC");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountry_Cached_LanguageExists() {
        String countryCode = "US";
        List<Locale> cachedLangs = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"));
        LocaleUtils.cLanguagesByCountry.put(countryCode, cachedLangs);

        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(cachedLangs, result);
    }

    @Test
    public void testLanguagesByCountry_Cached_LanguageNotExists() {
        String countryCode = "DE";
        LocaleUtils.cLanguagesByCountry.put(countryCode, null);

        List<Locale> locales = LocaleUtils.availableLocaleList();
        List<Locale> expected = new ArrayList<Locale>();
        for (int i = 0; i < locales.size(); ++i) {
            Locale locale = locales.get(i);
            if (countryCode.equals(locale.getCountry()) && locale.getVariant().isEmpty()) {
                expected.add(locale);
            }
        }
        expected = Collections.unmodifiableList(expected);
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, result);
        assertEquals(expected, LocaleUtils.cLanguagesByCountry.get(countryCode));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_Cached_LanguageExists() {
        String languageCode = "en";
        List<Locale> cachedCountries = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        LocaleUtils.cCountriesByLanguage.put(languageCode, cachedCountries);

        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(cachedCountries, result);
    }

    @Test
    public void testCountriesByLanguage_Cached_LanguageNotExists() {
        String languageCode = "es";
        LocaleUtils.cCountriesByLanguage.put(languageCode, null);

        List<Locale> locales = LocaleUtils.availableLocaleList();
        List<Locale> expected = new ArrayList<Locale>();
        for (int i = 0; i < locales.size(); ++i) {
            Locale locale = locales.get(i);
            if (languageCode.equals(locale.getLanguage()) && locale.getCountry().length() != 0 && locale.getVariant().isEmpty()) {
                expected.add(locale);
            }
        }
        expected = Collections.unmodifiableList(expected);
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, result);
        assertEquals(expected, LocaleUtils.cCountriesByLanguage.get(languageCode));
    }
}