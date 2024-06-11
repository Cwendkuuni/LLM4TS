package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_XXX"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XXX"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XXX_YYY"));
        try {
            LocaleUtils.toLocale("en_GB_XXX_YYY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_GB-XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en-GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, localeList.get(0));
        assertSame(Locale.ENGLISH, localeList.get(1));
        assertSame(Locale.ENGLISH, localeList.get(2));

        List<Locale> localeListWithDefault = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(Locale.ENGLISH, localeList.get(0));
        assertSame(Locale.ENGLISH, localeList.get(1));
        assertSame(Locale.ENGLISH, localeList.get(2));
        assertEquals(Locale.FRENCH, localeList.get(3));

        List<Locale> localeListEmpty = LocaleUtils.localeLookupList(null);
        assertTrue(localeListEmpty.isEmpty());

        List<Locale> localeListNull = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertTrue(localeListNull.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<String> availableLocaleListStrings = new ArrayList<>();
        for (Locale locale : availableLocaleList) {
            availableLocaleListStrings.add(locale.toString());
        }
        List<String> expectedAvailableLocaleListStrings = Arrays.asList(
                "af_ZA", "am_ET", "ar_AE", "ar_BH", "ar_DZ", "ar_EG", "ar_IQ", "ar_JO", "ar_KW", "ar_LB", "ar_LY",
                "ar_MA", "ar_OM", "ar_QA", "ar_SA", "ar_SD", "ar_SY", "ar_TN", "ar_YE", "as_IN", "ast_ES", "az_AZ",
                "be_BY", "be_BY,BE", "bg_BG", "bm_ML", "bn_BD", "bn_IN", "bo_CN", "br_FR", "bs_BA", "ca_AD",
                "ca_ES", "ca_ES,es", "cs_CZ", "cy_GB", "da_DK", "de_AT", "de_CH", "de_DE", "de_LI", "dsb_CZ",
                "dv_MV", "dz_BT", "el_GR", "en_019", "en_150", "en_AG", "en_AI", "en_AS", "en_AU", "en_BB",
                "en_BM", "en_BW", "en_CA", "en_CC", "en_IE", "en_JE", "en_KE", "en_KI", "en_LC", "en_MG", "en_MH",
                "en_MO", "en_MP", "en_NF", "en_PG", "en_PN", "en_SB", "en_SC", "en_SS", "en_TZ", "en_UG", "en_UM",
                "en_VC", "en_ZA", "en_ZM", "eo_001", "es_AR", "es_BO", "es_CL", "es_CO", "es_CR", "es_CU", "es_DO",
                "es_EC", "es_ES", "es_GT", "es_HN", "es_MX", "es_NI", "es_PA", "es_PE", "es_PR", "es_PY", "es_SV",
                "es_US", "es_UY", "es_VE", "et_EE", "eu_ES", "fa_IR", "fi_FI", "fil_PF", "fo_FO", "fr_BE", "fr_CA",
                "fr_CH", "fr_FR", "fr_GF", "fr_LU", "fur_IT", "ga_IE", "gd_GB", "gl_ES", "gn_PY", "gu_IN", "gu_IN,in",
                "ha_GH", "ha_NG", "he_IL", "hi_IN", "hr_HR", "hu_HU", "hy_AM", "id_ID", "ig_NG", "is_IS", "it_CH",
                "it_IT", "ja_JP", "jv_ID", "ka_GE", "kk_KZ", "kl_GL", "km_KH", "kn_IN", "ko_KR", "ks_IN", "ku_TR",
                "ky_KG", "la_LA", "lb_LU", "lt_LT", "lv_LV", "mg_MG", "mi_NZ", "mk_MK", "ml_IN", "mn_MN", "mr_IN",
                "ms_BN", "ms_MY", "mt_MT", "my_MM", "na_NA", "ne_NP", "nl_BE", "nl_NL", "nl_SR", "nb_NO", "nb_NO,no",
                "ny_ZW", "oc_FR", "pl_PL", "pt_BR", "pt_PT", "qu_EC", "rm_CH", "ro_RO", "ru_RU", "si_LK", "sk_SK",
                "sl_SI", "so_DJ", "so_SO", "sq_AL", "sr_BA", "sr_CS", "sr_ME", "su_ID", "sv_FI", "sv_SE", "sw_KE",
                "sw_TZ", "syr_SY", "ta_IN", "te_IN", "tg_TJ", "th_TH", "tk_TM", "to_TO", "tr_TR", "ts_ST", "ts_ZA",
                "tt_RU", "ug_CN", "uk_UA", "ur_PK", "uz_UZ", "vi_VN", "vo_AD", "wa_BE", "xh_ZA", "yi_IL", "yo_BJ",
                "zh_CN", "zh_HK", "zh_SG", "zu_ZA"
        );
        assertArrayEquals(expectedAvailableLocaleListStrings.toArray(new String[0]),
                availableLocaleListStrings.toArray(new String[0]));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.GERMANY, new Locale("zz_ZZ")));
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        List<String> englishLocaleStrings = new ArrayList<>();
        for (Locale locale : englishLocales) {
            englishLocaleStrings.add(locale.toString());
        }
        List<String> expectedEnglishLocaleListStrings = Arrays.asList(
                "en_US", "en_US,us"
        );
        assertArrayEquals(expectedEnglishLocaleListStrings.toArray(new String[0]),
                englishLocaleStrings.toArray(new String[0]));

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
        List<String> frenchLocaleStrings = new ArrayList<>();
        for (Locale locale : frenchLocales) {
            frenchLocaleStrings.add(locale.toString());
        }
        List<String> expectedFrenchLocaleListStrings = Arrays.asList(
                "fr_CA", "fr_CA,ca"
        );
        assertArrayEquals(expectedFrenchLocaleListStrings.toArray(new String[0]),
                frenchLocaleStrings.toArray(new String[0]));

        List<Locale> emptyLocaleList = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyLocaleList.isEmpty());

        List<Locale> emptyLocaleListWithDefault = LocaleUtils.languagesByCountry(null, Locale.ENGLISH);
        assertTrue(emptyLocaleList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = LocaleUtils.countriesByLanguage("en");
        List<String> englishLocaleStrings = new ArrayList<>();
        for (Locale locale : englishLocales) {
            englishLocaleStrings.add(locale.toString());
        }
        List<String> expectedEnglishLocaleListStrings = Arrays.asList(
                "US", "GB", "IE", "CA", "AU", "NZ", "JM", "SG", "TT", "BE", "ZA"
        );
        assertArrayEquals(expectedEnglishLocaleListStrings.toArray(new String[0]),
                englishLocaleStrings.toArray(new String[0]));

        List<Locale> frenchLocales = LocaleUtils.countriesByLanguage("fr");
        List<String> frenchLocaleStrings = new ArrayList<>();
        for (Locale locale : frenchLocales) {
            frenchLocaleStrings.add(locale.toString());
        }
        List<String> expectedFrenchLocaleListStrings = Arrays.asList(
                "CA", "CH", "BE"
        );
        assertArrayEquals(expectedFrenchLocaleListStrings.toArray(new String[0]),
                frenchLocaleStrings.toArray(new String[0]));

        List<Locale> emptyLocaleList = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyLocaleList.isEmpty());

        List<Locale> emptyLocaleListWithDefault = LocaleUtils.countriesByLanguage(null, Locale.ENGLISH);
        assertTrue(emptyLocaleList.isEmpty());
    }

}