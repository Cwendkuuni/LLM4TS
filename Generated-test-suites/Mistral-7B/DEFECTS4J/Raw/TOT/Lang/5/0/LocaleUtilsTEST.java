package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private String input;
    private Locale expected;

    public LocaleUtilsTest(String input, Locale expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: toLocale(\"{0}\") = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"en", new Locale("en")},
                {"en_GB", new Locale("en", "GB")},
                {"en_GB_XXX", new Locale("en", "GB", "XXX")},
                {"fr_CA_XXX", new Locale("fr", "CA", "XXX")},
                {"fr", new Locale("fr")},
                {"fr_CA", new Locale("fr", "CA")},
                {"fr_CA_XX", new Locale("fr", "CA", "XX")},
                {"fr_FR", new Locale("fr", "FR")},
                {"fr_FR_XX", new Locale("fr", "FR", "XX")},
                {"es_ES_XXX", new Locale("es", "ES", "XXX")},
                {"es", new Locale("es")},
                {"es_ES", new Locale("es", "ES")},
                {"es_ES_XX", new Locale("es", "ES", "XX")},
                {"es_ES_XX_XX", new Locale("es", "ES", "XX", "XX")},
                {"it", new Locale("it")},
                {"it_IT", new Locale("it", "IT")},
                {"it_IT_XX", new Locale("it", "IT", "XX")},
                {"it_CH", new Locale("it", "CH")},
                {"it_CH_XX", new Locale("it", "CH", "XX")},
                {"de", new Locale("de")},
                {"de_DE", new Locale("de", "DE")},
                {"de_DE_XX", new Locale("de", "DE", "XX")},
                {"de_AT", new Locale("de", "AT")},
                {"de_AT_XX", new Locale("de", "AT", "XX")},
                {"de_AT_AT", new Locale("de", "AT", "AT")},
                {"ru", new Locale("ru")},
                {"ru_RU", new Locale("ru", "RU")},
                {"ru_RU_XX", new Locale("ru", "RU", "XX")},
                {"ru_UA", new Locale("ru", "UA")},
                {"ru_UA_XX", new Locale("ru", "UA", "XX")},
                {"ru_UA_UA", new Locale("ru", "UA", "UA")},
                {"ja", new Locale("ja")},
                {"ja_JP", new Locale("ja", "JP")},
                {"ja_JP_XX", new Locale("ja", "JP", "XX")},
                {"ja_JP_JP", new Locale("ja", "JP", "JP")},
                {"zh_CN", new Locale("zh", "CN")},
                {"zh_CN_XX", new Locale("zh", "CN", "XX")},
                {"zh_CN_CN", new Locale("zh", "CN", "CN")},
                {"zh_TW", new Locale("zh", "TW")},
                {"zh_TW_XX", new Locale("zh", "TW", "XX")},
                {"zh_TW_TW", new Locale("zh", "TW", "TW")},
                {"invalid_format_1", null},
                {"invalid_format_2", null},
                {"invalid_format_3", null},
                {"invalid_format_4", null},
                {"invalid_format_5", null},
                {"invalid_format_6", null},
                {"invalid_format_7", null},
                {"invalid_format_8", null},
                {"invalid_format_9", null}
        });
    }

    @Test
    public void testToLocale() {
        Locale actual = LocaleUtils.toLocale(input);
        assertEquals("Expected locale to be equal", expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_invalidFormat_1() {
        LocaleUtils.toLocale("AA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_invalidFormat_2() {
        LocaleUtils.toLocale("en_GB_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_invalidFormat_3() {
        LocaleUtils.toLocale("en_GB_XXX_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_invalidFormat_4() {
        LocaleUtils.toLocale("en_GB_XXX_XXX_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_invalidFormat_5() {
        LocaleUtils.toLocale("en_GB_XXX_XXX_XXX_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_invalidFormat_6() {
        LocaleUtils.toLocale("en_GB_XXX_XXX_XXX_XXX_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_invalidFormat_7() {
        LocaleUtils.toLocale("en_GB_XXX_XXX_XXX_XXX_XXX_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_invalidFormat_8() {
        LocaleUtils.toLocale("en_GB_XXX_XXX_XXX_XXX_XXX_XXX_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_invalidFormat_9() {
        LocaleUtils.toLocale("en_GB_XXX_XXX_XXX_XXX_XXX_XXX_XXX_XXX");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        assertEquals("Expected size to be equal", 4, localeLookupList.size());
        assertTrue("Expected locale to be present", localeLookupList.contains(locale));
        assertTrue("Expected locale without variant to be present", localeLookupList.contains(new Locale("fr", "CA")));
        assertTrue("Expected locale for country only to be present", localeLookupList.contains(new Locale("fr")));
        Locale defaultLocale = new Locale("en");
        List<Locale> localeLookupListDefault = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals("Expected size to be equal", 5, localeLookupListDefault.size());
        assertTrue("Expected locale to be present", localeLookupListDefault.contains(locale));
        assertTrue("Expected locale without variant to be present", localeLookupListDefault.contains(new Locale("fr", "CA")));
        assertTrue("Expected locale for country only to be present", localeLookupListDefault.contains(new Locale("fr")));
        assertTrue("Expected default locale to be present", localeLookupListDefault.contains(defaultLocale));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue("Expected available locale to be present", LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue("Expected available locale to be present", LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertTrue("Expected available locale to be present", LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
        assertTrue("Expected available locale to be present", LocaleUtils.isAvailableLocale(new Locale("ru", "RU")));
        assertTrue("Expected available locale to be present", LocaleUtils.isAvailableLocale(new Locale("ja", "JP")));
        assertTrue("Expected available locale to be present", LocaleUtils.isAvailableLocale(new Locale("zh", "CN")));
        assertTrue("Expected available locale to be present", LocaleUtils.isAvailableLocale(new Locale("zh", "TW")));
        assertFalse("Expected invalid locale to not be present", LocaleUtils.isAvailableLocale(null));
        assertFalse("Expected invalid locale to not be present", LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
        assertFalse("Expected invalid locale to not be present", LocaleUtils.isAvailableLocale(new Locale("fr", "XX")));
        assertFalse("Expected invalid locale to not be present", LocaleUtils.isAvailableLocale(new Locale("de", "XX")));
        assertFalse("Expected invalid locale to not be present", LocaleUtils.isAvailableLocale(new Locale("ru", "XX")));
        assertFalse("Expected invalid locale to not be present", LocaleUtils.isAvailableLocale(new Locale("ja", "XX")));
        assertFalse("Expected invalid locale to not be present", LocaleUtils.isAvailableLocale(new Locale("zh", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assertEquals("Expected size to be equal", 2, languagesByCountry.size());
        assertTrue("Expected locale to be present", languagesByCountry.contains(new Locale("en", "CA")));
        assertTrue("Expected locale to be present", languagesByCountry.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("fr");
        assertEquals("Expected size to be equal", 3, countriesByLanguage.size());
        assertTrue("Expected locale to be present", countriesByLanguage.contains(new Locale("fr", "CA")));
        assertTrue("Expected locale to be present", countriesByLanguage.contains(new Locale("fr", "FR")));
        assertTrue("Expected locale to be present", countriesByLanguage.contains(new Locale("fr", "CH")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals("Expected size to be equal", 58, availableLocaleList.size());
        assertFalse("Expected invalid locale to not be present", availableLocaleList.contains(null));
        assertTrue("Expected locale to be present", availableLocaleList.contains(new Locale("en", "US")));
        assertTrue("Expected locale to be present", availableLocaleList.contains(new Locale("fr", "FR")));
        assertTrue("Expected locale to be present", availableLocaleList.contains(new Locale("de", "DE")));
        assertTrue("Expected locale to be present", availableLocaleList.contains(new Locale("ru", "RU")));
        assertTrue("Expected locale to be present", availableLocaleList.contains(new Locale("ja", "JP")));
        assertTrue("Expected locale to be present", availableLocaleList.contains(new Locale("zh", "CN")));
        assertTrue("Expected locale to be present", availableLocaleList.contains(new Locale("zh", "TW")));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertEquals("Expected size to be equal", 58, availableLocaleList().size());
        assertFalse("Expected invalid locale to not be present", availableLocaleSet.contains(null));
        assertTrue("Expected locale to be present", availableLocaleSet.contains(new Locale("en", "US")));
        assertTrue("Expected locale to be present", availableLocaleSet.contains(new Locale("fr", "FR")));
        assertTrue("Expected locale to be present", availableLocaleSet.contains(new Locale("de", "DE")));
        assertTrue("Expected locale to be present", availableLocaleSet.contains(new Locale("ru", "RU")));
        assertTrue("Expected locale to be present", availableLocaleSet.contains(new Locale("ja", "JP")));
        assertTrue("Expected locale to be present", availableLocaleSet.contains(new Locale("zh", "CN")));
        assertTrue("Expected locale to be present", availableLocaleSet.contains(new Locale("zh", "TW")));
    }
}