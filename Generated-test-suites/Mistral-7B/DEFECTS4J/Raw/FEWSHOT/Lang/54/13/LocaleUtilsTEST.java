package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    @Parameterized.Parameters(name = "{index}: toLocale('{0}')")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"", null}, {"en", null}, {"en_GB", null}, {"en_GB_UK", null}, {"en_GB_UK_xxx", null},
                {"en_US", null}, {"fr", null}, {"fr_CA", null}, {"fr_CA_CA", null}, {"fr_CA_CA_xxx", null},
                {"de_DE", null}, {"es_ES", null}, {"es_ES_ES", null}, {"es_ES_ES_xxx", null},
                {"zh_CN", null}, {"ja_JP", null}, {"ko_KR", null}, {"ru_RU", null}, {"it_IT", null},
                {"pt_BR", null}, {"nl_NL", null}, {"sv_SE", null}, {"da_DK", null}, {"fi_FI", null},
                {"cs_CZ", null}, {"pl_PL", null}, {"hu_HU", null}, {"sk_SK", null}, {"ro_RO", null},
                {"bg_BG", null}, {"tr_TR", null}, {"el_GR", null}, {"he_IL", null}, {"ar_SA", null},
                {"fa_IR", null}, {"ur_PK", null}, {"hi_IN", null}, {"id_ID", null}, {"vi_VN", null},
                {"ko_KP", null}, {"ms_MY", null}, {"ta_IN", null}, {"th_TH", null}, {"et_EE", null},
                {"lt_LT", null}, {"lv_LV", null}, {"mk_MK", null}, {"mt_MT", null}, {"sq_AL", null},
                {"sr_RS", null}, {"uk_UA", null}, {"be_BY", null}, {"lv_LV_LV", null}, {"lt_LT_LT", null},
                {"lv_LV_LV_xxx", null}, {"lt_LT_LT_xxx", null}, {"lt_LT_LT_xx_xxx", null},
                {"en", DEFAULT_LOCALE}, {"en_GB", DEFAULT_LOCALE}, {"en_GB_UK", DEFAULT_LOCALE},
                {"en_GB_UK_xxx", DEFAULT_LOCALE}, {"en_US", DEFAULT_LOCALE}, {"fr", DEFAULT_LOCALE},
                {"fr_CA", DEFAULT_LOCALE}, {"fr_CA_CA", DEFAULT_LOCALE}, {"fr_CA_CA_xxx", DEFAULT_LOCALE},
                {"de_DE", DEFAULT_LOCALE}, {"es_ES", DEFAULT_LOCALE}, {"es_ES_ES", DEFAULT_LOCALE},
                {"es_ES_ES_xxx", DEFAULT_LOCALE}, {"zh_CN", DEFAULT_LOCALE}, {"ja_JP", DEFAULT_LOCALE},
                {"ko_KR", DEFAULT_LOCALE}, {"ru_RU", DEFAULT_LOCALE}, {"it_IT", DEFAULT_LOCALE},
                {"pt_BR", DEFAULT_LOCALE}, {"nl_NL", DEFAULT_LOCALE}, {"sv_SE", DEFAULT_LOCALE},
                {"da_DK", DEFAULT_LOCALE}, {"fi_FI", DEFAULT_LOCALE}, {"cs_CZ", DEFAULT_LOCALE},
                {"pl_PL", DEFAULT_LOCALE}, {"hu_HU", DEFAULT_LOCALE}, {"sk_SK", DEFAULT_LOCALE},
                {"ro_RO", DEFAULT_LOCALE}, {"bg_BG", DEFAULT_LOCALE}, {"tr_TR", DEFAULT_LOCALE},
                {"el_GR", DEFAULT_LOCALE}, {"he_IL", DEFAULT_LOCALE}, {"ar_SA", DEFAULT_LOCALE},
                {"fa_IR", DEFAULT_LOCALE}, {"ur_PK", DEFAULT_LOCALE}, {"hi_IN", DEFAULT_LOCALE},
                {"id_ID", DEFAULT_LOCALE}, {"vi_VN", DEFAULT_LOCALE}, {"ko_KP", DEFAULT_LOCALE},
                {"ms_MY", DEFAULT_LOCALE}, {"ta_IN", DEFAULT_LOCALE}, {"th_TH", DEFAULT_LOCALE},
                {"et_EE", DEFAULT_LOCALE}, {"lt_LT", DEFAULT_LOCALE}, {"lv_LV", DEFAULT_LOCALE},
                {"mk_MK", DEFAULT_LOCALE}, {"mt_MT", DEFAULT_LOCALE}, {"sq_AL", DEFAULT_LOCALE},
                {"sr_RS", DEFAULT_LOCALE}, {"uk_UA", DEFAULT_LOCALE}, {"be_BY", DEFAULT_LOCALE},
                {"lv_LV_LV", DEFAULT_LOCALE}, {"lt_LT_LT", DEFAULT_LOCALE}, {"lv_LV_LV_xxx", DEFAULT_LOCALE},
                {"lt_LT_LT_xxx", DEFAULT_LOCALE}, {"lt_LT_LT_xx_xxx", DEFAULT_LOCALE},
                {"en_GB_xxx", DEFAULT_LOCALE}, {"en_US_xxx", DEFAULT_LOCALE},
                {"fr_CA_CA_xxx", DEFAULT_LOCALE}, {"de_DE_xxx", DEFAULT_LOCALE},
                {"es_ES_ES_xxx", DEFAULT_LOCALE}, {"zh_CN_xxx", DEFAULT_LOCALE},
                {"ja_JP_xxx", DEFAULT_LOCALE}, {"ko_KR_xxx", DEFAULT_LOCALE},
                {"ru_RU_xxx", DEFAULT_LOCALE}, {"it_IT_xxx", DEFAULT_LOCALE},
                {"pt_BR_xxx", DEFAULT_LOCALE}, {"nl_NL_xxx", DEFAULT_LOCALE},
                {"sv_SE_xxx", DEFAULT_LOCALE}, {"da_DK_xxx", DEFAULT_LOCALE},
                {"fi_FI_xxx", DEFAULT_LOCALE}, {"cs_CZ_xxx", DEFAULT_LOCALE},
                {"pl_PL_xxx", DEFAULT_LOCALE}, {"hu_HU_xxx", DEFAULT_LOCALE},
                {"sk_SK_xxx", DEFAULT_LOCALE}, {"ro_RO_xxx", DEFAULT_LOCALE},
                {"bg_BG_xxx", DEFAULT_LOCALE}, {"tr_TR_xxx", DEFAULT_LOCALE},
                {"el_GR_xxx", DEFAULT_LOCALE}, {"he_IL_xxx", DEFAULT_LOCALE},
                {"ar_SA_xxx", DEFAULT_LOCALE}, {"fa_IR_xxx", DEFAULT_LOCALE},
                {"ur_PK_xxx", DEFAULT_LOCALE}, {"hi_IN_xxx", DEFAULT_LOCALE},
                {"id_ID_xxx", DEFAULT_LOCALE}, {"vi_VN_xxx", DEFAULT_LOCALE},
                {"ko_KP_xxx", DEFAULT_LOCALE}, {"ms_MY_xxx", DEFAULT_LOCALE},
                {"ta_IN_xxx", DEFAULT_LOCALE}, {"th_TH_xxx", DEFAULT_LOCALE},
                {"et_EE_xxx", DEFAULT_LOCALE}, {"lt_LT_xxx", DEFAULT_LOCALE},
                {"lv_LV_xxx", DEFAULT_LOCALE}, {"mk_MK_xxx", DEFAULT_LOCALE},
                {"mt_MT_xxx", DEFAULT_LOCALE}, {"sq_AL_xxx", DEFAULT_LOCALE},
                {"sr_RS_xxx", DEFAULT_LOCALE}, {"uk_UA_xxx", DEFAULT_LOCALE},
                {"be_BY_xxx", DEFAULT_LOCALE}, {"lv_LV_LV_xx_xxx", DEFAULT_LOCALE},
                {"lt_LT_LT_xx_xxx", DEFAULT_LOCALE}, {"lv_LV_LV_xxxx_xxxx", DEFAULT_LOCALE},
                {"lt_LT_LT_xxxxxxxxx", DEFAULT_LOCALE}, {"lt_LT_LT_xxxxxxxxxxxx", DEFAULT_LOCALE}
        });
    }

    private final String localeString;

    public LocaleUtilsTest(String localeString) {
        this.localeString = localeString;
    }

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale(localeString);
        if (locale == null) {
            assertNull(locale);
            return;
        }
        assertEquals(localeString.substring(0, 2), locale.getLanguage());
        if (localeString.length() > 2) {
            assertEquals(localeString.substring(3, 5), locale.getCountry());
            if (localeString.length() > 5) {
                assertEquals(localeString.substring(6), locale.getVariant());
            } else {
                assertNull(locale.getVariant());
            }
        } else {
            assertNull(locale.getCountry());
            assertNull(locale.getVariant());
        }
    }

    @Test
    public void testIsAvailableLocale() {
        for (Locale locale : localeLookupList(Locale.getDefault(), null)) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
        assertFalse(LocaleUtils.isAvailableLocale(Locale.NULL));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid", "locale")));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.forLanguageTag("en_GB"),
                Locale.forLanguageTag("en"), Locale.getDefault());
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.getDefault());
        assertSame(expected, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(locales.size(), Locale.getAvailableLocales().length);
        for (Locale locale : locales) {
            assertNotNull(locale);
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertEquals(locales.size(), localeSet.size());
        for (Locale locale : localeSet) {
            assertTrue(locales.contains(locale));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> locales = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(locales.size() > 0);
        for (Locale locale : locales) {
            assertNotNull(locale);
            assertEquals(countryCode, locale.getCountry());
            if (locale.getVariant().length() > 0) {
                assertFalse(locale.getLanguage().isEmpty());
            } else {
                assertTrue(locale.getLanguage().length() > 0);
            }
        }
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> locales = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(locales.size() > 0);
        for (Locale locale : locales) {
            assertNotNull(locale);
            assertEquals(languageCode, locale.getLanguage());
            if (locale.getCountry().length() > 0) {
                assertFalse(locale.getCountry().isEmpty());
            } else {
                assertTrue(locale.getCountry().isEmpty());
            }
        }
    }
}