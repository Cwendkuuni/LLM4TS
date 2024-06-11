package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid tests
        assertEquals("LocaleUtils.toLocale(\"en\")", new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals("LocaleUtils.toLocale(\"en_GB\")", new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals("LocaleUtils.toLocale(\"en_GB_xxx\")", new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Edge cases
        assertEquals("LocaleUtils.toLocale(\"\")", Locale.getDefault(), LocaleUtils.toLocale(""));
        assertEquals("LocaleUtils.toLocale(null)", null, LocaleUtils.toLocale(null));
        assertEquals("LocaleUtils.toLocale(\"zz\")", new Locale("zz"), LocaleUtils.toLocale("zz"));
        assertEquals("LocaleUtils.toLocale(\"xx_yy\")", new Locale("xx", "yy"), LocaleUtils.toLocale("xx_yy"));

        // Error cases
        try {
            LocaleUtils.toLocale("zzz_yy");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_zx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxx_yyy");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals("LocaleUtils.localeLookupList(Locale(\"fr\", \"CA\", \"xxx\"))",
                Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr")), lookupList);
        List lookupListDefault = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals("LocaleUtils.localeLookupList(Locale(\"fr\", \"CA\", \"xxx\"), Locale(\"en\"))",
                Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en")), lookupListDefault);
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        List expectedAvailableLocaleList = Arrays.asList(new Locale("en_US"), new Locale("fr_FR"), new Locale("de_DE"),
                new Locale("es_ES"), new Locale("it_IT"), new Locale("ja_JP"), new Locale("ko_KR"), new Locale("zh_CN"),
                new Locale("zh_TW"), new Locale("pt_BR"), new Locale("ru_RU"), new Locale("tr_TR"), new Locale("vi_VN"));
        assertEquals("LocaleUtils.availableLocaleList()", expectedAvailableLocaleList, availableLocaleList);
    }

    @Test
    public void testLanguageByCountry() {
        List languagesByCountry = LocaleUtils.languagesByCountry("CA");
        List expectedLanguagesByCountry = Arrays.asList(new Locale("en"), new Locale("fr"));
        assertEquals("LocaleUtils.languagesByCountry(\"CA\")", expectedLanguagesByCountry, languagesByCountry);
    }

    @Test
    public void testCountryByLanguage() {
        List countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        List expectedCountriesByLanguage = Arrays.asList(new Locale("CA"), new Locale("GB"), new Locale("US"));
        assertEquals("LocaleUtils.countriesByLanguage(\"en\")", expectedCountriesByLanguage, countriesByLanguage);
    }
}