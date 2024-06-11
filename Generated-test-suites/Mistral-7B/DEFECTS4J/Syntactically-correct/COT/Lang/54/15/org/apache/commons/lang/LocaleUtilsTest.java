import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] validLocales = {"en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA"};
        for (String locale : validLocales) {
            Locale expectedLocale = new Locale(locale.split("_")[0], locale.split("_")[1]);
            Locale actualLocale = LocaleUtils.toLocale(locale);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            if (locale.split("_").length > 2) {
                assertEquals(locale.split("_")[2], actualLocale.getVariant());
            } else {
                assertNull(actualLocale.getVariant());
            }
        }

        String invalidLocale = "ab_CD_XXX"; // invalid locale format
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "CA");

        List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> expectedLocaleList = Arrays.asList(
                baseLocale,
                new Locale(baseLocale.getLanguage(), baseLocale.getCountry()),
                new Locale(baseLocale.getLanguage(), ""),
                defaultLocale
        );
        assertEquals(expectedLocaleList, localeList);

        localeList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        List<Locale> expectedLocaleList2 = Arrays.asList(
                baseLocale,
                new Locale(baseLocale.getLanguage(), baseLocale.getCountry()),
                new Locale(baseLocale.getLanguage(), ""),
                defaultLocale
        );
        assertEquals(expectedLocaleList2, localeList);

        List<Locale> emptyLocaleList = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), emptyLocaleList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        Set<String> expectedLocaleNames = new HashSet<>(Locale.getISOCountries());
        expectedLocaleNames.addAll(Locale.getISOLanguages());

        for (Locale locale : availableLocaleList) {
            String localeName = locale.toString();
            assertTrue(expectedLocaleNames.contains(localeName));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        Locale validLocale = new Locale("en", "US");
        Locale invalidLocale = new Locale("xx", "XX"); // unknown locale

        assertTrue(LocaleUtils.isAvailableLocale(validLocale));
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFR = LocaleUtils.languagesByCountry("FR");
        List<Locale> expectedLanguagesByCountryFR = Arrays.asList(
                new Locale("fr", "FR"),
                new Locale("fr", "BE"),
                new Locale("fr", "CA"),
                new Locale("fr", "CH"),
                new Locale("fr", "LU")
        );
        assertEquals(expectedLanguagesByCountryFR, languagesByCountryFR);

        List<Locale> emptyLanguagesByCountry = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), emptyLanguagesByCountry);
    }

    @Test
    public void testCountriesByLanguageEN() {
        List<Locale> countriesByLanguageEN = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguageEN = Arrays.asList(
                new Locale("en", "AG"),
                new Locale("en", "AI"),
                new Locale("en", "AS"),
                new Locale("en", "AU"),
                new Locale("en", "BB"),
                new Locale("en", "BE"),
                new Locale("en", "BM"),
                new Locale("en", "BS"),
                new Locale("en", "BW"),
                new Locale("en", "BZ"),
                new Locale("en", "CA"),
                new Locale("en", "CC"),
                new Locale("en", "CX"),
                new Locale("en", "DE"), // Germany
                new Locale("en", "DM"),
                new Locale("en", "DO"),
                new Locale("en", "EC"),
                new Locale("en", "EE"),
                new Locale("en", "EG"),
                new Locale("en", "FJ"),
                new Locale("en", "FK"),
                new Locale("en", "GB"),
                new Locale("en", "GD"),
                new Locale("en", "GG"),
                new Locale("en", "HK"),
                new Locale("en", "IE"),
                new Locale("en", "IM"),
                new Locale("en", "JE"),
                new Locale("en", "JM"),
                new Locale("en", "KE"),
                new Locale("en", "KI"),
                new Locale("en", "KM"),
                new Locale("en", "LS"),
                new Locale("en", "MG"),
                new Locale("en", "MH"),
                new Locale("en", "MO"),
                new Locale("en", "MP"),
                new Locale("en", "MS"),
                new Locale("en", "MT"),
                new Locale("en", "MU"),
                new Locale("en", "MY"),
                new Locale("en", "NA"),
                new Locale("en", "NF"),
                new Locale("en", "NG"),
                new Locale("en", "NR"),
                new Locale("en", "NU"),
                new Locale("en", "NZ"),
                new Locale("en", "PG"),
                new Locale("en", "PK"),
                new Locale("en", "PN"),
                new Locale("en", "SB"),
                new Locale("en", "SC"),
                new Locale("en", "SE"),
                new Locale("en", "SG"),
                new Locale("en", "SH"),
                new Locale("en", "SK"),
                new Locale("en", "SL"),
                new Locale("en", "SS"),
                new Locale("en", "STO"),
                new Locale("en", "TC"),
                new Locale("en", "TO"),
                new Locale("en", "TT"),
                new Locale("en", "TZ"),
                new Locale("en", "UG"),
                new Locale("en", "US"),
                new Locale("en", "VC"),
                new Locale("en", "VG"),
                new Locale("en", "VI"),
                new Locale("en", "WS"),
                new Locale("en", "ZA"),
                new Locale("en", "ZM"),
                new Locale("en", "ZW"),
                new Locale("en_AG"),
                new Locale("en_AI"),
                new Locale("en_AS"),
                new Locale("en_AU"),
                new Locale("en_BB"),
                new Locale("en_BM"),
                new Locale("en_BS"),
                new Locale("en_BW"),
                new Locale("en_BZ"),
                new Locale("en_CA"),
                new Locale("en_CC"),
                new Locale("en_CX"),
                new Locale("en_DM"),
                new Locale("en_DO"),
                new Locale("en_EC"),
                new Locale("en_EE"),
                new Locale("en_EG"),
                new Locale("en_FJ"),
                new Locale("en_FK"),
                new Locale("en_GD"),
                new Locale("en_GG"),
                new Locale("en_HK"),
                new Locale("en_IE"),
                new Locale("en_IM"),
                new Locale("en_JE"),
                new Locale("en_JM"),
                new Locale("en_KE"),
                new Locale("en_KI"),
                new Locale("en_KM"),
                new Locale("en_LS"),
                new Locale("en_MG"),
                new Locale("en_MH"),
                new Locale("en_MO"),
                new Locale("en_MP"),
                new Locale("en_MS"),
                new Locale("en_MT"),
                new Locale("en_MU"),
                new Locale("en_MY"),
                new Locale("en_NA"),
                new Locale("en_NF"),
                new Locale("en_NG"),
                new Locale("en_NR"),
                new Locale("en_NU"),
                new Locale("en_NZ"),
                new Locale("en_PG"),
                new Locale("en_PK"),
                new Locale("en_PN"),
                new Locale("en_SB"),
                new Locale("en_SC"),
                new Locale("en_SE"),
                new Locale("en_SG"),
                new Locale("en_SH"),
                new Locale("en_SK"),
                new Locale("en_SL"),
                new Locale("en_SS"),
                new Locale("en_STO"),
                new Locale("en_TC"),
                new Locale("en_TO"),
                new Locale("en_TT"),
                new Locale("en_TZ"),
                new Locale("en_UG"),
                new Locale("en_US"),
                new Locale("en_VC"),
                new Locale("en_VG"),
                new Locale("en_VI"),
                new Locale("en_WS"),
                new Locale("en_ZA"),
                new Locale("en_ZM"),
                new Locale("en_ZW")
        );
        assertEquals(expectedCountriesByLanguageEN, countriesByLanguageEN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguageUnknownLanguage() {
        List<Locale> countriesByLanguageXX = LocaleUtils.countriesByLanguage("xx"); // unknown language
    }
}